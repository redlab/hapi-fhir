package ca.uhn.fhir.ws;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.ws.exceptions.AbstractResponseException;
import ca.uhn.fhir.ws.exceptions.InternalErrorException;
import ca.uhn.fhir.ws.exceptions.MethodNotFoundException;
import ca.uhn.fhir.ws.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.ws.operations.DELETE;
import ca.uhn.fhir.ws.operations.GET;
import ca.uhn.fhir.ws.operations.POST;
import ca.uhn.fhir.ws.operations.PUT;

public abstract class RestfulServer extends HttpServlet {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);

	private static final long serialVersionUID = 1L;

	private FhirContext myFhirContext;

	private Map<Class<? extends IResource>, IResourceProvider<?>> myTypeToProvider = new HashMap<Class<? extends IResource>, IResourceProvider<?>>();

	// map of request handler resources keyed by resource name
	private Map<String, Resource> resources = new HashMap<String, Resource>();

	private boolean addResourceMethod(Resource resource, Method method) throws Exception {

		ResourceMethod rm = new ResourceMethod();

		// each operation name must have a request type annotation and be unique
		if (null != method.getAnnotation(GET.class)) {
			rm.setRequestType(ResourceMethod.RequestType.GET);
		} else if (null != method.getAnnotation(PUT.class)) {
			rm.setRequestType(ResourceMethod.RequestType.PUT);
		} else if (null != method.getAnnotation(POST.class)) {
			rm.setRequestType(ResourceMethod.RequestType.POST);
		} else if (null != method.getAnnotation(DELETE.class)) {
			rm.setRequestType(ResourceMethod.RequestType.DELETE);
		} else {
			return false;
		}

		rm.setMethod(method);
		rm.setResourceType(method.getReturnType());
		rm.setParameters(Util.getResourceParameters(method));
		
		resource.addMethod(rm);
		return true;
	}

	@SuppressWarnings("unused")
	private EncodingUtil determineResponseEncoding(Map<String, String[]> theParams) {
		String[] format = theParams.remove(Constants.PARAM_FORMAT);
		// TODO: handle this once we support JSON
		return EncodingUtil.XML;
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.DELETE, request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.GET, request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.POST, request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(ResourceMethod.RequestType.PUT, request, response);
	}

	private void findResourceMethods(IResourceProvider<? extends IResource> theProvider) throws Exception {

		Class<? extends IResource> resourceType = theProvider.getResourceType();
		RuntimeResourceDefinition definition = myFhirContext.getResourceDefinition(resourceType);
		
			Resource r = new Resource();
			r.setResourceProvider(theProvider);
			r.setResourceName(definition.getName());
			resources.put(definition.getName(), r);

		Class<?> clazz = theProvider.getClass();
		for (Method m : clazz.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers())) {

				boolean foundMethod = addResourceMethod(r, m);
				if (foundMethod) {
					ourLog.debug("found handler: " + m.getName());
				}
			}
		}
	}

	public abstract Collection<IResourceProvider<?>> getResourceProviders();

	protected void handleRequest(ResourceMethod.RequestType requestType, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String resourceName = null;
			Long identity = null;

			Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
			EncodingUtil responseEncoding = determineResponseEncoding(params);
			
			StringTokenizer tok = new StringTokenizer(request.getRequestURI(), "/");
			if (!tok.hasMoreTokens()) {
				throw new MethodNotFoundException("No resource name specified");
			}
			resourceName = tok.nextToken();

			Resource resourceBinding = resources.get(resourceName);
			if (resourceBinding == null) {
				throw new MethodNotFoundException("Unknown resource type: " + resourceBinding);
			}
			
			if (tok.hasMoreTokens()) {
				String identityString = tok.nextToken();
				try {
					identity = Long.parseLong(identityString);
				} catch (NumberFormatException e) {
					throw new NumberFormatException("Invalid identity token: " + identity);
				}
			}
						
			if (identity != null && !tok.hasMoreTokens()) {
				if (params == null || params.isEmpty()) {
					IResource resource = resourceBinding.getResourceProvider().getResourceById(identity);
					if (resource == null) {
						throw new ResourceNotFoundException(identity);
					}
					streamResponseAsResource(response, resource, resourceBinding, responseEncoding);
					return;
				}
			}
			
			ResourceMethod resourceMethod = resourceBinding.getMethod(params.keySet());
			if (null == resourceMethod) {
				throw new MethodNotFoundException("No resource method available for the supplied parameters " + params);
			}
			
			List<IResource> result = resourceMethod.invoke(resourceBinding.getResourceProvider(), params);
			streamResponseAsBundle(response, result, responseEncoding);
//			resourceMethod.get
		
		} catch (AbstractResponseException e) {
			
			response.setStatus(e.getStatusCode());
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append(e.getMessage());
			response.getWriter().close();
			
		} catch (Throwable t) {
			// TODO: handle this better
			ourLog.error("Failed to process invocation", t);
			throw new ServletException(t);
		}

	}

	@Override
	public void init() throws ServletException {
		try {
			ourLog.info("Initializing HAPI FHIR restful server");

			Collection<IResourceProvider<?>> resourceProvider = getResourceProviders();
			for (IResourceProvider<?> nextProvider : resourceProvider) {
				if (myTypeToProvider.containsKey(nextProvider.getResourceType())) {
					throw new ServletException("Multiple providers for type: " + nextProvider.getResourceType().getCanonicalName());
				}
				myTypeToProvider.put(nextProvider.getResourceType(), nextProvider);
			}

			ourLog.info("Got {} resource providers",myTypeToProvider.size());
			
			myFhirContext = new FhirContext(myTypeToProvider.keySet());

			for (IResourceProvider<?> provider : myTypeToProvider.values()) {
				findResourceMethods(provider);
			}

			
		} catch (Exception ex) {
			ourLog.error("An error occurred while loading request handlers!", ex);
			throw new ServletException("Failed to initialize FHIR Restful server", ex);
		}
	}

	private void streamResponseAsBundle(HttpServletResponse theHttpResponse, List<IResource> theResult, EncodingUtil theResponseEncoding) throws IOException {
		theHttpResponse.setStatus(200);
		theHttpResponse.setContentType(Constants.CT_FHIR_XML);
		theHttpResponse.setCharacterEncoding("UTF-8");
		
		Bundle bundle = new Bundle();
		bundle.getAuthorName().setValue(getClass().getCanonicalName());
		bundle.getId().setValue(UUID.randomUUID().toString());
		bundle.getPublished().setToCurrentTimeInLocalTimeZone();
		
		for (IResource next : theResult) {
			BundleEntry entry = new BundleEntry();
			bundle.getEntries().add(entry);
			
			entry.setResource(next);
		}
		
		bundle.getTotalResults().setValue(theResult.size());
		
		PrintWriter writer = theHttpResponse.getWriter();
		myFhirContext.newXmlParser().encodeBundleToWriter(bundle, writer);
		writer.close();
	}

	private void streamResponseAsResource(HttpServletResponse theHttpResponse, IResource theResource, Resource theResourceBinding, EncodingUtil theResponseEncoding) throws IOException {

		theHttpResponse.setStatus(200);
		theHttpResponse.setContentType(Constants.CT_FHIR_XML);
		theHttpResponse.setCharacterEncoding("UTF-8");
		
		PrintWriter writer = theHttpResponse.getWriter();
		myFhirContext.newXmlParser().encodeResourceToWriter(theResource, writer);
		writer.close();
		
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	private static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {

		if (null == packageName)
			throw new ClassNotFoundException("package name must be specified for JSON operations");

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}

		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}
}

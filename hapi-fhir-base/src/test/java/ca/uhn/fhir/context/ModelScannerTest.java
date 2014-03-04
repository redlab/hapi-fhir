package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.DataFormatException;

public class ModelScannerTest {

	@Test
	public void testScanExtensionTypes() throws DataFormatException {
		
		ModelScanner scanner = new ModelScanner(ResourceWithExtensionsA.class);
		RuntimeResourceDefinition def = (RuntimeResourceDefinition) scanner.getClassToElementDefinitions().get(ResourceWithExtensionsA.class);
		
		assertEquals(RuntimeChildCompositeDatatypeDefinition.class, def.getChildByNameOrThrowDataFormatException("identifier").getClass());
		
		RuntimeChildDeclaredExtensionDefinition ext = def.getDeclaredExtension("http://foo/1");
		assertNotNull(ext);
		BaseRuntimeElementDefinition<?> valueString = ext.getChildByName("valueString");
		assertNotNull(valueString);
		
		ext = def.getDeclaredExtension("http://foo/2");
		assertNotNull(ext);
		valueString = ext.getChildByName("valueString");
		assertNotNull(valueString);

		ext = def.getDeclaredExtension("http://bar/1");
		assertNotNull(ext);
		RuntimeChildDeclaredExtensionDefinition childExt = ext.getChildExtensionForUrl("http://bar/1/1");
		assertNotNull(childExt);
		BaseRuntimeElementDefinition<?> valueDate = childExt.getChildByName("valueDate");
		assertNotNull(valueDate);
		childExt = ext.getChildExtensionForUrl("http://bar/1/2");
		assertNotNull(childExt);
		childExt = childExt.getChildExtensionForUrl("http://bar/1/2/1");
		assertNotNull(childExt);
		valueDate = childExt.getChildByName("valueDate");
		assertNotNull(valueDate);
		
	}
	
}

package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.*;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.*;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface ISearchParamExtractor {

//	SearchParamSet<ResourceIndexedSearchParamCoords> extractSearchParamCoords(IBaseResource theResource);

	SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource);

	SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource);

	SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource);

	SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource);

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource);

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, RuntimeSearchParam theSearchParam);

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource);

	SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource);

	SearchParamSet<PathAndRef> extractResourceLinks(IBaseResource theResource);

	String[] split(String theExpression);

	List<IBase> extractValues(String thePaths, IBaseResource theResource);

	String toRootTypeName(IBase nextObject);

	String toTypeName(IBase nextObject);

	PathAndRef extractReferenceLinkFromResource(IBase theValue, String thePath);

	Date extractDateFromResource(IBase theValue, String thePath);

	ResourceIndexedSearchParamToken createSearchParamForCoding(String theResourceType, RuntimeSearchParam theSearchParam, IBase theValue);

	String getDisplayTextForCoding(IBase theValue);

	List<IBase> getCodingsFromCodeableConcept(IBase theValue);

	String getDisplayTextFromCodeableConcept(IBase theValue);

	class SearchParamSet<T> extends HashSet<T> {

		private List<String> myWarnings;

		public void addWarning(String theWarning) {
			if (myWarnings == null) {
				myWarnings = new ArrayList<>();
			}
			myWarnings.add(theWarning);
		}

		List<String> getWarnings() {
			if (myWarnings == null) {
				return Collections.emptyList();
			}
			return myWarnings;
		}

	}


}

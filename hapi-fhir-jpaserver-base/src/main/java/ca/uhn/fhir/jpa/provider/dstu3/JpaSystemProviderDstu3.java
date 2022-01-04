package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseJpaSystemProviderDstu2Plus;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public class JpaSystemProviderDstu3 extends BaseJpaSystemProviderDstu2Plus<Bundle, Meta> {

	@Autowired()
	@Qualifier("mySystemDaoDstu3")
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	// This is generated by hand:
	// ls hapi-fhir-structures-dstu2/target/generated-sources/tinder/ca/uhn/fhir/model/dstu2/resource/ | sort | sed "s/.java//" | sed "s/^/@OperationParam(name=\"/" | sed "s/$/\", type=IntegerType.class, min=0, max=1),/"
	@Operation(name = JpaConstants.OPERATION_GET_RESOURCE_COUNTS, idempotent = true, returnParameters = {
		@OperationParam(name = "AllergyIntolerance", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Appointment", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "AppointmentResponse", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "AuditEvent", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Basic", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Binary", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "BodySite", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Bundle", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "CarePlan", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "CarePlan2", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Claim", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ClaimResponse", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ClinicalImpression", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Communication", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "CommunicationRequest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Composition", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ConceptMap", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Condition", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Conformance", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Contract", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Contraindication", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Coverage", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DataElement", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Device", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DeviceComponent", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DeviceMetric", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DeviceUseRequest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DeviceUseStatement", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DiagnosticOrder", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DiagnosticReport", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DocumentManifest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "DocumentReference", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "EligibilityRequest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "EligibilityResponse", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Encounter", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "EnrollmentRequest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "EnrollmentResponse", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "EpisodeOfCare", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ExplanationOfBenefit", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "FamilyMemberHistory", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Flag", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Goal", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Group", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "HealthcareService", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ImagingObjectSelection", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ImagingStudy", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Immunization", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ImmunizationRecommendation", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ListResource", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Location", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Media", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Medication", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "MedicationAdministration", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "MedicationDispense", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "MedicationPrescription", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "MedicationStatement", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "MessageHeader", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "NamingSystem", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "NutritionOrder", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Observation", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "OperationDefinition", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "OperationOutcome", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Order", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "OrderResponse", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Organization", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Parameters", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Patient", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "PaymentNotice", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "PaymentReconciliation", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Person", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Practitioner", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Procedure", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ProcedureRequest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ProcessRequest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ProcessResponse", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Provenance", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Questionnaire", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "QuestionnaireAnswers", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ReferralRequest", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "RelatedPerson", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "RiskAssessment", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Schedule", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "SearchParameter", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Slot", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Specimen", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "StructureDefinition", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Subscription", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Substance", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "Supply", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "ValueSet", type = IntegerType.class, min = 0, max = 1),
		@OperationParam(name = "VisionPrescription", type = IntegerType.class, min = 0, max = 1)
	})
	@Description(shortDefinition = "Provides the number of resources currently stored on the server, broken down by resource type")
	public Parameters getResourceCounts() {
		Parameters retVal = new Parameters();

		Map<String, Long> counts = mySystemDao.getResourceCountsFromCache();
		counts = defaultIfNull(counts, Collections.emptyMap());
		counts = new TreeMap<>(counts);
		for (Entry<String, Long> nextEntry : counts.entrySet()) {
			retVal.addParameter().setName((nextEntry.getKey())).setValue(new IntegerType(nextEntry.getValue().intValue()));
		}

		return retVal;
	}

	@Operation(name = ProviderConstants.OPERATION_META, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	public Parameters meta(RequestDetails theRequestDetails) {
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("return").setValue(getDao().metaGetOperation(theRequestDetails));
		return parameters;
	}

	@Transaction
	public Bundle transaction(RequestDetails theRequestDetails, @TransactionParam Bundle theResources) {
		startRequest(((ServletRequestDetails) theRequestDetails).getServletRequest());
		try {
			return getDao().transaction(theRequestDetails, theResources);
		} finally {
			endRequest(((ServletRequestDetails) theRequestDetails).getServletRequest());
		}
	}

}

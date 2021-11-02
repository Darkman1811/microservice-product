package com.guestu.ecom.microserviceproduct.api;

import org.springframework.web.bind.annotation.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.*;

@RestController
@RequestMapping("/test")
@CrossOrigin("*")
public class testApi {

    @GetMapping("/testme")
    @ResponseBody
    public String testme(){
     return "Testme";
    }

    @GetMapping("/testfhir")
    public void testfhir(){
         FhirContext ctx = FhirContext.forR4();
     IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");
        Patient patient = new Patient();
        patient.addIdentifier()
                .setSystem("http://acme.org/mrns")
                .setValue("123456");
        patient.addName()
                .setFamily("Jameson")
                .addGiven("J")
                .addGiven("Jonah");
        patient.setGender(Enumerations.AdministrativeGender.MALE);

// Give the patient a temporary UUID so that other resources in
// the transaction can refer to it
        patient.setId(IdType.newRandomUuid());

// Create an observation object
     Observation observation = new Observation();
     observation.setStatus(Observation.ObservationStatus.FINAL);
     observation
             .getCode()
             .addCoding()
             .setSystem("http://loinc.org")
             .setCode("789-8")
             .setDisplay("Erythrocytes [#/volume] in Blood by Automated count");
     observation.setValue(
             new Quantity()
                     .setValue(4.12)
                     .setUnit("10 trillion/L")
                     .setSystem("http://unitsofmeasure.org")
                     .setCode("10*12/L"));

// The observation refers to the patient using the ID, which is already
// set to a temporary UUID
     observation.setSubject(new Reference(patient.getIdElement().getValue()));

// Create a bundle that will be used as a transaction
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

// Add the patient as an entry. This entry is a POST with an
// If-None-Exist header (conditional create) meaning that it
// will only be created if there isn't already a Patient with
// the identifier 12345
    bundle.addEntry()
             .setFullUrl(patient.getIdElement().getValue())
             .setResource(patient)
             .getRequest()
             .setUrl("Patient")
             .setIfNoneExist("identifier=http://acme.org/mrns|123456")
             .setMethod(Bundle.HTTPVerb.POST);

/*
        bundle.addEntry()
                .setFullUrl(patient.getIdElement().getValue())
                .setResource(patient)
                .getRequest()
                .setUrl("Patient")
                // .setIfNoneExist("identifier=http://acme.org/mrns|123456")
                .setMethod(Bundle.HTTPVerb.GET);

 /*    patient.setId("6");
     bundle.getEntryFirstRep()
             .setFullUrl(patient.getIdElement().getValue())
             .setResource(patient)
             .getRequest()
             .setUrl("Patient/6")
           //  .setIfNoneExist("identifier=http://acme.org/mrns|12345")
          //   .setIfNoneExist("identifier=http://acme.org/mrns|12345")
             .setMethod(Bundle.HTTPVerb.DELETE);*/

// Add the observation. This entry is a POST with no header
// (normal create) meaning that it will be created even if
// a similar resource already exists.
 /*    bundle.addEntry()
             .setResource(observation)
             .getRequest()
             .setUrl("Observation")
             .setMethod(Bundle.HTTPVerb.POST);
*/
// Log the request
     //   FhirContext ctx = FhirContext.forR4();
        //   System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
        ctx.getRestfulClientFactory().setConnectTimeout(2000000);
        Bundle resp = client.transaction().withBundle(bundle).execute();


// Create a client and post the transaction to the server
    /*   IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");
        Bundle resp = client.transaction().withBundle(bundle).execute();

   //Bundle response=client.search().forResource(Patient.class).where(Patient.RES_ID.exactly().identifier("8")).preferResponseTypes(List<Patient.class>).execute();
        Property id=resp.getEntry().get(0).getResponse().getNamedProperty("id");
        Property name=resp.getEntry().get(0).getResponse().getNamedProperty("name");

        List<IBaseResource> patients = new ArrayList<>();
        patients.addAll(BundleUtil.toListOfResources(ctx, bundle));
        Patient patient1= (Patient) patients.get(0);
        String name1=patient1.getName().get(0).getFamily();

        System.out.println("name:"+name1);
        // Log the response
        // System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
*/

    }
}

package com.guestu.ecom.microserviceproduct;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class TestingApis {
    public static void main(String [] args){
      TestingApis testingApis=new TestingApis();
    //  testingApis.testGetall();
        testingApis.testDelete();
    }

    public void testDelete(){
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");

        //Creer l'objer patient
        Patient patient = new Patient();
        patient.setId("1652");


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
                .setUrl("Patient/"+patient.getId())
                //  .setIfNoneExist("identifier=http://acme.org/mrns|12345")
                .setMethod(Bundle.HTTPVerb.DELETE);

        ctx.getRestfulClientFactory().setConnectTimeout(2000000);
        Bundle resp = client.transaction().withBundle(bundle).execute();
    }
    public void testGetall(){
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");

        Bundle bundle= client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();

        List<IBaseResource> patients = new ArrayList<>();
        patients.addAll(BundleUtil.toListOfResources(ctx, bundle));

        while (bundle.getLink(IBaseBundle.LINK_NEXT)!=null){
            bundle=client.loadPage().next(bundle).execute();
            patients.addAll(BundleUtil.toListOfResources(ctx,bundle));
        }

        patients.forEach(pat->{
            Patient patient=(Patient) pat;
            patient.getName().forEach(nam->{
                System.out.println(nam.getFamily());
            });


        });
        System.out.println(patients.size());
        /*
        System.out.println(patients.size());
        Patient patient1= (Patient) patients.get(0);
      //  String name1=patient1.getName().get(0).getFamily();
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient1));
*/
    }
}

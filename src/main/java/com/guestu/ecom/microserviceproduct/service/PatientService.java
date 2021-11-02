package com.guestu.ecom.microserviceproduct.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    public void addPatient(Patient patient){
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");


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
                //.setIfNoneExist("identifier=http://acme.org/mrns|")
                .setMethod(Bundle.HTTPVerb.POST);

        ctx.getRestfulClientFactory().setConnectTimeout(2000000);
        Bundle resp = client.transaction().withBundle(bundle).execute();
       // return this.getOne(patient.getId());

    }
public  void delete(Patient patient){
    FhirContext ctx = FhirContext.forR4();
    IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");



    Bundle bundle = new Bundle();
    bundle.setType(Bundle.BundleType.TRANSACTION);

    bundle.addEntry()
            .setFullUrl(patient.getIdElement().getValue())
            .setResource(patient)
            .getRequest()
            .setUrl("Patient/"+patient.getId())
            .setMethod(Bundle.HTTPVerb.DELETE);

    ctx.getRestfulClientFactory().setConnectTimeout(2000000);
    Bundle resp = client.transaction().withBundle(bundle).execute();
}
    public void updatePatient(Patient patient){
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);
        bundle.addEntry()
                .setFullUrl(patient.getIdElement().getValue())
                .setResource(patient)
                .getRequest()
                .setUrl(patient.getId())
                .setMethod(Bundle.HTTPVerb.PUT);

        ctx.getRestfulClientFactory().setConnectTimeout(2000000);
        Bundle resp = client.transaction().withBundle(bundle).execute();
    }

    public Patient getOne(String id){
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");
        Patient patient=client.read().resource(Patient.class).withId(id).execute();
        return patient;
    }

    public List<Patient> getAll(){
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir");
        Bundle bundle= client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
        List<IBaseResource> patients = new ArrayList<>();
        patients.addAll(BundleUtil.toListOfResources(ctx, bundle));

        while (bundle.getLink(IBaseBundle.LINK_NEXT)!=null){
            bundle=client.loadPage().next(bundle).execute();
            patients.addAll(BundleUtil.toListOfResources(ctx,bundle));
        }

        List<Patient> ret=new ArrayList<>();
        patients.forEach(pat->{
            Patient patient=(Patient) pat;
            ret.add(patient);
        });

        return ret;
    }
}

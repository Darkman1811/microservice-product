package com.guestu.ecom.microserviceproduct.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.guestu.ecom.microserviceproduct.service.PatientService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin("*")
public class PatientControler {
    private FhirContext ctx;
    private  IParser parser;

    PatientControler(){
         ctx=FhirContext.forR4();
         parser=ctx.newJsonParser();
    }

    @Autowired
    PatientService patientService;

    @PostMapping
    public void addPatient(@RequestBody String patientPath){
        System.out.println(patientPath);

        Patient patient=this.parser.parseResource(Patient.class,patientPath);
        System.out.println("request for patient");
        System.out.println(patient.getName().get(0).getFamily());
        patient.setId(IdType.newRandomUuid());
        patientService.addPatient(patient);
    }

    @PutMapping
    public void updatePatient(@RequestBody String patientPath){
        Patient patient=this.parser.parseResource(Patient.class,patientPath);
        patientService.updatePatient(patient);
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable String id){
      Patient patient= this.patientService.getOne(id);
      return this.parser.setPrettyPrint(true).encodeResourceToString(patient);
    }
    @GetMapping("/getAll")
    public List<String> getAll(){
        List<Patient> patients=this.patientService.getAll();
        List<String> ret=new ArrayList<>();
        patients.forEach(patient -> {
           String pat= ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
           ret.add(pat);
        });

        return ret;
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable String id){
        Patient patient = new Patient();
        patient.setId(id);
        patientService.delete(patient);
    }

    @PostMapping("/add")
    public void addPatient(){

        //Creer l'objer patient
        Patient patient = new Patient();
        patient.addIdentifier()
                .setSystem("http://acme.org/mrns")
                .setValue("123456");
        patient.addName()
                .setFamily("Diop")
                .addGiven("J")
                .addGiven("Fatou");
        patient.setGender(Enumerations.AdministrativeGender.MALE);

        // Give the patient a temporary UUID so that other resources in
// the transaction can refer to it
        patient.setId(IdType.newRandomUuid());
        patientService.addPatient(patient);
    }


}

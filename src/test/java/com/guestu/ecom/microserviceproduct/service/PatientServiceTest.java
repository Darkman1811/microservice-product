package com.guestu.ecom.microserviceproduct.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PatientServiceTest {
   // private  Patient patient;

   // private PatientService patientService= Mockito.mock(PatientService.class);

    @BeforeAll
    static void setUpAll(){

    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }


    @Test
    @Order(1)
    void getAll() {
        PatientService patientService=new PatientService();
        List<Patient> patientList= patientService.getAll();

        assertTrue(!patientList.isEmpty());
        assertNotNull(patientList.get(0));
        assertTrue(patientList.get(0) instanceof Patient);
    }

    @Test
    @Order(2)
    void addPatient() {
        PatientService patientService=new PatientService();

        int id=(int)Math.random()*1000;
        Patient patient=new Patient();

        patient.setId(String.valueOf(id+10));
        patient.addName(new HumanName());
        patient.getName().get(0).addGiven("Moussa");
        patient.getName().get(0).setFamily("Diop");

        int befor= patientService.getAll().size();
        patientService.addPatient(patient);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
             int after= patientService.getAll().size();

      assertNotNull(patient);
      assertTrue(after==befor+1);

      Bundle bundle=new Bundle();
      IGenericClient client=Mockito.mock(IGenericClient.class);
       Mockito.when(client.transaction().withBundle(bundle).execute()).thenReturn(bundle);
      //  Mockito.doNothing().when(patientService).addPatient(Mockito.isA(Patient.class));

    }

    @Test
    @Order(3)
    void getOne() {
        PatientService patientService=new PatientService();

        Patient expected= patientService.getOne("2");

        assertNotNull(expected);
        assertTrue(expected instanceof Patient);
    }


    @Test
    @Order(4)
    void updatePatient() {
        Patient patient=new Patient();

        patient.setId("2");
        patient.addName(new HumanName());
        patient.getName().get(0).addGiven("Moussa");
        patient.getName().get(0).setFamily("Diop");

        PatientService patientService=new PatientService();

        String new_family_name="Diaw";
        patient.getName().get(0).setFamily(new_family_name);
        Patient expected= patientService.getOne(patient.getId());

        assertTrue(expected.getName().get(0).getFamily()
                .equals(
                        patient.getName().get(0).getFamily()
                ));
    }

    @Test
    @Order(5)
    void deletePatient(){
        Patient patient=new Patient();

        patient.setId("2");
        patient.addName(new HumanName());
        patient.getName().get(0).addGiven("Moussa");
        patient.getName().get(0).setFamily("Diop");

        PatientService patientService=new PatientService();

        patientService.delete(patient);
        assertThrows(ResourceGoneException.class,() -> {
            patientService.getOne(patient.getId());
        });
        //assertTrue(PatientServiceTest2.patientService.getOne(patient.getId())==null);
    }

}
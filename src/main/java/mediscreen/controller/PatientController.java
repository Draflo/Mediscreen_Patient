package mediscreen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Patient;
import mediscreen.service.PatientService;

@RestController
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}
	
	@GetMapping("/patients")
	public List<Patient> getAllPatients() {
		List<Patient> findAll = patientService.getAllPatients();
		return findAll;
	}
	
	@GetMapping("/patient")
	public Patient getPatientByFirstNameAndLastName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws DoesNotExistsException {
		Patient patient = patientService.getPatient(firstName, lastName);
		return patient;
	}
	
	@PostMapping("/patient/add")
	public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
		try {
			Patient createdPatient = patientService.createPatient(patient);
			return ResponseEntity.created(null).body(createdPatient);
		} catch (AlreadyExistsException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

}

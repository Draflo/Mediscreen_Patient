package mediscreen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Patient;
import mediscreen.service.PatientService;

@CrossOrigin(origins = "http://localhost:3000")
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
	public ResponseEntity<Object> addPatient(@RequestBody Patient patient) {
		try {
			Patient createdPatient = patientService.createPatient(patient);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/patient")
	public Patient updatePatient(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestBody Patient patientUpdated) throws DoesNotExistsException {
		Patient patientToUpdate = patientService.getPatient(firstName, lastName);
		return patientService.updatePatient(patientToUpdate, patientUpdated);
	}

}

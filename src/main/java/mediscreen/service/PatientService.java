package mediscreen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Patient;
import mediscreen.repository.PatientRepository;

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository patientRepository;
	
	public Patient createPatient(Patient patient) throws AlreadyExistsException {
		if(patientRepository.findByFirstNameAndLastName(patient.getFirstName(), patient.getLastName()).isPresent())
			throw new AlreadyExistsException(patient.getFirstName() + " " + patient.getLastName() + " already exists");
		return patientRepository.save(patient);
	}

	public Patient getPatient(String firstName, String lastName) throws DoesNotExistsException {
		Optional<Patient> optionalPatient = patientRepository.findByFirstNameAndLastName(firstName, lastName);
		if( ! optionalPatient.isPresent()) {
			throw new DoesNotExistsException(firstName + " " + lastName + " does not exists");
		}
		return optionalPatient.get();
	}
	
	public List<Patient> getAllPatients() {
		List<Patient> allPatients = patientRepository.findAll();
		return allPatients;
	}
	
	public Patient updatePatient(Patient patientToUpdate, Patient patientUpdated) throws DoesNotExistsException {
		patientToUpdate.setAddress(patientUpdated.getAddress());
		patientToUpdate.setCity(patientUpdated.getCity());
		patientToUpdate.setPhone(patientUpdated.getPhone());
		return patientRepository.save(patientToUpdate);
	}
}

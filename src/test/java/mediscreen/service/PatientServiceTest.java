package mediscreen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Patient;
import mediscreen.repository.PatientRepository;

@WebMvcTest(PatientService.class)
class PatientServiceTest {
	
	private static Patient testPatient;
	private static Validator validator;
	
	@MockBean
	private PatientRepository patientRepository;
	
	@Autowired
	private PatientService patientService;
	
	@BeforeAll
	public static void setUpTestPatient() {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
		
		LocalDate birtDate = LocalDate.of(1994, 5, 29);
		String firstName = "Jean";
		String lastName = "Michel";
		String phone = "0606060606";
		String sex = "M";
		String address = "666 Place de l'église";
		String city = "Hell";
		
		testPatient = new Patient(0, firstName, lastName, birtDate, sex, phone, address, city);
	}

	@Test
	final void getAllPatient_shouldReturnPatientList() {
		List<Patient> findAll = new ArrayList<>();
		findAll.add(testPatient);
		when(patientRepository.findAll()).thenReturn(findAll);
		List<Patient> foundAll = patientService.getAllPatients();
		assertThat(foundAll).isEqualTo(findAll);
	}
	
	@Test
	final void createPatient_shouldReturnNewPatient() throws AlreadyExistsException {
		Patient createdPatient = new Patient();
		
		createdPatient.setFirstName("Dude");
		createdPatient.setLastName("Dude");
		createdPatient.setPhone("0606060606");
		createdPatient.setAddress("666 Place de l'église");
		createdPatient.setSex("M");
		createdPatient.setCity("Hell");
		createdPatient.setBirthdate(LocalDate.of(1994, 5, 29));
		
		when(patientRepository.findByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
		when(patientRepository.save(createdPatient)).thenReturn(createdPatient);
		patientService.createPatient(createdPatient);
		verify(patientRepository).save(createdPatient);
	}
	
	@Test
	final void createDuplicatedPatientTest_shouldThrowException() throws AlreadyExistsException {
		when(patientRepository.findByFirstNameAndLastName("Jean", "Michel")).thenReturn(Optional.of(testPatient));
		assertThrows(AlreadyExistsException.class, () -> patientService.createPatient(testPatient));

	}
	
	@Test
	final void getPatientTest_shouldReturnPatient() throws DoesNotExistsException {
		Patient createdPatient = new Patient();
		
		createdPatient.setFamily("Dude");
		createdPatient.setGiven("Dude");
		createdPatient.setPhone("0606060606");
		createdPatient.setAddress("666 Place de l'église");
		createdPatient.setSex("M");
		createdPatient.setCity("Hell");
		createdPatient.setDob(LocalDate.of(1994, 5, 29));
		
		when(patientRepository.findByFirstNameAndLastName("Dude", "Dude")).thenReturn(Optional.of(createdPatient));
		Patient foundPatient = patientService.getPatient("Dude", "Dude");
		assertThat(foundPatient).isEqualTo(createdPatient);
		
	}
	
	@Test
	final void getPatientUnknown_shouldThrowException() throws DoesNotExistsException {
		assertThrows(DoesNotExistsException.class, () -> patientService.getPatient("Test", "Test"));
	}
	
	@Test
	final void updatePatientTest_shouldReturnUpdatedPatient() throws DoesNotExistsException {
		Patient updatedPatient = new Patient();
		
		updatedPatient.setPhone("0707070707");
		updatedPatient.setAddress("123 Impasse des chats");
		updatedPatient.setCity("CatsTown");
		
		when(patientRepository.save(testPatient)).thenReturn(testPatient);
		patientService.updatePatient(testPatient, updatedPatient);
		
		assertThat(testPatient.getAddress()).isEqualTo("123 Impasse des chats");
		assertThat(testPatient.getCity()).isEqualTo("CatsTown");
		assertThat(testPatient.getPhone()).isEqualTo("0707070707");

	}
	
	@Test
	final void createPatientWithFieldsErrorTest_shouldThrowException() {
		
		Patient patient1 = new Patient(0, "", "Michel", LocalDate.of(1984, 8, 30), "M", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations1 = validator.validate(patient1);
		assertFalse(violations1.isEmpty());
		
		Patient patient2 = new Patient(0, "Jean", "", LocalDate.of(1984, 8, 30), "M", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations2 = validator.validate(patient2);
		assertFalse(violations2.isEmpty());
				
		Patient patient3 = new Patient(0, null, "Michel", LocalDate.of(1984, 8, 30), "M", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations3 = validator.validate(patient3);
		assertFalse(violations3.isEmpty());
		
		Patient patient4 = new Patient(0, "Jean", null, LocalDate.of(1984, 8, 30), "M", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations4 = validator.validate(patient4);
		assertFalse(violations4.isEmpty());
		
		Patient patient5 = new Patient(0, "Jean", "Michel", null, "M", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations5 = validator.validate(patient5);
		assertFalse(violations5.isEmpty());
		
		Patient patient6 = new Patient(0, "Jean", "Michel", LocalDate.of(2222, 8, 30), "M", null, "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations6 = validator.validate(patient6);
		assertFalse(violations6.isEmpty());
		
		Patient patient7 = new Patient(0, "Jean", "Michel", LocalDate.of(1984, 8, 30), "", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations7 = validator.validate(patient7);
		assertFalse(violations7.isEmpty());
		
		Patient patient8 = new Patient(0, "Jean", "Michel", LocalDate.of(1984, 8, 30), "A", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations8 = validator.validate(patient8);
		assertFalse(violations8.isEmpty());
		
		Patient patient9 = new Patient(0, "Jean", "Michel", LocalDate.of(1984, 8, 30), "Male", "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations9 = validator.validate(patient9);
		assertFalse(violations9.isEmpty());
		
		Patient patient10 = new Patient(0, "Jean", "Michel", LocalDate.of(1984, 8, 30), null, "0606060606", "666 Place de l'église", "Hell");
		Set<ConstraintViolation<Patient>> violations10 = validator.validate(patient10);
		assertFalse(violations10.isEmpty());
	
	}

}

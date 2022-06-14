package mediscreen.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Patient;
import mediscreen.service.PatientService;

@WebMvcTest(controllers = PatientController.class)
class PatientControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PatientService patientService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static Patient testPatient;
	private static List<Patient> getAllPatients = new ArrayList<>();
	
	@BeforeAll
	public static void setUpTestPatient() {
		
		LocalDate birtDate = LocalDate.of(1994, 5, 29);
		String firstName = "Jean";
		String lastName = "Michel";
		String phone = "0606060606";
		String sex = "M";
		String address = "666 Place de l'église";
		String city = "Hell";
		
		testPatient = new Patient(0, firstName, lastName, birtDate, sex, phone, address, city);
		getAllPatients.add(testPatient);
	}

	@Test
	final void getAllPatients_shouldReturnStatusOk() throws Exception {
		when(patientService.getAllPatients()).thenReturn(getAllPatients);
		mockMvc.perform(get("/patients")).andExpect(status().isOk());
	}
	
	@Test
	final void getAPatient_shouldReturnStatusOk() throws Exception {
		when(patientService.getPatient("Jean", "Michel")).thenReturn(testPatient);
		mockMvc.perform(get("/patient").param("firstName", "Jean").param("lastName", "Michel")).andExpect(status().isOk());
	}
	
	@Test
	final void postPatient_shouldReturnStatusCreated() throws Exception {
		when(patientService.createPatient(Mockito.any(Patient.class))).thenReturn(testPatient);
		mockMvc.perform(post("/patient/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(testPatient))).andExpect(status().isCreated());
	}
	
	@Test
	final void postPatientAlreadyExist_shouldReturnError400() throws Exception {
		when(patientService.createPatient(Mockito.any(Patient.class))).thenThrow(AlreadyExistsException.class);
		mockMvc.perform(post("/patient/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(testPatient))).andExpect(status().isBadRequest());
	}
	
	@Test
	final void getPatientDoesntExist_shouldReturnError400() throws Exception {
		when(patientService.getPatient(Mockito.anyString(), Mockito.anyString())).thenThrow(DoesNotExistsException.class);
		Assertions.assertThatThrownBy(() -> 
		mockMvc.perform(get("/patient").param("firstName", "Jean").param("lastName", "Michel")).andExpect(status().isInternalServerError()))
		.hasCause(new DoesNotExistsException(null));
	}
	
	@Test
	final void updatePatient_shouldReturnStatusOk() throws Exception {
		when(patientService.updatePatient(Mockito.any(Patient.class), Mockito.any(Patient.class))).thenReturn(testPatient);
		when(patientService.getPatient("Jean", "Michel")).thenReturn(testPatient);
		mockMvc.perform(put("/patient").param("firstName", "Jean").param("lastName", "Michel").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(testPatient))).andExpect(status().isOk());
	}

}

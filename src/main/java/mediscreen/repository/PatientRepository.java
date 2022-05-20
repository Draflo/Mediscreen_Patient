package mediscreen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mediscreen.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
	
	Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

}

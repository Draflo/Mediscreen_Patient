package mediscreen.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient")
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank
	@Length(max = 20, message = "Maximum 20 characters")
	@Column(name = "first_name")
	private String firstName;
	
	@NotBlank
	@Length(max = 20, message = "Maximum 20 characters")
	@Column(name = "last_name")
	private String lastName;
	
	@Past
	@NotNull
	@Column(name = "birthdate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate birthdate;
	
	@NotBlank
	@Pattern(regexp = "M|F")
	@Column(name = "sex")
	private String sex;

	@Length(max = 20, message = "Maximum 20 characters")
	@Column(name = "phone")
	private String phone;

	@Length(max = 100, message = "Maximum 100 characters")
	@Column(name = "address")
	private String address;

	@Length(max = 20, message = "Maximum 20 characters")
	@Column(name = "city")
	private String city;

	//Setter for curl request
	
	public void setFamily(String family) {
			lastName = family;
	}
		
	public void setGiven(String given) {
			firstName = given;
	}
		
	public void setDob(LocalDate dob) {
			birthdate = dob;
	}
	

}

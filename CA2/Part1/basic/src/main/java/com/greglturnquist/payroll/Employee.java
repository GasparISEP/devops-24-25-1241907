/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.payroll;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Entity // <1>
public class Employee {

	private @Id @GeneratedValue Long id; // <2>
	private String firstName;
	private String lastName;
	private String description;
	private int jobYears;
	private String jobTitle;
	private String email;

	protected Employee() {}

	public Employee(String firstName, String lastName, String description, int jobYears, String jobTitle, String email) {

		if (isAttributeValid(firstName)) {
			throw new IllegalArgumentException("First name cannot be empty.");
		}
		this.firstName = firstName;

		if (isAttributeValid(lastName)) {
			throw new IllegalArgumentException("Last name cannot be empty.");
		}
		this.lastName = lastName;

		if (isAttributeValid(description)) {
			throw new IllegalArgumentException("Description cannot be empty.");
		}
		this.description = description;

		if (isNumberValid(jobYears)) {
			throw new IllegalArgumentException("Job Years cannot be negative.");
		}
		this.jobYears = jobYears;

		if (isAttributeValid(jobTitle)) {
			throw new IllegalArgumentException("Job Title cannot be empty.");
		}
		this.jobTitle = jobTitle;

		if (!isEmailValid(email)) {
			throw new IllegalArgumentException("E-mail must be valid.");
		}
		this.email = email;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return Objects.equals(id, employee.id) &&
				Objects.equals(firstName, employee.firstName) &&
				Objects.equals(lastName, employee.lastName) &&
				Objects.equals(description, employee.description) &&
				Objects.equals(jobYears, employee.jobYears) &&
				Objects.equals(jobTitle, employee.jobTitle) &&
				Objects.equals(email, employee.email);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, firstName, lastName, description, jobYears, jobTitle, email);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if (isAttributeValid(firstName)) {
			throw new IllegalArgumentException("First name cannot be empty.");
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (isAttributeValid(lastName)) {
			throw new IllegalArgumentException("Last name cannot be empty.");
		}
		this.lastName = lastName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (isAttributeValid(description)) {
			throw new IllegalArgumentException("Description cannot be empty.");
		}
		this.description = description;
	}

	public int getJobYears() {
		return jobYears;
	}

	public void setJobYears(int jobYears) {
		if (isNumberValid(jobYears)) {
			throw new IllegalArgumentException("Job Years cannot be negative.");
		}
		this.jobYears = jobYears;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		if (isAttributeValid(jobTitle)) {
			throw new IllegalArgumentException("Job Title cannot be empty.");
		}
		this.jobTitle = jobTitle;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (!isEmailValid(email)) {
			throw new IllegalArgumentException("E-mail must be valid.");
		}
		this.email = email;
	}

	private boolean isAttributeValid(String attribute) {
		return attribute == null || attribute.isBlank();
	}

	private boolean isNumberValid(int number) {
		return number < 0;
	}

	private boolean isEmailValid(String email) {
		if(email == null || email.isBlank()) {
			return false;
		}
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
		return email.matches(emailRegex);
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", description='" + description + '\'' +
				", jobYears= '" + jobYears + '\'' +
				", jobTitle= '" + jobTitle + '\'' +
				", email= '" + email + '\'' +
				'}';
	}
}
// end::code[]
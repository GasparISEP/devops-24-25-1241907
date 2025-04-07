package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
class EmployeeTest {

    @ParameterizedTest
    @CsvSource({
            "Frodo, Baggins, ring bearer, 5, Hobbit, frodo.baggins@lordoftherings.com",
            "Bilbo, Baggins, burglar, 0, Hobbit, frodo.baggins@lordoftherings.com"
    })

    void testValidEmployee(String firstName, String lastName, String description, int jobYears, String jobTitle, String email) {
        assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears, jobTitle, email));
    }

    @ParameterizedTest
    @CsvSource({
            "'', Baggins, ring bearer, 5, Hobbit, frodo.baggins@lordoftherings.com",
            "Frodo, '', ring bearer, 5, Hobbit, frodo.baggins@lordoftherings.com",
            "Frodo, Baggins, '', 5, Hobbit, frodo.baggins@lordoftherings.com",
            "Frodo, Baggins, ring bearer, -1, Hobbit, frodo.baggins@lordoftherings.com",
            "Frodo, Baggins, ring bearer, 5, '', frodo.baggins@lordoftherings.com",
            "Frodo, Baggins, ring bearer, 5, Hobbit, ''",

    })

    void testInvalidAttributesForEmployee(String firstName, String lastName, String description, int jobYears, String jobTitle, String email) {
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears, jobTitle, email));
    }

    @ParameterizedTest
    @MethodSource("provideNullAttributes")
    void testNullAttributes(String firstName, String lastName, String description, int jobYears, String jobTitle, String email, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Employee(firstName, lastName, description, jobYears, jobTitle, email));
        assertEquals(expectedMessage, exception.getMessage());
    }

    static Stream<Arguments> provideNullAttributes() {
        return Stream.of(
                Arguments.of(null, "Baggins", "ring bearer", 5, "Hobbit", "frodo.baggins@lordoftherings.com", "First name cannot be empty."),
                Arguments.of("Frodo", null, "ring bearer", 5, "Hobbit", "frodo.baggins@lordoftherings.com", "Last name cannot be empty."),
                Arguments.of("Frodo", "Baggins", null, 5, "Hobbit", "frodo.baggins@lordoftherings.com","Description cannot be empty."),
                Arguments.of("Frodo", "Baggins", "ring bearer", -1, "Hobbit", "frodo.baggins@lordoftherings.com","Job Years cannot be negative."),
                Arguments.of("Frodo", "Baggins", "ring bearer", 5, null, "frodo.baggins@lordoftherings.com", "Job Title cannot be empty."),
                Arguments.of("Frodo", "Baggins", "ring bearer", 5, "Hobbit", null, "E-mail must be valid.")
                );
    }

    @Test
    void testGettersAndSetters() {
        Employee emp = new Employee("Frodo", "Baggins", "ring bearer", 5, "Hobbit", "frodo.baggins@lordoftherings.com");
        emp.setId(1L);

        emp.setFirstName("Sam");
        emp.setLastName("Gamgee");
        emp.setDescription("gardener");
        emp.setJobYears(10);
        emp.setJobTitle("Unicorn");
        emp.setEmail("bilbo.baggins@lordoftherings.com");

        assertEquals(1L, emp.getId());
        assertEquals("Sam", emp.getFirstName());
        assertEquals("Gamgee", emp.getLastName());
        assertEquals("gardener", emp.getDescription());
        assertEquals(10, emp.getJobYears());
        assertEquals("Unicorn", emp.getJobTitle());
        assertEquals("bilbo.baggins@lordoftherings.com", emp.getEmail());
    }

    @Test
    void testSettersThrowExceptionForInvalidValues() {
        Employee emp = new Employee("Frodo", "Baggins", "ring bearer", 5, "Hobbit", "frodo.baggins@lordoftherings.com");

        // Teste para o primeiro nome inválido
        IllegalArgumentException firstNameException = assertThrows(IllegalArgumentException.class, () -> emp.setFirstName(""));
        assertEquals("First name cannot be empty.", firstNameException.getMessage());

        // Teste para o último nome inválido
        IllegalArgumentException lastNameException = assertThrows(IllegalArgumentException.class, () -> emp.setLastName(""));
        assertEquals("Last name cannot be empty.", lastNameException.getMessage());

        // Teste para a descrição inválida
        IllegalArgumentException descriptionException = assertThrows(IllegalArgumentException.class, () -> emp.setDescription(""));
        assertEquals("Description cannot be empty.", descriptionException.getMessage());

        // Teste para anos de trabalho negativo
        IllegalArgumentException jobYearsException = assertThrows(IllegalArgumentException.class, () -> emp.setJobYears(-1));
        assertEquals("Job Years cannot be negative.", jobYearsException.getMessage());

        //Teste para Job Title invalido
        IllegalArgumentException jobTitleException = assertThrows(IllegalArgumentException.class, () -> emp.setJobTitle(""));
        assertEquals("Job Title cannot be empty.", jobTitleException.getMessage());

        //Teste para Job Title invalido
        IllegalArgumentException emailException = assertThrows(IllegalArgumentException.class, () -> emp.setEmail(""));
        assertEquals("E-mail must be valid.", emailException.getMessage());

    }

    @Test
    void testEqualsAndHashCode() {
        Employee emp1 = new Employee("Frodo", "Baggins", "ring bearer", 5, "Hobbit", "frodo.baggins@lordoftherings.com");
        Employee emp2 = new Employee("Frodo", "Baggins", "ring bearer", 5, "Hobbit", "frodo.baggins@lordoftherings.com");
        emp1.setId(1L);
        emp2.setId(1L);

        assertEquals(emp1, emp2);
        assertEquals(emp1.hashCode(), emp2.hashCode());

        Employee emp3 = new Employee("Bilbo", "Baggins", "burglar", 0, "Hobbit", "bilbo.baggins@lordoftherings.com");
        emp3.setId(2L);

        assertNotEquals(emp1, emp3);
        assertNotEquals(emp1.hashCode(), emp3.hashCode());
    }

    @Test
    void testToString() {
        Employee emp = new Employee("Frodo", "Baggins", "ring bearer", 5, "Hobbit", "frodo.baggins@lordoftherings.com");
        emp.setId(1L);

        String empString = emp.toString();
        assertTrue(empString.contains("Frodo"));
        assertTrue(empString.contains("Baggins"));
        assertTrue(empString.contains("ring bearer"));
        assertTrue(empString.contains("5"));
        assertTrue(empString.contains("Hobbit"));
        assertTrue(empString.contains("frodo.baggins@lordoftherings.com"));
    }

    @Test
    void testNoArgConstructor() {
        Employee emp = new Employee();

        assertNull(emp.getId());
        assertNull(emp.getFirstName());
        assertNull(emp.getLastName());
        assertNull(emp.getDescription());
        assertEquals(0, emp.getJobYears());
        assertNull(emp.getJobTitle());
        assertNull(emp.getEmail());
    }
}
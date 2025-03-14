# CA1: Version Control with Git

**DevOps**

**Gaspar Duarte da Costa Figueiras**

**1241907**

## Table of Contents

- [Introduction](#introduction)

- [Environment Setup](#environment-setup)

- [Part 1: Development Without Branches](#part-1-development-without-branches)
    - [Goals and Requirements](#goals-and-requirements)
    - [Key Developments](#key-developments)
- [Part 2: Development Using Branches](#part-2-development-using-branches)
    - [Goals and Requirements](#goals-and-requirements-1)
    - [Key Developments](#key-developments-1)


## Introduction
This document outlines the assignment on Version Control using Git for the DevOps course. 
The task is divided into two primary segments: in Step 1, a straightforward approach to version control is employed without the use of branches, whereas in Step 2, branching is introduced to support the integration of new features and the correction of bugs. 
The Final Results section showcases the project’s outcomes, illustrating how the application has progressively evolved through the implementation of new functionalities and the resolution of issues. 
Additionally, the report examines an Alternative Approach by assessing Subversion (SVN), comparing its attributes and potential applicability in achieving the assignment’s objectives.


## Environment Setup
Initially, I cloned a repository that already contained the Tutorial React.js and Spring Data REST application, thereby creating a local duplicate of the tutorial project. 
Afterward, I established a personal repository specifically for hosting the class assignments, ensuring that every development change was systematically tracked via version control.

**Creating My Repository:**
I set up a dedicated folder on my local machine for the DevOps class assignments and converted it into a Git repository.
This action marked the initial phase in creating my project workspace.

```shell
mkdir ~/devops-24-25-1241907
cd ~/devops-24-25-1241907
git init
```
**Copying the Tutorial Application:** 
In order to incorporate the tutorial application into my project, 
I transferred all of its files into my repository. 
This procedure guaranteed that every critical component needed for the assignment was systematically maintained under version control.

```shell
cp -r ~/basic ~/devops-24-25-1241907
```

**Linking to GitHub:**
After transferring the tutorial application into my repository, I proceeded to associate my local repository with a newly created GitHub repository.
This setup enabled me to push my updates to a remote server, ensuring both reliable backup and effortless sharing.

```shell
git remote add origin git@github.com:GasparISEP/devops-24-25-1241907.git
```
**First Commit:**
Once the repository was fully configured and all necessary files were verified to be in place, I proceeded to add the README file. 
This initial addition was committed with the message “initial commit”, marking the official start of my assignment work.

```shell
echo "# devops-24-25-1241907" >> README.md
git add README.md
git commit -m "initial commit"
```
**Pushing to Remote:**
Finally, I pushed my inaugural commit to the GitHub repository, thereby initiating the remote version history for my assignments.

```shell
git branch -M main
git push -u origin main
```
This approach provided a tidy and well-structured beginning for my class assignments, establishing a direct connection to the original tutorial application while keeping my repository ready for all future developments.


## Part 1: Development Without Branches

### Goals and Requirements
- The first segment of the assignment is dedicated to mastering basic version control techniques without using branching.
- The tasks involve setting up the project environment, making direct modifications to the primary branch, and committing these changes.
- A key objective is to integrate a new functionality (for example, adding a jobYears attribute to an Employee object) while ensuring proper version tagging—beginning with an initial version and updating it after the new feature is implemented.
- The overall focus is on practicing commit procedures, understanding the evolution of the commit history, and effectively using tags for versioning.

### Key Developments

1. **Opened an issue on the remote repository via GitHub.**

The title of the issue is “In this first part of the exercise, we will use only the main branch. #1”.
This was done to document and communicate that, for this initial phase of the exercise, all development should occur exclusively on the main branch, ensuring a straightforward and unified version control workflow.

2. **Transfer the code of the Tutorial React.js and Spring Data REST Application into a new folder named part1, which is located inside the CA1 directory.**

```shell
mkdir CA1/part1
cp -r ~/devops-24-25-1241907/basic ~/devops-24-25-1241907/CA1/part1
```

3. **Commit the changes and then push them.**

Once the application code was in the part1 directory and everything was running, I committed the changes to the main branch, referencing the issue I had opened earlier.

```shell
git add .
git commit -m "Initialize repository and connect to remote (#1)"
git push
```

4. **Opened a sub-issue linked to the main issue.**

The sub-issue was titled “Tag the initial version. #2”, following a proper procedure by using the issue system for the entire development of the assignment.

5. **Tag the repository to mark the application’s version.**

Following the versioning pattern outlined in the assignment (major.minor.revision), I tagged the initial setup as v1.1.0 and subsequently pushed that tag to the remote repository.

```shell
git tag -a v1.1.0 -m "v1.1.0"
git push origin v1.1.0
```
Sub-issue closed.

6. **Opened another sub-issue linked to the main issue.**

Once again, the objective here is to reinforce the habit of using issues and addressing them.

7. **Develop a new feature that adds a field to the application.**

The main objective of this initial phase was to introduce a new feature by incorporating the `jobYears` field into the application, which captures the number of years an employee has worked at the company.
To ensure data integrity, I also developed unit tests that validate the proper creation of Employee instances and the accuracy of their attributes. 
These tests specifically confirm that the `jobYears` field only accepts non-negative integer values, and that String-type fields cannot be null or empty.

- **Employee.java**: This Java class, which models an employee entity, was enhanced by introducing a new integer attribute named `jobYears`. 
This addition involved defining the field, along with implementing its corresponding getter and setter methods to facilitate proper data encapsulation and access. 
Moreover, parameter validation was integrated to ensure data integrity.
The following outlines the main enhancements and modifications applied to the `Employee` class to accommodate the new feature and uphold data validation standards.

```java
public Employee(String firstName, String lastName, String description, int jobYears) {

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
          Objects.equals(jobYears, employee.jobYears);
}

@Override
public int hashCode() {

  return Objects.hash(id, firstName, lastName, description, jobYears);
}

public void setFirstName(String firstName) {
  if (isAttributeValid(firstName)) {
    throw new IllegalArgumentException("First name cannot be empty.");
  }
  this.firstName = firstName;
}

public void setLastName(String lastName) {
  if (isAttributeValid(lastName)) {
    throw new IllegalArgumentException("Last name cannot be empty.");
  }
  this.lastName = lastName;
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

private boolean isAttributeValid(String attribute) {
  return attribute == null || attribute.isBlank();
}

private boolean isNumberValid(int number) {
  return number < 0;
}

@Override
public String toString() {
  return "Employee{" +
          "id=" + id +
          ", firstName='" + firstName + '\'' +
          ", lastName='" + lastName + '\'' +
          ", description='" + description + '\'' +
          ", jobYears= '" + jobYears + '\'' +
          '}';
}
```

- **EmployeeTest.java**: This file was updated to include unit tests ensuring the robustness of the `jobYears` field and overall attribute validation within the `Employee` class. Key aspects of the tests include:
  - **Validation of Correct Inputs**: Utilized `@ParameterizedTest` with `@CsvSource` to confirm that valid attribute values, including `jobYears`, result in successful object creation without exceptions.
  - **Validation of Invalid Inputs**: Implemented parameterized tests to ensure that invalid values, such as empty Strings or negative `jobYears`, correctly trigger exceptions, thus safeguarding object integrity.
  - **Null Value Testing**: Employed `@MethodSource` to verify that null values for String attributes trigger the appropriate validation exceptions, ensuring accurate error messaging.
  - **Getter and Setter Verification**: Confirmed through direct assertions that all getter and setter methods function as expected, correctly retrieving and assigning attribute values.
  - **Object Equality and Hashing**: Tested the `equals` and `hashCode` methods to verify accurate object comparison and hashing behavior, which is critical for collections and data handling.
  - **String Representation**: Validated the `toString` method to ensure it includes all essential details of the `Employee` object, aiding in effective debugging and logging.
  - **No-Argument Constructor**: Verified that the default constructor correctly initializes attributes, particularly ensuring that numeric fields default to zero and object fields to null.

```java
class EmployeeTest {

    @ParameterizedTest
    @CsvSource({
            "Frodo, Baggins, ring bearer, 5",
            "Bilbo, Baggins, burglar, 0"
    })
    void testValidEmployee(String firstName, String lastName, String description, int jobYears) {
        assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears));
    }

    @ParameterizedTest
    @CsvSource({
            "'', Baggins, ring bearer, 5",
            "Frodo, '', ring bearer, 5",
            "Frodo, Baggins, '', 5",
            "Frodo, Baggins, ring bearer, -1"
    })
    void testInvalidAttributesForEmployee(String firstName, String lastName, String description, int jobYears) {
        assertThrows(IllegalArgumentException.class, () -> new Employee(firstName, lastName, description, jobYears));
    }

    @ParameterizedTest
    @MethodSource("provideNullAttributes")
    void testNullAttributes(String firstName, String lastName, String description, int jobYears, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Employee(firstName, lastName, description, jobYears));
        assertEquals(expectedMessage, exception.getMessage());
    }
    static Stream<Arguments> provideNullAttributes() {
        return Stream.of(
                Arguments.of(null, "Baggins", "ring bearer", 5, "First name cannot be empty."),
                Arguments.of("Frodo", null, "ring bearer", 5, "Last name cannot be empty."),
                Arguments.of("Frodo", "Baggins", null, 5, "Description cannot be empty.")
        );
    }

    @Test
    void testGettersAndSetters() {
        Employee emp = new Employee("Frodo", "Baggins", "ring bearer", 5);
        emp.setId(1L);

        emp.setFirstName("Sam");
        emp.setLastName("Gamgee");
        emp.setDescription("gardener");
        emp.setJobYears(10);

        assertEquals(1L, emp.getId());
        assertEquals("Sam", emp.getFirstName());
        assertEquals("Gamgee", emp.getLastName());
        assertEquals("gardener", emp.getDescription());
        assertEquals(10, emp.getJobYears());
    }

  @Test
  void testSettersThrowExceptionForInvalidValues() {
    Employee emp = new Employee("Frodo", "Baggins", "ring bearer", 5);

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
  }

  @Test
    void testEqualsAndHashCode() {
        Employee emp1 = new Employee("Frodo", "Baggins", "ring bearer", 5);
        Employee emp2 = new Employee("Frodo", "Baggins", "ring bearer", 5);
        emp1.setId(1L);
        emp2.setId(1L);

        assertEquals(emp1, emp2);
        assertEquals(emp1.hashCode(), emp2.hashCode());

        Employee emp3 = new Employee("Bilbo", "Baggins", "burglar", 0);
        emp3.setId(2L);

        assertNotEquals(emp1, emp3);
        assertNotEquals(emp1.hashCode(), emp3.hashCode());
    }

    @Test
    void testToString() {
        Employee emp = new Employee("Frodo", "Baggins", "ring bearer", 5);
        emp.setId(1L);

        String empString = emp.toString();
        assertTrue(empString.contains("Frodo"));
        assertTrue(empString.contains("Baggins"));
        assertTrue(empString.contains("ring bearer"));
        assertTrue(empString.contains("5"));
    }

    @Test
    void testNoArgConstructor() {
        Employee emp = new Employee();

        assertNull(emp.getId());
        assertNull(emp.getFirstName());
        assertNull(emp.getLastName());
        assertNull(emp.getDescription());
        assertEquals(0, emp.getJobYears());
    }
}
```

- **DatabaseLoader.java**: This class, tasked with initializing the database with sample data, was updated to incorporate the `jobYears` attribute for sample employees.
This enhancement ensures that the application's new field functionality is demonstrated immediately upon launch.
The following code snippet highlights how `DatabaseLoader` was modified to include `jobYears` when creating sample employees.

```java
@Override
public void run(String... strings) throws Exception { // <4>
  this.repository.save(new Employee("Frodo", "Baggins", "ring bearer", 5));
}
```

- **app.js**: The React components within `app.js` were updated to display the new `jobYears` field in the employee list.
Both the `EmployeeList` and `Employee` components were modified to include an additional column labeled 'Job Years' in the rendered table.
This enhancement enables users to easily view the duration of each employee's tenure with the company alongside their other details.
The code snippet below demonstrates how the `jobYears` field was integrated into the application's frontend.

```javascript
class EmployeeList extends React.Component{
  render() {
    const employees = this.props.employees.map(employee =>
            <Employee key={employee._links.self.href} employee={employee}/>
    );
    return (
            <table>
              <tbody>
                <tr>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Description</th>
                  <th>Job Years</th>
                </tr>
                {employees}
              </tbody>
            </table>
    )
  }
}
```
```javascript
class Employee extends React.Component{
  render() {
    return (
            <tr>
              <td>{this.props.employee.firstName}</td>
              <td>{this.props.employee.lastName}</td>
              <td>{this.props.employee.description}</td>
              <td>{this.props.employee.jobYears}</td>
            </tr>
    )
  }
}
```

7. **Testing and Debugging Server and Client Components**

To ensure the seamless integration of the `jobYears` field, I executed the application using `./mvnw spring-boot:run` and tested its functionality at `http://localhost:8080/`.
This process allowed for real-time verification of the feature within the application's interface.
Additionally, I conducted a comprehensive code review, focusing on data handling on the server side and the correct display of the `jobYears` attribute on the client side.
This thorough examination was essential to confirm that the feature operated correctly, maintaining both functionality and code quality.

8. **Conclusion of the Assignment**

After confirming the stability and performance of the newly implemented feature, I committed the final changes to the repository, ensuring the commit message clearly described the enhancements.
The updated code was then pushed to the remote server to facilitate collaboration and maintain project continuity.
To highlight this major update, the commit was tagged as v1.2.0, adhering to the project’s semantic versioning approach.
Additionally, to signify the completion of this phase of the assignment, the repository was marked with the tag ca1-part1.1.

## Part 2: Development Using Branches

### Goals and Requirements
-   In the second part, the focus shifts to using branches for developing new features and fixing bugs.
-   There is a strong emphasis on creating isolated development environments and employing effective merge strategies.
-   Create dedicated feature branches for new developments or bug fixes, ensuring that changes do not affect the main codebase until they are fully ready for integration.
-   The section concludes by tagging the main branch after successful merges, marking new application versions and demonstrating efficient branch management in version control.

### Key Developments

In the second part, the approach shifted to leveraging branch-based development to further enhance the application’s features and resolve existing bugs, all while ensuring that the main branch remains stable for releasing production-ready versions.
Since the method for adding new features and fixing bugs is largely similar to what was described in Part 1, I will not duplicate all the code details here. 
The key distinction is the integration of branches.

1. **Start using the main branch**

To verify that I was on the correct branch specifically, the main branch used for releasing stable versions, I ran the `git branch` command.
This step was critical in the second part, as the current working branch is highlighted by an asterisk (*) in the output.

2. **Develop new features in branches**

During the development phase aimed at integrating an email field into our application, managing branches was essential.
I began by creating a dedicated feature branch named `email-field` to isolate all changes related to this functionality.
Once the branch was established, I switched my working context to `email-field` to start development.
To confirm that I was on the correct branch, I executed the `git branch` command, which verified the active branch.

```shell
git branch email-field
git checkout email-field
git branch
```

3. **Integration and Testing of the Email Field**

The process for integrating an email field into the application, along with ensuring strong validation, followed a similar approach to the implementation of the jobYears field in Part 1.
- **Code Implementation**: In line with the previous feature, I enhanced the Employee class by adding an email field along with its getter and setter methods. 
This required updating data models, forms, and views to ensure the new field was properly integrated into both the frontend and backend of the application.
- **Unit Testing**: As per the established methodology, I created comprehensive unit tests to verify that Employee instances are correctly instantiated with the new email field. 
These tests also enforce validation rules to ensure that the email attribute is neither null nor empty.
- **Debugging**: Both the server and client components of the application were thoroughly debugged to identify and resolve any issues related to the addition of the email field, ensuring a smooth and reliable user experience.

4. **Merge the code into the main branch**

Completing the email field feature required several steps to merge the changes into the main branch and update the application’s version.
First, the final modifications on the email-field branch were committed and then pushed to the remote repository, preparing the branch for merging.
A no-fast-forward merge was used to ensure that the commit history remained intact.
After merging, the updated main branch was pushed to the remote repository, and finally, the new version was tagged and pushed to mark this significant update.

```shell
# Commit the feature changes:
git add .
git commit -m "added e-mail fiel to the application"

# Push the feature branch upstream:
git push --set-upstream origin email-field

# Switch to the main branch and merge changes:
git checkout main
git merge --no-ff email-field

# Push the merged changes to update the main branch:
git push

# Tag the new version and push the tag:
git tag -a v1.3.0 -m "v1.3.0"
git push origin v1.3.0
```

After pushing the tag, I noticed that I hadn’t referenced the issue I had previously opened on GitHub.

```shell
# Commit an amend to the lat commit:
git commit --amend -m "added e-mail fiel to the application (#6)"

# Force-push to the repository, as it was only intended to amend the last commit:
git push --force
```

5. **Create a new branch to fix a bug**

In addressing the bug fix for email validation in the Employee class, a branch named `fix-invalid-email` was created following the established workflow.
The development, testing, and merging processes were consistent with those used for previous features and fixes, with a strong focus on maintaining code integrity and application stability.
The core of the bug fix involved enhancing the Employee class by implementing improved validation logic for the email field.
This logic ensures that the email includes an “@” character and adheres to the regex pattern "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", which verifies that the email address conforms to a robust and acceptable format.

```java
private boolean isEmailValid(String email) {
  if(email == null || email.isBlank()) {
    return false;
  }
  String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
  return email.matches(emailRegex);
}
```

6. **End of the assignment**

After applying the fix and thoroughly testing its effectiveness, the changes were merged into the master branch and the application version was updated to v1.3.1 to reflect the minor fix.
This version increment underscores the ongoing improvement in the application’s functionality and reliability. At the conclusion of the assignment, I tagged the repository with ca1-part1.2.
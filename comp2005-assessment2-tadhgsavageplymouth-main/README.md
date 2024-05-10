# Hospital Maternity Unit WebService API

## Overview
This project encompasses a Hospital Maternity Unit WebService that serves detailed information about patient admissions and staff allocations within a UK Hospital's Maternity Unit. It includes both a backend API and a desktop GUI application.

### API Details
- **Base URL**: [Hospital Maternity API](https://web.socem.plymouth.ac.uk/COMP2005/api/)
- **Swagger Documentation**: [API Documentation](https://web.socem.plymouth.ac.uk/COMP2005/api/index.html)

## Project Structure
The project is divided into two main parts:

### Part A: Web-Service API
Developed using Java with Spark framework, it provides endpoints to manage and retrieve maternity data effectively.

#### Features:
- **F1**: List all admissions for a specific patient.
- **F2**: List of currently admitted patients who have not been discharged.
- **F3**: Identify the staff member with the most admissions.
- **F4**: List of staff members with no admissions.

### Part B: Desktop Application
A Java Swing UI application that connects to the API to allow user interaction with the maternity data.

## API Endpoints

### Admissions
- **GET /Admissions**: Returns all patient admissions.
- **GET /Admissions/{id}**: Returns details of a specific admission.

### Allocations
- **GET /Allocations**: Shows all allocations of employees to admissions.
- **GET /Allocations/{id}**: Details of a specific allocation.

### Employees
- **GET /Employees**: Lists all employees.
- **GET /Employees/{id}**: Details of a specific employee.

### Patients
- **GET /Patients**: Lists all patients.
- **GET /Patients/{id}**: Details of a specific patient.

## Models
- **Admission**: Includes fields like `id`, `admissionDate`, `dischargeDate`, and `patientID`.
- **Allocation**: Fields include `id`, `admissionID`, `employeeID`, `startTime`, and `endTime`.
- **Employee**: Consists of `id`, `surname`, `forename`.
- **Patient**: Includes `id`, `surname`, `forename`, `nhsNumber`.

## Testing
Comprehensive testing strategies include:
- Unit testing with mock objects.
- Integration testing.
- System testing.
- Usability testing for the GUI application.

## Build and Run
### Requirements
- Java 17
- Gradle

### Building the Project
To build the project, run the following command in the terminal:
```bash
gradle build
```
### Running the API
To start the web service, execute:
```bash
gradle run
```
### Running Tests
Execute the following command to run all tests:
```bash
gradle test
```

## Alternatively Run with IntelliJ IDEA
### Step 1: Open IntelliJ IDEA

- **Launch** IntelliJ IDEA.
- Clone GitHub Repo into a preffered directory
- If you see the Welcome screen, click on **"Open or Import"**. If IntelliJ IDEA is already open, select **File > Open** from the menu.

### Step 2: Import the Project

- **Navigate** to the directory where your project is located.
- **Select** the `build.gradle.kts` file or the directory containing it.
- **Click** "OK".
- If prompted to **"Open as Project"**, choose this option.
- IntelliJ will ask if you want to import the Gradle project. Select **"Import as a Gradle project"** and click **"OK"**.
- Allow some time for IntelliJ to index and set up the project.

### Step 3: Configure the JDK

- Go to **File > Project Structure**.
- In the **Project** tab, set the **Project SDK** to Java 17. If Java 17 is not listed:
    - Click **"New..."** and then select JDK. Navigate to your JDK 17 path or download it if prompted.
- Make sure the **language level** reflects Java 17 compatibility.
- Click **"Apply"** and then **"OK"**.

### Step 4: Running the Application

- In the **Project** pane on the left, navigate to the `src/main/java` directory and locate your main application class (`App.java` for your API).
- Right-click the file and select **"Run 'App.main()'"** or open the file and click the green play button beside the main method in the gutter.
- IntelliJ will build and run your application.
- To check if the application is running, you can look at the output in the **Run** tab at the bottom of the IDE.

### Step 5: Running Tests

- To run tests, navigate to the `src/test/java` directory.
- You can right-click on individual test classes or the whole directory and select **"Run Tests in ..."**.
- Test results will be displayed in the **Run** window, showing which tests passed or failed.

## Contact
- **Student**: Tadhg Savage: tadhg.savage@students.plymouth.ac.uk
- **Lecturer**: [Mark Dixon](https://www.plymouth.ac.uk/staff/mark-dixon)
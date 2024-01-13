Requirements
---
In order to run this application, you will need the following things:

* Java (version 17+)
* Maven ([installation guide available here](https://maven.apache.org/install.html))
* Internet Access (to connect to the preconfigured database)
* Web Browser

Starting the Application
---
This project is also available in the following Git repository:
https://github.com/aangu5/LabTrack

Steps to start the application:
1. Ensure the requirements listed above are met and can run Maven commands in the terminal.
2. Open the root directory of the project in a terminal. If cloned from git, it will likely be LabTrack.
3. Run "mvn clean install" in the terminal, this will ensure all necessary packages are installed and all tests run successfully.
4. Run "mvn spring-boot:run" in the terminal, this will start the application.
5. Access the application in a web browser (ideally Google Chrome or Mirosoft Edge) by going to "localhost:8080" where you will be shown the login screen.

Important Information
---
This version of the application is pointed to a PostgreSQL database hosted by one of the authors.
This can be changed to any other database if required and can be configured in:

src/main/resources/application.properties

The database is configured to update or create any tables necessary to start the application

This application has been stored in 

Credentials for the application:
* Username: pedro.baptistamachado@ntu.ac.uk
* Password: P03rddr30p!
# FileCloud
Browser-based file storage solution using Spring Boot

This application was my introduction to the Spring Boot framework.  Please let me know if you see areas for improvement.  It runs a tomcat server on the host computer that allows clients to upload and download files up to 200MB from the browser.  It was created using macOS 10.11.6, netbeans version 8.2, java version 1.8.0_191, postgreSQL version 11.1-1, and other software as defined in pom.xml.  It includes an administrator user with default password 'password' who is responsible for creating and deleting user accounts.  The password for the administrator account should be changed as soon as possible.
Once a user account is created, that user may log in with the password provided by the administrator and change the password for his or her account. 

## Preliminary Setup ##
These steps are current for macOS users as of Dec. 11, 2018.
1.  [Install JDK 8 Update 191](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2.  [Install Netbeans 8.2 Java EE version](https://netbeans.org/downloads/)
3.  [Install PostgreSQL using interactive installer by EnterpriseDB](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

## Necessary Configuration ##
1.  PostgreSQL
	1.  Navigate to /Library/PostgreSQL/11/bin
	2.  Create new role: `sudo -u postgres ./createuser --interactive --password fileCloud`
		1.  The new role need not be a superuser
		2.  The new role may create new databases
		3.  The new role need not create more new roles
	3.  Create new database owned by fileCloud role: `sudo -u postgres ./createdb fileclouddb -O fileCloud`
	4.  Note that the role name: `fileCloud` and the database name: `fileclouddb` can be changed, but they must match their counterparts in the `application.properties` file
		1.  spring.datasource.url=jdbc:postgresql://localhost:5432/fileclouddb
		2.  spring.datasource.username=fileCloud
	5.  Finally, enter the password for the fileCloud role in `application.properties`: spring.datasource.password=CHANGE_ME
2.  IP Address
	1.  Retrieve host IP address from System Preferences-->Network IP Address field
	2.  Enter this address at `application.properties`: server.address=CHANGE_ME
3.  HTTPS Configuration
	1.  Enter `keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 3650`
	2.  Enter a password at the two prompts and place it in `application.properties`: server.ssl.key-store-password=CHANGE_ME
	3.  Copy keystore.p12 to src/main/resources/
4.  Uploads Directory
	1.  Files uploaded by users are stored in /src/main/resources/uploads/ by default
	2.  The location of this directory is set in `application.properties`: upload.file.directory=classpath:/uploads/

## Build and Run ##
1.  Clone this repository or download its zipped version

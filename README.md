# FileCloud
Browser-based file storage solution using Spring Boot

This application was my introduction to the Spring Boot framework.  Please let me know if you see areas for improvement.  It runs a tomcat server on the host computer that allows clients to upload and download files up to 200MB from the browser.  It was created using macOS 10.11.6, netbeans version 8.2, java version 1.8.0_191, postgreSQL version 11.1-1, and other software as defined in pom.xml.  It includes an administrator user with default password 'password' who is responsible for creating and deleting user accounts.  The password for the administrator account should be changed as soon as possible.
Once a user account is created, that user may log in with the password provided by the administrator and change the password for his or her account. 

## Resources Used ##
As this was my first time using Spring Boot, I found it necessary to consult many resources.  I have tried to list them all here, but if any of the code I present here was influenced by some other resource then I will gladly cite it or remove this repository.  Likewise, if any of the authors presented here have an issue, please contact me.  I do not intend any infringement.
1.  [User Management author unknown](https://spring.io/guides/gs/securing-web/)
2.  [https Support](https://www.thomasvitale.com/https-spring-boot-ssl-certificate/) [by Thomas Vitale](https://www.thomasvitale.com/)
3.  [H2 Configuration](https://springframework.guru/using-the-h2-database-console-in-spring-boot-with-spring-security/) [by John Thompson](https://springframework.guru/about/)
4.  [Password Hashing](https://hellokoding.com/registration-and-login-example-with-spring-security-spring-boot-spring-data-jpa-hsql-jsp/) [by Giau Ngo](https://hellokoding.com/author/giau/index.html)
5.  [File Upload/Download Using JPA](https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/) [by Rajeev Kumar Singh](https://www.callicoder.com/about/)
6.  [File Storage and Exception Handling](https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/) [by Rajeev Kumar Singh](https://www.callicoder.com/about/)
7.  [JPA/Hibernate One-to-Many Relationship](https://www.callicoder.com/hibernate-spring-boot-jpa-one-to-many-mapping-example/) [by Rajeev Kumar Singh](https://www.callicoder.com/about/)
8.  [PostgreSQL setup](http://zetcode.com/springboot/postgresql/) [by Jan Bodnar](https://github.com/janbodnar)

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
	1.  Retrieve host IP address from System Preferences --> Network --> IP Address field
	2.  Enter this address at `application.properties`: server.address=CHANGE_ME
3.  HTTPS Configuration
	1.  In terminal enter `keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650`
	2.  Enter a password at the two prompts and place it in `application.properties`: server.ssl.key-store-password=CHANGE_ME
	3.  Copy keystore.p12 to src/main/resources/
4.  Uploads Directory
	1.  Files uploaded by users are stored in /src/main/resources/uploads/ by default
	2.  The location of this directory is set in `application.properties`: upload.file.directory=classpath:/uploads/

## Build and Run ##
1.  Clone this repository or download its zipped version
2.  Open as a project in Netbeans
3.  Make necessary changes to `application.properties`

Text in properties file | Updated value
--- | ---
server.address=CHANGE_ME | IP Address from section 2 above
spring.datasource.password=CHANGE_ME | datasource password from section 1 above
server.ssl.key-store-password=CHANGE_ME | keystore password from section 3 above

4.  Clean and build project with alt + F11
5.  Use terminal to navigate to /target/
6.  Enter: `java -jar file-cloud-0.1.0.jar`
7.  Open a browser and enter: `https://[IP_ADDRESS]:8443`
	1.  Accept the self-signed certificate if prompted
8.  Enter administrator and password as username and password
9.  Click the link to navigate to the welcome page
10.  Click "Manage Account"
12.  Change password for the administrator user
13.  At this point you are ready to create/delete users and upload/download files

## Additional Notes ##
1.  If an error about tomcat finding a premature EOF is encountered, be sure that the keystore.p12 generated in step 3 of the necessary configuration was copied to the resources directory.
2.  No default sites for error or forbidden conditions have been configured.
3.  Multiple warnings regarding nonexistent relations (e.g. "files" and "users_roles") appear when launching the jar.  These are believed to be caused by a lack of schema definition.  Please reach out if you know the solution.

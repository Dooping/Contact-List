# Contact List
## Software Security Final Project
### Database
 
Informations about DB 

- Program: mySql
- run the ss.sql file provided
	
The SS database will be created with all the necessary tables and users for testing purposes.

The user and password for access to the database is 'root' and 'root', this values can be changes in the class DatabaseConnection.java by changing the constants USER and PASSWORD.

### Pre-requisites

To run the project it is necessary the following resources:

	- Apache Taglibs: https://tomcat.apache.org/download-taglibs.cgi 
	- Json Simple: http://www.java2s.com/Code/Jar/j/Downloadjsonsimple111jar.htm
	- Mysql Connector: https://dev.mysql.com/downloads/connector/j/ ou https://mvnrepository.com/artifact/mysql/mysql-connector-java/5.1.40

### Test Application
	
- Admin ( name:root ): This user can do login, create new users, change own password, block users and do logout. 

- Normal User ( name: 1, 2, 3, 4, 5 ): This type of user can do login, logout and change own password.

- The default password of all created users is 'root'
	

### Points to consider

It was designed and implemented a simple authenticator and user management system.


By default, profile data (job, birth date, age, place of birth, location), contacts (email, phone number) and internal description are only available for friends.
The external description is available for everyone.
The friendlist is private.
It is possible to change all these visibility levels in the settings.

If a user wants to access a resource that is only visible to friends, he can make a friendship request and when the other user accepts he will be able to see all resources with internal permission.

It was used a nouce of the resource's owner to sign Capabilities, thus allowing the system to verify if the capabilities were generated correctly, and also allowing the user to change permissions of the resources and revoke emitted capabilities from settings by just changing the nounce.

The user 'root' when seeing the full list of users (Contact List), sees also the users that are locked with a dark grey background, while the normal users only see non-locked users.

#### Final grade: 17/20

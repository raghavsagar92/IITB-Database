
### How to use my code:

1. My code in Java SE, with following modules used:
  1. [JDBC](http://en.wikipedia.org/wiki/Java_Database_Connectivity) for Postgresql connection
  2. LDAP Class Libraries for Java ([JLDAP](http://www.openldap.org/jldap/))
2. This repo has 2 directories:
  * __src__: contains all the source code
  * __libs__: contains all external libraries used
3. Setting up in IDE:
  * You can use any IDE for Java SE, import the source from the 'src' directory.
  * Import all dependencies by adding all JARs from 'libs' directory.
4. Changes to be made:
  * __src/db/DbConfig.java__: Change variables 'driverName', 'url', 'username' and 'password' according to your database specifications.
  * __src/utils/Config.java__: Change variable 'LDAP_DUMP_FILE' as per your requirements.
5. You are ready now, you can build and run the project.


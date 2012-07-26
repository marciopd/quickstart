ejb-multi-server: EJB applications deployed in different servers communicating vie EJB remote calls
======================================================
Author: Wolf-Dieter Fink
Level: Intermediate
Technologies: EJB remote standalone application


What is it?
-----------

This example demonstrates the communication between standalone clients and JBoss AS7 application server

Also the configuration is done by CLI batch-scripts.


The example is composed of multiple maven projects, each with a shared parent. The projects are as follows:

ic-properties : Different Main classes to demonstrate how the new scoped InitialContext can be used

The root `pom.xml` builds each of the subprojects in the above order.



System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Configure Optional Components
-------------------------

Contributor: If your quickstart requires any additional components, decribe how to set them up here. If your quickstart requires a secured user, PostgreSQL, or Byteman, you can link to the instructions in the README file located in the root folder of the quickstart directory. Here are some examples:

This quickstart requires the ejb-multi-server project. Please follow the instruction of these project to ensure that the JBoss-domain is well configured and the application is correct deployed. You may not need the point "Access the Application" but if you run it you are sure that the project works so far.

Start and configure JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

With this quickstart scripts are provided to extend the configuration and deployments of the multi-server quickstart.

 * Make sure you have build the multi-server project.
 * Make sure you have started the JBoss Server as described in the multi-server project.
 * navigate to the project root directory and run the following command
 
     `JBOSS_HOME/bin/jboss-cli.sh --connect --file=extend-domain.sh`
     
 * TODO (necessary?) Restart the domain. This is necessary because of some configurations, otherwise the deploy will not work.


Build and access the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started and configured the JBoss Server successful as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean package
        


Access the ic-properties application
---------------------

1. Make sure that the deployment are successful as described above.
2. navigate to the ic-properties root directory of this quickstart.
3. Type this command to run the application

        `mvn exec:mvn exec:java -Dexec.mainClass="org.jboss.as.quickstarts.ejb.clients.MultiContentClient`
        
        The output of the client will show you that the applications are invoked as expected on different servers:
        
					Call successfully reached the server 'master:app-one'
					Call successfully reached the server 'master:app-oneA'

        Also messages if a invocation can be reach different server:

					Call successfully reached the server 'master:app-one' out of [master:app-one, master:app-oneA]
					Call successfully reached the server 'master:app-oneA' out of [master:app-one, master:app-oneA]

        In the logfiles of the different servers you might follow the invocations on server-side.



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc


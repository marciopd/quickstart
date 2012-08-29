ejb-multi-server: EJB applications deployed in different servers communicating vie EJB remote calls
======================================================
Author: Wolf-Dieter Fink
Level: Intermediate
Technologies: EJB, EAR


What is it?
-----------

This example demonstrates the communication between applications deployed to different servers.
Each application is deployed as an EAR that contains a simple EJB3.1 bean which only log the invocation.

Also the configuration is done by CLI batch-scripts.


The example is composed of multiple maven projects, each with a shared parent. The projects are as follows:

app-*   : Simple application contain an ejb and ear sub-project to build the ejb.jar and app.ear file
          The application is only one EJB that log a statement if called and return the jboss.node.name

app-main: Application which can is called by the 'client' and call the different sub-applications

client  : This project builds the standalone client and execute it

The root `pom.xml` builds each of the subprojects in the above order.



System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start and configure JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

With this quickstart scripts are provided to configure a standard JBoss AS7 or EAP6 via CLI batch scripts.

 * Unzip or install a fresh AS7 or EAP6 instance
 * Start the instance
 
     `bin/domain.sh`
     
 * navigate to the project root directory and run the following command
 
     `JBOSS_HOME/bin/jboss-cli.sh --connect --file=install-domain.cli`
     
 * Add application user  [Add an Application User](../README.md#addapplicationuser)
   To add all necessary users run the following commands, please use this usernames and paswords because the domain configuration and the client use it.
   
     `bin/add-user.sh -a -u quickuser -p quick-123 --silent`
     `bin/add-user.sh -a -u quickuser1 -p quick123+ --silent`
     `bin/add-user.sh -a -u quickuser2 -p quick+123 --silent`
 


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started and configured the JBoss Server successful as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install
        
4. Deploy the applications by using the provided CLI batch script

       `JBOSS_HOME/bin/jboss-cli.sh --connect --file=deploy-domain.cli`
       
     This will deploy the app-*.ear files to different server-groups of the running domain.


Access the application
---------------------

1. Make sure that the deployment are successful as described above.
2. navigate to the client root directory of this quickstart.
3. Type this command to run the application

        `mvn exec:exec`
        

        The output of the client will show you a simple line with the informations provided by the different applications:
        
          InvokeAll succeed: MainApp[anonymous]@master:app-main  >  app1[anonymous]@master:app-one

        The line shows that the MainApp is called with the user 'anonymous' at node 'master:app-main' and the sub-call is proceeded by the 'master:app-one' node.
        In the logfiles of the different servers you might follow the invocations on server-side.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

       `JBOSS_HOME/bin/jboss-cli.sh --connect --file=undeploy-domain.cli`




Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc


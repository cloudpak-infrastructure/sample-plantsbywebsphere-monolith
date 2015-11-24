# Evolving a monolithic Java EE application to microservices

Examine what it means for a Java EE developer to evolve a monolithic application into a microservices architecture. The Plants By WebSphere sample is used as a representative monolithic application.

## Background

Plants by WebSphere is an Internet storefront application that specializes in the sale of plants and gardening tools. Using the Plants by WebSphere store front, customers can open accounts, browse for items to purchase, view product details, and place orders.

This sample is designed using a typical Java EE web Model-View-Controller (MVC) pattern. The MVC pattern provides separation of concerns for persistent data, presentation of information, and the control logic that is used to tie them together. The sample has been updated to utilize the following technologies:

* For the presentation tier, Plants By WebSphere uses a combination of JSF 2.0 and Facelets.
* The controllers are a combination of Managed Beans and EJB session beans.
* The model portion is generally handled by JPA, though some presentation side wrapping is done to provide additional presentation support. A Derby database provides persistence for a catalog, inventory of items, order handling, as well as a simple user registry.
* All of the above is tied together using CDI.

# Installation
Full installation instructions are in the docs directory, but the following extract provides a quick start

The Plants By WebSphere sample uses an enhanced EAR to capture the deployment configuration information for the EAR. This information creates the resources required by the application to access the Derby database as well as a resource for a mail provider. The mail provider resource is a dummy mail provider that will attempt to send mail to a fictitous SMTP server.

The enhanced EAR data was configured using RAD and saved as part of the EAR file metadata in the pbw-ear sub-project. If you want to change the location of the Derby database, you will also need to update the resource configuration within the enhanced EAR metadata and rebuild the sample.

Applications may be installed in WebSphere by copying the EAR file to a well known location and can be uninstalled by removing the EAR file from that location. However, this is not turned on by default. Monitoring may be turned on by using the following command:

* In the Integrated Solutions Console, click Applications > Global deployment settings.
* Click the check box to Monitor directory to automatically deploy applications. Note: remember the directory location used here.
* Apply the settings, Save your changes, and restart the server.

Once this is complete, the Plants by WebSphere application may be installed by copying it to the directory location specified in the Global deployment settings panel.*

# Further reading
[Starting the evolution to microservices (using the Plants By WebSphere sample)](https://developer.ibm.com/wasdev/docs/starting-evolution-microservices-using-plants-websphere-sample/)

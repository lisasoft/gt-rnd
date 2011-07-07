Welcome to a quick geotools prototype working showing how to integrate Features and POJOs
using the gt-swing module.

To build this project (or even load it in your IDE) you will need:
- Java 6 or newer
- Maven 2.2.1 or newer
- Eclipse 3.6 or newer

Quick start:

0. mvn install
1. mvn eclipse:eclipse
   
   This will generate the .project and .classpath projects allowing
   the project to be loaded in eclipse.
2. In eclipse set up the following:
- Windows --> Preferences
- Classpath Variables
- Add a classpath variable
- M2_REPO = ~/.m2/repository
  M2_REPO = /Users/YOURNAME/.m2/repository

3. Import the project
- File --> Import to open the Import Wizard
- General --> Existing Projects into Workspace
- Locate the root directory and import the project into your workspace
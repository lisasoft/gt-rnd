Welcome to a quick geotools prototype working showing how to integrate Features and POJOs
using the gt-swing module.

To build this project (or even load it in your IDE) you will need:
- Java 6 or newer
- Maven 2.2.1 or newer
- Eclipse 3.6 or newer

HOW TO RUN COMMAND LINE

0. mvn install
1. mvn exec:java -Dexec.mainClass="com.lisasoft.face.Prototype"

HOW TO RUN ECLIPSE

1 From the command line:
     
     mvn eclipse:eclipse
   
  This will generate the .project and .classpath projects allowing the project to be loaded in eclipse.

2 In eclipse set up the following:
- Windows --> Preferences
- Classpath Variables
- Add a classpath variable
- M2_REPO = ~/.m2/repository
  M2_REPO = /Users/YOURNAME/.m2/repository

3 Import the project
- File --> Import to open the Import Wizard
- General --> Existing Projects into Workspace
- Locate the root directory and import the project into your workspace

4. Right click on "Prototype" and run as a Java application


h2. Tasks

0. SH: Commit change to EPSG code so that the triangles show up in the correct location.
1. SH: Use a list selection model on the Table to update the selectedLayer filter; using the
   existing code as an example of selection using a set of feature IDs.
2. SH: DuplicatingStyleVisitor needed to make relative icon references be absolute
   (alternatives listed below) but this one we can perform in our application. See examples
   in geotools docs; should be a class with one method overriden to peform the transformation.
   similar to XSLT but for object data strutures.
   This is used to allow the correct Icon for "Face" display.
   Note the style can also use a Label (in blue) and a line from the point location to the label
   offset. This line can be drawn with a line symbolizer using a geometry function (rather than
   just a propertyName reference).
3. JG: DAO is code complete producing features; need to improve test coverage
   and then migrate Prototype to use it use it (in feature form).
4. SH: Create a table model based on the DAO object List<Face>
5. ML: Refactor working code from prototype to MapComponent; tools will need to be seperate classes
   etc...
6. SH: Ask MapComponent to work with Objects directly for the table model
7. SH: Ask MapComponent to store the selected features; and base the selected layer and tool selection
   and list selection model off this
8. Review requirements for supported status with mbedward (see below)

h2. Feedback on gt-swing

The following issues were identified that made coding more verbose than needed:

* MapMouseEvent ReferencedEnvelope based on number of pixels
  
  https://jira.codehaus.org/browse/GEOT-3715 

* Relative paths for ExternalGraphics (ie Icon references) is not well supported
  
  Options (these are alternaitves):
  
  - Preprocess Style using a visior to rewrite relativ paths
  - Update ImageGraphicFactory or SLDStyleFactory to handle relative file paths
  - Use env vairable subst: so ${base}/face.png would refer to the SLD document base

Required work:

* MapContext is deprecated; the adption to use the MapContent is progressing as we speak
  on trunk showing active maintence of the library by the module maintainer.
  
* JMapPanel can use a tutoiral on how to embeded it into your own application. The JMapFrame
  class is set up for tutorials; and does not serve as a good example itself (too many helper
  methods that are "off topic").
  
* The documentation is out of date:
  
  http://docs.geotools.org/latest/userguide/unsupported/swing/jmappane.html#
  
* Test case coverage is low; may be able to make the argument that the coverage by the tutorial
  code is more than sufficient to keep the API stable

* Small trouble sharing data between base map and selection layer
  https://jira.codehaus.org/browse/GEOT-3716
    
* JMapPane handles a single background MapContent; it would be good to use a second BufferedImage
  for draw quick feedback on (such as feedback from tools).
  Motivation: This would prevent the entire map needing to redraw to show selection.
 
* Ability to change the icons for the Actions would be nice; this is a low priority

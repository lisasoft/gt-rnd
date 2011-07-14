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

1. JG: DuplicatingStyleVisitor needed to make relative icon references be absolute
   (alternatives listed below) but this one we can perform in our application. See examples
   in geotools docs; should be a class with one method overriden to peform the transformation.
   similar to XSLT but for object data strutures.
   This is used to allow the correct Icon for "Face" display.
   Note the style can also use a Label (in blue) and a line from the point location to the label
   offset. This line can be drawn with a line symbolizer using a geometry function (rather than
   just a propertyName reference).
2. JG: Review requirements for supported status with mbedward (see below)
3. JG: Go over assessment of this solution/approach

h2. Feedback on gt-swing

The following issues were identified that made coding more verbose than needed:

* Unable to smoothly ask the map to redraw itself in response to selection changes; since we do
  not have a layer list this was difficult. The JMapComponent needs a method to accept
  MapChangedEvents and trigger the appropriate redraw activity.
  (This was actually amsuing as we would call JComponent.repaint() and get the same map generated
   as the component draws into a back buffer).
* MapMouseEvent ReferencedEnvelope based on number of pixels: https://jira.codehaus.org/browse/GEOT-3715
  Update this issue has been closed (2 day response time).

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

* The name JMapPane actually caused real confusion; because it extends JPanel :-)

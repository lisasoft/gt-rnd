Welcome to a quick geotools prototype working showing how to integrate Features and POJOs
using the gt-swing module.

To build this project (or even load it in your IDE) you will need:
- Java 6 or newer
- Maven 2.2.1 or newer
- Eclipse 3.6 or newer

h2. How to Run in Eclipse

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

h2. Assesment of Prototype

The prototype was procduced promptly with a small team; documenation for the GeoTools library
was good; even for this "unsupported" gt-swing module. This is helped by the fact that the
gt-swing code is used in all the tutorials.

The prototype illustrates how to remap a java bean as a "Feature" for display. Feature are literally
a feature of map that are drawn. This work took one day and proceeded smoothly; the only wrinkle was
changing styles over to use the Java Bean wrapper; as some of the property names had changed names
from the origional sample data.

The GeoTools styling system is more than capable showing great rendering performance.

The simulated data access object was sufficent to run the prototype but did not explore fully
the kind of notifcations and queries required to work with large amounts of data. In particular
a bounding box based query would be useful evaulation point when hooking up to a proper OR mapper.

The tool API was easy to follow with clear seperation of concerns; we would like a better technque
then changing the cursor in order to provide visual feedback during editing. This is best 
accomplished using the Swing glass pane layer; or by adding the concept of a transparent
layer to the JMapPane component.

The JMapComponent renderings as single buffered image in a background thread; we woudl prefer
to control the number of images produced; in order to isolate "base map" data from the layer
comprising the buisness objects. This would allow the map to redraw with out flickering.

We recommend this approach based on our knowledge of your development team; it would be good
to evaulate the intergration of this component with your existing work in order to focus
on any performance or notification concerns.

We wish AGP the best of luck with their continued development.

h2. Feedback on gt-swing

Required work to make gt-swing supported:

* MapContext is deprecated; the adption to use the MapContent is progressing as we speak
  on trunk showing active maintence of the library by the module maintainer.
  
* JMapPanel can use a tutoiral on how to embeded it into your own application. The JMapFrame
  class is set up for tutorials; and does not serve as a good example itself (too many helper
  methods that are "off topic").
  
* The documentation is out of date:
  
  http://docs.geotools.org/latest/userguide/unsupported/swing/jmappane.html#
  
* Test case coverage is low; may be able to make the argument that the coverage by the tutorial
  code is more than sufficient to keep the API stable
    
* JMapPane handles a single background MapContent; it would be good to use a second BufferedImage
  for draw quick feedback on (such as feedback from tools).
  Motivation: This would prevent the entire map needing to redraw to show selection.
 
* Ability to change the icons for the Actions would be nice; this is a low priority

Trvial feedback (raised as issues in the issue tracker):
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

* Small trouble sharing data between base map and selection layer
  https://jira.codehaus.org/browse/GEOT-3716
  
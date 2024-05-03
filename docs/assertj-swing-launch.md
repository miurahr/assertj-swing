# Launching

### Applications from a `main()` method

AssertJ Swing supports launching an application from its `main()` method. It is quite
easy, and requires only one line of code. You only need to use the
<a href="swing/api/org/assertj/swing/launcher/ApplicationLauncher.html" target="_blank">`ApplicationLauncher`</a>.

Let's assume we have the following application

```java
public class JavaApp {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JFrame frame = new JFrame("Java Application");
        frame.setPreferredSize(new Dimension(200, 200));
        frame.pack();
        frame.setVisible(true);
      }
    });
  }
}
```

We can launch this application programmatically either by the main class or its fully qualified name:

```java
//import static org.assertj.swing.launcher.ApplicationLauncher.*;

// 1. by class object without arguments
application(JavaApp.class).start();
// 2. by class object with arguments
application(JavaApp.class).withArgs("arg1", "arg2").start();
// 3. by qualified name without arguments
application("com.mycompany.JavaApp").start();
// 4. by qualified name with arguments
application("com.mycompany.JavaApp").withArgs("arg1", "arg2").start();
```


Once the application is started, we can get a reference of its `JFrame`. In this example we're
using a `WindowFinder`. For more information about `WindowFinder`, please visit
<a href="assertj-swing-lookup.html#long-duration">Testing Long-Duration Tasks</a>.

```java
// import static org.assertj.swing.finder.WindowFinder.findFrame;

FrameFixture frame = findFrame(new GenericTypeMatcher&lt;Frame&gt;() {
  protected boolean isMatching(Frame frame) {
    return "Java Application".equals(frame.getTitle()) && frame.isShowing();
  }
}).using(robot);
```

### Applets

AssertJ Swing provides its own <em>applet viewer</em> to support GUI testing of Java applets. Testing an
applet involves three steps: start the applet, the actual test and resource cleanup.

#### Start the applet

There are two ways to start an applet:

##### Using an `AppletLauncher` to start an `AppletViewer`

The class `AppletLauncher` provides a fluent interface for launching an applet:

```java
AppletViewer viewer = AppletLauncher.applet("org.assertj.swing.applet.MyApplet").start();
// or
AppletViewer viewer = AppletLauncher.applet(MyApplet.class).start();
// or
AppletViewer viewer = AppletLauncher.applet(new MyApplet()).start();
```

In addition, we can pass parameters to the applet. This works as if you'd specify them in the
`param` tag of the embedding `HTML`:

```java
AppletViewer viewer = AppletLauncher.applet(new MyApplet())
                                    .withParameters(
                                       name("bgcolor").value("blue"),
                                       name("color").value("red"),
                                       name("pause").value("200")
                                     )
                                    .start();

// or

Map&lt;String, String&gt; parameters = new HashMap&lt;String, String&gt;();
parameters.put("bgcolor", "blue");
parameters.put("color", "red");
parameters.put("pause", "200");

AppletViewer viewer = AppletLauncher.applet(new MyApplet()).withParameters(parameters).start();
```


##### Starting an `AppletViewer` directly

The `AppletLauncher` provides a simple and compact API to start applets in an
  `AppletViewer`. The created `AppletViewer` uses basic implementations of
  `AppletStub` and `AppletContext` though. If you need to test an applet with more
  advanced implementations of `AppletStub` or `AppletContext`, you can use
  `AppletViewer` directly, passing your own `AppletStub` (which will return your own
  `AppletContext`).

```java
AppletViewer viewer = AppletViewer.newViewer(new MyApplet(), new MyAppletStub());
```


#### Test the applet

Once we have an `AppletViewer` up and running the applet to test, we simply can <em>wrap</em>
the `AppletViewer` with a `FrameFixture` and start testing.

```java
private AppletViewer viewer;
private FrameFixture applet;

@BeforeMethod public void setUp() {
  viewer = // get the viewer using AppletLauncher or create a new AppletViewer
  applet = new FrameFixture(viewer);
  applet.show();
}

@Test public void shouldChangeLabelOnButtonClick() {
  applet.button("ok").click();
  applet.label("text").requireText("Hello");
}

@AfterMethod public void tearDown() {
  viewer.unloadApplet();
  applet.cleanUp();
}
```


#### Resource cleanup

As our previous example showed, in addition to call `cleanUp()` we need to unload the applet by
calling `unloadApplet` in the `AppletViewer` used in our test.
# Basics of AssertJ Swing

This section introduces the basics of AssertJ Swing to you. The basics are:

- <a href="#base-test-case">Base test class</a>
- <a href="#configuration">Configuration</a>
- <a href="#verifications">Verifications with AssertJ Swing</a>
- <a href="#verifying-colors-fonts">Special case: font and color verifications</a>

### Base test class

AssertJ Swing provides base test case classes that take care of most of the plumbing involved when writing a
GUI test. This feature is available for TestNG via `AssertJSwingTestngTestCase` and for JUnit via
`AssertJSwingJUnitTestCase`.

In general, the provided test classes do the following

1. Install `FailOnThreadViolationRepaintManager` to check that all access to Swing components
    is performed in the EDT
2. Create a new `Robot`, using a new component hierarchy
3. Clean up resources (e.g. release the semaphore that ensures sequential test execution)

> **_Do not create a new Robot!_**
> When using a base test case, do not create a new `Robot`. The base test
>  case creates one for you! If there is more than one `Robot` in your test, <strong>only the
>  first one will have access to the screen</strong>, while the rest will block till they get the
>  **screen lock**. A `Robot` can be created manually or indirectly using the constructors
>  `FrameFixture(Frame)` or `DialogFixture(Dialog)`. Please use the overloaded
>  versions that take a `Robot` as parameter, passing the already created `Robot`
>  (`robot()`).


To compare both scenarios (with and without using the base test cases), here is the example code from
the <a href="assertj-swing-getting-started.html">getting started</a> site. Without base test case:

```java
public class SimpleCopyApplicationTest {
  private FrameFixture window;

  @BeforeClass
  public static void setUpOnce() {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp() {
    SimpleCopyApplication frame = GuiActionRunner.execute(() -&gt; new SimpleCopyApplication());
    window = new FrameFixture(frame);
    window.show(); // shows the frame to test
  }

  @Test
  public void shouldCopyTextInLabelWhenClickingButton() {
    window.textBox("textToCopy").enterText("Some random text");
    window.button("copyButton").click();
    window.label("copiedText").requireText("Some random text");
  }

  @After
  public void tearDown() {
    window.cleanUp();
  }
}
```


And the following is with using the base test case:

```java
public class SimpleCopyApplication_UseBaseTest extends AssertJSwingJUnitTestCase {
  private FrameFixture window;

  @Override
  protected void onSetUp() {
    SimpleCopyApplication frame = GuiActionRunner.execute(() -&gt; new SimpleCopyApplication());
    // IMPORTANT: note the call to 'robot()'
    // we must use the Robot from AssertJSwingJUnitTestCase
    window = new FrameFixture(robot(), frame);
    window.show(); // shows the frame to test
  }

  @Test
  public void shouldCopyTextInLabelWhenClickingButton() {
    window.textBox("textToCopy").enterText("Some random text");
    window.button("copyButton").click();
    window.label("copiedText").requireText("Some random text");
  }
}
```


### Configuration

AssertJ Swing provides the class `org.assertj.swing.core.Settings` which holds the configurable
properties. There are properties that can be modified at runtime. These options include scope of component look-ups, delays
and timeouts for different actions, and many more.

For example to set the delay between generated events to 50&nbsp;ms, you would write

```java

Robot robot = BasicRobot.robotWithNewAwtHierarchy();
robot.settings().delayBetweenEvents(50);
```

Each property has a default value which is provided by AssertJ Swing and can be found in the JavaDoc
but these defaults can be changed. You can add a file called `assertj-swing.properties` to
the classpath. This file may contain key value pairs that the defaults provided byoverride AssertJ Swing.

If this is not enough one may also override specific properties via Java System Properties.

For example the default delay between generated events might be overridden with a `assertj-swing.properties`
file containing `org.assertj.swing.delay.between_events = 75`

 This might be overridden with the following additional parameter at startup
 `-Dorg.assertj.swing.delay.between_events=80`

There are also properties that cannot be modified at runtime. These options can only be modified via a
properties file. Or via a system property.

For more information about the available configuration settings (including defaults and valid values),
see the _Settings Javadoc_.

### Verifications with AssertJ Swing

AssertJ Swing allows you to check the state of the components via the component fixtures. Each fixture
has different verification methods.

For example the `JLabelFixture` allows you to verify the state of a `JLabel` and
therefore provides the following methods

```java
label.requireDisabled();
label.requireEnabled();
label.requireFocused();
label.requireText("expected");
label.requireToolTip("expected tooltip");
label.requireNotVisible();
label.requireVisible();
```


As you can see, the verification methods usually start with `require`. Therefore your IDE
should support you finding all possible verification methods.

### Font and color verifications

AssertJ Swing supports **just functional** GUI testing and therefore testing the **behavior**
of a system. We don't provide a pixel verification tool.

Regardless whether wise men think font and color verification is part of **functional** GUI
testing we allow you to do it! Whereas in general colors and fonts might not be part of the
functionality, we can think of cases where the color and font are part of the functionality.

All component fixtures allow you to verify their color and font via a `ColorFixture` or a
`FontFixture`. These verifications are accessed via the `foreground()`,
`background()` and `font()` methods of a component fixture. Let's assume we want
to check that a label shows black font on a blue background being bold:

```java
label.background().requireEqualTo("0000FF");
label.foreground().requireEqualTo(Color.BLACK);
label.font().requireBold();
```

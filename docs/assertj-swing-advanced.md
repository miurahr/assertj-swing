# Advanced features


## Assertions with Timeouts<

Sometimes it is necessary to have a timeout when verifying that a particular condition has been satisfied or the application has reached a particular state. For example, some UI tests may involve asynchronous operations or time-consuming tasks.

The following code listing shows how to perform assertions using timeouts. In our example, we are going to
assume that a `JButton` is enabled after calling a web service asynchronously. In our example,
if the `JButton` is not enabled after 10&nbsp;seconds, the test will fail. The exception thrown
will include the description of the `Condition`.

```java
// import static org.assertj.swing.timing.Pause.pause;
// import static org.assertj.swing.timing.Timeout.timeout;
// import static org.assertj.swing.edt.GuiActionRunner.execute;

final JButton okButton = //obtain a reference to the "OK" button
pause(new Condition("OK button to be enabled") {

public boolean test() {
return execute(okButton::isEnabled);
}

}, timeout(10000));`
```

## Component Formatters

A component formatter is an implementation of the interface `ComponentFormatter` that creates a
`String` representation of a GUI component. AssertJ Swing provides default component
formatters for all the Swing components in the JDK. Unlike the `toString()` method in Swing
components, the provided component formatters display only the information that can help developers solve
problems in functional tests, excluding any information related to the appearance of GUI components
(e.g. colors, layouts, sizes, etc.). Here are some examples:

```
org.assertj.swing.test.TestFrame[name='frame', title='FormattingTest', enabled=true, showing=true]
javax.swing.JButton[name='button', text='A button', enabled=false]
javax.swing.JList[name='list', selectedValues=['One', 2], contents=['One', 2, 'Three', 4], selectionMode=MULTIPLE_INTERVAL_SELECTION, enabled=true]
javax.swing.JOptionPane[message='A message', messageType=ERROR_MESSAGE, optionType=DEFAULT_OPTION, enabled=true, showing=false]
javax.swing.JTabbedPane[name='tabbedPane', selectedTabIndex=1, selectedTabTitle='Second', tabCount=2, tabTitles=['First', 'Second'], enabled=true]:
```

### Custom Component Formatters

There might be cases that you might want to create your own custom formatter to override an existing one or
to add support for custom GUI components.

#### Implement the ComponentFormatter interface

The interface `ComponentFormatter` provides two methods:

- `Class&lt;? extends Component&gt; targetType()` returns the type of component this formatter supports. For example, by returning `JButton.class` a formatter indicates that it supports instances of `JButton` and its subclasses.

- `String format(Component c)` returns the `String` representation of the given GUI component.

#### Configure an IntrospectionComponentFormatter<

The easiest way to create a component formatter is to configure an instance of
`IntrospectionComponentFormatter`, which, as the name suggests, uses introspection to display
property values of a GUI component. The following code listing shows how to configure a
`IntrospectionComponentFormatter` to support `JLabel` (and subclasses) and the
properties to show:

```java
IntrospectionComponentFormatter formatter =
new IntrospectionComponentFormatter(JLabel.class, "name", "text", "enabled");`</pre>
```

#### Register your custom formatter

After creating a custom formatter, we need to register it with AssertJ Swing. It is very simple, we
    only need to call the static method `register` in `org.assertj.swing.format.Formatting`.

```java
Formatting.register(new MyFormatter());
```

> **_NOTE:_** 
> AssertJ Swing uses the formatter that supports the type that is the closest to the type of the given
> GUI component. For example, if we have `formatter1`, a formatter registered to format instances of
> `JButton`, and `formatter2`, a formatter registered to format instances of
> `MyOwnButton` (a subclass of `JButton`), AssertJ Swing will use
> `formatter2` to format instances of `MyOwnButton`.

## Extending AssertJ Swing

It is very likely that, in your application, you are using <em>custom</em> Swing components (e.g. Flamingo,
JIDE, or your own). Since AssertJ Swing currently supports only the <em>standard</em> Swing components
(the ones that come in the JDK), you might want to create your own AssertJ Swing fixtures to test
your application.

The following are some suggestions or recommendations you can follow when creating your own
AssertJ Swing fixture:

### Take a look at the code and Javadocs of existing AssertJ Swing fixtures

By reading AssertJ Swing's code you can learn to use the `BasicRobot` to simulate a user
moving the mouse, clicking a mouse button or pressing keyboard keys. We have separated the structure of a
component fixture in several layers (from bottom to top):</p>

1. `BasicRobot`. Simulates a user interacting with a mouse and keyboard. It uses the AWT Robot to
    generate native input events.
2. Component driver. This layer does all the **heavy lifting**. All interaction with a GUI component is
    done in this layer. It knows how to simulate events and check the state of a specific GUI component.
    For example, `JComboBoxDriver` knows how to simulate a user using a `JComboBox`
    (selecting a particular element) and how to verify the state of it (which element should be
    selected).
3. Component fixture. This layer sits on top of the driver. It provides a fluent interface to that makes
    the API easier to write and read. Users of AssertJ Swing write their GUI tests using fixtures,
    not drivers. Fixtures can be considered the <em>user interface</em> of the AssertJ Swing
    library.

### Extend an existing AssertJ Swing fixture

If the component you want to test is a subclass of a JDK Swing component (e.g. you have a custom button that
extends `JButton`), you can extend an existing concrete AssertJ Swing fixture
(`JButtonFixture` in our example).

If the custom GUI component does not extend any JDK Swing component, or if you prefer to create a
AssertJ Swing fixture from scratch, please read the following

- Extend _AbstractComponentFixture_.
  This class provides all the necessary wiring of a GUI component to test and a `Robot`. It also
  provides some very basic functionality and convenience methods.
- If want to simulate a user right-clicking on your component and showing a `JPopupMenu`,
  extend _AbstractJPopupMenuInvokerFixture_.

> **_Warning_**
> To avoid unexpected side effects in your tests, you must access Swing components in the event dispatch thread.

### Create an extension for AbstractContainerFixture

By default, implementations of <a href="swing/api/org/assertj/swing/fixture/AbstractContainerFixture.html" target="_blank">AbstractContainerFixture</a>
provide shortcut methods to access the standard Swing components (the ones that come in the JDK) in a Container. For example, the following code listing
shows shortcuts methods to access a `JLabel` and a `JTree` from a `JFrame` being managed by a `FrameFixture`:

```java
FrameFixture frame = new FrameFixture(new MyFrame());
frame.label("pathLabel").requireText("Path:");
frame.tree("navigationTree").selectPath("c:/projects/fest");
```


If you have created a fixture for your custom GUI component, it is not possible to add a shortcut to
`AbstractContainerFixture` due to the lack of extension methods in Java. For example, let's assume you have
created a custom GUI component called `MyCalendar` and a fixture to use this custom component in
your GUI tests called `MyCalendarFixture`. It is not possible to add the shortcut method calendar
to `AbstractContainerFixture` and have all its implementations look like this:

```java
frame.calendar("myCalendar").selectDate("10/18/08");
```

To overcome this limitation, AssertJ Swing provides <a href="swing/api/org/assertj/swing/fixture/ComponentFixtureExtension.html" target="_blank">ComponentFixtureExtension</a>.
The following code listing shows an extension to add a shortcut to a `MyCalendarFixture`. This extension looks up a
`MyCalendar` that has a matching name and is showing on the screen:

```java

public class MyCalendarFixtureExtension extends ComponentFixtureExtension<MyCalendar, MyCalendarFixture> {

    public static MyCalendarFixtureExtension calendarWithName(String name) {
        return new MyCalendarFixtureExtension(name);
    }

    private final String name;

    private MyCalendarFixtureExtension(String name) {
        this.name = name;
    }

    public MyCalendarFixture createFixture(Robot robot, Container root) {
        MyCalendar calendar = robot.finder().findByName(root, name, MyCalendar.class, true);
        return new MyCalendarFixture(robot, calendar);
    }
}
```

The only method that needs to be implemented is `createFixture(Robot, Container)`, which is
responsible for creating our custom fixture. The static method `calendarWithName(String)` is just
a convenience factory method, which we will use to connect our extension to any implementation of
`AbstractContainerFixture`:

```java
// import static org.assertj.swing.sample.MyCalendarFixtureExtension.calendarWithName;
FrameFixture frame = new FrameFixture(new MyFrame());
frame.with(calendarWithName("myCalendar")).selectDate("10/18/08");`</pre>
```

## Handling System.exit

A common topic in the mailing list is how to handle `System.exit`. The typical example is
simulating a user selecting the menus <em>File</em>, <em>Exit</em> from a GUI test. By doing this, the
application will quit and any GUI test trying to use the application will fail.

To prevent this failure and to test that in fact `System.exit` is being handled correctly by the
application, AssertJ Swing provides a `NoExitSecurityManager` that will prevent the
application to end. To use it, simply add a call to `NoExitSecurityManagerInstaller` as
follows:

```java
    private static NoExitSecurityManagerInstaller noExitSecurityManagerInstaller;

    @BeforeClass
    public static void setUpOnce() {
        noExitSecurityManagerInstaller = NoExitSecurityManagerInstaller.install();
    }

    @AfterClass
    public static void tearDown() {
        noExitSecurityManagerInstaller.uninstall();
    }
```

By default, `NoExitSecurityManager` will just prevent the application from quitting if
`System.exit` is called. If you want to do something when `System.exit` is called, do
the following:

- Implement a `ExitHook`
- Pass an instance of your `ExitHook` to `NoExitSecurityManagerInstaller.install(ExitHook)`

## Support for Platform-Specific Features

Sometimes, when testing our applications, we need to deal with platform-specific issues.
AssertJ Swing provides the utility class `Platform` to overcome this type of issues.

Currently `Platform` provides one method, `controlOrCommandKey()` that returns the
code of either the <em>Command</em> key (if MacOS) or the code of the <em>Control</em> key (if other than
MacOS, for example Windows). A good example of the utility of this method is simulation of a user copying
some text to the clipboard. In windows we need to simulate a user pressing the keys <kbd>Ctrl</kbd> and
<kbd>C</kbd>, while in MacOS we need to simulate a user pressing the keys <kbd>Cmd</kbd> and
<kbd>C</kbd>. With AssertJ Swing we can simply write:

```java
// import static org.assertj.swing.util.Platform.controlOrCommandKey;
// import static java.awt.event.KeyEvent.VK_C;

    int controlOrCommand = controlOrCommandKey();

    dialog.textBox("username").selectAll()
    .pressKey(controlOrCommand)
    .pressAndReleaseKeys(VK_C)
    .releaseKey(controlOrCommand);
```

## Testing containers without a frame

AssertJ Swing provides the utility class `Containers` to help test a `Container`
(e.g. a `JPanel`) without having a `Frame` (or `JFrame`). The following
code listing shows how to test our own `JPanel` of type `MyCoolJPanel`:

```java
// import static org.assertj.swing.fixture.Containers.showInFrame;

    private FrameFixture frameFixture;

    @Before
    public void setUp() {
    MyCoolJPanel panel = createPanel() // create panel in EDT.
    frameFixture = showInFrame(panel);
    }
```

AssertJ Swing will show the panel in a `JFrame`, wrapped in a `FrameFixture`.

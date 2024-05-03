# Component Lookup

To test a GUI, we first need to find the components we want to perform user input simulation on or state
verification. This section explains how to find GUI components in AssertJ Swing.


### Types of GUI Component Lookup

To simulate user input on a GUI, AssertJ Swing first needs to find the GUI component(s) that will
participate in the test. For example, to simulate a user pressing a <em>ok</em> button, AssertJ Swing
first needs to find that button. AssertJ Swing provides different ways to lookup a GUI component:

#### Lookup by name

Using a unique name for GUI components guarantees that we can always find them, regardless of any change in
the GUI (as long as the components have not been removed). For example, to find a button having the name
`ok`, we need to set that name in the button before we execute our tests:

```java
JButton okButton = new JButton("OK");
okButton.setName("ok");
```


#### Lookup by type

Finding components by type is reliable as long as the GUI under test has only one component of such type.

#### Lookup using a GenericTypeMatcher
j
There are times when the GUI under test does not provide unique names for their components (e.g. legacy
applications). To overcome this limitation, AssertJ Swing provides a way to specify custom search
criteria using a `GenericTypeMatcher`. In this example we create a `GenericTypeMatcher`
that matches a `JButton` containing the text `OK`:

```java
GenericTypeMatcher&lt;JButton&gt; textMatcher = new GenericTypeMatcher&lt;JButton&gt;(JButton.class) {
  @Override protected boolean isMatching(JButton button) {
    return "OK".equals(button.getText());
  }
};
```

> **_Note_**
> `GenericTypeMatcher` guarantees that the component passed as argument in the method
> `isMatching(Component)` is never `null`.

### Common Component Matchers

By default, AssertJ Swing supports <a href="#lookup-types">looking up components by name, type and
                                                                 custom search criteria</a>. To specify
custom search criteria, users need to provide an implementation of `GenericTypeMatcher` or
`ComponentMatcher`.

AssertJ Swing provides some common-use component matchers in the package
`org.assertj.swing.core.matcher`:

<dl>
<dt>`DialogMatcher`</dt>
<dd>Matches a `Dialog` containing the specified name, title or visibility</dd>

<dt>`FrameMatcher`</dt>
<dd>Matches a `Frame` containing the specified name, title or visibility</dd>

<dt>`JButtonMatcher`</dt>
<dd>Matches a `JButton` containing the specified name, title or visibility</dd>

<dt>`JLabelMatcher`</dt>
<dd>Matches a `JLabel` containing the specified name, title or visibility</dd>

<dt>`JTextComponentMatcher`</dt>
<dd>Matches a `JTextComponent` containing the specified name, title or visibility</dd>
</dl>

#### Example

The following example shows how to find and click a `JButton` with name `ok` and text
`Ok`, in a `JFrame` using a `JButtonMatcher`:

```java
// import static org.assertj.swing.core.matcher.JButtonMatcher.withText;

FrameFixture window = new FrameFixture(robot(), frame);
window.show();
window.button(withName("ok").andText("OK")).click();
```


### Performing GUI Component Lookup

Regardless of the lookup type, AssertJ Swing provides two ways to perform GUI component lookup

#### Using a `ContainerFixture`

The abstract class
<a href="swing/api/org/assertj/swing/fixture/ContainerFixture.html" target="_blank">ContainerFixture</a>
provides shortcuts to look up GUI components inside the `Container` being tested. There are three
overloaded versions of each shortcut method, one for each component lookup type (by name, type or
`GenericTypeMatcher`).

The following example uses a `FrameFixture`, a concrete implementation of
`ContainerFixture`, to look up a <em>login</em> `JButton` by name, type and custom
search criteria:

```java
// by name
JButtonFixture button = window.button("login");

// by type
JButtonFixture button = window.button();

// custom search criteria (the button's text)
JButtonFixture button = window.button(new GenericTypeMatcher&lt;JButton&gt;(JButton.class) {
  @Override
  protected boolean isMatching(JButton button) {
    return "Login".equals(button.getText());
  }
});
```


##### Scope of component lookups</h5>

By default, only components that are showing on the screen can participate in component lookups (except for
`JMenuItem`, which are not showing until they are selected). To change the scope of component
lookups, simply call the method `componentLookupScope(ComponentLookupScope)` in
`Settings`. All instances of `Robot` contain their own `Settings`,
which is returned by the method `settings()`. For more information about component lookup
scope, please read `ComponentLookupScope`'s
<a href="swing/api/org/assertj/swing/core/ComponentLookupScope.html" target="_blank">documentation</a>.

```java
// including showing and not-showing components in component lookups

Robot robot = BasicRobot.robotWithNewAwtHierarchy();
robot.settings().componentLookupScope(ComponentLookupScope.ALL);
```

> **_Note_** 
> This setting affects component <strong>lookup by name and type only</strong>. Creators of
> implementations of `GenericTypeMatcher` need to specify if lookup includes non-showing
> components or not.

#### Using a `ComponentFinder`

Although we recommend to look up GUI components using a `ContainerFixture`, you can also use a
`ComponentFinder`. It supports different types of component lookup (please find details in
its <a href="swing/api/org/assertj/swing/core/ComponentFinder.html" target="_blank">documentation</a>):

- by name
- by type
- by name and type
- custom search criteria using a `ComponentMatcher`
- custom search criteria using a `GenericTypeMatcher`

##### Obtaining a ComponentFinder</h5>

There are three ways to obtain a ComponentFinder:

1. By creating a new `ComponentFinder` that has access to all the GUI components in the AWT hierarchy.
```java
ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
```
2. By creating a new `ComponentFinder` that only has access to the GUI components created after it. In the following example, finder has access to `MainFrame` but not to `LoginFrame`.
```java
// new LoginFrame();
ComponentFinder finder = BasicComponentFinder.finderWithNewAwtHierarchy();
finder.findByName("login", true); // will fail finding component of login frame
// new MainFrame();
finder.findByName("pw", true); // will work finding label of main frame
```
3. By reusing an existing `ComponentFinder` from a `Robot`. In the following
example we're going to use the `Robot` from the
<a href="assertj-swing-basics.html#base-test-case">base test class</a>:
```java
ComponentFinder finder = robot().finder();
```

### Testing Long-Duration Tasks

AssertJ Swing provides support for finding GUI components after the execution of a long-duration task
is complete. Currently, it provides the following finders (in the package `org.assertj.swing.finder`):

- `<a href="swing/api/org/assertj/swing/finder/WindowFinder.html" target="_blank">WindowFinder</a>`
(which can find instances of Frame and Dialog)
- `<a href="swing/api/org/assertj/swing/finder/JOptionPaneFinder.html" target="_blank">JOptionPaneFinder</a>`
- `<a href="swing/api/org/assertj/swing/finder/JFileChooserFinder.html" target="_blank">JFileChooserFinder</a>`

A good example for a long running task is the login procedure of an application. The main window is being
shown after the user's credentials have been successfully verified. The following are the typical steps to
complete such scenario:

1. User launches the application
1. A login window appears
1. User enters her username and password and clicks the <em>Login</em> button
1. User is authenticated and authorized successfully
1. The main window of the application is displayed

The <em>tricky</em> part here is step 4. Authentication/authorization can take some time (depending on
network traffic, etc.) and we need to wait for the main window to appear in order to continue our test. It
is possible to test this scenario with AssertJ Swing:

#### Lookup by name

```java
// import static org.assertj.swing.finder.WindowFinder.findFrame;
window.textBox("username").enterText("yvonne");
window.textBox("password").enterText("welcome");
window.button("login").click();

// now the interesting part, we need to wait till the main window is shown.
FrameFixture mainFrame = findFrame("main").using(robot());

// we can continue testing the main window.
```

The `findFrame()` method (statically imported from `WindowFinder`) can look up a
Frame having `main` as its name with a default timeout of 5&nbsp;seconds. This means that if in
5&nbsp;seconds the frame we're looking for isn't found, the test will fail.

We can also specify a custom value for the timeout. For example, we can set the timeout to 10&nbsp;seconds
in two ways:

```java
// import static org.assertj.swing.finder.WindowFinder.findFrame;
// import static java.util.concurrent.TimeUnit.SECONDS;
FrameFixture mainFrame = findFrame("main").withTimeout(10000).using(robot());

// or
FrameFixture mainFrame = findFrame("main").withTimeout(10, SECONDS).using(robot());
```


#### Lookup by type

We can also find components by type. In this example, we look up a showing a `Frame` of type
`MainFrame`, using the default timeout of 5&nbsp;seconds.

```java
// import static org.assertj.swing.finder.WindowFinder.findFrame;
FrameFixture mainFrame = findFrame(MainFrame.class).using(robot());
```


#### Lookup using a GenericTypeMatcher

All finders accept a `GenericTypeMatcher` to find components using custom search criteria. In
the following example, we look up a showing frame whose title starts with `News`:

```java
// import static org.assertj.swing.finder.WindowFinder.findFrame;
GenericTypeMatcher&lt;JFrame&gt; matcher = new GenericTypeMatcher&lt;JFrame&gt;(JFrame.class) {
  protected boolean isMatching(JFrame frame) {
    return frame.getTitle() != null &amp;&amp; frame.getTitle().startsWith("News") &amp;&amp; frame.isShowing();
  }
};
FrameFixture frame = findFrame(matcher).using(robot());
```

#### Reusing Robots

In the example above, we used the method call `using(loginDialog.robot)`. Although strange,
this is necessary because, in a given test, only one instance of `Robot` can be running, to
prevent GUI tests from blocking each other on the screen. In other words, in a test class you can only use
one and only one instance of Robot.


# Swing's Event Dispatch Thread (EDT)

The cardinal rule of
   <a href="http://docs.oracle.com/javase/6/docs/api/javax/swing/package-summary.html#threading"
      target="_blank">Swing threading</a> states that all access to all Swing components should be done in
   the event dispatch thread (EDT). To avoid unexpected behavior in your GUI tests (e.g. random test
   failures), this rule also applies to test code.

### Writing EDT-safe GUI tests

If you follow these tips you ensure that your GUI tests respect Swing's threading rules:

- Read about
      <a href="http://docs.oracle.com/javase/6/docs/api/javax/swing/package-summary.html#threading"
         target="_blank">Swing's threading rules</a>. Keep in mind the
      <strong><abbr title="ALL access to all Swing components should be done in the EDT"
                    class="initialism">cardinal rule</abbr></strong>
- Creation and any direct access to Swing components should be done via `GuiActionRunner
       using a `GuiQuery or `GuiTask, because JUnit and TestNG tests do not run on
       the EDT. More details <a href="#accessing-on-edt">below on this page</a>.
- Access of Swing components through AssertJ Swing APIs is already EDT-safe.
- It is strongly recommended to use `FailOnThreadViolationRepaintManager` to catch EDT-access violations.
  More details [below on this page](#Testing-that-access-to-Swing-components-is-done-in-the-EDT)

Alex Ruiz wrote an <a href="http://alexruiz.developerblogs.com/?p=160" target="_blank">entry on his blog</a>
         about how to write EDT-safe GUI tests.

### Accessing Swing Components in the EDT

AssertJ Swing performs all access to Swing components in the EDT. AssertJ Swing also exposes a convenient
mechanism to access Swing components in the EDT from your test code. This mechanism involves one (with lambda expressions) to three (traditional way) classes:

- <a href="swing/api/org/assertj/swing/edt/GuiQuery.html" target="_blank">GuiQuery</a>*, for performing
    actions in the EDT that return a value
- <a href="swing/api/org/assertj/swing/edt/GuiTask.html" target="_blank">GuiTask</a>*, for performing
    actions in the EDT that do not return a value
- <a href="swing/api/org/assertj/swing/edt/GuiActionRunner.html" target="_blank">GuiActionRunner</a>,
    executes a `GuiQuery or `GuiTask (or Callable/Runnable via lambda expressions) in the EDT, rethrowing any exceptions thrown when executing any GUI
    action in the EDT.

If you use lambda expressions, you don't need to use this class directly.

#### Examples

When you would like to **read** the text of a label, execute the following code:

```java
final JLabel nameLabel = ... // get a reference to a JLabel
String text = GuiActionRunner.execute(() -> nameLabel.getText());
```

When you would like to **set** the text of a label, execute:

```java
final JLabel nameLabel = ... // get a reference to a JLabel
GuiActionRunner.execute(() -> nameLabel.setText("Name:"));
```

> **_Note_**
>
> In some cases the lambda expression does not work. You can always use the above examples with
> anonymous or dedicated subclasses of `GuiQuery` and `GuiTask`.

### Testing that access to Swing components is done in the EDT

AssertJ Swing provides the class `FailOnThreadViolationRepaintManager`
that forces a test failure if access to Swing components is not performed on the EDT. This repaint manager
is based on `ThreadCheckingRepaintManager` by Scott Delap and Alex Potochkin. For more details
about how to check if GUI component is done outside the EDT, please check Alex Potochkin's article
<a href="http://weblogs.java.net/blog/alexfromsun/archive/2006/02/debugging_swing.html" target="_blank">Debugging Swing, the final summary</a>.

Installing `FailOnThreadViolationRepaintManager` is pretty straightforward. The following example shows
how to install it in the class-level setup method of a JUnit test:

```java
@BeforeClass
public static void setUpOnce() {
  FailOnThreadViolationRepaintManager.install();
}
```

When using Sun's JVM, a new instance of `FailOnThreadViolationRepaintManager` will be set as the
default repaint manager in Swing once and only once, regardless of the number of times `install()
is called. On other JVMs this optimization is not guaranteed.

Once a `FailOnThreadViolationRepaintManager` is installed, it will throw an
   `EdtViolationException` if a GUI test is not accessing Swing components in the EDT.

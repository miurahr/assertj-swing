# One minute starting guide

Assuming you have only little time, here's the quickest introduction to AssertJ Swing. Depending on your
reading and coding skills, this should be done in about one minute. &#9786;

### Get AssertJ Swing

AssertJ Swing artifacts are in Maven central repository. There are two main artifacts:
`assertj-swing-junit` and `assertj-swing-junit-jupiter`.

### Setup your test case

Make some static imports

```java
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.finder.WindowFinder.findFrame;
```

extend our base test case,

```java
// either for JUnit
AssertJSwingJUnitTestCase
// or for TestNG
AssertJSwingTestngTestCase
```

and add the following to the `onSetUp()` method to start your application via its `main` method.

```java
// without arguments
application(YourMainClass.class).start();
// with arguments
application(YourMainClass.class).withArgs(arguments).start();
```


Then you just need to tell AssertJ Swing to find the main frame of your application and start testing:

```java
FrameFixture frame = findFrame(new GenericTypeMatcher&lt;Frame&gt;(Frame.class) {
  protected boolean isMatching(Frame frame) {
    return "Your application title".equals(frame.getTitle()) && frame.isShowing();
  }
}).using(robot());
```

Now `frame` refers to the test fixture for your main frame and you're ready to test it.

### Use code completion

Type `frame.` and your IDE will show you the available commands. Assuming your frame
  contains a table that should have 42 rows you could for instance write:

```java
frame.table().requireRowCount(42);
```

That's all!

### Want to see more?

See our more detailed <a href="assertj-swing-getting-started.html">getting started guide</a> to get
  the maximum out of it!

You can learn more by looking at <a href="swing/api/index.html" target="_blank">AssertJ Swing javadoc</a>.

Another way is to go see <a href="https://github.com/joel-costigliola/assertj-examples/tree/master">assertj-examples</a>,
  it covers what is possible with AssertJ and contains projects for AssertJ Swing, too. You can clone it
  and run its tests!

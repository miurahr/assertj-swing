# Troubleshooting component lookups

AssertJ Swing throws a `ComponentLookupException` when a component lookup fails. Failures
are due to one of the following reasons

- a component that matches the specified search criteria could not be found
- there is more than one component that matches the specified search criteria


## No matching component could be found

When a matching component could not be found, regardless of the lookup type, the thrown
`ComponentLookupException` includes a formatted component hierarchy that can help us determine why
the component lookup failed.

For our example, we are going to use a frame that contains a `JButton` with name
`click`. The following code listing shows a mistake we made when looking up that button in our
test. We accidentally used the name `ok` instead of `click`:

```java
FrameFixture frame = new FrameFixture(new TestFrame());
frame.show();

frame.button("ok").click();
```

When we run our test, it will obviously fail, because the frame does not have any button with name
`ok`. To help us diagnose the problem, the thrown `ComponentLookupException` displays
a nicely formatted component hierarchy:

```
org.assertj.swing.exception.ComponentLookupException: Unable to find component using matcher org.assertj.swing.core.NameMatcher[name='ok', requireShowing=false].

  Component hierarchy:
  org.assertj.swing.test.TestFrame[name='testFrame', title='Test', enabled=true, showing=true]
    javax.swing.JRootPane[]
      javax.swing.JPanel[name='null.glassPane']
      javax.swing.JLayeredPane[]
        javax.swing.JPanel[name='null.contentPane']
          javax.swing.JButton[name='click', text='Click Me', enabled=true]

    at org.assertj.swing.core.BasicComponentFinder.componentNotFound(BasicComponentFinder.java:81)
```


The displayed component hierarchy, unlike the default `toString()` method in Swing components,
does not contain any information related to how the components look (e.g. component sizes, colors,
layouts, etc.). That information is pretty much useless, since functional testing focuses on the behavior
of the GUI. Instead, AssertJ Swing uses custom formatters to show properties that can help us solve
problems in our functional tests. For more details about custom component formatters (including how to
override the provided ones with your own), please visit the <a href="assertj-swing-advanced.html#formatters">Component Formatters</a> section.

## More than one matching component could be found

There are cases that more than one GUI component matches the search criteria used in a lookup. For our
example, we are going to use a frame that contains two instances of `JButton` with names
`first` and `second`. In the following code listing we are looking up a
`JButton` by type:

```java
FrameFixture frame = new FrameFixture(new TestFrame());
frame.show();

frame.button().click();
```


This test will fail, because the frame has two components of type `JButton`. To help us diagnose
the problem, the thrown `ComponentLookupException` displays all the matching components found:

````
  org.assertj.swing.exception.ComponentLookupException: Found more than one component using matcher org.assertj.swing.core.TypeMatcher[type=javax.swing.JButton, requireShowing=false].

  Found:
  javax.swing.JButton[name='first', text='First Button', enabled=true]
  javax.swing.JButton[name='second', text='Second Button', enabled=true]

    at org.assertj.swing.core.BasicComponentFinder.multipleComponentsFound(BasicComponentFinder.java:102)

```

The thrown `ComponentLookupException` uses component formatters to display information that can
help us solve problems in our functional tests. Just like when finding no matching component.

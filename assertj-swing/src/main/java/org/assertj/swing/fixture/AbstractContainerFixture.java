/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.swing.fixture;

import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.swing.core.ComponentLookupScope.SHOWING_ONLY;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.timing.Timeout.timeout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;

import org.assertj.swing.annotation.RunsInEDT;
import org.assertj.swing.core.ComponentFinder;
import org.assertj.swing.core.ComponentFoundCondition;
import org.assertj.swing.core.ComponentMatcher;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.NameMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.TypeMatcher;
import org.assertj.swing.driver.ComponentDriver;
import org.assertj.swing.timing.Timeout;

/**
 * Looks up AWT or Swing {@code Component}s contained in a {@code Container}.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href="http://goo.gl/fjgOM"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>.&quot;
 * @param <C> the type of {@code Container} that this fixture can manage.
 * @param <D> the type of {@link ComponentDriver} that this fixture uses internally.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public abstract class AbstractContainerFixture<S, C extends Container, D extends ComponentDriver> extends
    AbstractComponentFixture<S, C, D> implements ComponentContainerFixture {
  private static final Timeout DEFAULT_DIALOG_LOOKUP_TIMEOUT = timeout();

  private final JMenuItemFinder menuItemFinder;

  /**
   * Creates a new {@link AbstractContainerFixture}.
   *
   * @param selfType the "self type."
   * @param robot performs simulation of user events on a {@code Container}.
   * @param type the type of the {@code Container} to find using the given {@code Robot}.
   * @throws NullPointerException if {@code robot} is {@code null}.
   * @throws NullPointerException if {@code type} is {@code null}.
   * @throws org.assertj.swing.exception.ComponentLookupException if a matching component could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one matching component is found.
   * @see org.assertj.swing.core.ComponentFinder#findByType(Class)
   */
  public AbstractContainerFixture(@NotNull Class<S> selfType, @NotNull Robot robot, @NotNull Class<? extends C> type) {
    super(selfType, robot, type);
    menuItemFinder = new JMenuItemFinder(robot, target());
  }

  /**
   * Creates a new {@link AbstractContainerFixture}.
   *
   * @param selfType the "self type."
   * @param robot performs simulation of user events on a {@code Container}.
   * @param name the name of the {@code Container} to find using the given {@code Robot}.
   * @param type the type of the {@code Container} to find using the given {@code Robot}.
   * @throws NullPointerException if {@code robot} is {@code null}.
   * @throws NullPointerException if {@code type} is {@code null}.
   * @throws org.assertj.swing.exception.ComponentLookupException if a matching component could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one matching component is found.
   * @see org.assertj.swing.core.ComponentFinder#findByName(String, Class)
   */
  public AbstractContainerFixture(@NotNull Class<S> selfType, @NotNull Robot robot, @Nullable String name,
                                  @NotNull Class<? extends C> type) {
    super(selfType, robot, name, type);
    menuItemFinder = new JMenuItemFinder(robot, target());
  }

  /**
   * Creates a new {@link AbstractContainerFixture}.
   *
   * @param selfType the "self type."
   * @param robot performs simulation of user events on the given {@code Container}.
   * @param target the {@code Container} to be.
   * @throws NullPointerException if {@code robot} is {@code null}.
   * @throws NullPointerException if {@code target} is {@code null}.
   */
  public AbstractContainerFixture(@NotNull Class<S> selfType, @NotNull Robot robot, @NotNull C target) {
    super(selfType, robot, target);
    menuItemFinder = new JMenuItemFinder(robot, target());
  }

  @RunsInEDT
  @Override
  @NotNull public JButtonFixture button() {
    return new JButtonFixture(robot(), findByType(JButton.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JButtonFixture button(@NotNull GenericTypeMatcher<? extends JButton> matcher) {
    return new JButtonFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JButtonFixture button(@Nullable String name) {
    return new JButtonFixture(robot(), findByName(name, JButton.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JCheckBoxFixture checkBox() {
    return new JCheckBoxFixture(robot(), findByType(JCheckBox.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JCheckBoxFixture checkBox(@NotNull GenericTypeMatcher<? extends JCheckBox> matcher) {
    return new JCheckBoxFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JCheckBoxFixture checkBox(@Nullable String name) {
    return new JCheckBoxFixture(robot(), findByName(name, JCheckBox.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JComboBoxFixture comboBox() {
    return new JComboBoxFixture(robot(), findByType(JComboBox.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JComboBoxFixture comboBox(@NotNull GenericTypeMatcher<? extends JComboBox> matcher) {
    return new JComboBoxFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JComboBoxFixture comboBox(@Nullable String name) {
    return new JComboBoxFixture(robot(), findByName(name, JComboBox.class));
  }

  @RunsInEDT
  @Override
  @NotNull public DialogFixture dialog() {
    return dialog(defaultDialogLookupTimeout());
  }

  @RunsInEDT
  @Override
  @NotNull public DialogFixture dialog(@NotNull Timeout timeout) {
    TypeMatcher matcher = new TypeMatcher(Dialog.class, requireShowing());
    return findDialog(matcher, timeout);
  }

  @RunsInEDT
  @Override
  @NotNull public DialogFixture dialog(@NotNull GenericTypeMatcher<? extends Dialog> matcher) {
    return dialog(matcher, defaultDialogLookupTimeout());
  }

  @RunsInEDT
  @Override
  @NotNull public DialogFixture dialog(@NotNull GenericTypeMatcher<? extends Dialog> matcher, @NotNull Timeout timeout) {
    return findDialog(matcher, timeout);
  }

  @RunsInEDT
  @Override
  @NotNull public DialogFixture dialog(@Nullable String name) {
    return dialog(name, defaultDialogLookupTimeout());
  }

  @RunsInEDT
  @Override
  @NotNull public DialogFixture dialog(@Nullable String name, @NotNull Timeout timeout) {
    NameMatcher matcher = new NameMatcher(name, Dialog.class, requireShowing());
    return findDialog(matcher, timeout);
  }

  @NotNull private DialogFixture findDialog(@NotNull ComponentMatcher matcher, @NotNull Timeout timeout) {
    String description = "dialog to be found using matcher " + matcher;
    ComponentFoundCondition condition = new ComponentFoundCondition(description, robot().finder(), matcher);
    pause(condition, timeout);
    Dialog dialog = (Dialog) condition.found();
    return new DialogFixture(robot(), checkNotNull(dialog));
  }

  @RunsInEDT
  @Override
  @NotNull public JFileChooserFixture fileChooser() {
    return fileChooser(defaultDialogLookupTimeout());
  }

  @RunsInEDT
  @Override
  @NotNull public JFileChooserFixture fileChooser(@NotNull Timeout timeout) {
    TypeMatcher matcher = new TypeMatcher(JFileChooser.class, requireShowing());
    return findFileChooser(matcher, timeout);
  }

  @RunsInEDT
  @Override
  @NotNull public JFileChooserFixture fileChooser(@NotNull GenericTypeMatcher<? extends JFileChooser> matcher) {
    return fileChooser(matcher, defaultDialogLookupTimeout());
  }

  @RunsInEDT
  @Override
  @NotNull public JFileChooserFixture fileChooser(@NotNull GenericTypeMatcher<? extends JFileChooser> matcher,
                                                  @NotNull Timeout timeout) {
    return findFileChooser(matcher, timeout);
  }

  @RunsInEDT
  @Override
  @NotNull public JFileChooserFixture fileChooser(@Nullable String name) {
    return new JFileChooserFixture(robot(), findByName(name, JFileChooser.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JFileChooserFixture fileChooser(@Nullable String name, @NotNull Timeout timeout) {
    NameMatcher matcher = new NameMatcher(name, JFileChooser.class, requireShowing());
    return findFileChooser(matcher, timeout);
  }

  @NotNull private JFileChooserFixture findFileChooser(@NotNull ComponentMatcher matcher, @NotNull Timeout timeout) {
    String description = "file chooser to be found using matcher " + matcher;
    ComponentFoundCondition condition = new ComponentFoundCondition(description, robot().finder(), matcher);
    pause(condition, timeout);
    JFileChooser fileChooser = (JFileChooser) condition.found();
    return new JFileChooserFixture(robot(), checkNotNull(fileChooser));
  }

  @RunsInEDT
  @Override
  @NotNull public JInternalFrameFixture internalFrame() {
    return new JInternalFrameFixture(robot(), findByType(JInternalFrame.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JInternalFrameFixture internalFrame(@NotNull GenericTypeMatcher<? extends JInternalFrame> matcher) {
    return new JInternalFrameFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JInternalFrameFixture internalFrame(@Nullable String name) {
    return new JInternalFrameFixture(robot(), findByName(name, JInternalFrame.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JLabelFixture label() {
    return new JLabelFixture(robot(), findByType(JLabel.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JLabelFixture label(@NotNull GenericTypeMatcher<? extends JLabel> matcher) {
    return new JLabelFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JLabelFixture label(@Nullable String name) {
    return new JLabelFixture(robot(), findByName(name, JLabel.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JListFixture list() {
    return new JListFixture(robot(), findByType(JList.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JListFixture list(@NotNull GenericTypeMatcher<? extends JList> matcher) {
    return new JListFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JListFixture list(@Nullable String name) {
    return new JListFixture(robot(), findByName(name, JList.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JMenuItemFixture menuItemWithPath(@NotNull String... path) {
    return new JMenuItemFixture(robot(), menuItemFinder.menuItemWithPath(path));
  }

  @RunsInEDT
  @Override
  @NotNull public JMenuItemFixture menuItem(@Nullable String name) {
    boolean requireShowing = SHOWING_ONLY.equals(robot().settings().componentLookupScope());
    return new JMenuItemFixture(robot(), finder().findByName(target(), name, JMenuItem.class, requireShowing));
  }

  @RunsInEDT
  @Override
  @NotNull public JMenuItemFixture menuItem(@NotNull GenericTypeMatcher<? extends JMenuItem> matcher) {
    return new JMenuItemFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JOptionPaneFixture optionPane() {
    return optionPane(defaultDialogLookupTimeout());
  }

  @RunsInEDT
  @Override
  @NotNull public JOptionPaneFixture optionPane(@NotNull Timeout timeout) {
    TypeMatcher matcher = new TypeMatcher(JOptionPane.class, requireShowing());
    String description = "option pane to be found using matcher " + matcher;
    ComponentFoundCondition condition = new ComponentFoundCondition(description, robot().finder(), matcher);
    pause(condition, timeout);
    JOptionPane optionPane = (JOptionPane) condition.found();
    return new JOptionPaneFixture(robot(), checkNotNull(optionPane));
  }

  @RunsInEDT
  @Override
  @NotNull public JPanelFixture panel() {
    return new JPanelFixture(robot(), findByType(JPanel.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JPanelFixture panel(@NotNull GenericTypeMatcher<? extends JPanel> matcher) {
    return new JPanelFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JPanelFixture panel(@Nullable String name) {
    return new JPanelFixture(robot(), findByName(name, JPanel.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JProgressBarFixture progressBar() {
    return new JProgressBarFixture(robot(), findByType(JProgressBar.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JProgressBarFixture progressBar(@NotNull GenericTypeMatcher<? extends JProgressBar> matcher) {
    return new JProgressBarFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JProgressBarFixture progressBar(@Nullable String name) {
    return new JProgressBarFixture(robot(), findByName(name, JProgressBar.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JRadioButtonFixture radioButton() {
    return new JRadioButtonFixture(robot(), findByType(JRadioButton.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JRadioButtonFixture radioButton(@NotNull GenericTypeMatcher<? extends JRadioButton> matcher) {
    return new JRadioButtonFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JRadioButtonFixture radioButton(@Nullable String name) {
    return new JRadioButtonFixture(robot(), findByName(name, JRadioButton.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JScrollBarFixture scrollBar() {
    return new JScrollBarFixture(robot(), findByType(JScrollBar.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JScrollBarFixture scrollBar(@NotNull GenericTypeMatcher<? extends JScrollBar> matcher) {
    return new JScrollBarFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JScrollBarFixture scrollBar(@Nullable String name) {
    return new JScrollBarFixture(robot(), findByName(name, JScrollBar.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JScrollPaneFixture scrollPane() {
    return new JScrollPaneFixture(robot(), findByType(JScrollPane.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JScrollPaneFixture scrollPane(@NotNull GenericTypeMatcher<? extends JScrollPane> matcher) {
    return new JScrollPaneFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JScrollPaneFixture scrollPane(@Nullable String name) {
    return new JScrollPaneFixture(robot(), findByName(name, JScrollPane.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JSliderFixture slider() {
    return new JSliderFixture(robot(), findByType(JSlider.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JSliderFixture slider(@NotNull GenericTypeMatcher<? extends JSlider> matcher) {
    return new JSliderFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JSliderFixture slider(@Nullable String name) {
    return new JSliderFixture(robot(), findByName(name, JSlider.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JSpinnerFixture spinner() {
    return new JSpinnerFixture(robot(), findByType(JSpinner.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JSpinnerFixture spinner(@NotNull GenericTypeMatcher<? extends JSpinner> matcher) {
    return new JSpinnerFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JSpinnerFixture spinner(@Nullable String name) {
    return new JSpinnerFixture(robot(), findByName(name, JSpinner.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JSplitPaneFixture splitPane() {
    return new JSplitPaneFixture(robot(), findByType(JSplitPane.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JSplitPaneFixture splitPane(@NotNull GenericTypeMatcher<? extends JSplitPane> matcher) {
    return new JSplitPaneFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JSplitPaneFixture splitPane(@Nullable String name) {
    return new JSplitPaneFixture(robot(), findByName(name, JSplitPane.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTabbedPaneFixture tabbedPane() {
    return new JTabbedPaneFixture(robot(), findByType(JTabbedPane.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTabbedPaneFixture tabbedPane(@NotNull GenericTypeMatcher<? extends JTabbedPane> matcher) {
    return new JTabbedPaneFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JTabbedPaneFixture tabbedPane(@Nullable String name) {
    return new JTabbedPaneFixture(robot(), findByName(name, JTabbedPane.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTableFixture table() {
    return new JTableFixture(robot(), findByType(JTable.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTableFixture table(@NotNull GenericTypeMatcher<? extends JTable> matcher) {
    return new JTableFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JTableFixture table(@Nullable String name) {
    return new JTableFixture(robot(), findByName(name, JTable.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTextComponentFixture textBox() {
    return new JTextComponentFixture(robot(), findByType(JTextComponent.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTextComponentFixture textBox(@NotNull GenericTypeMatcher<? extends JTextComponent> matcher) {
    return new JTextComponentFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JTextComponentFixture textBox(@Nullable String name) {
    return new JTextComponentFixture(robot(), findByName(name, JTextComponent.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JToggleButtonFixture toggleButton() {
    return new JToggleButtonFixture(robot(), findByType(JToggleButton.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JToggleButtonFixture toggleButton(@NotNull GenericTypeMatcher<? extends JToggleButton> matcher) {
    return new JToggleButtonFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JToggleButtonFixture toggleButton(@Nullable String name) {
    return new JToggleButtonFixture(robot(), findByName(name, JToggleButton.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JToolBarFixture toolBar() {
    return new JToolBarFixture(robot(), findByType(JToolBar.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JToolBarFixture toolBar(@NotNull GenericTypeMatcher<? extends JToolBar> matcher) {
    return new JToolBarFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JToolBarFixture toolBar(@Nullable String name) {
    return new JToolBarFixture(robot(), findByName(name, JToolBar.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTreeFixture tree() {
    return new JTreeFixture(robot(), findByType(JTree.class));
  }

  @RunsInEDT
  @Override
  @NotNull public JTreeFixture tree(@NotNull GenericTypeMatcher<? extends JTree> matcher) {
    return new JTreeFixture(robot(), find(matcher));
  }

  @RunsInEDT
  @Override
  @NotNull public JTreeFixture tree(@Nullable String name) {
    return new JTreeFixture(robot(), findByName(name, JTree.class));
  }

  /**
   * Finds a component by type, contained in this fixture's {@code Container}.
   *
   * @param <T> the type of component to find.
   * @param type the class for the type.
   * @return the found component.
   * @throws org.assertj.swing.exception.ComponentLookupException if a matching component could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one matching component is found.
   */
  protected final @NotNull <T extends Component> T findByType(@NotNull Class<T> type) {
    return finder().findByType(target(), type, requireShowing());
  }

  /**
   * Finds a component by name and type, contained in this fixture's {@code Container}.
   *
   * @param name the name of the component to find.
   * @param <T> the type of component to find.
   * @param type the class for the type.
   * @return the found component.
   * @throws org.assertj.swing.exception.ComponentLookupException if a matching component could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one matching component is found.
   */
  protected final @NotNull <T extends Component> T findByName(@Nullable String name, @NotNull Class<T> type) {
    return finder().findByName(target(), name, type, requireShowing());
  }

  /**
   * Finds a {@code Component} using the given {@link GenericTypeMatcher}, contained in this fixture's {@code Container}
   * .
   *
   * @param <T> the type of component to find.
   * @param matcher the matcher to use to find the component.
   * @return the found component.
   * @throws org.assertj.swing.exception.ComponentLookupException if a matching component could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one matching component is found.
   */
  protected final @NotNull <T extends Component> T find(@NotNull GenericTypeMatcher<? extends T> matcher) {
    return finder().find(target(), matcher);
  }

  @RunsInEDT
  @Override
  @NotNull public <T extends Component, F extends AbstractComponentFixture<?, T, ?>> F with(
                                                                                            @NotNull ComponentFixtureExtension<T, F> extension) {
    return extension.createFixture(robot(), target());
  }

  /**
   * @return the {@code ComponentFinder} contained in this fixture's {@code Robot}.
   */
  protected final @NotNull ComponentFinder finder() {
    return robot().finder();
  }

  /**
   * @return the timeout to use when looking for a dialog. It's value is 100 ms.
   */
  @Override
  @NotNull public Timeout defaultDialogLookupTimeout() {
    return DEFAULT_DIALOG_LOOKUP_TIMEOUT;
  }
}

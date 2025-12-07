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

import java.awt.Component;
import java.awt.Dialog;

import org.jspecify.annotations.Nullable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
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
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.timing.Timeout;

/**
 * Looks up AWT or Swing {@code Component}s in a {@code Container}.
 *
 * @author Alex Ruiz
 */
public interface ComponentContainerFixture {
  /**
   * Returns a {@link JButton} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JButton} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JButton} is found.
   */
  @RunsInEDT
  JButtonFixture button();

  /**
   * Finds a {@link JButton} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JButton}.
   * @return a fixture that manages the {@code JButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JButton} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JButton} that matches the
   *           given search criteria is found.
   */
  @RunsInEDT
  JButtonFixture button(GenericTypeMatcher<? extends JButton> matcher);

  /**
   * Finds a {@link JButton} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JButton} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JButton} having a matching
   *           name is found.
   */
  @RunsInEDT
  JButtonFixture button(@Nullable String name);

  /**
   * Returns a {@code JCheckBox} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JCheckBox} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JCheckBox} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JCheckBox} is found.
   */
  @RunsInEDT
  JCheckBoxFixture checkBox();

  /**
   * Finds a {@code JCheckBox} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JCheckBox}.
   * @return a fixture that manages the {@code JCheckBox} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JCheckBox} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JCheckBox} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JCheckBoxFixture checkBox(GenericTypeMatcher<? extends JCheckBox> matcher);

  /**
   * Finds a {@code JCheckBox} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JCheckBox} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JCheckBox} having a matching name could
   *           not be found.
   */
  @RunsInEDT
  JCheckBoxFixture checkBox(@Nullable String name);

  /**
   * Returns a {@code JComboBox} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JComboBox} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JComboBox} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JComboBox} is found.
   */
  @RunsInEDT
  JComboBoxFixture comboBox();

  /**
   * Finds a {@code JComboBox} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JComboBox}.
   * @return a fixture that manages the {@code JComboBox} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JComboBox} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JComboBox} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JComboBoxFixture comboBox(GenericTypeMatcher<? extends JComboBox> matcher);

  /**
   * Finds a {@code JComboBox} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JComboBox} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JComboBox} having a matching name could
   *           not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JComboBox} having a matching
   *           name is found.
   */
  @RunsInEDT
  JComboBoxFixture comboBox(@Nullable String name);

  /**
   * Returns the only {@code Dialog} currently available (if any). This method uses the value defined in
   * {@link #defaultDialogLookupTimeout()} as the default lookup timeout.
   *
   * @return a fixture that manages the {@code Dialog} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code Dialog} could not be found.
   * @see #dialog(Timeout)
   */
  @RunsInEDT
  DialogFixture dialog();

  /**
   * Returns the only {@code Dialog} currently available (if any).
   *
   * @param timeout the amount of time to wait for a {@code Dialog} to be found.
   * @return a fixture that manages the {@code Dialog} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code Dialog} could not be found.
   */
  @RunsInEDT
  DialogFixture dialog(Timeout timeout);

  /**
   * Finds a {@code Dialog} that matches the specified search criteria. This method uses the value defined in
   * {@link #defaultDialogLookupTimeout()} as the default lookup timeout.
   *
   * @param matcher contains the search criteria for finding a {@code Dialog}.
   * @return a fixture that manages the {@code Dialog} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code Dialog} that matches the given search criteria
   *           could not be found.
   * @see #dialog(GenericTypeMatcher, Timeout)
   */
  @RunsInEDT
  DialogFixture dialog(GenericTypeMatcher<? extends Dialog> matcher);

  /**
   * Finds a {@code Dialog} that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code Dialog}.
   * @param timeout the amount of time to wait for a {@code Dialog} to be found.
   * @return a fixture that manages the {@code Dialog} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code Dialog} that matches the given search criteria
   *           could not be found.
   */
  @RunsInEDT
  DialogFixture dialog(GenericTypeMatcher<? extends Dialog> matcher, Timeout timeout);

  /**
   * Finds a {@code Dialog} with a name matching the specified one. This method uses the value defined in
   * {@link #defaultDialogLookupTimeout()} as the default lookup timeout.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code Dialog} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code Dialog} that a matching name could not be found.
   * @see #dialog(String, Timeout)
   */
  @RunsInEDT
  DialogFixture dialog(@Nullable String name);

  /**
   * Finds a {@code Dialog} with a name matching the specified one.
   *
   * @param name the name to match.
   * @param timeout the amount of time to wait for a {@code Dialog} to be found.
   * @return a fixture that manages the {@code Dialog} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code Dialog} that a matching name could not be found.
   */
  @RunsInEDT
  DialogFixture dialog(@Nullable String name, Timeout timeout);

  /**
   * Returns the only {@code JFileChooser} currently available (if any). This method uses the value defined in
   * {@link #defaultDialogLookupTimeout()} as the default lookup timeout.
   *
   * @return a fixture that manages the {@code JFileChooser} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JFileChooser} could not be found.
   * @see #fileChooser(Timeout)
   */
  @RunsInEDT
  JFileChooserFixture fileChooser();

  /**
   * Returns the only {@code JFileChooser} currently available (if any).
   *
   * @param timeout the amount of time to wait for a {@code JFileChooser} to be found.
   * @return a fixture that manages the {@code JFileChooser} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JFileChooser} could not be found.
   */
  @RunsInEDT
  JFileChooserFixture fileChooser(Timeout timeout);

  /**
   * Finds a {@code JFileChooser} that matches the specified search criteria. This method uses the value defined in
   * {@link #defaultDialogLookupTimeout()} as the default lookup timeout.
   *
   * @param matcher contains the search criteria for finding a {@code JFileChooser}.
   * @return a fixture that manages the {@code JFileChooser} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JFileChooser} could not be found.
   * @see #fileChooser(GenericTypeMatcher, Timeout)
   */
  @RunsInEDT
  JFileChooserFixture fileChooser(GenericTypeMatcher<? extends JFileChooser> matcher);

  /**
   * Finds a {@code JFileChooser} that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JFileChooser}.
   * @param timeout the amount of time to wait for a {@code JFileChooser} to be found.
   * @return a fixture that manages the {@code JFileChooser} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JFileChooser} could not be found.
   */
  @RunsInEDT
  JFileChooserFixture fileChooser(GenericTypeMatcher<? extends JFileChooser> matcher, Timeout timeout);

  /**
   * Finds a {@code JFileChooser} with a name matching the specified one. This method uses the value defined in
   * {@link #defaultDialogLookupTimeout()} as the default lookup timeout.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JFileChooser} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JFileChooser} could not be found.
   * @see #fileChooser(String, Timeout)
   */
  @RunsInEDT
  JFileChooserFixture fileChooser(@Nullable String name);

  /**
   * Finds a {@code JFileChooser} with a name matching the specified one.
   *
   * @param name the name to match.
   * @param timeout the amount of time to wait for a {@code JFileChooser} to be found.
   * @return a fixture that manages the {@code JFileChooser} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JFileChooser} could not be found.
   */
  @RunsInEDT
  JFileChooserFixture fileChooser(@Nullable String name, Timeout timeout);

  /**
   * Returns a {@link JInternalFrame} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JInternalFrame} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JInternalFrame} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JInternalFrame} is found.
   */
  @RunsInEDT
  JInternalFrameFixture internalFrame();

  /**
   * Finds a {@link JInternalFrame} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JInternalFrame}.
   * @return a fixture that manages the {@code JInternalFrame} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JInternalFrame} that matches the given
   *           search criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JInternalFrame} that matches
   *           the given search criteria is found.
   */
  @RunsInEDT
  JInternalFrameFixture internalFrame(GenericTypeMatcher<? extends JInternalFrame> matcher);

  /**
   * Finds a {@link JInternalFrame} in this fixture's {@code Container} whose name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JInternalFrame} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JInternalFrame} having a matching name
   *           could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JInternalFrame} having a
   *           matching name is found.
   */
  @RunsInEDT
  JInternalFrameFixture internalFrame(@Nullable String name);

  /**
   * Returns a {@code JLabel} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JLabel} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JLabel} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JLabel} is found.
   */
  @RunsInEDT
  JLabelFixture label();

  /**
   * Finds a {@code JLabel} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JLabel}.
   * @return a fixture that manages the {@code JLabel} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JLabel} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JLabel} that matches the given
   *           search criteria is found.
   */
  @RunsInEDT
  JLabelFixture label(GenericTypeMatcher<? extends JLabel> matcher);

  /**
   * Finds a {@code JLabel} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JLabel} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JLabel} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JLabel} having a matching name
   *           could is found.
   */
  @RunsInEDT
  JLabelFixture label(@Nullable String name);

  /**
   * Returns a {@code JList} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JList} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JList} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JList} is found.
   */
  @RunsInEDT
  JListFixture list();

  /**
   * Finds a {@code JList} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JList}.
   * @return a fixture that manages the {@code JList} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JList} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JList} that matches the given
   *           search criteria is found.
   */
  @RunsInEDT
  JListFixture list(GenericTypeMatcher<? extends JList> matcher);

  /**
   * Finds a {@code JList} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JList} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JList} having a matching name could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JList} having a matching name
   *           is found.
   */
  @RunsInEDT
  JListFixture list(@Nullable String name);

  /**
   * Finds a {@code JMenuItem} in this fixture's {@code Container}, which path matches the given one.
   * <p>
   * For example, if we are looking for the menu with text "New" contained under the menu with text "File", we can
   * simply call
   * </p>
   *
   * <pre>
   * JMenuItemFixture menuItem = container.<strong>menuItemWithPath(&quot;File&quot;, &quot;Menu&quot;)</strong>;
   * </pre>
   *
   * @param path the path of the menu to find.
   * @return a fixture that manages the {@code JMenuItem} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JMenuItem} under the given path could not
   *           be found.
   * @throws AssertionError if the {@code Component} found under the given path is not a {@code JMenuItem}.
   */
  @RunsInEDT
  JMenuItemFixture menuItemWithPath(String... path);

  /**
   * Finds a {@code JMenuItem}, contained in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JMenuItem} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JMenuItem} having a matching name could
   *           not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JMenuItem} having a matching
   *           name is found.
   */
  @RunsInEDT
  JMenuItemFixture menuItem(@Nullable String name);

  /**
   * Finds a {@code JMenuItem}, contained in this fixture's {@code Container}, that matches the specified search
   * criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JMenuItem}.
   * @return a fixture that manages the {@code JMenuItem} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JMenuItem} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JMenuItem} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JMenuItemFixture menuItem(GenericTypeMatcher<? extends JMenuItem> matcher);

  /**
   * Returns the only {@code JOptionPane} currently available (if any). This method uses the value defined in
   * {@link #defaultDialogLookupTimeout()} as the default lookup timeout.
   *
   * @return a fixture that manages the {@code JOptionPane} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JOptionPane} could not be found.
   * @see #optionPane(Timeout)
   */
  @RunsInEDT
  JOptionPaneFixture optionPane();

  /**
   * Returns the only {@code JOptionPane} currently available (if any).
   *
   * @param timeout the amount of time to wait for a {@code JOptionPane} to be found.
   * @return a fixture that manages the {@code JOptionPane} found.
   * @throws org.assertj.swing.exception.WaitTimedOutError if a {@code JOptionPane} could not be found.
   */
  @RunsInEDT
  JOptionPaneFixture optionPane(Timeout timeout);

  /**
   * Returns a {@code JPanel} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JPanel} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JPanel} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JPanel} is found.
   */
  @RunsInEDT
  JPanelFixture panel();

  /**
   * Finds a {@code JPanel} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JPanel}.
   * @return a fixture that manages the {@code JPanel} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JPanel} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JPanel} that matches the given
   *           search criteria is found.
   */
  @RunsInEDT
  JPanelFixture panel(GenericTypeMatcher<? extends JPanel> matcher);

  /**
   * Finds a {@code JPanel} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JPanel} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JPanel} having a matching name could not
   *           be found.
   */
  @RunsInEDT
  JPanelFixture panel(@Nullable String name);

  /**
   * Returns a {@code JProgressBar} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JProgressBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JProgressBar} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JProgressBar} is found.
   */
  @RunsInEDT
  JProgressBarFixture progressBar();

  /**
   * Finds a {@code JProgressBar} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JProgressBar}.
   * @return a fixture that manages the {@code JProgressBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JProgressBar} that matches the given
   *           search criteria could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JProgressBar} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JProgressBarFixture progressBar(GenericTypeMatcher<? extends JProgressBar> matcher);

  /**
   * Finds a {@code JProgressBar} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JProgressBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JProgressBar} having a matching name could
   *           not be found.
   */
  @RunsInEDT
  JProgressBarFixture progressBar(@Nullable String name);

  /**
   * Returns a {@code JRadioButton} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JRadioButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JRadioButton} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JRadioButton} is found.
   */
  @RunsInEDT
  JRadioButtonFixture radioButton();

  /**
   * Finds a {@code JRadioButton} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JRadioButton}.
   * @return a fixture that manages the {@code JRadioButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JRadioButton} that matches the given
   *           search criteria could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JRadioButton} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JRadioButtonFixture radioButton(GenericTypeMatcher<? extends JRadioButton> matcher);

  /**
   * Finds a {@code JRadioButton} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JRadioButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JRadioButton} having a matching name could
   *           not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JRadioButton} having a
   *           matching name is found.
   */
  @RunsInEDT
  JRadioButtonFixture radioButton(@Nullable String name);

  /**
   * Returns a {@code JScrollBar} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JScrollBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JScrollBar} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JScrollBar} is found.
   */
  @RunsInEDT
  JScrollBarFixture scrollBar();

  /**
   * Finds a {@code JScrollBar} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JScrollBar}.
   * @return a fixture that manages the {@code JScrollBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JScrollBar} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JScrollBar} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JScrollBarFixture scrollBar(GenericTypeMatcher<? extends JScrollBar> matcher);

  /**
   * Finds a {@code JScrollBar} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JScrollBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JScrollBar} having a matching name could
   *           not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JScrollBar} having a matching
   *           name is found.
   */
  @RunsInEDT
  JScrollBarFixture scrollBar(@Nullable String name);

  /**
   * Returns a {@code JScrollPane} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JScrollPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JScrollPane} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JScrollPane} is found.
   */
  @RunsInEDT
  JScrollPaneFixture scrollPane();

  /**
   * Finds a {@code JScrollPane} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JScrollPane}.
   * @return a fixture that manages the {@code JScrollPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JScrollPane} that matches the given search
   *           criteria could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JScrollPane} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JScrollPaneFixture scrollPane(GenericTypeMatcher<? extends JScrollPane> matcher);

  /**
   * Finds a {@code JScrollPane} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JScrollPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JScrollPane} having a matching name could
   *           not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JScrollPane} having a matching
   *           name is found.
   */
  @RunsInEDT
  JScrollPaneFixture scrollPane(@Nullable String name);

  /**
   * Returns a {@code JSlider} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JSlider} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSlider} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSlider} is found.
   */
  @RunsInEDT
  JSliderFixture slider();

  /**
   * Finds a {@code JSlider} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JSlider}.
   * @return a fixture that manages the {@code JSlider} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSlider} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSlider} that matches the
   *           given search criteria is found.
   */
  @RunsInEDT
  JSliderFixture slider(GenericTypeMatcher<? extends JSlider> matcher);

  /**
   * Finds a {@code JSlider} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JSlider} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSlider} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSlider} having a matching
   *           name is found.
   */
  @RunsInEDT
  JSliderFixture slider(@Nullable String name);

  /**
   * Returns a {@code JSpinner} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JSpinner} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSpinner} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSpinner} is found.
   */
  @RunsInEDT
  JSpinnerFixture spinner();

  /**
   * Finds a {@code JSpinner} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JSpinner}.
   * @return a fixture that manages the {@code JSpinner} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSpinner} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSpinner} that matches the
   *           given search criteria is found.
   */
  @RunsInEDT
  JSpinnerFixture spinner(GenericTypeMatcher<? extends JSpinner> matcher);

  /**
   * Finds a {@code JSpinner} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JSpinner} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSpinner} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSpinner} having a matching
   *           name is found.
   */
  @RunsInEDT
  JSpinnerFixture spinner(@Nullable String name);

  /**
   * Returns the {@code JSplitPane} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JSplitPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSplitPane} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSplitPane} is found.
   */
  @RunsInEDT
  JSplitPaneFixture splitPane();

  /**
   * Finds a {@code JSplitPane} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JSplitPane}.
   * @return a fixture that manages the {@code JSplitPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSplitPane} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSplitPane} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JSplitPaneFixture splitPane(GenericTypeMatcher<? extends JSplitPane> matcher);

  /**
   * Finds a {@code JSplitPane} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JSplitPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JSplitPane} having a matching name could
   *           not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JSplitPane} having a matching
   *           name is found.
   */
  @RunsInEDT
  JSplitPaneFixture splitPane(@Nullable String name);

  /**
   * Returns a {@code JTabbedPane} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JTabbedPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTabbedPane} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTabbedPane} is found.
   */
  @RunsInEDT
  JTabbedPaneFixture tabbedPane();

  /**
   * Finds a {@code JTabbedPane} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JTabbedPane}.
   * @return a fixture that manages the {@code JTabbedPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTabbedPane} that matches the given search
   *           criteria could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTabbedPane} that matches the
   *           given search criteria is
   *           found.
   */
  @RunsInEDT
  JTabbedPaneFixture tabbedPane(GenericTypeMatcher<? extends JTabbedPane> matcher);

  /**
   * Finds a {@code JTabbedPane} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JTabbedPane} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTabbedPane} having a matching name could
   *           not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTabbedPane} having a matching
   *           name is found.
   */
  @RunsInEDT
  JTabbedPaneFixture tabbedPane(@Nullable String name);

  /**
   * Returns a {@code JTable} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JTable} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTable} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTable} having a matching name
   *           is found.
   */
  @RunsInEDT
  JTableFixture table();

  /**
   * Finds a {@code JTable} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JTable}.
   * @return a fixture that manages the {@code JTable} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTable} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTable} that matches the given
   *           search criteria is found.
   */
  @RunsInEDT
  JTableFixture table(GenericTypeMatcher<? extends JTable> matcher);

  /**
   * Finds a {@code JTable} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JTable} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTable} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTable} having a matching name
   *           is found.
   */
  @RunsInEDT
  JTableFixture table(@Nullable String name);

  /**
   * Returns a {@code JTextComponent} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JTextComponent} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTextComponent} having a matching name
   *           could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTextComponent} having a
   *           matching name is found.
   */
  @RunsInEDT
  JTextComponentFixture textBox();

  /**
   * Finds a {@code JTextComponent} in this fixture's {@code Container} managed by this fixture, that matches the
   * specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JTextComponent}.
   * @return a fixture that manages the {@code JTextComponent} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTextComponent} that matches the given
   *           search criteria could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTextComponent} that matches
   *           the given search criteria is
   *           found.
   */
  @RunsInEDT
  JTextComponentFixture textBox(GenericTypeMatcher<? extends JTextComponent> matcher);

  /**
   * Finds a {@code JTextComponent} in this fixture's {@code Container} managed by this fixture, which name matches the
   * specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JTextComponent} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTextComponent} having a matching name
   *           could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTextComponent} having a
   *           matching name is found.
   */
  JTextComponentFixture textBox(String name);

  /**
   * Returns a {@code JToggleButton} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JToggleButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JToggleButton} could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JToggleButton} is found.
   */
  @RunsInEDT
  JToggleButtonFixture toggleButton();

  /**
   * Finds a {@code JToggleButton} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JToggleButton}.
   * @return a fixture that manages the {@code JToggleButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JToggleButton} that matches the given
   *           search criteria could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JToggleButton} that matches
   *           the given search criteria is
   *           found.
   */
  @RunsInEDT
  JToggleButtonFixture toggleButton(GenericTypeMatcher<? extends JToggleButton> matcher);

  /**
   * Finds a {@code JToggleButton} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JToggleButton} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JToggleButton} having a matching name
   *           could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JToggleButton} having a
   *           matching name is found.
   */
  @RunsInEDT
  JToggleButtonFixture toggleButton(@Nullable String name);

  /**
   * Returns a {@code JToolBar} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JToolBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JToolBar} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JToolBar} having a matching
   *           name could is found.
   */
  @RunsInEDT
  JToolBarFixture toolBar();

  /**
   * Finds a {@code JToolBar} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JToolBar}.
   * @return a fixture that manages the {@code JToolBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JToolBar} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JToolBar} that matches the
   *           given search criteria is found.
   */
  @RunsInEDT
  JToolBarFixture toolBar(GenericTypeMatcher<? extends JToolBar> matcher);

  /**
   * Finds a {@code JToolBar} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JToolBar} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JToolBar} having a matching name could not
   *           be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JToolBar} having a matching
   *           name is found.
   */
  @RunsInEDT
  JToolBarFixture toolBar(@Nullable String name);

  /**
   * Returns a {@code JTree} found in this fixture's {@code Container}.
   *
   * @return a fixture that manages the {@code JTree} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTree} having a matching name could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTree} having a matching name
   *           is found.
   */
  @RunsInEDT
  JTreeFixture tree();

  /**
   * Finds a {@code JTree} in this fixture's {@code Container}, that matches the specified search criteria.
   *
   * @param matcher contains the search criteria for finding a {@code JTree}.
   * @return a fixture that manages the {@code JTree} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTree} that matches the given search
   *           criteria could not be found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTree} that matches the given
   *           search criteria is found.
   */
  @RunsInEDT
  JTreeFixture tree(GenericTypeMatcher<? extends JTree> matcher);

  /**
   * Finds a {@code JTree} in this fixture's {@code Container}, which name matches the specified one.
   *
   * @param name the name to match.
   * @return a fixture that manages the {@code JTree} found.
   * @throws org.assertj.swing.exception.ComponentLookupException if a {@code JTree} having a matching name could not be
   *           found.
   * @throws org.assertj.swing.exception.ComponentLookupException if more than one {@code JTree} having a matching name
   *           is found.
   */
  @RunsInEDT
  JTreeFixture tree(@Nullable String name);

  /**
   * Returns a {@link AbstractComponentFixture} managing a component inside this fixture's {@code Container}. This is an
   * extension method, to allow implementations of {@link AbstractContainerFixture} handle custom GUI components.
   *
   * @param <C> the type of component to manage.
   * @param <F> the type of fixture managing the component.
   * @param extension the {@code ComponentFixtureExtension} that creates the {@code ComponentFixture} to return.
   * @return a {@code ComponentFixture} managing a component inside this fixture's {@code Container}.
   */
  @RunsInEDT
  <C extends Component, F extends AbstractComponentFixture<?, C, ?>> F with(ComponentFixtureExtension<C, F> extension);

  /**
   * @return the timeout to use when looking for a dialog. It's value is 100 ms.
   */
  Timeout defaultDialogLookupTimeout();
}
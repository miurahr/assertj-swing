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
package org.assertj.swing.driver;

import static java.awt.event.KeyEvent.VK_F2;
import static org.assertj.swing.core.MouseButton.LEFT_BUTTON;

import java.awt.Point;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import org.assertj.swing.annotation.RunsInEDT;
import org.assertj.swing.core.Robot;
import org.assertj.swing.exception.ActionFailedException;

/**
 * {@link org.assertj.swing.cell.JTableCellWriter} that knows how to use {@code JTextComponent}s as cell editors.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class JTableTextComponentEditorCellWriter extends AbstractJTableCellWriter {
  protected final JTextComponentDriver driver;

  public JTableTextComponentEditorCellWriter(Robot robot) {
    super(robot);
    driver = new JTextComponentDriver(robot);
  }

  @RunsInEDT
  @Override
  public void enterValue(JTable table, int row, int column, String value) {
    JTextComponent editor = doStartCellEditing(table, row, column);
    driver.replaceText(editor, value);
    stopCellEditing(table, row, column);
  }

  @RunsInEDT
  @Override
  public void startCellEditing(JTable table, int row, int column) {
    doStartCellEditing(table, row, column);
  }

  @RunsInEDT
  private JTextComponent doStartCellEditing(JTable table, int row, int column) {
    Point cellLocation = cellLocation(table, row, column, location());
    JTextComponent textComponent = null;
    try {
      textComponent = activateEditorWithF2Key(table, row, column, cellLocation);
    } catch (ActionFailedException e) {
      textComponent = activateEditorWithDoubleClick(table, row, column, cellLocation);
    }
    cellEditor(cellEditor(table, row, column));
    return Objects.requireNonNull(textComponent);
  }

  @RunsInEDT
  @Nullable
  private JTextComponent activateEditorWithF2Key(JTable table, int row, int column,
                                                 Point cellLocation) {
    robot.click(table, cellLocation);
    robot.pressAndReleaseKeys(VK_F2);
    return waitForEditorActivation(table, row, column);
  }

  @RunsInEDT
  @Nullable
  private JTextComponent activateEditorWithDoubleClick(JTable table, int row, int column,
                                                       Point cellLocation) {
    robot.click(table, cellLocation, LEFT_BUTTON, 2);
    return waitForEditorActivation(table, row, column);
  }

  @RunsInEDT
  @Nullable
  private JTextComponent waitForEditorActivation(JTable table, int row, int column) {
    return waitForEditorActivation(table, row, column, JTextComponent.class);
  }
}

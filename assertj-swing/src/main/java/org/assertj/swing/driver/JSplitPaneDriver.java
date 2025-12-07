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

import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static org.assertj.swing.core.MouseButton.LEFT_BUTTON;
import static org.assertj.swing.driver.ComponentPreconditions.checkEnabledAndShowing;
import static org.assertj.swing.driver.JSplitPaneLocationCalculator.locationToMoveDividerTo;
import static org.assertj.swing.driver.JSplitPaneSetDividerLocationTask.setDividerLocation;
import static org.assertj.swing.edt.GuiActionRunner.execute;

import java.awt.Point;
import java.util.Objects;

import javax.swing.JSplitPane;

import org.assertj.swing.annotation.RunsInCurrentThread;
import org.assertj.swing.annotation.RunsInEDT;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.internal.annotation.InternalApi;
import org.assertj.swing.util.GenericRange;

/**
 * <p>
 * Supports functional testing of {@code JSplitPane}s.
 * </p>
 *
 * <p>
 * <b>Note:</b> This class is intended for internal use only. Please use the classes in the package
 * {@link org.assertj.swing.fixture} in your tests.
 * </p>
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@InternalApi
public class JSplitPaneDriver extends JComponentDriver {
  /**
   * Creates a new {@link JSplitPaneDriver}.
   *
   * @param robot the robot to use to simulate user input.
   */
  public JSplitPaneDriver(Robot robot) {
    super(robot);
  }

  /**
   * Sets the divider position to an absolute position.
   * <p>
   * Since 1.2, this method respects the minimum and maximum values of the left and right components inside the given
   * {@code JSplitPane}.
   * </p>
   *
   * @param splitPane the target {@code JSplitPane}.
   * @param location the location to move the divider to.
   * @throws IllegalStateException if the {@code JSplitPane} is disabled.
   * @throws IllegalStateException if the {@code JSplitPane} is not showing on the screen.
   */
  @RunsInEDT
  public void moveDividerTo(JSplitPane splitPane, int location) {
    int newLocation = locationToMoveDividerTo(splitPane, location);
    simulateMovingDivider(splitPane, newLocation);
    setDividerLocation(splitPane, newLocation);
    robot.waitForIdle();
  }

  @RunsInEDT
  private void simulateMovingDivider(JSplitPane split, int location) {
    if (split.getOrientation() == VERTICAL_SPLIT) {
      simulateMovingDividerVertically(split, location);
      return;
    }
    simulateMovingDividerHorizontally(split, location);
  }

  @RunsInEDT
  private void simulateMovingDividerVertically(JSplitPane splitPane, int location) {
    GenericRange<Point> whereToMove = findWhereToMoveDividerVertically(splitPane, location);
    simulateMovingDivider(splitPane, whereToMove);
  }

  @RunsInEDT
  private static GenericRange<Point> findWhereToMoveDividerVertically(final JSplitPane splitPane,
                                                                      final int location) {
    GenericRange<Point> result = execute(new GuiQuery<GenericRange<Point>>() {
      @Override
      protected GenericRange<Point> executeInEDT() {
        checkEnabledAndShowing(splitPane);
        return whereToMoveDividerVertically(splitPane, location);
      }
    });
    return Objects.requireNonNull(result);
  }

  @RunsInCurrentThread
  private static GenericRange<Point> whereToMoveDividerVertically(JSplitPane splitPane, int location) {
    int x = splitPane.getWidth() / 2;
    int dividerLocation = splitPane.getDividerLocation();
    return new GenericRange<Point>(new Point(x, dividerLocation), new Point(x, location));
  }

  private void simulateMovingDividerHorizontally(JSplitPane splitPane, int location) {
    GenericRange<Point> whereToMove = findWhereToMoveDividerHorizontally(splitPane, location);
    simulateMovingDivider(splitPane, whereToMove);
  }

  @RunsInEDT
  private static GenericRange<Point> findWhereToMoveDividerHorizontally(final JSplitPane splitPane,
                                                                        final int location) {
    GenericRange<Point> result = execute(new GuiQuery<GenericRange<Point>>() {
      @Override
      protected GenericRange<Point> executeInEDT() {
        checkEnabledAndShowing(splitPane);
        return whereToMoveDividerHorizontally(splitPane, location);
      }
    });
    return Objects.requireNonNull(result);
  }

  @RunsInCurrentThread
  private static GenericRange<Point> whereToMoveDividerHorizontally(JSplitPane splitPane, int location) {
    int y = splitPane.getHeight() / 2;
    int dividerLocation = splitPane.getDividerLocation();
    return new GenericRange<Point>(new Point(dividerLocation, y), new Point(location, y));
  }

  @RunsInEDT
  private void simulateMovingDivider(JSplitPane splitPane, GenericRange<Point> range) {
    try {
      robot.moveMouse(splitPane, range.from());
      robot.pressMouseWhileRunning(LEFT_BUTTON, () -> robot.moveMouse(splitPane, range.to()));
    } catch (RuntimeException ignored) {}
  }
}

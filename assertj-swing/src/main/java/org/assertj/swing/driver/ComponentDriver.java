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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.swing.awt.AWT.visibleCenterOf;
import static org.assertj.swing.core.MouseButton.LEFT_BUTTON;
import static org.assertj.swing.core.MouseButton.RIGHT_BUTTON;
import static org.assertj.swing.driver.ComponentEnabledCondition.untilIsEnabled;
import static org.assertj.swing.driver.ComponentPerformDefaultAccessibleActionTask.performDefaultAccessibleAction;
import static org.assertj.swing.driver.ComponentPreconditions.checkEnabledAndShowing;
import static org.assertj.swing.driver.ComponentPreconditions.checkShowing;
import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.assertj.swing.format.Formatting.format;
import static org.assertj.swing.query.ComponentEnabledQuery.isEnabled;
import static org.assertj.swing.query.ComponentHasFocusQuery.hasFocus;
import static org.assertj.swing.query.ComponentSizeQuery.sizeOf;
import static org.assertj.swing.query.ComponentVisibleQuery.isVisible;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.util.TimeoutWatch.startWatchWithTimeoutOf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.assertj.core.description.Description;
import org.assertj.swing.annotation.RunsInCurrentThread;
import org.assertj.swing.annotation.RunsInEDT;
import org.assertj.swing.core.ComponentDragAndDrop;
import org.assertj.swing.core.KeyPressInfo;
import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.MouseClickInfo;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.Settings;
import org.assertj.swing.edt.GuiLazyLoadingDescription;
import org.assertj.swing.internal.annotation.InternalApi;
import org.assertj.swing.timing.Timeout;
import org.assertj.swing.util.TimeoutWatch;

/**
 * <p>
 * Supports functional testing of AWT or Swing {@code Component}s.
 * </p>
 *
 * <p>
 * <b>Note:</b> This class is intended for internal use only. Please use the classes in the package
 * {@link org.assertj.swing.fixture} in your tests.
 * </p>
 *
 * @author Alex Ruiz
 */
@InternalApi
public class ComponentDriver {
  private static final String ENABLED_PROPERTY = "enabled";
  private static final String SIZE_PROPERTY = "size";
  private static final String VISIBLE_PROPERTY = "visible";

  protected final Robot robot;

  private final ComponentDragAndDrop dragAndDrop;

  /**
   * Creates a new {@link ComponentDriver}.
   *
   * @param robot the robot to use to simulate user input.
   */
  public ComponentDriver(Robot robot) {
    this.robot = robot;
    dragAndDrop = new ComponentDragAndDrop(robot);
  }

  /**
   * Simulates a user clicking once the given AWT or Swing {@code Component} using the left mouse button.
   *
   * @param c the {@code Component} to click on.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void click(Component c) {
    checkClickAllowed(c);
    robot.click(c);
  }

  /**
   * Simulates a user clicking once the given AWT or Swing {@code Component} using the given mouse button.
   *
   * @param c the {@code Component} to click on.
   * @param button the mouse button to use.
   * @throws NullPointerException if the given {@code MouseButton} is {@code null}.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void click(Component c, MouseButton button) {
    click(c, button, 1);
  }

  /**
   * Simulates a user clicking the given mouse button, the given times on the given AWT or Swing {@code Component}.
   *
   * @param c the {@code Component} to click on.
   * @param mouseClickInfo specifies the button to click and the times the button should be clicked.
   * @throws NullPointerException if the given {@code MouseClickInfo} is {@code null}.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void click(Component c, MouseClickInfo mouseClickInfo) {
    checkNotNull(mouseClickInfo);
    click(c, mouseClickInfo.button(), mouseClickInfo.times());
  }

  /**
   * Simulates a user double-clicking the given AWT or Swing {@code Component}.
   *
   * @param c the {@code Component} to click on.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void doubleClick(Component c) {
    click(c, LEFT_BUTTON, 2);
  }

  /**
   * Simulates a user right-clicking the given AWT or Swing {@code Component}.
   *
   * @param c the {@code Component} to click on.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void rightClick(Component c) {
    click(c, RIGHT_BUTTON);
  }

  /**
   * Simulates a user clicking the given mouse button, the given times on the given AWT or Swing {@code Component}.
   *
   * @param c the {@code Component} to click on.
   * @param button the mouse button to click.
   * @param times the number of times to click the given mouse button.
   * @throws NullPointerException if the given {@code MouseButton} is {@code null}.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void click(Component c, MouseButton button, int times) {
    checkNotNull(button);
    checkClickAllowed(c);
    robot.click(c, button, times);
  }

  /**
   * Simulates a user clicking at the given position on the given AWT or Swing {@code Component}.
   *
   * @param c the {@code Component} to click on.
   * @param where the position where to click.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void click(Component c, Point where) {
    checkClickAllowed(c);
    robot.click(c, where);
  }

  protected Settings settings() {
    return robot.settings();
  }

  /**
   * Asserts that the size of the AWT or Swing {@code Component} is equal to given one.
   *
   * @param c the target {@code Component}.
   * @param size the given size to match.
   * @throws AssertionError if the size of the {@code Component} is not equal to the given size.
   */
  @RunsInEDT
  public void requireSize(Component c, Dimension size) {
    assertThat(sizeOf(c)).as(propertyName(c, SIZE_PROPERTY)).isEqualTo(size);
  }

  /**
   * Asserts that the AWT or Swing {@code Component} is visible.
   *
   * @param c the target {@code Component}.
   * @throws AssertionError if the {@code Component} is not visible.
   */
  @RunsInEDT
  public void requireVisible(Component c) {
    assertThat(isVisible(c)).as(visibleProperty(c)).isTrue();
  }

  /**
   * Asserts that the AWT or Swing {@code Component} is not visible.
   *
   * @param c the target {@code Component}.
   * @throws AssertionError if the {@code Component} is visible.
   */
  @RunsInEDT
  public void requireNotVisible(Component c) {
    assertThat(isVisible(c)).as(visibleProperty(c)).isFalse();
  }

  @RunsInEDT
  private static Description visibleProperty(Component c) {
    return propertyName(c, VISIBLE_PROPERTY);
  }

  /**
   * Asserts that the AWT or Swing {@code Component} has input focus.
   *
   * @param c the target {@code Component}.
   * @throws AssertionError if the {@code Component} does not have input focus.
   */
  @RunsInEDT
  public void requireFocused(Component c) {
    assertThat(hasFocus(c)).as(requiredFocusedErrorMessage(c)).isTrue();
  }

  private static Description requiredFocusedErrorMessage(final Component c) {
    return new GuiLazyLoadingDescription() {
      @Override
      protected String loadDescription() {
        return String.format("Expected component %s to have input focus", format(c));
      }
    };
  }

  /**
   * Asserts that the AWT or Swing {@code Component} is enabled.
   *
   * @param c the target {@code Component}.
   * @throws AssertionError if the {@code Component} is disabled.
   */
  @RunsInEDT
  public void requireEnabled(Component c) {
    assertThat(isEnabled(c)).as(enabledProperty(c)).isTrue();
  }

  /**
   * Asserts that the AWT or Swing {@code Component} is enabled.
   *
   * @param c the target {@code Component}.
   * @param timeout the time this fixture will wait for the {@code Component} to be enabled.
   * @throws org.assertj.swing.exception.WaitTimedOutError if the {@code Component} is never enabled.
   */
  @RunsInEDT
  public void requireEnabled(Component c, Timeout timeout) {
    pause(untilIsEnabled(c), timeout);
  }

  /**
   * Asserts that the AWT or Swing {@code Component} is disabled.
   *
   * @param c the target {@code Component}.
   * @throws AssertionError if the {@code Component} is enabled.
   */
  @RunsInEDT
  public void requireDisabled(Component c) {
    assertThat(isEnabled(c)).as(enabledProperty(c)).isFalse();
  }

  @RunsInEDT
  private static Description enabledProperty(Component c) {
    return propertyName(c, ENABLED_PROPERTY);
  }

  /**
   * Simulates a user pressing and releasing the given keys on the AWT or Swing {@code Component}.
   *
   * @param c the target {@code Component}.
   * @param keyCodes one or more codes of the keys to press.
   * @throws NullPointerException if the given array of codes is {@code null}.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @throws IllegalArgumentException if the given code is not a valid key code.
   * @see java.awt.event.KeyEvent
   */
  @RunsInEDT
  public void pressAndReleaseKeys(Component c, int... keyCodes) {
    checkNotNull(keyCodes);
    checkInEdtEnabledAndShowing(c);
    focusAndWaitForFocusGain(c);
    robot.pressAndReleaseKeys(keyCodes);
  }

  /**
   * Simulates a user pressing and releasing the given key on the AWT or Swing {@code Component}. Modifiers is a mask
   * from the available AWT {@code InputEvent} masks.
   *
   * @param c the target {@code Component}.
   * @param keyPressInfo specifies the key and modifiers to press.
   * @throws NullPointerException if the given {@code KeyPressInfo} is {@code null}.
   * @throws IllegalArgumentException if the given code is not a valid key code.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @see java.awt.event.KeyEvent
   * @see java.awt.event.InputEvent
   */
  @RunsInEDT
  public void pressAndReleaseKey(Component c, KeyPressInfo keyPressInfo) {
    Objects.requireNonNull(keyPressInfo);
    pressAndReleaseKey(c, keyPressInfo.keyCode(), keyPressInfo.modifiers());
  }

  /**
   * Simulates a user pressing and releasing the given key on the AWT or Swing {@code Component}. Modifiers is a mask
   * from the available AWT {@code InputEvent} masks.
   *
   * @param c the target {@code Component}.
   * @param keyCode the code of the key to press.
   * @param modifiers the given modifiers.
   * @throws IllegalArgumentException if the given code is not a valid key code. *
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @see java.awt.event.KeyEvent
   * @see java.awt.event.InputEvent
   */
  @RunsInEDT
  public void pressAndReleaseKey(Component c, int keyCode, int[] modifiers) {
    focusAndWaitForFocusGain(c);
    robot.pressAndReleaseKey(keyCode, modifiers);
  }

  /**
   * Simulates a user pressing given key on the AWT or Swing {@code Component}.
   *
   * @param c the target {@code Component}.
   * @param keyCode the code of the key to press.
   * @throws IllegalArgumentException if the given code is not a valid key code.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @see #pressKeyWhileRunning(Component, int, Runnable)
   * @see java.awt.event.KeyEvent
   */
  @RunsInEDT
  public void pressKey(Component c, int keyCode) {
    focusAndWaitForFocusGain(c);
    robot.pressKey(keyCode);
  }

  /**
   * Simulates a user pressing given key on the AWT or Swing {@code Component}, running the given runnable and releasing
   * the key again.
   *
   * @param c the target {@code Component}.
   * @param keyCode the code of the key to press.
   * @param runnable the {@link Runnable} to run while the key is pressed
   * @throws IllegalArgumentException if the given code is not a valid key code.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @see #pressKey(Component, int)
   * @see java.awt.event.KeyEvent
   */
  @RunsInEDT
  public void pressKeyWhileRunning(Component c, int keyCode, Runnable runnable) {
    focusAndWaitForFocusGain(c);
    robot.pressKeyWhileRunning(keyCode, runnable);
  }

  /**
   * Simulates a user releasing the given key on the AWT or Swing {@code Component}.
   *
   * @param c the target {@code Component}.
   * @param keyCode the code of the key to release.
   * @throws IllegalArgumentException if the given code is not a valid key code.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @see java.awt.event.KeyEvent
   */
  @RunsInEDT
  public void releaseKey(Component c, int keyCode) {
    focusAndWaitForFocusGain(c);
    robot.releaseKey(keyCode);
  }

  /**
   * Gives input focus to the given AWT or Swing {@code Component} and waits until the {@code Component} has focus.
   *
   * @param c the {@code Component} to give focus to.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void focusAndWaitForFocusGain(Component c) {
    checkInEdtEnabledAndShowing(c);
    robot.focusAndWaitForFocusGain(c);
  }

  /**
   * Gives input focus to the given AWT or Swing {@code Component}. Note that the {@code Component} may not yet have
   * focus when this method returns.
   *
   * @param c the {@code Component} to give focus to.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  public void focus(Component c) {
    checkInEdtEnabledAndShowing(c);
    robot.focus(c);
  }

  /**
   * Performs a drag action at the given point.
   *
   * @param c the target {@code Component}.
   * @param where the point where to start the drag action.
   */
  @RunsInEDT
  protected final void drag(Component c, Point where) {
    dragAndDrop.drag(c, where);
  }

  /**
   * Ends a drag operation at the centre of the {@code Component}.
   *
   * @param c the target {@code Component}.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @throws org.assertj.swing.exception.ActionFailedException if there is no drag action in effect.
   */
  @RunsInEDT
  public void drop(Component c) {
    checkInEdtEnabledAndShowing(c);
    drop(c, visibleCenterOf(c));
  }

  /**
   * <p>
   * Ends a drag operation, releasing the mouse button over the given target location.
   * </p>
   * <p>
   * This method is tuned for native drag/drop operations, so if you get odd behavior, you might try using a simple
   * {@link Robot#moveMouse(Component, int, int)} and {@link Robot#releaseMouseButtons()}.
   *
   * @param c the target {@code Component}.
   * @param where the point where the drag operation ends.
   * @throws org.assertj.swing.exception.ActionFailedException if there is no drag action in effect.
   */
  @RunsInEDT
  protected final void drop(Component c, Point where) {
    dragAndDrop.drop(c, where);
  }

  /**
   * Move the mouse appropriately to get from the source to the destination. Enter/exit events will be generated where
   * appropriate.
   *
   * @param c the target {@code Component}.
   * @param where the point to drag over.
   */
  protected final void dragOver(Component c, Point where) {
    dragAndDrop.dragOver(c, where);
  }

  /**
   * <p>
   * Performs the {@code AccessibleAction} in the given AWT or Swing {@code Component}'s event queue.
   * </p>
   *
   * <p>
   * <b>Note:</b> This method is accessed in the current executing thread. Such thread may or may not be the event
   * dispatch thread (EDT). Client code must call this method from the EDT.
   * </p>
   *
   * @param c the given {@code Component}.
   * @throws org.assertj.swing.exception.ActionFailedException if something goes wrong.
   */
  @RunsInCurrentThread
  protected final void performAccessibleActionOf(Component c) {
    performDefaultAccessibleAction(c);
    robot.waitForIdle();
  }

  /**
   * <p>
   * Wait the given number of milliseconds for the AWT or Swing {@code Component} to be showing and ready. Returns
   * {@code false} if the operation times out.
   * </p>
   *
   * <p>
   * <b>Note:</b> This method is accessed in the current executing thread. Such thread may or may not be the event
   * dispatch thread (EDT). Client code must call this method from the EDT.
   * </p>
   *
   * @param c the given {@code Component}.
   * @param timeout the time in milliseconds to wait for the {@code Component} to be showing and ready.
   * @return {@code true} if the {@code Component} is showing and ready, {@code false} otherwise.
   */
  @RunsInCurrentThread
  protected final boolean waitForShowing(Component c, long timeout) {
    // TODO test
    if (robot.isReadyForInput(c)) {
      return true;
    }
    TimeoutWatch watch = startWatchWithTimeoutOf(timeout);
    while (!robot.isReadyForInput(c)) {
      if (c instanceof JPopupMenu) {
        // move the mouse over the parent menu item to ensure the sub-menu shows
        Component invoker = ((JPopupMenu) c).getInvoker();
        if (invoker instanceof JMenu) {
          robot.jitter(invoker);
        }
      }
      if (watch.isTimeOut()) {
        return false;
      }
      pause();
    }
    return true;
  }

  /**
   * Shows a pop-up menu using the given AWT or Swing {@code Component} as the invoker of the pop-up menu.
   *
   * @param c the invoker of the {@code JPopupMenu}.
   * @return the displayed pop-up menu.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @throws org.assertj.swing.exception.ComponentLookupException if a pop-up menu cannot be found.
   */
  @RunsInEDT
  public JPopupMenu invokePopupMenu(Component c) {
    checkClickAllowed(c);
    return robot.showPopupMenu(c);
  }

  /**
   * Shows a pop-up menu at the given point using the given AWT or Swing {@code Component} as the invoker of the pop-up
   * menu.
   *
   * @param c the invoker of the {@code JPopupMenu}.
   * @param p the given point where to show the pop-up menu.
   * @return the displayed pop-up menu.
   * @throws NullPointerException if the given point is {@code null}.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   * @throws org.assertj.swing.exception.ComponentLookupException if a pop-up menu cannot be found.
   */
  @RunsInEDT
  public JPopupMenu invokePopupMenu(Component c, Point p) {
    checkNotNull(p);
    checkClickAllowed(c);
    return robot.showPopupMenu(c, p);
  }

  /**
   * Verifies that the given AWT or Swing {@code Component} is enabled and showing on the screen.
   *
   * @param c the {@code Component} to check.
   * @throws IllegalStateException if the {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  protected static void checkInEdtEnabledAndShowing(final Component c) {
    execute(() -> checkEnabledAndShowing(c));
  }

  /**
   * Verifies that the given AWT or Swing {@code Component} is showing on the screen.
   *
   * @param c the {@code Component} to check.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  protected static void checkInEdtShowing(final Component c) {
    execute(() -> checkShowing(c));
  }

  /**
   * Verifies that the given AWT or Swing {@code Component} is enabled and showing on the screen.
   *
   * @param c the {@code Component} to check.
   * @throws IllegalStateException if {@link Settings#clickOnDisabledComponentsAllowed()} is <code>false</code> and the
   *           {@code Component} is disabled.
   * @throws IllegalStateException if the {@code Component} is not showing on the screen.
   */
  @RunsInEDT
  protected void checkClickAllowed(final Component c) {
    if (robot.settings().clickOnDisabledComponentsAllowed()) {
      checkInEdtShowing(c);
    } else {
      checkInEdtEnabledAndShowing(c);
    }
  }

  /**
   * Formats the name of a property of the given AWT or Swing {@code Component} by concatenating the value obtained from
   * {@link org.assertj.swing.format.Formatting#format(Component)} with the given property name.
   *
   * @param c the given {@code Component}.
   * @param propertyName the name of the property.
   * @return the description of a property belonging to a {@code Component}.
   * @see org.assertj.swing.format.ComponentFormatter
   * @see org.assertj.swing.format.Formatting#format(Component)
   */
  @RunsInEDT
  public static Description propertyName(final Component c, final String propertyName) {
    return new GuiLazyLoadingDescription() {
      @Override
      protected String loadDescription() {
        return String.format("%s - property:'%s'", format(c), propertyName);
      }
    };
  }

  /**
   * Simulates a user moving the mouse pointer to the given coordinates relative to the given AWT or Swing
   * {@code Component}. This method will <b>not</b> throw any exceptions if the it was not possible to move the mouse
   * pointer.
   *
   * @param c the given {@code Component}.
   * @param p coordinates relative to the given {@code Component}.
   */
  @RunsInEDT
  protected final void moveMouseIgnoringAnyError(Component c, Point p) {
    moveMouseIgnoringAnyError(c, p.x, p.y);
  }

  /**
   * Simulates a user moving the mouse pointer to the given coordinates relative to the given AWT or Swing
   * {@code Component}. This method will <b>not</b> throw any exceptions if the it was not possible to move the mouse
   * pointer.
   *
   * @param c the given {@code Component}.
   * @param x horizontal coordinate relative to the given {@code Component}.
   * @param y vertical coordinate relative to the given {@code Component}.
   */
  @RunsInEDT
  protected final void moveMouseIgnoringAnyError(Component c, int x, int y) {
    try {
      robot.moveMouse(c, x, y);
    } catch (RuntimeException ignored) {}
  }

  /**
   * Returns the font of the given AWT or Swing {@code Component}.
   *
   * @param c the given {@code Component}.
   * @return the font of the given {@code Component}.
   */
  @RunsInEDT
  public Font fontOf(final Component c) {
    Font result = execute(() -> c.getFont());
    return checkNotNull(result);
  }

  /**
   * Returns the background color of the given AWT or Swing {@code Component}.
   *
   * @param c the given {@code Component}.
   * @return the background color of the given {@code Component}.
   */
  @RunsInEDT
  public Color backgroundOf(final Component c) {
    Color result = execute(() -> c.getBackground());
    return checkNotNull(result);
  }

  /**
   * Returns the foreground color of the given AWT or Swing {@code Component}.
   *
   * @param c the given {@code Component}.
   * @return the foreground color of the given {@code Component}.
   */
  @RunsInEDT
  public Color foregroundOf(final Component c) {
    Color result = execute(c::getForeground);
    return Objects.requireNonNull(result);
  }
}

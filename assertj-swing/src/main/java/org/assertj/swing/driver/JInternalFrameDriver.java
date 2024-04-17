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
import static org.assertj.swing.driver.ComponentPreconditions.checkShowing;
import static org.assertj.swing.driver.JInternalFrameAction.DEICONIFY;
import static org.assertj.swing.driver.JInternalFrameAction.ICONIFY;
import static org.assertj.swing.driver.JInternalFrameAction.MAXIMIZE;
import static org.assertj.swing.driver.JInternalFrameAction.NORMALIZE;
import static org.assertj.swing.driver.JInternalFrameIconQuery.isIconified;
import static org.assertj.swing.driver.JInternalFrameSetIconTask.setIcon;
import static org.assertj.swing.driver.JInternalFrameSetMaximumTask.setMaximum;
import static org.assertj.swing.driver.JInternalFrameTitleQuery.titleOf;
import static org.assertj.swing.driver.WindowLikeContainers.closeButtonLocation;
import static org.assertj.swing.driver.WindowLikeContainers.iconifyButtonLocation;
import static org.assertj.swing.driver.WindowLikeContainers.maximizeButtonLocation;
import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.assertj.swing.exception.ActionFailedException.actionFailure;
import static org.assertj.swing.format.Formatting.format;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyVetoException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;

import org.assertj.core.util.VisibleForTesting;
import org.assertj.swing.annotation.RunsInCurrentThread;
import org.assertj.swing.annotation.RunsInEDT;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.exception.UnexpectedException;
import org.assertj.swing.internal.annotation.InternalApi;
import org.assertj.swing.util.Pair;
import org.assertj.swing.util.Triple;

/**
 * <p>
 * Supports functional testing of {@code JInternalFrame}s.
 * </p>
 *
 * <p>
 * <b>Note:</b> This class is intended for internal use only. Please use the classes in the package
 * {@link org.assertj.swing.fixture} in your tests.
 * </p>
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Christian Rösch
 */
@InternalApi
public class JInternalFrameDriver extends JComponentDriver {
  /**
   * Creates a new {@link JInternalFrameDriver}.
   *
   * @param robot the robot to use to simulate user input.
   */
  public JInternalFrameDriver(@NotNull Robot robot) {
    super(robot);
  }

  /**
   * Brings the given {@code JInternalFrame} to the front.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   */
  @RunsInEDT
  public void moveToFront(final @NotNull JInternalFrame internalFrame) {
    execute(() -> // it seems that moving to front always works, regardless if the internal frame is invisible and/or
                  // disabled.
    internalFrame.toFront());
  }

  /**
   * Brings the given {@code JInternalFrame} to the back.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   */
  @RunsInEDT
  public void moveToBack(final @NotNull JInternalFrame internalFrame) {
    execute(() -> // it seems that moving to back always works, regardless if the internal frame is invisible and/or
                  // disabled.
    internalFrame.moveToBack());
  }

  /**
   * Maximises the given {@code JInternalFrame}, deconifying it first if it is iconified.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @throws IllegalStateException if the {@code JInternalFrame} is not maximisable.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws org.assertj.swing.exception.ActionFailedException if the {@code JInternalFrame} vetoes the action.
   */
  @RunsInEDT
  public void maximize(@NotNull JInternalFrame internalFrame) {
    Pair<Container, Point> maximizeLocation = maximizeLocationOf(internalFrame);
    maximizeOrNormalize(internalFrame, MAXIMIZE, maximizeLocation);
  }

  @RunsInEDT
  @NotNull private static Pair<Container, Point> maximizeLocationOf(final @NotNull JInternalFrame internalFrame) {
    Pair<Container, Point> result = execute(new GuiQuery<Pair<Container, Point>>() {
      @Override
      @Nullable protected Pair<Container, Point> executeInEDT() {
        checkCanMaximize(internalFrame);
        return findMaximizeLocation(internalFrame);
      }
    });
    return checkNotNull(result);
  }

  @RunsInCurrentThread
  private static void checkCanMaximize(@NotNull JInternalFrame internalFrame) {
    checkShowingOrIconified(internalFrame);
    if (!internalFrame.isMaximizable()) {
      String msg = String.format("The JInternalFrame <%s> is not maximizable", format(internalFrame));
      throw new IllegalStateException(msg);
    }
  }

  /**
   * Normalises the given {@code JInternalFrame}, deconifying it first if it is iconified.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws org.assertj.swing.exception.ActionFailedException if the {@code JInternalFrame} vetoes the action.
   */
  @RunsInEDT
  public void normalize(@NotNull JInternalFrame internalFrame) {
    Pair<Container, Point> normalizeLocation = validateAndFindNormalizeLocation(internalFrame);
    maximizeOrNormalize(internalFrame, NORMALIZE, normalizeLocation);
  }

  @RunsInEDT
  private static Pair<Container, Point> validateAndFindNormalizeLocation(final @NotNull JInternalFrame internalFrame) {
    return execute(new GuiQuery<Pair<Container, Point>>() {
      @Override
      protected Pair<Container, Point> executeInEDT() {
        checkShowingOrIconified(internalFrame);
        return findMaximizeLocation(internalFrame);
      }
    });
  }

  @RunsInCurrentThread
  private static void checkShowingOrIconified(@NotNull JInternalFrame internalFrame) {
    if (!internalFrame.isIcon()) {
      checkShowing(internalFrame);
    }
  }

  @RunsInCurrentThread
  @NotNull private static Pair<Container, Point> findMaximizeLocation(@NotNull JInternalFrame internalFrame) {
    Container clickTarget = internalFrame.isIcon() ? internalFrame.getDesktopIcon() : internalFrame;
    Point location = maximizeButtonLocation(checkNotNull(clickTarget));
    return Pair.of(clickTarget, location);
  }

  @RunsInEDT
  private void maximizeOrNormalize(@NotNull JInternalFrame internalFrame, @NotNull JInternalFrameAction action,
                                   @NotNull Pair<Container, Point> toMoveMouseTo) {
    moveMouseIgnoringAnyError(toMoveMouseTo.first, toMoveMouseTo.second);
    setMaximumProperty(internalFrame, action);
  }

  @RunsInEDT
  private void setMaximumProperty(@NotNull JInternalFrame internalFrame, @NotNull JInternalFrameAction action) {
    try {
      setMaximum(internalFrame, action);
      robot.waitForIdle();
    } catch (UnexpectedException unexpected) {
      failIfVetoed(internalFrame, action, unexpected);
    }
  }

  /**
   * Iconifies the given {@code JInternalFrame}.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws IllegalStateException if the {@code JInternalFrame} is not iconifiable.
   * @throws org.assertj.swing.exception.ActionFailedException if the {@code JInternalFrame} vetoes the action.
   */
  @RunsInEDT
  public void iconify(@NotNull JInternalFrame internalFrame) {
    Pair<Boolean, Point> iconifyInfo = findIconifyInfo(internalFrame);
    if (iconifyInfo.first) {
      return; // internal frame is already iconified
    }
    moveMouseIgnoringAnyError(internalFrame, iconifyInfo.second);
    setIconProperty(internalFrame, ICONIFY);
  }

  @RunsInEDT
  @NotNull private static Pair<Boolean, Point> findIconifyInfo(final @NotNull JInternalFrame internalFrame) {
    Pair<Boolean, Point> result = execute(new GuiQuery<Pair<Boolean, Point>>() {
      @Override
      @Nullable protected Pair<Boolean, Point> executeInEDT() throws Throwable {
        checkShowingOrIconified(internalFrame);
        if (!internalFrame.isIconifiable()) {
          String msg = String.format("The JInternalFrame <%s> is not iconifiable.", format(internalFrame));
          throw new IllegalStateException(msg);
        }
        return iconifyInfo(internalFrame);
      }
    });
    return checkNotNull(result);
  }

  @RunsInCurrentThread
  @NotNull private static Pair<Boolean, Point> iconifyInfo(@NotNull JInternalFrame internalFrame) {
    boolean iconified = isIconified(internalFrame);
    if (iconified) {
      return Pair.of(true, null);
    }
    return Pair.of(iconified, findIconifyLocation(internalFrame));
  }

  /**
   * De-iconifies the given {@code JInternalFrame}.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws org.assertj.swing.exception.ActionFailedException if the {@code JInternalFrame} vetoes the action.
   */
  @RunsInEDT
  public void deiconify(@NotNull JInternalFrame internalFrame) {
    Triple<Boolean, Container, Point> deiconifyInfo = validateAndfindDeiconifyInfo(internalFrame);
    if (deiconifyInfo.first) {
      return; // internal frame is already de-iconified
    }
    moveMouseIgnoringAnyError(deiconifyInfo.second, deiconifyInfo.third);
    setIconProperty(internalFrame, DEICONIFY);
  }

  @RunsInEDT
  @NotNull private static Triple<Boolean, Container, Point> validateAndfindDeiconifyInfo(
                                                                                         final @NotNull JInternalFrame internalFrame) {
    Triple<Boolean, Container, Point> result = execute(new GuiQuery<Triple<Boolean, Container, Point>>() {
      @Override
      @Nullable protected Triple<Boolean, Container, Point> executeInEDT() throws Throwable {
        checkShowingOrIconified(internalFrame);
        return deiconifyInfo(internalFrame);
      }
    });
    return checkNotNull(result);
  }

  @RunsInCurrentThread
  @NotNull private static Triple<Boolean, Container, Point> deiconifyInfo(@NotNull JInternalFrame internalFrame) {
    boolean deiconified = !isIconified(internalFrame);
    if (deiconified) {
      return Triple.of(true, null, null);
    }
    Container desktopIcon = checkNotNull(internalFrame.getDesktopIcon());
    return Triple.of(deiconified, desktopIcon, iconifyButtonLocation(desktopIcon));
  }

  @RunsInCurrentThread
  @NotNull private static Point findIconifyLocation(JInternalFrame internalFrame) {
    JDesktopIcon desktopIcon = checkNotNull(internalFrame.getDesktopIcon());
    return iconifyButtonLocation(desktopIcon);
  }

  @RunsInEDT
  private void setIconProperty(@NotNull JInternalFrame internalFrame, @NotNull JInternalFrameAction action) {
    try {
      setIcon(internalFrame, action);
      robot.waitForIdle();
    } catch (UnexpectedException unexpected) {
      failIfVetoed(internalFrame, action, unexpected);
    }
  }

  @VisibleForTesting
  void failIfVetoed(@NotNull JInternalFrame internalFrame, @NotNull JInternalFrameAction action,
                    @NotNull UnexpectedException unexpected) {
    PropertyVetoException vetoError = vetoFrom(unexpected);
    if (vetoError == null) {
      return;
    }
    String msg = String.format("%s of %s was vetoed: <%s>", action.name, format(internalFrame), vetoError.getMessage());
    throw actionFailure(msg);
  }

  @Nullable private PropertyVetoException vetoFrom(@NotNull UnexpectedException unexpected) {
    Throwable cause = unexpected.getCause();
    if (!(cause instanceof PropertyVetoException)) {
      return null;
    }
    return (PropertyVetoException) cause;
  }

  /**
   * Resizes the {@code JInternalFrame} horizontally.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @param width the width that the {@code JInternalFrame} should have after being resized.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws IllegalStateException if the {@code JInternalFrame} is not resizable by the user.
   */
  @RunsInEDT
  public void resizeWidth(@NotNull JInternalFrame internalFrame, int width) {
    doResizeWidth(internalFrame, width);
  }

  /**
   * Resizes the {@code JInternalFrame} vertically.
   *
   * @param w the target {@code JInternalFrame}.
   * @param height the height that the {@code JInternalFrame} should have after being resized.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws IllegalStateException if the {@code JInternalFrame} is not resizable by the user.
   */
  @RunsInEDT
  public void resizeHeight(@NotNull JInternalFrame w, int height) {
    doResizeHeight(w, height);
  }

  /**
   * Resizes the {@code JInternalFrame} to the given size.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @param size the size to resize the {@code JInternalFrame} to.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws IllegalStateException if the {@code JInternalFrame} is not resizable by the user.
   */
  @RunsInEDT
  public void resizeTo(@NotNull JInternalFrame internalFrame, @NotNull Dimension size) {
    resize(internalFrame, size.width, size.height);
  }

  /**
   * Moves the {@code JInternalFrame} to the given location.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @param where the location to move the {@code JInternalFrame} to.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   */
  @RunsInEDT
  public void move(@NotNull JInternalFrame internalFrame, @NotNull Point where) {
    move(internalFrame, where.x, where.y);
  }

  /**
   * Closes the given {@code JInternalFrame}.
   *
   * @param internalFrame the target {@code JInternalFrame}.
   * @throws IllegalStateException if the {@code JInternalFrame} is not showing on the screen.
   * @throws IllegalStateException if the {@code JInternalFrame} is not closable.
   */
  @RunsInEDT
  public void close(@NotNull JInternalFrame internalFrame) {
    Point closeButtonLocation = findCloseButtonLocation(internalFrame);
    if (closeButtonLocation == null) {
      return; // internal frame is already closed
    }
    moveMouseIgnoringAnyError(internalFrame, closeButtonLocation);
    JInternalFrameCloseTask.close(internalFrame);
    robot.waitForIdle();
  }

  @RunsInEDT
  @Nullable private static Point findCloseButtonLocation(final @NotNull JInternalFrame internalFrame) {
    return execute(() -> {
      checkShowing(internalFrame);
      if (!internalFrame.isClosable()) {
        String msg = String.format("The JInternalFrame <%s> is not closable", format(internalFrame));
        throw new IllegalStateException(msg);
      }
      if (internalFrame.isClosed()) {
        return null;
      }
      return closeButtonLocation(internalFrame);
    });
  }

  /**
   * Verifies that the title of the given {@code JInternalFrame} is equal to the expected one.
   *
   * @param frame the target {@code JInternalFrame}.
   * @param expected the expected title.
   * @throws AssertionError if the title of the given {@code JInternalFrame} is not equal to the expected one.
   */
  @RunsInEDT
  public void requireTitle(@NotNull JInternalFrame frame, String expected) {
    String actual = titleOf(frame);
    assertThat(actual).as(propertyName(frame, "title")).isEqualTo(expected);
  }
}

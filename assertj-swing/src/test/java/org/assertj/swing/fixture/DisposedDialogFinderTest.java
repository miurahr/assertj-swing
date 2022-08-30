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

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.test.core.EDTSafeTestCase;
import org.assertj.swing.test.swing.TestDialog;
import org.assertj.swing.test.swing.TestWindow;
import org.assertj.swing.timing.Pause;
import org.junit.Test;

public class DisposedDialogFinderTest extends EDTSafeTestCase {

  @Test
  public void test() {
    Robot robot = BasicRobot.robotWithNewAwtHierarchy();
    TestWindow testWindow = TestWindow.createNewWindow(DisposedDialogFinderTest.class);
    TestDialog testDialog = TestDialog.createNewDialog(testWindow);
    robot.showWindow(testDialog);
    GuiActionRunner.execute(() -> {
      testDialog.setVisible(false);
      testDialog.dispose();
    });
    Pause.pause(1000);
    robot.showWindow(testDialog);
    WindowFinder.findDialog(TestDialog.class).using(robot);
    GuiActionRunner.execute(() -> testDialog.setVisible(false));
  }

}

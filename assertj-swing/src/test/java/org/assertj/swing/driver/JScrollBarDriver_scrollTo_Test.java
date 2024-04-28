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

import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.test.ExpectedException.assertThatIllegalStateExceptionCauseIsDisabledComponent;
import static org.assertj.swing.test.ExpectedException.assertThatIllegalStateExceptionCauseIsNotShowingComponent;

/**
 * Tests for {@link JScrollBarDriver#scrollTo(javax.swing.JScrollBar, int)}.
 * 
 * @author Alex Ruiz
 */
public class JScrollBarDriver_scrollTo_Test extends JScrollBarDriver_TestCase {
  @Test
  public void should_Scroll_To_Given_Position() {
    showWindow();
    driver.scrollTo(scrollBar, 68);
    assertThatScrollBarValueIs(68);
  }

  @Test
  public void should_Throw_Error_If_JScrollBar_Is_Disabled() {
    disableScrollBar();
    assertThatIllegalStateExceptionCauseIsDisabledComponent(() -> driver.scrollTo(scrollBar, 68));
  }

  @Test
  public void should_Throw_Error_If_JScrollBar_Is_Not_Showing_On_The_Screen() {
    assertThatIllegalStateExceptionCauseIsNotShowingComponent(() -> driver.scrollTo(scrollBar, 68));
  }

  @Test
  public void should_Throw_Error_If_Position_Is_Less_Than_Minimum() {
    thrown.expectIllegalArgumentException("Position <0> is not within the JScrollBar bounds of <10> and <80>");
    driver.scrollTo(scrollBar, 0);
  }

  @Test
  public void should_Throw_Error_If_Position_Is_Greater_Than_Maximum() {
    Throwable t = Assert.assertThrows(IllegalArgumentException.class, () -> driver.scrollTo(scrollBar, 90));
    assertThat(t.getMessage()).contains("Position <90> is not within the JScrollBar bounds of <10> and <80>");
  }
}

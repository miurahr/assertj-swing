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

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Tests for {@link JComboBoxDriver#enterText(javax.swing.JComboBox, String)}.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JComboBoxDriver_enterText_Test extends JComboBoxDriver_TestCase {
  @Test
  public void should_Throw_Error_If_JComboBox_Is_Disabled() {
    disableComboBox();
    Assert.assertThrows(IllegalStateException.class, () -> driver.enterText(comboBox, "Hello"));
  }

  @Test
  public void should_Throw_Error_If_JComboBox_Is_Not_Showing_On_The_Screen() {
    Assert.assertThrows(IllegalStateException.class, () -> driver.enterText(comboBox, "Hello"));
  }

  @Test
  public void should_Throw_Error_If_JComboBox_Is_Not_Z_Editable() {
    showWindow();
    assertThatIllegalStateExceptionCauseIsNotEditableComboBox(() -> driver.enterText(comboBox, "Hello"));
  }

  @Test
  public void should_Z_Enter_Text() {
    showWindow();
    makeEditable();
    driver.enterText(comboBox, "Hello");
    assertThat(textIn(comboBox)).contains("Hello");
  }
}

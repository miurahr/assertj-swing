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

/**
 * Tests for {@link ComponentDriver#requireEnabled(java.awt.Component)}.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ComponentDriver_requireEnabled_Test extends ComponentDriver_TestCase {
  @Test
  public void should_Pass_If_Component_Is_Enabled() {
    driver.requireEnabled(window.button);
  }

  @Test
  public void should_Fail_If_Component_Is_Not_Enabled() {
    disableButton();
    Throwable t = Assert.assertThrows(AssertionError.class, () -> driver.requireEnabled(window.button));
    assertThat(t.getMessage()).contains("property:'enabled'").contains("expected:<[tru]e> but was:<[fals]e>");
  }
}

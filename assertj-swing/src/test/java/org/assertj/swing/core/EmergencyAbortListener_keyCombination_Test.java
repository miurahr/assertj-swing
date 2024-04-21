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
package org.assertj.swing.core;

import static java.awt.event.InputEvent.*;
import static java.awt.event.KeyEvent.VK_C;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.core.InputModifiers.*;
import static org.assertj.swing.core.KeyPressInfo.keyCode;
import static org.assertj.swing.test.awt.Toolkits.singletonToolkitMock;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Tests for {@link EmergencyAbortListener#keyCombination(KeyPressInfo)}.
 * 
 * @author Alex Ruiz
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmergencyAbortListener_keyCombination_Test {
  private EmergencyAbortListener listener;

  @Before
  public void setUp() {
    listener = new EmergencyAbortListener(singletonToolkitMock());
  }

  @Test(expected = NullPointerException.class)
  public void should_Throw_Error_If_KeyPressInfo_Is_Null() {
    listener.keyCombination(null);
  }

  @Test
  public void should_Update_Key_Combination() {
    listener.keyCombination(keyCode(VK_C).modifiers(ALT_DOWN_MASK, META_DOWN_MASK));
    assertThat(listener.keyCode()).isEqualTo(VK_C);
    assertThatModifiersAreAltAndMeta(listener.modifiers());
  }

  private void assertThatModifiersAreAltAndMeta(int modifiers) {
    assertThat(isAltDownEx(modifiers)).isTrue();
    assertThat(isMetaDownEx(modifiers)).isTrue();
    assertThat(isAltGraphDownEx(modifiers)).isFalse();
    assertThat(isControlDownEx(modifiers)).isFalse();
    assertThat(isShiftDownEx(modifiers)).isFalse();
  }
}

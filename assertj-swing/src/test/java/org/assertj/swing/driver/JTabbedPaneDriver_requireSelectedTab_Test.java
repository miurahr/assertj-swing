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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.swing.data.Index.atIndex;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link JTabbedPaneDriver#requireSelectedTab(javax.swing.JTabbedPane, org.assertj.swing.data.Index)}.
 *
 * @author Christian Rösch
 */
public class JTabbedPaneDriver_requireSelectedTab_Test extends JTabbedPaneDriver_TestCase {
  @Test
  public void should_Fail_If_Index_Is_Not_Equal_To_Expected() {
    Throwable t = Assert.assertThrows(AssertionError.class, () -> driver.requireSelectedTab(tabbedPane, atIndex(12)));
    assertThat(t.getMessage()).contains("selectedIndex").contains("12").contains("0");
  }

  @Test
  public void should_Pass_If_Index_Is_Equal_To_Expected() {
    driver.requireSelectedTab(tabbedPane, atIndex(0));
  }
}

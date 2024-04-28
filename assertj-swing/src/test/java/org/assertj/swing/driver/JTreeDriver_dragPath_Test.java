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

import org.assertj.swing.exception.LocationUnavailableException;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.swing.test.ExpectedException.assertThatIllegalStateExceptionCauseIsNotShowingComponent;

/**
 * Tests for <code>{@link JTreeDriver#drag(javax.swing.JTree, String)}.
 * 
 * @author Alex Ruiz
 */
public class JTreeDriver_dragPath_Test extends JTreeDriver_dragAndDrop_TestCase {
  @Test
  public void should_Throw_Error_If_Path_Does_Not_Exist() {
    showWindow();
    Throwable t = Assert.assertThrows(LocationUnavailableException.class, () -> driver.drag(tree, "somePath"));
    Assert.assertTrue(t.getMessage().contains("Unable to find path 'somePath'"));
  }

  @Test
  public void should_Throw_Error_If_JTree_Is_Disabled() {
    disableTree();
    Assert.assertThrows(IllegalStateException.class, () -> driver.drag(tree, "root"));
  }

  @Test
  public void should_Throw_Error_If_JTree_Is_Not_Showing_On_The_Screen() {
    assertThatIllegalStateExceptionCauseIsNotShowingComponent(() -> driver.drag(tree, "root"));
  }
}

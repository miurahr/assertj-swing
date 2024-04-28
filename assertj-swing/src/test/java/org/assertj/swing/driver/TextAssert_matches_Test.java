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

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link TextAssert#matches(java.util.regex.Pattern)}.
 * 
 * @author Alex Ruiz
 */
public class TextAssert_matches_Test {
  @Test
  public void should_Fail_If_Actual_Does_Not_Match_Regex_Pattern() {
    Throwable t = Assert.assertThrows(AssertionError.class, () -> new TextAssert("hello")
                                                                                         .matches(Pattern.compile("bye")));
    Assert.assertTrue(t.getMessage().contains("Expecting actual:\n  \"hello\"\nto match pattern:\n  \"bye\""));
  }

  @Test
  public void should_Fail_Showing_Description_If_Actual_Does_Not_Match_Regex_Pattern() {
    Throwable t = Assert.assertThrows(AssertionError.class, () -> new TextAssert("hello").as("A Test")
                                                                                         .matches(Pattern.compile("bye")));
    Assert.assertTrue(t.getMessage().contains("[A Test] \nExpecting actual:\n  \"hello\"\nto match pattern:\n  \"bye\""));
  }

  @Test
  public void should_Pass_If_Actual_Matches_Regex_Pattern() {
    new TextAssert("Hello").matches(Pattern.compile("Hel.*"));
  }
}

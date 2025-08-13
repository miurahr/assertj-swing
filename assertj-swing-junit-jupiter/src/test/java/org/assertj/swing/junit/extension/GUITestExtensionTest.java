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
package org.assertj.swing.junit.extension;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.junit.runner.FailureScreenshotTaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class GUITestExtensionTest {
  private FailureScreenshotTaker failureScreenshotTaker;
  private GUITestExtension guiTestExtension;

  @BeforeEach
  void setUp() {
    failureScreenshotTaker = mock(FailureScreenshotTaker.class);
    guiTestExtension = new GUITestExtension(failureScreenshotTaker);
  }

  @Test
  @SuppressWarnings("unchecked")
  void should_Not_Take_Screenshot_If_No_Throwable() throws Throwable {
    InvocationInterceptor.Invocation<Void> invocation = (InvocationInterceptor.Invocation<Void>) mock(InvocationInterceptor.Invocation.class);
    ReflectiveInvocationContext<Method> reflectiveInvocationContext = (ReflectiveInvocationContext<Method>) mock(ReflectiveInvocationContext.class);
    ExtensionContext extensionContext = mock(ExtensionContext.class);

    guiTestExtension.interceptTestMethod(invocation, reflectiveInvocationContext, extensionContext);

    verify(invocation).proceed();
    verifyNoMoreInteractions(failureScreenshotTaker);
    verifyNoMoreInteractions(reflectiveInvocationContext);
  }

  @Test
  @SuppressWarnings("unchecked")
  void should_Not_Take_Screenshot_On_Throwable_In_Non_Gui_Test_Method() throws Throwable {
    RuntimeException exception = new RuntimeException("TestExpectedException");
    InvocationInterceptor.Invocation<Void> invocation = (InvocationInterceptor.Invocation<Void>) mock(InvocationInterceptor.Invocation.class);
    doThrow(exception).when(invocation).proceed();
    ReflectiveInvocationContext<Method> reflectiveInvocationContext = (ReflectiveInvocationContext<Method>) mock(ReflectiveInvocationContext.class);
    when(reflectiveInvocationContext.getExecutable()).thenReturn(TestGuiTest.class.getMethod("nonGuiTestMethod"));

    assertThatThrownBy(() -> guiTestExtension.interceptTestMethod(
                                                                  invocation,
                                                                  reflectiveInvocationContext,
                                                                  mock(ExtensionContext.class))).isEqualTo(exception);

    verify(invocation).proceed();
    verifyNoMoreInteractions(failureScreenshotTaker);
  }

  @Test
  @SuppressWarnings("unchecked")
  void should_Take_Screenshot_On_Throwable_In_Gui_Test_Method() throws Throwable {
    RuntimeException exception = new RuntimeException("TestExpectedException");
    InvocationInterceptor.Invocation<Void> invocation = (InvocationInterceptor.Invocation<Void>) mock(InvocationInterceptor.Invocation.class);
    doThrow(exception).when(invocation).proceed();
    ReflectiveInvocationContext<Method> reflectiveInvocationContext = (ReflectiveInvocationContext<Method>) mock(ReflectiveInvocationContext.class);
    when(reflectiveInvocationContext.getExecutable()).thenReturn(TestGuiTest.class.getMethod("guiTestMethod"));

    assertThatThrownBy(() -> guiTestExtension.interceptTestMethod(
                                                                  invocation,
                                                                  reflectiveInvocationContext,
                                                                  mock(ExtensionContext.class))).isEqualTo(exception);

    verify(invocation).proceed();
    verify(failureScreenshotTaker).saveScreenshot(
                                                  "org.assertj.swing.junit.extension.GUITestExtensionTest$TestGuiTest.guiTestMethod");
  }

  private static class TestGuiTest {
    @GUITest
    public void guiTestMethod() {
      // empty
    }

    public void nonGuiTestMethod() {
      // empty
    }
  }
}

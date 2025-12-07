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

import java.awt.event.InputEvent;

import org.jspecify.annotations.Nullable;

/**
 * Utility methods related to input modifiers.
 * 
 * @author Alex Ruiz
 */
final class InputModifiers {
  static int unify(@Nullable int... modifiers) {
    if (modifiers == null) {
      return 0;
    }
    int unified = 0;
    if (modifiers.length > 0) {
      unified = modifiers[0];
      for (int i = 1; i < modifiers.length; i++) {
        unified |= modifiers[i];
      }
    }
    return unified;
  }

  @Deprecated
  static boolean isShiftDown(int modifiers) {
    return (modifiers & InputEvent.SHIFT_MASK) != 0;
  }

  @Deprecated
  static boolean isControlDown(int modifiers) {
    return (modifiers & InputEvent.CTRL_MASK) != 0;
  }

  @Deprecated
  static boolean isMetaDown(int modifiers) {
    return (modifiers & InputEvent.META_MASK) != 0;
  }

  @Deprecated
  static boolean isAltDown(int modifiers) {
    return (modifiers & InputEvent.ALT_MASK) != 0;
  }

  @Deprecated
  static boolean isAltGraphDown(int modifiers) {
    return (modifiers & InputEvent.ALT_GRAPH_MASK) != 0;
  }

  static boolean isShiftDownEx(int modifiers) {
    return (modifiers & InputEvent.SHIFT_DOWN_MASK) != 0;
  }

  static boolean isControlDownEx(int modifiers) {
    return (modifiers & InputEvent.CTRL_DOWN_MASK) != 0;
  }

  static boolean isMetaDownEx(int modifiers) {
    return (modifiers & InputEvent.META_DOWN_MASK) != 0;
  }

  static boolean isAltDownEx(int modifiers) {
    return (modifiers & InputEvent.ALT_DOWN_MASK) != 0;
  }

  static boolean isAltGraphDownEx(int modifiers) {
    return (modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0;
  }

  static boolean modifiersMatch(InputEvent e, int modifiers) {
    if (e.isAltDown() != isAltDownEx(modifiers)) {
      return false;
    }
    if (e.isAltGraphDown() != isAltGraphDownEx(modifiers)) {
      return false;
    }
    if (e.isControlDown() != isControlDownEx(modifiers)) {
      return false;
    }
    if (e.isMetaDown() != isMetaDownEx(modifiers)) {
      return false;
    }
    return e.isShiftDown() == isShiftDownEx(modifiers);
  }

  private InputModifiers() {}
}

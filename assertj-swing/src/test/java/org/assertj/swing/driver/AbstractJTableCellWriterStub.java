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

import org.jetbrains.annotations.NotNull;
import javax.swing.JTable;

import org.assertj.swing.core.Robot;

class AbstractJTableCellWriterStub extends AbstractJTableCellWriter {
  AbstractJTableCellWriterStub(Robot robot) {
    super(robot);
  }

  @Override
  public void enterValue(@NotNull JTable table, int row, int column, @NotNull String value) {
  }

  @Override
  public void startCellEditing(@NotNull JTable table, int row, int column) {
  }

  @Override
  public void stopCellEditing(@NotNull JTable table, int row, int column) {
  }

  @Override
  public void cancelCellEditing(@NotNull JTable table, int row, int column) {
  }
}
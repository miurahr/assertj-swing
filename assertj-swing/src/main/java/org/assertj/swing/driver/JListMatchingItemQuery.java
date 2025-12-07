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

import static java.util.Collections.sort;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.assertj.swing.driver.JListCellBoundsQuery.cellBounds;
import static org.assertj.swing.driver.JListCellCenterQuery.cellCenter;
import static org.assertj.swing.edt.GuiActionRunner.execute;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import javax.swing.JList;

import org.assertj.swing.annotation.RunsInCurrentThread;
import org.assertj.swing.annotation.RunsInEDT;
import org.assertj.swing.cell.JListCellReader;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.util.StringTextMatcher;
import org.assertj.swing.util.TextMatcher;

/**
 * Looks up the first item in a {@code JList} whose value matches a given one. This query is executed in the event
 * dispatch thread (EDT).
 *
 * @author Alex Ruiz
 */
final class JListMatchingItemQuery {
  @RunsInEDT
  static Point centerOfMatchingItemCell(final JList<?> list, final @Nullable String value,
                                        final JListCellReader cellReader) {
    Point result = execute(() -> {
      int itemIndex = matchingItemIndex(list, new StringTextMatcher(value), cellReader);
      Rectangle cellBounds = Objects.requireNonNull(cellBounds(list, itemIndex));
      return cellCenter(list, cellBounds);
    });
    return Objects.requireNonNull(result);
  }

  @RunsInCurrentThread
  static int matchingItemIndex(JList<?> list, TextMatcher matcher, JListCellReader cellReader) {
    int size = list.getModel().getSize();
    for (int i = 0; i < size; i++) {
      if (matcher.isMatching(cellReader.valueAt(list, i))) {
        return i;
      }
    }
    return -1;
  }

  @RunsInEDT
  static List<Integer> matchingItemIndices(final JList<?> list, final TextMatcher matcher,
                                           final JListCellReader cellReader) {
    List<Integer> result = execute(new GuiQuery<List<Integer>>() {
      @Override
      protected List<Integer> executeInEDT() {
        Set<Integer> indices = newHashSet();
        int size = list.getModel().getSize();
        for (int i = 0; i < size; i++) {
          if (matcher.isMatching(cellReader.valueAt(list, i))) {
            indices.add(i);
          }
        }
        List<Integer> indexList = newArrayList(indices);
        sort(indexList);
        return indexList;
      }
    });
    return Objects.requireNonNull(result);
  }

  @RunsInEDT
  static List<String> matchingItemValues(final JList<?> list, final TextMatcher matcher,
                                         final JListCellReader cellReader) {
    List<String> result = execute(new GuiQuery<List<String>>() {
      @Override
      protected List<String> executeInEDT() {
        List<String> values = newArrayList();
        int size = list.getModel().getSize();
        for (int i = 0; i < size; i++) {
          String value = cellReader.valueAt(list, i);
          if (matcher.isMatching(value)) {
            values.add(value);
          }
        }
        return values;
      }
    });
    return Objects.requireNonNull(result);
  }

  private JListMatchingItemQuery() {}
}

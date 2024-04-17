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

import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Strings.quote;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.assertj.swing.annotation.RunsInCurrentThread;
import org.assertj.swing.cell.JTreeCellReader;
import org.assertj.swing.exception.LocationUnavailableException;

/**
 * Lookup of {@code TreePath}s which text matches the given one.
 * 
 * @author Alex Ruiz
 */
class JTreePathFinder {
  // TODO TEST
  private static final String SEPARATOR = "/";

  private JTreeCellReader cellReader;
  private String separator;

  JTreePathFinder() {
    replaceCellReader(new BasicJTreeCellReader());
    replaceSeparator(SEPARATOR);
  }

  @RunsInCurrentThread
  @NotNull
  TreePath findMatchingPath(@NotNull JTree tree, @NotNull String path) {
    String[] pathStrings = splitPath(path);
    TreeModel model = tree.getModel();
    List<Object> newPathValues = newArrayList();
    Object node = model.getRoot();
    int pathElementCount = pathStrings.length;
    for (int stringIndex = 0; stringIndex < pathElementCount; stringIndex++) {
      String pathString = pathStrings[stringIndex];
      Object match = null;
      if (stringIndex == 0 && tree.isRootVisible()) {
        if (!pathString.equals(value(tree, node))) {
          throw pathNotFound(path);
        }
        newPathValues.add(node);
        continue;
      }
      int childCount = model.getChildCount(node);
      for (int childIndex = 0; childIndex < childCount; childIndex++) {
        Object child = model.getChild(node, childIndex);
        if (pathString.equals(value(tree, child))) {
          if (match != null) {
            throw multipleMatchingNodes(pathString, value(tree, node));
          }
          match = child;
        }
      }
      if (match == null) {
        throw pathNotFound(path);
      }
      newPathValues.add(match);
      node = match;
    }
    return new TreePath(newPathValues.toArray());
  }

  @NotNull private LocationUnavailableException pathNotFound(@NotNull String path) {
    throw new LocationUnavailableException(String.format("Unable to find path %s", quote(path)));
  }

  @NotNull private String[] splitPath(@NotNull String path) {
    List<String> result = newArrayList();
    int separatorSize = separator.length();
    int index = 0;
    int pathSize = path.length();
    while (index < pathSize) {
      int separatorPosition = path.indexOf(separator, index);
      if (separatorPosition == -1) {
        separatorPosition = pathSize;
      }
      result.add(path.substring(index, separatorPosition));
      index = separatorPosition + separatorSize;
    }
    return result.toArray(new String[result.size()]);
  }

  @NotNull private LocationUnavailableException multipleMatchingNodes(@NotNull String matchingText,
      @Nullable Object parentText) {
    String msg = String.format("There is more than one node with value '%s' under", matchingText, quote(parentText));
    throw new LocationUnavailableException(msg);
  }

  @Nullable private String value(@NotNull JTree tree, @Nullable Object modelValue) {
    return cellReader.valueAt(tree, modelValue);
  }

  @NotNull
  String separator() {
    return separator;
  }

  void replaceSeparator(@NotNull String newSeparator) {
    separator = newSeparator;
  }

  void replaceCellReader(@NotNull JTreeCellReader newCellReader) {
    cellReader = newCellReader;
  }

  @NotNull
  JTreeCellReader cellReader() {
    return cellReader;
  }
}

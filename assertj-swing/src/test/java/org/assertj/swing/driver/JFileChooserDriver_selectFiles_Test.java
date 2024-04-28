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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Files.newTemporaryFile;
import static org.assertj.core.util.Files.newTemporaryFolder;
import static org.assertj.swing.edt.GuiActionRunner.execute;

import java.io.File;

import javax.swing.JFileChooser;

import org.assertj.swing.annotation.RunsInEDT;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link JFileChooserDriver#selectFiles(javax.swing.JFileChooser, java.io.File[])}.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class JFileChooserDriver_selectFiles_Test extends JFileChooserDriver_TestCase {
  @Test
  public void should_Throw_Error_If_JFileChooser_Is_Disabled() {
    Assert.assertThrows(IllegalStateException.class, () -> driver.selectFiles(fileChooser, array(fakeFile())));
  }

  @Test
  public void should_Throw_Error_When_Selecting_Files_And_JFileChooser_Cannot_Handle_Multiple_Selection() {
    disableMultipleSelection();
    showWindow();
    Throwable t = Assert.assertThrows(IllegalStateException.class,
                                      () -> driver.selectFiles(fileChooser, array(new File("Fake1"), new File("Fake2"))));
    assertThat(t.getMessage()).contains("Expecting file chooser");
  }

  @RunsInEDT
  private void disableMultipleSelection() {
    setMultipleSelectionEnabled(fileChooser, false);
    robot.waitForIdle();
  }

  @Test
  public void should_Throw_Error_When_Selecing_Folders_While_JFileChooser_Can_Only_Select_Files() {
    enableMultipleSelection();
    TemporaryFolderAndFile folderAndFile = new TemporaryFolderAndFile();
    makeFileChooserSelectDirectoriesOnly();
    showWindow();
    Throwable t = Assert.assertThrows(IllegalStateException.class, () -> {
      try {
        driver.selectFiles(fileChooser, folderAndFile.contents());
      } finally {
        folderAndFile.delete();
      }
    });
    assertThat(t.getMessage()).contains("the file chooser can only open directories");
  }

  @Test
  public void should_Throw_Error_When_Selecting_Files_While_JFileChooser_Can_Only_Select_Folders() {
    enableMultipleSelection();
    TemporaryFolderAndFile folderAndFile = new TemporaryFolderAndFile();
    makeFileChooserSelectFilesOnly();
    showWindow();
    Throwable t = Assert.assertThrows(IllegalArgumentException.class, () -> {
      try {
        driver.selectFiles(fileChooser, folderAndFile.contents());
      } finally {
        folderAndFile.delete();
      }
    });
    assertThat(t.getMessage()).contains("the file chooser can only open files");
  }

  @Test
  public void should_Select_Single_Given_File_When_JFileChooser_Cannot_Handle_Multiple_Selection() {
    disableMultipleSelection();
    File temporaryFile = newTemporaryFile();
    showWindow();
    try {
      driver.selectFiles(fileChooser, array(temporaryFile));
      File[] selectedFiles = selectedFilesIn(fileChooser);
      assertThat(selectedFiles).containsOnly(temporaryFile);
    } finally {
      temporaryFile.delete();
    }
  }

  @Test
  public void should_Select_Files() {
    // TODO(Alex): Test in Windows
    enableMultipleSelection();
    TemporaryFolderAndFile folderAndFile = new TemporaryFolderAndFile();
    makeFileChooserSelectFilesAndDirectories();
    showWindow();
    try {
      driver.selectFiles(fileChooser, folderAndFile.contents());
      File[] selectedFiles = selectedFilesIn(fileChooser);
      assertThat(selectedFiles).containsOnly(folderAndFile.folder, folderAndFile.file);
    } finally {
      folderAndFile.delete();
    }
  }

  @RunsInEDT
  private void enableMultipleSelection() {
    setMultipleSelectionEnabled(fileChooser, true);
    robot.waitForIdle();
  }

  @RunsInEDT
  private static void setMultipleSelectionEnabled(final JFileChooser fileChooser, final boolean b) {
    execute(() -> fileChooser.setMultiSelectionEnabled(b));
  }

  @RunsInEDT
  private static File[] selectedFilesIn(final JFileChooser fileChooser) {
    return execute(() -> fileChooser.getSelectedFiles());
  }

  private static class TemporaryFolderAndFile {
    final File folder;
    final File file;

    TemporaryFolderAndFile() {
      folder = newTemporaryFolder();
      file = newTemporaryFile();
    }

    File[] contents() {
      return array(folder, file);
    }

    void delete() {
      folder.delete();
      file.delete();
    }
  }
}

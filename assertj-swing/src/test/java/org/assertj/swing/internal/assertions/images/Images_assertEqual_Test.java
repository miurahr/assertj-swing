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
package org.assertj.swing.internal.assertions.images;

import static java.awt.Color.BLUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.assertions.data.Point.atPoint;
import static org.assertj.swing.assertions.error.ShouldBeEqualColors.shouldBeEqualColors;
import static org.assertj.swing.assertions.error.ShouldBeEqualImages.shouldBeEqualImages;
import static org.assertj.swing.assertions.error.ShouldHaveDimension.shouldHaveDimension;
import static org.assertj.swing.test.awt.AwtTestData.*;

import java.awt.image.BufferedImage;

import org.assertj.core.api.AssertionInfo;
import org.assertj.swing.internal.assertions.Images;
import org.assertj.swing.internal.assertions.ImagesBaseTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for <code>{@link Images#assertEqual(AssertionInfo, BufferedImage, BufferedImage)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Images_assertEqual_Test extends ImagesBaseTest {

  @Test
  public void should_Pass_If_Images_Are_Equal() {
    images.assertEqual(someInfo(), actual, newImage(5, 5, BLUE));
  }

  @Test
  public void should_Pass_If_Images_Are_Same() {
    images.assertEqual(someInfo(), actual, actual);
  }

  @Test
  public void should_Pass_If_Both_Images_Are_Null() {
    images.assertEqual(someInfo(), null, null);
  }

  @Test
  public void should_Fail_If_Actual_Is_Null_And_Expected_Is_Not() {
    AssertionInfo info = someInfo();
    Throwable t = Assert.assertThrows(AssertionError.class, () -> images.assertEqual(info, null, fivePixelBlueImage()));
    assertThat(t.getMessage()).isEqualTo(shouldBeEqualImages(offset).create(info.description(), info.representation()));
  }

  @Test
  public void should_Fail_If_Expected_Is_Null_And_Actual_Is_Not() {
    AssertionInfo info = someInfo();
    Throwable t = Assert.assertThrows(AssertionError.class, () -> images.assertEqual(info, actual, null));
    assertThat(t.getMessage()).isEqualTo(shouldBeEqualImages(offset).create(info.description(), info.representation()));
  }

  @Test
  public void should_Fail_If_Images_Have_Different_Size() {
    AssertionInfo info = someInfo();
    BufferedImage expected = newImage(6, 6, BLUE);
    Throwable t = Assert.assertThrows(AssertionError.class, () -> images.assertEqual(info, actual, expected));
    assertThat(t.getMessage()).isEqualTo(shouldHaveDimension(actual, sizeOf(actual),
                                                             sizeOf(expected)).create(info.description(), info.representation()));
  }

  @Test
  public void should_Fail_If_Images_Have_Same_Size_But_Different_Color() {
    AssertionInfo info = someInfo();
    BufferedImage expected = fivePixelYellowImage();
    Throwable t = Assert.assertThrows(AssertionError.class, () -> images.assertEqual(info, actual, expected));
    assertThat(t.getMessage()).isEqualTo(shouldBeEqualColors(yellow(), blue(), atPoint(0, 0),
                                                             offset).create(info.description(), info.representation()));
  }
}

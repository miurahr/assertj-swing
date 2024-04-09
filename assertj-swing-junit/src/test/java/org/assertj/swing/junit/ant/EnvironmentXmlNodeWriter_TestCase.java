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
package org.assertj.swing.junit.ant;

import org.assertj.swing.junit.xml.XmlNode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

/**
 * Base test case for <code>{@link EnvironmentXmlNodeWriter}</code>.
 * 
 * @author Alex Ruiz
 */
public abstract class EnvironmentXmlNodeWriter_TestCase {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  XmlNode targetNode;
  TimestampFormatter timeStampFormatter;
  HostNameReader hostNameReader;
  EnvironmentXmlNodeWriter writer;

  @Before
  public final void setUp() {
    targetNode = Mockito.mock(XmlNode.class);
    timeStampFormatter = Mockito.mock(TimestampFormatter.class);
    hostNameReader = Mockito.mock(HostNameReader.class);
    writer = new EnvironmentXmlNodeWriter(timeStampFormatter, hostNameReader);
    onSetUp();
  }

  void onSetUp() {
  }
}

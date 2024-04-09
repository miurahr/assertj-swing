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

import static org.apache.tools.ant.taskdefs.optional.junit.XMLConstants.HOSTNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.junit.xml.XmlAttribute.name;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.assertj.swing.junit.xml.XmlNode;
import org.junit.Test;

/**
 * Tests for <code>{@link EnvironmentXmlNodeWriter#writeHostName(XmlNode)}</code>.
 *
 * @author Alex Ruiz
 */
public class EnvironmentXmlNodeWriter_writeHostName_Test extends EnvironmentXmlNodeWriter_TestCase {

  @Test
  public void should_Write_Host_Name_As_Attribute() throws UnknownHostException {
    final String hostName = "myHost";
    when(hostNameReader.localHostName()).thenReturn(hostName);
    targetNode.addAttribute(name(HOSTNAME).value(hostName));
    verify(targetNode).addAttribute(any());
    assertThat(writer.writeHostName(targetNode)).isSameAs(writer);
  }

  @Test
  public void should_Write_Local_Host_As_Attribute_If_Host_Name_Could_Not_Be_Obtained() throws UnknownHostException {
    when(hostNameReader.localHostName()).thenThrow(UnknownHostException.class);
    targetNode.addAttribute(name(HOSTNAME).value("localhost"));
    verify(targetNode).addAttribute(any());
    assertThat(writer.writeHostName(targetNode)).isSameAs(writer);
  }
}

module org.assertj.swing.junit {
  requires java.base;
  requires org.assertj.core;
  requires org.apache.commons.codec;
  requires junit;
  requires org.fest.reflect;

  exports org.assertj.swing.junit.ant;
  exports org.assertj.swing.junit.runner;
  exports org.assertj.swing.junit.testcase;
  exports org.assertj.swing.junit.xml;
}
module org.assertj.swing {
  requires org.assertj.core;
  requires org.fest.reflect;
  requires java.datatransfer;
  requires java.desktop;
  requires java.logging;
  requires org.jetbrains.annotations;

  exports org.assertj.swing.annotation;
  exports org.assertj.swing.applet;
  exports org.assertj.swing.assertions;
  exports org.assertj.swing.awt;
  exports org.assertj.swing.cell;
  exports org.assertj.swing.core;
  exports org.assertj.swing.data;
  exports org.assertj.swing.driver;
  exports org.assertj.swing.edt;
  exports org.assertj.swing.exception;
  exports org.assertj.swing.finder;
  exports org.assertj.swing.fixture;
  exports org.assertj.swing.format;
  exports org.assertj.swing.hierarchy;
  exports org.assertj.swing.image;
  exports org.assertj.swing.input;
  exports org.assertj.swing.keystroke;
  exports org.assertj.swing.launcher;
  exports org.assertj.swing.listener;
  exports org.assertj.swing.lock;
  exports org.assertj.swing.monitor;
  exports org.assertj.swing.query;
  exports org.assertj.swing.security;
  exports org.assertj.swing.testing;
  exports org.assertj.swing.text;
  exports org.assertj.swing.timing;
  exports org.assertj.swing.util;
}
module org.assertj.swing.junit.jupiter {
  requires java.base;
  requires org.assertj.swing;
  requires org.assertj.swing.junit;
  requires org.junit.jupiter;
  requires org.junit.jupiter.engine;

  exports org.assertj.swing.junit.extension;
}
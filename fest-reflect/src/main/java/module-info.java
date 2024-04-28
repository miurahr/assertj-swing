module org.fest.reflect {
    requires java.base;
    requires java.desktop;
    requires org.assertj.core;
    exports org.fest.reflect.beanproperty;
    exports org.fest.reflect.constructor;
    exports org.fest.reflect.core;
    exports org.fest.reflect.exception;
    exports org.fest.reflect.field;
    exports org.fest.reflect.innerclass;
    exports org.fest.reflect.method;
    exports org.fest.reflect.reference;
    exports org.fest.reflect.type;
    exports org.fest.reflect.util;
}
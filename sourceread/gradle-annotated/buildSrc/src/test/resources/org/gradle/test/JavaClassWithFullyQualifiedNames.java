package org.gradle.test;

public class JavaClassWithFullyQualifiedNames extends org.gradle.test.sub.SubGroovyClass implements org.gradle.test.sub.SubJavaInterface, java.lang.Runnable {
    org.gradle.test.sub.SubJavaInterface getProp() {
        return this;
    }

    public void run() {
    }
}

#!/usr/bin/env bash
java -jar lib/soot-*.jar -f J -cp DynamicAnalyzer/target/scala-2.11/classes:DynamicAnalyzer/target/scala-2.11/test-classes:$JAVA_HOME/jre/lib/rt.jar:GradualConstraints/InstrumentationSupport/target/scala-2.11/classes:JGSSupport/target/scala-2.11/classes $*


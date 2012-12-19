#!/bin/bash

MY_DIR=`dirname $0`
java -Djava.security.policy=$MY_DIR/java.policy -Dfile.encoding=UTF-8 -classpath $MY_DIR/../bin node.Node

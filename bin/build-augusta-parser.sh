#!/bin/bash

cd src/edu/vtc/augusta
java -Xmx2048M -cp ../../../../lib/antlr-4.5.1.jar org.antlr.v4.Tool -visitor Ada.g4
cd ../../../..


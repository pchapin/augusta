@echo off
REM
REM This is the testing tool described in "Definitive ANTRL 4."
REM

REM Set up the class path to grab the jar files from the Ivy cache.
set CPATH=%HOME%\.ivy2\cache\org.antlr\antlr4\jars\antlr4-4.13.2.jar
set CPATH=%CPATH%;%HOME%\.ivy2\cache\org.antlr\antlr4-runtime\jars\antlr4-runtime-4.13.2.jar
set CPATH=%CPATH%;%HOME%\.ivy2\cache\org.abego.treelayout\org.abego.treelayout.core\bundles\org.abego.treelayout.core-1.0.3.jar

REM Execute the tool on the Ada grammar. Note that the test data is at ..\..\..\testData
cd target\scala-3.3.4\classes
java -cp .;%CPATH% org.antlr.v4.gui.TestRig org.pchapin.augusta.Ada %*
cd ..\..\..

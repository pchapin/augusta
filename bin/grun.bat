@echo off
REM
REM This is the testing tool described in "Definitive ANTRL 4."
REM Usage: From the root of the repository run bin\grun.bat as follows:
REM
REM bin\grun.bat <start-rule> [-tokens|-tree|-gui] <path-to-input-file>

REM Set up the class path to grab the jar files from the Coursier cache.
set COURSIER=%USERPROFILE%\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2
set CPATH=%COURSIER%\org\antlr\antlr4\4.13.2\antlr4-4.13.2.jar
set CPATH=%CPATH%;%COURSIER%\org\antlr\antlr4-runtime\4.13.2\antlr4-runtime-4.13.2.jar
set CPATH=%CPATH%;%COURSIER%\org\abego\treelayout\org.abego.treelayout.core\1.0.3\org.abego.treelayout.core-1.0.3.jar
set CPATH=%CPATH%;target\scala-3.3.4\classes

REM Execute the tool on the Augusta grammar.
java -cp .;%CPATH% org.antlr.v4.gui.TestRig org.kelseymountain.agc.Augusta %*

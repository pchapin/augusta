@echo off
REM
REM This is the testing tool described in "Definitive ANTRL 4."
REM

cd build\production\Rabbit\lev0
java -cp .;..\..\..\..\..\..\lib\antlr-4.5.1.jar org.antlr.v4.runtime.misc.TestRig edu.vtc.rabbit.Rabbit %1 %2 %3 %4
cd ..\..\..\..

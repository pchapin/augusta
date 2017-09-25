@echo off

cd src\org\pchapin\augusta
java -Xmx2048M -cp ..\..\..\..\lib\antlr4-4.7.jar;..\..\..\..\lib\antlr4-runtime-4.7.jar;..\..\..\..\lib\antlr-runtime-3.5.2.jar;..\..\..\..\lib\ST4-4.0.8.jar  org.antlr.v4.Tool -visitor Ada.g4
cd ..\..\..\..


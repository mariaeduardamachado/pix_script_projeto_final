@echo off
mvn clean test
mvn exec:java -Dexec.args="exemplos/valido.pix"
pause

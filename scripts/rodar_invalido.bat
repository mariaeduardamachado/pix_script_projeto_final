@echo off
mvn clean test
mvn exec:java -Dexec.args="exemplos/invalido.pix"
pause

#!/usr/bin/env bash
mvn clean test
mvn exec:java -Dexec.args="exemplos/valido.pix"

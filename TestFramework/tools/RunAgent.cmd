@echo off
SET JIM=../../RoboCupJim
IF NOT EXIST "%JIM%" (
	SET JIM=../../Jim
)
IF NOT EXIST "%JIM%" (
	SET JIM=../Jim
)
IF NOT EXIST "%JIM%" (
	SET JIM=../RoboCupJim
)
cd %JIM%
java -classpath ../RoboCupLibrary/bin;bin;lib/aspectjrt.jar;lib/bsf.jar;lib/jruby-complete-1.4.0.jar;lib/commons-logging-1.1.jar;lib/commons-net-2.2.jar sk.fiit.jim.init.Main %*
@echo off
IF "%RCSSSERVER3D_DIR"=="" (
	SET RCSERVER=c:/fiit/TP/rcssserver
) ELSE (
	SET RCSERVER="%RCSSSERVER3D_DIR%"
)
IF "%1"=="start" (
	cd %RCSERVER%/bin
	start rcssserver3d.cmd
	ping 128.1.1.1 -n 1 -w 1000 > nul
	start rcssmonitor3d.cmd
) ELSE IF "%1"=="stop" (
	taskkill /F /IM rcssmonitor3d.exe
	taskkill /F /IM rcssserver3d.exe
) ELSE IF "%1"=="restart" (
	taskkill /F /IM rcssmonitor3d.exe
	taskkill /F /IM rcssserver3d.exe
	ping 128.1.1.1 -n 1 -w 1000 > nul
	cd %RCSERVER%/bin
	start rcssserver3d.cmd
	ping 128.1.1.1 -n 1 -w 1000 > nul
	start rcssmonitor3d.cmd
) ELSE (
	echo "Usage: RunServer.cmd [start|stop|restart]"
)

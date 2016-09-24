@echo off
title Deploying EZ.JWAF Message
call msg info "[INFO] Deploying EZ.JWAF Message" & echo.

set BASEDIR=%~sdp0

pushd %BASEDIR%\..\src\message
  call mvn clean deploy -DperformTest=true -DperformSource=true -DperformDeploy=true
popd

call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 

pause
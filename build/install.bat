@echo off
title Installing EZ.JWAF Message
call msg info "[INFO] Installing EZ.JWAF Message" & echo.

set BASEDIR=%~sdp0

pushd %BASEDIR%\..\src
  call mvn clean install -DperformTest=true -DperformSource=true
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

timeout /t 10 >NUL 
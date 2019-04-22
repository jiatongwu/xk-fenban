title jerems

@echo off

setlocal & pushd

set MAIN_CLASS=cn.xvkang.utils.deployserver.Main

set APP_BASE_PATH=%~dp0
set CP=%APP_BASE_PATH%config;%APP_BASE_PATH%lib\*
java -Xverify:none %JAVA_OPTS% -cp %CP% %MAIN_CLASS%

endlocal & popd
pause
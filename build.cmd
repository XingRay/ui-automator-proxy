@echo off

rem 获取命令行参数，如果没有参数则使用默认版本号
set VERSION=%1
if "%VERSION%"=="" set VERSION=1.0.0

set OUTPUT_DIR=output

set APP_NAME=ui-automator-proxy
set PACKAGE_NAME=com.github.xingray.uiautomatorproxy
set TEST_CLASS_NAME=UiAutomatorProxy

if not exist %OUTPUT_DIR% (
    mkdir %OUTPUT_DIR%
) else (
    del /Q %OUTPUT_DIR%\*.*
)

call gradle clean
call gradle assembleDebug
call gradle assembleDebugAndroidTest

rem 获取当前日期，格式为YYYYMMDD
for /f "tokens=1-3 delims=/ " %%a in ('echo %date%') do (
    set "today=%%a%%b%%c"
)

copy /Y app\build\outputs\apk\debug\app-debug.apk %OUTPUT_DIR%\%APP_NAME%-debug-%VERSION%.apk
copy /Y app\build\outputs\apk\androidTest\debug\app-debug-androidTest.apk %OUTPUT_DIR%\%APP_NAME%-debug-androidTest-%VERSION%.apk
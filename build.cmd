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

adb uninstall %PACKAGE_NAME%
adb uninstall %PACKAGE_NAME%.test

call gradle clean
call gradle assembleDebug
call gradle assembleDebugAndroidTest

rem 获取当前日期，格式为YYYYMMDD
for /f "tokens=1-3 delims=/ " %%a in ('echo %date%') do (
    set "today=%%a%%b%%c"
)

copy /Y app\build\outputs\apk\debug\app-debug.apk %OUTPUT_DIR%\%APP_NAME%-%VERSION%-debug.apk
copy /Y app\build\outputs\apk\androidTest\debug\app-debug-androidTest.apk %OUTPUT_DIR%\%APP_NAME%-%VERSION%-debug-androidTest.apk

call adb install %OUTPUT_DIR%\%APP_NAME%-%VERSION%-debug.apk
call adb install %OUTPUT_DIR%\%APP_NAME%-%VERSION%-debug-androidTest.apk

call adb shell pm grant "%PACKAGE_NAME%" "android.permission.DUMP"
call adb shell pm grant "%PACKAGE_NAME%" "android.permission.PACKAGE_USAGE_STATS"
call adb shell pm grant "%PACKAGE_NAME%" "android.permission.CHANGE_CONFIGURATION"

call adb forward tcp:51234 tcp:51234

@rem https://stackoverflow.com/questions/31474776/uidevice-getinstancegetinstrumentation-crashes-as-null-object-reference-wh
@rem -w 参数不能少, 没有 -w 参数, 无法获取 UiAutomation 和  UiDevice 对象
call adb shell am instrument -w -r -e debug false -e class "%PACKAGE_NAME%.%TEST_CLASS_NAME%" "%PACKAGE_NAME%.test/androidx.test.runner.AndroidJUnitRunner"

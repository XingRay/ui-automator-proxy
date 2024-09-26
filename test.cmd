@echo off

set APP_NAME=ui-automator-proxy
set PACKAGE_NAME=com.github.xingray.uiautomatorproxy
set CLASS_NAME=UiAutomatorProxy

call gradle assembleDebugAndroidTest
call adb install app\build\outputs\apk\androidTest\debug\app-debug-androidTest.apk

call adb shell pm grant "%PACKAGE_NAME%" "android.permission.DUMP"
call adb shell pm grant "%PACKAGE_NAME%" "android.permission.PACKAGE_USAGE_STATS"
call adb shell pm grant "%PACKAGE_NAME%" "android.permission.CHANGE_CONFIGURATION"

call adb shell am instrument -r -e debug false -e class "%PACKAGE_NAME%.%CLASS_NAME%" "%PACKAGE_NAME%.test/androidx.test.runner.AndroidJUnitRunner"
@rem call adb shell am instrument -r -e debug false -e class "com.github.xingray.uiautomatorproxy.UiAutomatorProxy" "com.github.xingray.uiautomatorproxy.test/androidx.test.runner.AndroidJUnitRunner"
call adb forward tcp:51234 tcp:51234
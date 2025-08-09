# ui-automator-proxy
ui-automator-proxy, running at android phone , as a webservice, provide UiAutomator functions by websocket



## how to use

1 clone repo

```shell
git clone git@github.com:XingRay/ui-automator-proxy.git
```



2 ensure adb is set, and only one device is connected

```shell
adb devices
```



2 run build script

```shell
cd ui-automator-proxy
./build.cmd
```



3 open broser or use curl, or other httpclient tools

http://127.0.0.1:51234/device/hierarchy



## how to

### 1 how to use if more than one device is connected

```shell
adb devices
```

get device serial, like: 192.168.0.100:5555 or 29DKW473LU92C7

edit build.cmd, add -s xxx after 'adb' in every adb cmd



before

```
call adb install %OUTPUT_DIR%\%APP_NAME%-%VERSION%-debug.apk
```

after

```
call adb -s 192.168.0.100:5555 install %OUTPUT_DIR%\%APP_NAME%-%VERSION%-debug.apk
```



### 2 how to change port

edit this line:

```
call adb forward tcp:51234 tcp:51234
```

change first tcp:51234 to which port you want, for example

```shell
call adb forward tcp:54321 tcp:51234
```

**do not change second port in this cmd if you not change app code**


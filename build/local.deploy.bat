@echo off
set plugin_name=coriander-haarlem

rem note: set permanently using: setx TEAMCITY_BUILDSERVER_ROOT "C:\Documents\.BuildServer"

set plugin_root="%TEAMCITY_BUILDSERVER_ROOT%\plugins"

echo Copying "%1" to "%plugin_root%"
copy /Y "%1" "%plugin_root%"
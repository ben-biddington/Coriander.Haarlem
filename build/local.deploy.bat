@echo off
set plugin_name=coriander-haarlem

set plugin_root="C:\Documents\.BuildServer\plugins"

echo Copying "%1" to "%plugin_root%"
copy /Y "%1" "%plugin_root%"

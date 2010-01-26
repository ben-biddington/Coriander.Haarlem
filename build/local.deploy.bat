@echo off
set plugin_name=coriander-haarlem

set plugin_root="C:\TeamCity\webapps\ROOT\WEB-INF\plugins"

mkdir "%plugin_root%\coriander-haarlem\server"
echo Copying "%1" to "%plugin_root%\%plugin_name%\server"
copy /Y "%1" "%plugin_root%\%plugin_name%\server"

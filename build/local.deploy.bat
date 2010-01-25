@echo off
set plugin_name=coriander-haarlem

set web_root="C:\Teamcity\webapps\ROOT\WEB-INF\lib"
set plugin_root="C:\TeamCity\webapps\ROOT\WEB-INF\plugins"
set plugin_root_1="C:\TeamCity\webapps\ROOT\plugins"

echo Copying jar: %1 to %web_root%
copy /Y "%1" %web_root%

REM echo Copying jar: %1 to %plugin_root%
REM This should be enough
REM copy /Y "%1" %plugin_root%

mkdir "%plugin_root%\coriander-haarlem\server"
echo Copying jar: %1 to "%plugin_root%\%plugin_name%\server"
copy /Y "%1" "%plugin_root%\%plugin_name%\server"

@echo off

set web_root="C:\Teamcity\webapps\ROOT\WEB-INF\lib"
set plugin_root="C:\TeamCity\webapps\ROOT\WEB-INF\plugins"

echo Copying jar: %1 to %web_root%

copy /Y "%1" %web_root%  REM Teamcity 4.x, shouldn't have to do this
copy /Y "%1" %plugin_root%

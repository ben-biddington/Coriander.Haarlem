@echo off

set web_root="C:\Teamcity\webapps\ROOT\WEB-INF\lib"

echo Copying jar: %1 to %web_root%

copy /Y "%1" %web_root%

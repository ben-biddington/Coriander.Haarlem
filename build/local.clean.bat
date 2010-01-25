@echo off
set plugin_name=coriander-haarlem

set web_root="C:\Teamcity\webapps\ROOT\WEB-INF\lib"
set plugin_root="C:\TeamCity\webapps\ROOT\WEB-INF\plugins"
set plugin_root_1="C:\TeamCity\webapps\ROOT\plugins"

rmdir /S /Q %web_root%\%plugin_name%
rmdir /S /Q %plugin_root%\%plugin_name%
rmdir /S /Q %plugin_root_1%\%plugin_name%

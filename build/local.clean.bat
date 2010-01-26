@echo off
set plugin_name=coriander-haarlem
set plugin_root="C:\TeamCity\webapps\ROOT\WEB-INF\plugins"

echo deleting "%plugin_root%\%plugin_name%" 
rmdir /S /Q %plugin_root%\%plugin_name%

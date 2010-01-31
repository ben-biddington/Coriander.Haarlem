@echo off

set count=100
set flags=-ir
set for_what=%1
set where=%2

set base_dir="C:\TeamCity\webapps\ROOT\plugins\coriander-haarlem\bin"

cd %base_dir%

grep.exe %flags% %for_what% "%where%" | sort | head -n %count%

exit 0

REM USAGE: grep what where
REM EXAMPLE: grep CHUBBY C:\Documents\.BuildServer\system\artifacts\Plinkton
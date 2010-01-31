@echo off

set count=100
set flags=-ir
set for_what=%2
set where=%2

grep %flags% "%for_what%" "%where%" | sort | head -n %count%

REM USAGE: grep what where
REM EXAMPLE: grep CHUBBY C:\Documents\.BuildServer\system\artifacts\Plinkton
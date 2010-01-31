@echo off

set count=100
set flags=-ir

grep %flags% "%1" . %2 | sort | head -n %count%

REM USAGE: grep what where
REM EXAMPLE: grep CHUBBY C:\Documents\.BuildServer\system\artifacts\Plinkton
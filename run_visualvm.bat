@echo off
call mvn clean package
call "C:\Program Files\7-Zip\7z.exe" x -y -o"visualvm4idea-dist\target" visualvm4idea-dist\target\visualvm4idea-*.zip
call ..\visualvm_101\bin\visualvm.exe -J-javaagent:"D:\DEVEL\VisualVM for IDEA\visualvm4idea\visualvm4idea-dist\target\visualvm4idea\lib\visualvm4idea-visualvm-agent.jar"="lib=D:\DEVEL\VisualVM for IDEA\visualvm4idea\visualvm4idea-dist\target\visualvm4idea\lib\visualvm4idea-core.jar"

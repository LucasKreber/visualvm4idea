@echo off
call mvn clean package
call rmdir /s /q "%USERPROFILE%\.IntelliJIdea80\system\plugins-sandbox\plugins\visualvm4idea"
call "C:\Program Files\7-Zip\7z.exe" x -y -o"%USERPROFILE%\.IntelliJIdea80\system\plugins-sandbox\plugins" visualvm4idea-dist\target\visualvm4idea-*.zip
call "C:\Program Files\JetBrains\idea-8.0-jdk15\bin\idea.exe"

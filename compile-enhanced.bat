@echo off
echo ==========================================
echo   Compiling Ludo Master with Chat
echo ==========================================
echo.

REM Clean and create bin directory
if exist bin rmdir /s /q bin
mkdir bin

REM Compile all Java files
javac -d bin -sourcepath src src\Main.java src\client\*.java src\model\*.java src\server\*.java src\util\*.java

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Compilation Successful!
    echo ==========================================
    echo.
) else (
    echo.
    echo ==========================================
    echo   Compilation Failed!
    echo ==========================================
    echo.
)

pause

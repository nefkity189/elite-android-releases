@echo off
title Ambiente de Testes - Elite + Maestro MCP
set "PROJECT_DIR=D:\Aplicativos_RODANDO\AISLE_Proj"
set "EMULATOR_EXE=%LOCALAPPDATA%\Android\Sdk\emulator\emulator.exe"
set "ADB_EXE=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"

echo ===================================================
echo  1. Iniciando o Emulador Android...
echo ===================================================
for /f "delims=" %%i in ('"%EMULATOR_EXE%" -list-avds') do (
    set "AVD_NAME=%%i"
    goto :start_emu
)

:start_emu
if "%AVD_NAME%"=="" (
    echo [ERRO] Nenhum emulador encontrado.
    pause
    exit /b
)

echo Abrindo emulador: %AVD_NAME%
start "" "%EMULATOR_EXE%" -avd "%AVD_NAME%" -no-snapshot-load

echo.
echo ===================================================
echo  2. Aguardando o Android terminar de carregar...
echo ===================================================
"%ADB_EXE%" wait-for-device

:boot_loop
for /f "delims=" %%b in ('"%ADB_EXE%" shell getprop sys.boot_completed 2^>nul') do set "BOOT=%%b"
if not "%BOOT%"=="1" (
    timeout /t 2 /nobreak >nul
    goto :boot_loop
)
echo Emulador online!

echo.
echo ===================================================
echo  3. Buscando o APK mais recente do projeto...
echo ===================================================
set "LATEST_APK="
for /f "delims=" %%f in ('dir "%PROJECT_DIR%\app\build\outputs\apk\*.apk" /s /b /o:-d 2^>nul') do (
    set "LATEST_APK=%%f"
    goto :install_apk
)

:install_apk
if "%LATEST_APK%"=="" (
    echo [AVISO] Nenhum APK encontrado em app\build\outputs\apk. Compile o projeto no Android Studio primeiro.
) else (
    for %%A in ("%LATEST_APK%") do echo APK selecionado: %%~nxA ^(Modificado em: %%~tA^)
    echo Caminho completo: %LATEST_APK%
    echo Instalando no emulador...
    "%ADB_EXE%" install -r -t "%LATEST_APK%"
)

echo.
echo ===================================================
echo  4. Abrindo Viewer do Maestro e Claude Code...
echo ===================================================
start http://localhost:9999
cd /d "%PROJECT_DIR%"
claude
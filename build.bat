@echo off
setlocal
cd /d "%~dp0"

echo === Проверка JDK ===
where java >nul 2>nul || (echo FAIL: JDK не найден. Установите JDK 17. & exit /b 1)
java -version

echo === Проверка Android SDK ===
if "%ANDROID_HOME%"=="" if "%ANDROID_SDK_ROOT%"=="" (
  echo FAIL: ANDROID_HOME не задан. Установите Android SDK.
  exit /b 1
)

echo === Загрузка bootstrap-aarch64.zip ===
if not exist "app\src\main\assets\bootstrap-aarch64.zip" (
  mkdir "app\src\main\assets" 2>nul
  curl -L -o "app\src\main\assets\bootstrap-aarch64.zip" ^
    "https://github.com/termux/termux-packages/releases/download/bootstrap-2024.10.28-r1/bootstrap-aarch64.zip"
)

echo === Сборка APK ===
call gradlew.bat assembleDebug
echo === Готово: app\build\outputs\apk\debug\app-debug.apk ===
pause

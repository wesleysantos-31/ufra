@echo off
chcp 65001 >nul

echo ==============================================
echo   Script de Execução - Sistema de Ingressos
echo ==============================================

IF NOT EXIST "mysql-connector-j-8.0.33.jar" (
    echo [INFO] Baixando driver de conexão MySQL...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar' -OutFile 'mysql-connector-j-8.0.33.jar'"
    echo [INFO] Download concluído!
)

echo.
echo [INFO] Compilando cinema.java...
javac cinema.java

IF %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha na compilação do código Java!
    pause
    exit /b
)

echo [INFO] Iniciando o sistema...
echo ==============================================
echo.

java -cp ".;mysql-connector-j-8.0.33.jar" cinema

echo.
pause

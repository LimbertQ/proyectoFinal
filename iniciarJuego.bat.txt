@echo off
REM El punto (.) representa el directorio actual donde esta el .bat
set JAVAFX_PATH=.\lib 

java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp TheCelestials.jar thecelestials.Main
pause
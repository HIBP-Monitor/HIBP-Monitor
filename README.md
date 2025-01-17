# HIBP-Monitoring Daemon
Authors:
nadlp1
eberv1
gausf1

## Scripts
bin/jar/run_standalone_jar.bat
bin/jar/run_standalone_jar.sh
bin/linux_deb_installer/install_and_autostart.sh
compile.sh
run.sh
compile_and_run.sh

## Installation
The application provides several installation options to accommodate different operating systems. Please follow the instructions for your respective platform:

Standalone Jar (Java 23 installation required): <br>
- Windows:
  Execute the script `bin/jar/run_standalone_jar.bat`. It will start the application.

- Linux and macOS:
  Execute the script `bin/jar/run_standalone_jar.sh`. It will start the application.

Note: Ensure that Java 23 is installed on your system. You can verify this by running `java -version` in your terminal or command prompt.

Build and Run the Application (Java 23 installation required): <br>
- Linux and macOS or Windows with bash installed:
 1. Execute the script 'compile_and_run.sh'.
2. It will:
   - build the application and generate the jar in the target/ folder
   - starts the application by executing the target/monitoring-demon-1.0-shaded.jar

Note: Ensure that Java 23 is installed on your system. You can verify this by running `java -version` in your terminal or command prompt.

Windows Installer: <br>
To install the application as a native app on your computer:
1. Use the installer file `bin\windows_installer\windows_installer.msi`.
2. The installer will:
   - Install the application on your system.
   - Start the application immediately after installation.
   - Add the application to the autostart entries, which you will need to enable manually.

Linux .deb Installer: <br>
To install the application as a native app on your computer:
1. Execute the script `bin/linux_deb_installer/install_and_autostart.sh`.
2. The script will:
   - Install the application using the HIBPWNDMonitor-1.0.deb file.
   - Start the application immediately after installation.
   - Add the application to the autostart entries.

Note: After installation, you can start the application in the future by using the command: HIBPWNDMonitor

MacOS installer: <br>
To install the application as a native app on your computer:
1. Use the installer file bin/macOS_installer/HIBPMonitoringDeamon-1.0.pkg
2. The script will:
   - Install the application using the HIBPMonitoringDeamon-1.0.pkg file.


Known Issues: <br>
- Linux Notifications:
  On most Linux systems, notifications may not work correctly because GNOME does not support the system tray by default.

Logs: <br>
- The application creates a `logs` folder in the installation path.
- Logs are written to the file:
  logs/HIBPWNDMonitor.log
- You can review this log file to troubleshoot issues or monitor application behavior.


## License

MIT License

Copyright (c) 2025 Vanessa Eberhard, Frank Erich Gauss, Pascal Joël Nadler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
#!/bin/bash
 
# Variables
DEB_FILE="HIBPWNDMonitor-1.0.deb"
INSTALL_DIR="/opt/hibpwndmonitor"
AUTOSTART_DIR="$HOME/.config/autostart"
AUTOSTART_FILE="$AUTOSTART_DIR/HIBPWNDMonitor.desktop"
WRAPPER_SCRIPT="/usr/local/bin/HIBPWNDMonitor"
 
# Check if the .deb file exists
if [ ! -f "$DEB_FILE" ]; then
    echo "Error: $DEB_FILE not found in the current directory!"
    exit 1
fi
 
# Install the .deb file
echo "Installing $DEB_FILE..."
sudo apt install ./$DEB_FILE -y
 
if [ $? -ne 0 ]; then
    echo "Error: Failed to install $DEB_FILE!"
    exit 1
fi
 
# Ensure the logs directory exists and is writable
LOG_DIR="$INSTALL_DIR/bin/logs"
echo "Creating log directory: $LOG_DIR"
sudo mkdir -p "$LOG_DIR"
sudo chmod 777 "$LOG_DIR"
 
if [ $? -ne 0 ]; then
    echo "Error: Failed to create or set permissions for $LOG_DIR!"
    exit 1
fi
 
# Create a wrapper script to ensure the application starts correctly
echo "Creating wrapper script: $WRAPPER_SCRIPT"
sudo bash -c "cat <<EOL > $WRAPPER_SCRIPT
#!/bin/bash
cd $INSTALL_DIR/bin
exec ./HIBPWNDMonitor \"\$@\"
EOL"
sudo chmod +x "$WRAPPER_SCRIPT"
 
if [ $? -ne 0 ]; then
    echo "Error: Failed to create wrapper script!"
    exit 1
fi
 
# Create the autostart directory if it doesn't exist
if [ ! -d "$AUTOSTART_DIR" ]; then
    echo "Creating autostart directory: $AUTOSTART_DIR"
    mkdir -p "$AUTOSTART_DIR"
fi
 
# Create the autostart .desktop file
echo "Creating autostart entry..."
cat <<EOL > "$AUTOSTART_FILE"
[Desktop Entry]
Type=Application
Exec=HIBPWNDMonitor
Hidden=false
NoDisplay=false
X-GNOME-Autostart-enabled=true
Name=HIBPWNDMonitor
Comment=Start HIBPWNDMonitor on login
EOL
 
if [ $? -eq 0 ]; then
    echo "Autostart entry created: $AUTOSTART_FILE"
else
    echo "Error: Failed to create autostart entry!"
    exit 1
fi
 
# Start the application
echo "Starting the application..."
HIBPWNDMonitor &
 
if [ $? -ne 0 ]; then
    echo "Error: Failed to start the application!"
    exit 1
fi
 
echo "Installation, configuration, and autostart setup complete!"

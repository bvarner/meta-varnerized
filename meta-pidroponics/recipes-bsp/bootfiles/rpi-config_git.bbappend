# Appends the dtoverlays to the bootfiles config.txt for pidroponics.
do_deploy_append() {
    echo "# Enable HC-SR04 ultrasonic transponders" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    echo "dtoverlay=srf04" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    echo "# Enable 4 Channel Relays" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    echo "dtoverlay=4channel-relay" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    echo "# Enabling pidroponic ADC."
    echo "dtoverlay=ads1115-pidroponic" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
}

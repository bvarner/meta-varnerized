# Appends the dtoverlays to the bootfiles config.txt for pidroponics.
do_deploy_append() {
    echo "# Enable HC-SR04 ultrasonic transponders" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "dtoverlay=srf04" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "# Enable 4 Channel Relays" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "dtoverlay=4channel-relay" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "# Enabling pidroponic ADC."
    echo "dtoverlay=ads1115-pidroponic" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
}

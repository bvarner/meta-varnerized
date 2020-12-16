do_deploy_append() {
    echo "# Enable HX711 load cell" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "dtoverlay=hx711-rocketstand" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
}

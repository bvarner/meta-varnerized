do_deploy_append() {
    echo "# Enable HX711 load cell" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    echo "dtoverlay=hx711-rocketstand" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
}

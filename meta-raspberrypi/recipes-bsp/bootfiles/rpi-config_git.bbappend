do_deploy_append() {
    echo "#Enable hardware watchdog." >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
    echo "dtparam=watchdog=on" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
}

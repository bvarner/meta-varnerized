# Appends the dtoverlays to the bootfiles config.txt
do_deploy_append() {
    echo "dtoverlay=pigaragedoor" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
}

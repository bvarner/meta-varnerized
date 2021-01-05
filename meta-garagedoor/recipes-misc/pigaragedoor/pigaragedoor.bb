DESCRIPTION = "Use a raspberry pi to control a relay for a garage door opener."
SECTION = "misc"
HOMEPAGE = "https://github.com/bvarner/pigaragedoor/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Append the SRC_URI for CERT files if the variables are set.
def certfile_src(d):
    files = ''
    if d.getVar('PIGARAGEDOOR_CERT') is not None:
        files = files + 'file://' + d.getVar('PIGARAGEDOOR_CERT', True)

    if d.getVar('PIGARAGEDOOR_CERT_KEY') is not None:
        files = files + ' ' + 'file://' + d.getVar('PIGARAGEDOOR_CERT_KEY', True)

    print('adding files: ' + files)

    return files

SRC_URI = "\
	git://${GO_IMPORT} \
	file://systemd-units/pigaragedoor.service \
	file://avahi/pigaragedoor.service \
        ${@certfile_src(d)} \
"

SRCREV = "${AUTOREV}"
PV = "1.0+git${SRCPV}"

GO_IMPORT = "github.com/bvarner/pigaragedoor"
GO_INSTALL = "${GO_IMPORT}"

DEPENDS = "\
	go-rpigpio \
	avahi \
"

RDEPENDS_${PN}_append = "\
	avahi-daemon \
	avahi-autoipd \
"

inherit go gorice systemd
GO_RICE_EMBEDTYPE = 'go'

do_install_append() {
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/systemd-units/pigaragedoor.service ${D}${systemd_unitdir}/system
	
	install -d ${D}${sysconfdir}/avahi/services
	install -m 0644 ${WORKDIR}/avahi/pigaragedoor.service ${D}${sysconfdir}/avahi/services

	install -d ${D}${sysconfdir}/ssl/certs/pigaragedoor
	# Certs should be copied by the fetcher into the workdir so we can stage them into the image.
	if [ -n "${PIGARAGEDOOR_CERT}" ]; then
		install -m 0644 ${WORKDIR}/${PIGARAGEDOOR_CERT} ${D}${sysconfdir}/ssl/certs/pigaragedoor
	fi
	if [ -n "${PIGARAGEDOOR_CERT_KEY}" ]; then
		install -m 0644 ${WORKDIR}/${PIGARAGEDOOR_CERT_KEY} ${D}${sysconfdir}/ssl/certs/pigaragedoor
	fi

}

SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = "pigaragedoor.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

# The file-rdeps is picking up a dependency to 'bash' from the shell-script to build this with travis. Yick.
INSANE_SKIP_${PN}-dev = "file-rdeps"

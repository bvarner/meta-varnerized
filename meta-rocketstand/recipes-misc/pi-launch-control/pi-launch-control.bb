DESCRIPTION = "Use a raspberry pi to control model rocket launches."
SECTION = "misc"
HOMEPAGE = "https://github.com/bvarner/pi-launch-control/"

LICENSE = "APSL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/APSL-2.0;md5=f9e4701d9a216a87ba145bbe25f54c58"

SRCNAME = "pi-launch-control"
PKG_NAME = "github.com/bvarner/${SRCNAME}"
SRC_URI = "\
	git://${PKG_NAME};branch=develop \
	file://systemd-units/pi-launch-control.service \
	file://avahi/pi-launch-control.service \
"
SRCREV = "${AUTOREV}"
PV = "1.0+git${SRCPV}"

DEPENDS = "\
	avahi \
"

RDEPENDS_${PN}_append = "\
	avahi-daemon \
	avahi-autoipd \
"

RDEPENDS_${PN}-staticdev_append = "\
	perl \
	bash \
"

inherit go-mod gorice systemd

GO_LINKSHARED = ''
GO_IMPORT = "${PKG_NAME}"
GO_INSTALL = "${GO_IMPORT}/..."

# Add the import path to the rice command.
RICE_ARGS = "-v -i ${GO_IMPORT}/${SRCNAME}"
GO_RICE_EMBEDTYPE = 'go'


do_install_append() {
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/systemd-units/${SRCNAME}.service ${D}${systemd_unitdir}/system
	
	install -d ${D}${sysconfdir}/avahi/services
	install -m 0644 ${WORKDIR}/avahi/${SRCNAME}.service ${D}${sysconfdir}/avahi/services

	install -d ${D}${sysconfdir}/ssl/certs
}

SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = "${SRCNAME}.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

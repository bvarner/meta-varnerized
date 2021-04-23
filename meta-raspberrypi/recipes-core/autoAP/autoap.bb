DESCRIPTION = "Auto AccessPoint Mode."
SECTION = "misc"
HOMEPAGE = "https://github.com/bvarner/meta-varnerized/"

LICENSE = "APSL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/APSL-2.0;md5=f9e4701d9a216a87ba145bbe25f54c58"

SRCNAME = "autoap"
PKG_NAME = "${SRCNAME}"
SRC_URI = "\
	file://systemd-units/wpa_cli@.service \
	file://autoAP.sh \
"

# As this package is tied to systemd, only build it when we're also building systemd.
inherit features_check
REQUIRED_DISTRO_FEATURES = "systemd"

RDEPENDS_${PN}_append = "\
	bash \
"
FILES_${PN}_append = "\
	/lib/systemd/system \
	/lib/systemd/system/wpa_cli@.service \
"

do_install_append() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/autoAP.sh ${D}${bindir}/
	
	# This is the autoap service that interacts with the wpa supplicant via wpa_cli to restart networking for AP fallback.
	install -d ${D}${systemd_unitdir}/system/
	install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
	install -m 0644 ${WORKDIR}/systemd-units/wpa_cli@.service ${D}${systemd_unitdir}/system
	# Enable the serivce.
	ln -sf ${systemd_unitdir}/system/wpa_cli@.service \
		${D}${sysconfdir}/systemd/system/multi-user.target.wants/wpa_cli@wlan0.service
}

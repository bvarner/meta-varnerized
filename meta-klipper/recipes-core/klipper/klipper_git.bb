SUMMARY = "Klipper host daemon and MCU firmware tooling"
DESCRIPTION = "Klipper host daemon plus helper tooling for building and flashing MCU firmware."
HOMEPAGE = "https://www.klipper3d.org/"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "\
    git://github.com/Klipper3d/klipper.git;protocol=https;branch=master \
    file://klipper.service \
    file://klipper-mcu \
"
SRCREV = "${AUTOREV}"
PV = "0.0.1_git${SRCPV}"
S = "${WORKDIR}/git"

inherit systemd

RDEPENDS:${PN} = "\
    bash \
    python3 \
    python3-cffi \
    python3-greenlet \
    python3-jinja2 \
    python3-pyserial \
    python3-core \
    perl \
    udev \
"

PACKAGES =+ "${PN}-mcu-tools"

FILES:${PN}-mcu-tools = "${bindir}/klipper-mcu"

FILES:${PN} += "\
    ${datadir}/klipper \
    ${sysconfdir}/klipper \
    ${systemd_system_unitdir}/klipper.service \
"

RDEPENDS:${PN}-mcu-tools = "\
    bash \
    python3-curses \
"

SYSTEMD_SERVICE:${PN} = "klipper.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

do_compile() {
    cd "${S}/klippy/chelper"
    ${CC} ${CFLAGS} -fPIC ${LDFLAGS} -shared -o c_helper.so *.c
}

do_install() {
    install -d "${D}${datadir}"
    cp -R --no-dereference --preserve=mode,links "${S}/." "${D}${datadir}/klipper"
    rm -rf "${D}${datadir}/klipper/.git"

    install -d "${D}${sysconfdir}/klipper"
    install -d "${D}${systemd_system_unitdir}"
    install -m 0644 "${WORKDIR}/klipper.service" "${D}${systemd_system_unitdir}/klipper.service"

    install -d "${D}${bindir}"
    install -m 0755 "${WORKDIR}/klipper-mcu" "${D}${bindir}/klipper-mcu"
}

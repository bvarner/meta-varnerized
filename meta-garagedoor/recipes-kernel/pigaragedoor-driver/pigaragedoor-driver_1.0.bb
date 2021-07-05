SUMMARY = "Pigaragedoor Kernel Module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/driver/COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

inherit module

SRCREV = "${AUTOREV}"
PV = "1.0+git${SRCPV}"

PR = "r0"

KERNEL_MODULE_AUTOLOAD += "pigaragedoor"

SRC_URI = "git://git@github.com:/bvarner/pigaragedoor.git;protocol=ssh;branch=feature/kernel-driver"
 
S = "${WORKDIR}/git/driver"


# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.


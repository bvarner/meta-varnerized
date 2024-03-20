DESCRIPTION = "Minica is a simple CA intended for use in situations where the CA operator also operates each host where a certificate will be used."
SECTION = "misc"
HOMEPAGE = "https://github.com/jsha/minica"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCNAME = "minica"
PKG_NAME = "github.com/jsha/${SRCNAME}"
SRCREV = "v1.0.2"
SRC_URI = "\
	git://${PKG_NAME}.git;tag=${SRCREV} \
"

DEPENDS = ""

RDEPENDS_${PN}-dev_append = "\
	bash \
"

RDEPENDS_${PN}-staticdev_append = "\
	bash \
	perl \
"

GO_IMPORT = "${PKG_NAME}"
GO_INSTALL = "github.com/jsha/minica/minica"

inherit go-mod

# -buildmode=pie breaks a lot of things.
GOBUILDFLAGS_remove += ' -buildmode=pie'
# This overrides the inherited functions from go.bbclass
python() {
    d.appendVar('INSANE_SKIP_%s' % d.getVar('PN'), " textrel")
}

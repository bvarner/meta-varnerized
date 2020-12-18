DESCRIPTION = "go.rice is a Go package that makes working with resources such as html very easy."
SECTION = "misc"
HOMEPAGE = "https://github.com/GeertJohan/go.rice"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f"

SRCNAME = "go.rice"
PKG_NAME = "github.com/GeertJohan/${SRCNAME}"
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
GO_INSTALL = "github.com/GeertJohan/go.rice github.com/GeertJohan/go.rice/rice"

inherit go-mod

# -buildmode=pie breaks rice. Skip "textrel" QA checking, and avoid setting -buildmode=pie.
GOBUILDFLAGS_remove += ' -buildmode=pie'
# This overrides the inherited functions from go.bbclass
python() {
    d.appendVar('INSANE_SKIP_%s' % d.getVar('PN'), " textrel")
}

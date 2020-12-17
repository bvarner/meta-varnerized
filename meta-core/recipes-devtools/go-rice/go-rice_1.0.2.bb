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

GO_LINKSHARED = ''
GO_IMPORT = "${PKG_NAME}"
GO_INSTALL = "${GO_IMPORT}/..."

BBCLASSEXTEND += "native"

inherit go-mod

# Modern go builds that use go modules need dependencies to be fetched before compile...
#do_compile_prepend() {
#    ( cd ${WORKDIR}/build/src/${GO_IMPORT} && ${GO} get -v -d ./... && chmod -R +rw .. )
#    
#}


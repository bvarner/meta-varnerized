# Generates certificates for the given packagename if provided with a list of semicolon separated domains or ip addresses.

DEPENDS_GENCERT_class-target = "go-minica-native"
DEPENDS_GENCERT_class-native = "go-minica-native"

DEPENDS_append = " ${DEPENDS_GENCERT}"

export MINICA = "${STAGING_BINDIR_NATIVE}/minica"

MINICA_ROOT_DIR ?= '${WORKDIR}'


# Recipes inheriting this class can define these, and have certs generated for them.
GENCERT_DOMAINS ?= ''
GENCERT_IPS ?= ''

gencert_domain_certs() {
	local IFS=";"
	for dir in $1;
	do
		if [ ! -d "$dir" ]; then
			${MINICA} -domains "$dir"
		fi
	done
}

gencert_ip_certs() {
	local IFS=";"
	for dir in $1;
	do
		if [ ! -d "$dir" ]; then
			${MINICA} -ip-addresses "$dir"
		fi
	done
}

do_compile_prepend() {
	origDir=$PWD
	
	if [ ! -d "${MINICA_ROOT_DIR}" ]; then
		mkdir -p ${MINICA_ROOT_DIR}
	fi
	
	
	cd ${MINICA_ROOT_DIR}
	if [ -n "${GENCERT_DOMAINS}" ]; then
		gencert_domain_certs "${GENCERT_DOMAINS}"
	fi
	if [ -n "${GENCERT_IPS}" ]; then
		gencert_ip_certs "${GENCERT_IPS}"
	fi
	cd $origDir
}

gencert_install_files() {
	local IFS=";"
	for dir in $1;
	do
		bbdebug 2 "gencerts_install: $dir"
		if [ -n "$dir" ]; then
		    install -d ${D}${sysconfdir}/ssl/certs/${PN}/${dir}
			install -m 0444 ${MINICA_ROOT_DIR}/$dir/cert.pem ${D}${sysconfdir}/ssl/certs/${PN}/$dir
			install -m 0444 ${MINICA_ROOT_DIR}/$dir/key.pem ${D}${sysconfdir}/ssl/certs/${PN}/$dir
			install -m 0444 ${MINICA_ROOT_DIR}/minica.pem ${D}${sysconfdir}/ssl/certs/${PN}-root.pem
			install -m 0444 ${MINICA_ROOT_DIR}/minica-key.pem ${D}${sysconfdir}/ssl/certs/${PN}-root-key.pem
		fi
	done
}

do_install_append() {
	if [ -n "${GENCERT_DOMAINS}${GENCERT_IPS}" ]; then
		gencert_install_files "${GENCERT_DOMAINS};${GENCERT_IPS}"
	fi
}

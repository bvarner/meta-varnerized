# Generates certificates for the given packagename if provided with a list of semicolon separated domains or ip addresses.

DEPENDS_GENCERT_class-target = "go-minica-native"
DEPENDS_GENCERT_class-native = "go-minica-native"

DEPENDS_append = " ${DEPENDS_GENCERT}"

export MINICA = "${STAGING_BINDIR_NATIVE}/minica"

MINICA_ROOT_DIR ?= '${WORKDIR}'


# Recipes inheriting this class can define these, and have certs generated for them.
GENCERT_DOMAINS ?= ''
GENCERT_IPS ?= ''

def get_gencert_domains(d):
    domains = d.getVar("GENCERT_DOMAINS")
    if domains is not None:
        domains = domains.replace(";", ",")
    return domains

def get_gencert_ips(d):
    ips = d.getVar("GENCERT_IPS")
    if ips is not None:
        ips = ips.replace(";", ",")
    return ips

do_compile_prepend() {
	origDir=$PWD
	
	# Make the proper working dir if it doesn't exist.
	if [ ! -d "${MINICA_ROOT_DIR}" ]; then
		mkdir -p ${MINICA_ROOT_DIR}
	fi

	# Set working dir.
	cd ${MINICA_ROOT_DIR}
	
	cleanDirs="${GENCERT_DOMAINS};${GENCERT_IPS}"

	# Cleanup any existing certs
	local IFS=";"
	for dir in $cleanDirs
	do
		if [ -n "${dir}" ]; then
			rm -fr ${dir}
		fi
	done
	
	if [ -n "${GENCERT_DOMAINS}" ]; then
		bbdebug 2 "domains: ${@get_gencert_domains(d)} ips: ${@get_gencert_ips(d)}"
		${MINICA} -domains "${@get_gencert_domains(d)}" -ip-addresses "${@get_gencert_ips(d)}"
	fi

	# Restore Working dir.
	cd $origDir
}

gencert_install_files() {
	install -d ${D}${sysconfdir}/ssl/certs/${PN}
	install -m 0444 ${MINICA_ROOT_DIR}/minica.pem ${D}${sysconfdir}/ssl/certs/${PN}-root.pem
	install -m 0444 ${MINICA_ROOT_DIR}/minica-key.pem ${D}${sysconfdir}/ssl/certs/${PN}-root-key.pem

	local IFS=";"
	for dir in $1;
	do
		bbdebug 2 "gencerts_install: $dir"
		if [ -n "$dir" ]; then
			if [ -d "${MINICA_ROOT_DIR}/$dir" ]; then
				install -m 0444 ${MINICA_ROOT_DIR}/$dir/cert.pem ${D}${sysconfdir}/ssl/certs/${PN}
				install -m 0444 ${MINICA_ROOT_DIR}/$dir/key.pem ${D}${sysconfdir}/ssl/certs/${PN}
			fi
		fi
	done
}

do_install_append() {
	if [ -n "${GENCERT_DOMAINS}${GENCERT_IPS}" ]; then
		gencert_install_files "${GENCERT_DOMAINS};${GENCERT_IPS}"
	fi
}

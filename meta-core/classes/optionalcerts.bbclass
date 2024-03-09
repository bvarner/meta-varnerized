# Adds optional certificates for the given packagename if the path to the certificate and key is provided to the build.

# Returns a list of absolute file paths for cert / key definitions.
def optionalcerts_paths(d):
    files = []

    # If ${PN}_CERTDIR is defined, default to using ${PN}_CERTDIR/[cert|key].pem
    certdir = d.getVar(d.getVar("PN") + '_CERTDIR', True)
    if certdir is not None:
        if not os.path.isdir(certdir):
            bb.fatal("Specified certdir: " + certdir + " does not exist.")

        # Determine the file names to use for cert & key
        certfile = 'cert.pem'
        keyfile = 'key.pem'

        # Allow for CERTFILE and KEYFILE overrides.
        pncertfile = d.getVar(d.getVar("PN") + '_CERTFILE', True)
        if pncertfile is not None:
            certfile = pncertfile
        
        pnkeyfile = d.getVar(d.getVar("PN") + '_KEYFILE', True)
        if pnkeyfile is not None:
            keyfile = pncertfile

        files.append(os.path.join(certdir, certfile))
        files.append(os.path.join(certdir, keyfile))
    
    # If there is a specific cert file specified, add it.
    cert = d.getVar(d.getVar("PN") + '_CERT', True)
    if cert is not None:
        if not os.path.isfile(cert):
            bb.fatal("Specified cert file: " + cert + " does not exist.")

        files.append(cert)
    
    # If there is a specific key file specified, add it.
    key = d.getVar(d.getVar("PN") + '_CERT_KEY', True)
    if key is not None:
        if not os.path.isfile(key):
            bb.fatal("Specified key file: " + key + " does not exist.")

        files.append(key)

    return files

# Prepend the file:// URI and return the big long string of paths.
def optionalcerts_src(d):
    file_uris = ''
    for path in optionalcerts_paths(d):
        file_uris += 'file://' + path + ' '

    return file_uris

# Separate the paths with ';' for parsing in shell scripts.
def optionalcerts_sep(d):
    quotepaths = ''
    for path in optionalcerts_paths(d):
        quotepaths += path + ';'

    if len(quotepaths) > 0:
        quotepaths = quotepaths[:-1]
    
    return quotepaths

# Append any cert files
SRC_URI += "${@optionalcerts_src(d)}"

OPTIONALCERT_FILES = "${@optionalcerts_sep(d)}"

optionalcerts_install_files() {
	local IFS=";"
	for path in $1;
	do
		if [ -n "$path" ]; then
			install -m 0444 ${WORKDIR}/$path ${D}${sysconfdir}/ssl/certs/${PN}
		fi
	done
}

do_install:append() {
	if [ -n "${OPTIONALCERT_FILES}" ]; then
		install -d ${D}${sysconfdir}/ssl/certs/${PN}
		optionalcerts_install_files "${OPTIONALCERT_FILES}"
	fi
}

# The list of packages that should have gencert packaging scripts added.
# For each entry, optionally include GENCERT_DOMAINS_[package] and
# GENCERT_IPS_[package] to specify the domains and ips for the cert to generate.
GENCERT_PACKAGES ?= '${PN}'

DEPENDS_GENCERT ?= ""
DEPENDS_GENCERT_class-target = "go-minica-native ca-certificates"
DEPENDS_GENCERT_class-native = "go-minica-native openssl"

DEPENDS:append = " ${DEPENDS_GENCERT}"

inherit minica

python gencert_populate_packages() {
    import subprocess
    
    bb.debug(1, "MINICA_ROOT_DIR " + d.getVar("MINICA_ROOT_DIR"))

    # Add files to FILES_pkg if existent and not already done
    def gencert_append_file(pkg, file_append):
        appended = False
        if os.path.exists(oe.path.join(d.getVar("MINICA_ROOT_DIR"), file_append)):
            files = d.getVar('FILES_' + pkg, False) or ""
            if file_append not in files.split():
                d.appendVar('FILES_' + pkg, " " + file_append)
                bb.debug(1, 'Added to FILES_' + pkg + "  " + file_append)
                appended = True
        return appended
        
    if os.path.exists(d.getVar("D")):
        # For each package that inherits us, run the stuff.
        for pkg in d.getVar('GENCERT_PACKAGES').split():
            bb.debug(1, 'gencert_populate_packages for %s' % pkg)

            cleanDirs = ''
            domains = (d.getVar('GENCERT_DOMAINS_' + pkg) or '')
            ips = (d.getVar('GENCERT_IPS_' + pkg) or '')
            cleanDirs = ";".join([domains, ips]).replace(";", " ")

            bb.debug(1, '  cleanDirs: %s' % cleanDirs)
            for dir in cleanDirs.split(" "):
                cmd = 'rm -fr ' + d.getVar('MINICA_ROOT_DIR') + dir
                bb.debug(1, '  %s' % cmd)
                subprocess.check_output(cmd, shell=True, stderr=subprocess.STDOUT)

            bb.utils.mkdirhier(d.getVar('MINICA_ROOT_DIR'))

            cmd = '' + d.getVar('MINICA')
            if domains:
                cmd = cmd + ' -domains "' + domains + '"'

            if ips:
                cmd = cmd + ' -ip-addresses "' + ips + '"'

            bb.debug(1, 'executing: %s' % cmd)
            subprocess.check_output(cmd, cwd=d.getVar('MINICA_ROOT_DIR'), shell=True, stderr=subprocess.STDOUT)
            
            # Get a list of all the .pems generated in the minica root directory. Copy them and add a .crt.
            cmd = 'find ' + d.getVar('MINICA_ROOT_DIR') + ' -type f -name \'*.pem\' -not -name \'*key.pem\' | sed \'s,^' + d.getVar('MINICA_ROOT_DIR') + ',,\' | sort'
            certlist = subprocess.check_output(cmd, cwd=d.getVar('MINICA_ROOT_DIR'), shell=True, stderr=subprocess.STDOUT).decode('utf-8')
            # For each certificate, copy it to a .crt and that to the file list for the package.
            for pem in certlist.split():
                cmd = 'cp ' + d.getVar('MINICA_ROOT_DIR') + pem + ' ' + d.getVar('MINICA_ROOT_DIR') + pem.rsplit('.', 1)[0] + '.crt'
                bb.debug(1, 'executing: ' + cmd)
                subprocess.check_output(cmd, cwd=d.getVar('MINICA_ROOT_DIR'), shell=True, stderr=subprocess.STDOUT)
                gencert_append_file('ca-certificates', pem.rsplit('.', 1)[0] + '.crt')
}

# Ensure that we install the generated cert & key pem pairs as part of this package.
do_install:append() {
    install -d ${D}${sysconfdir}/ssl/certs/${PN}
    
    bbdebug 1 "Looking for gencert files to install..."
    
	files=$(find ${MINICA_ROOT_DIR} -type f -name '*.pem' | sed "s|^${MINICA_ROOT_DIR}||" | sort)

	for file in $files; do
	    dest_file="${D}${sysconfdir}/ssl/certs/${PN}/${file}"

        dest_dir="$(dirname "$dest_file")"
        
        bbdebug 1 "Installing -d $dest_dir"
        mkdir -p "$dest_dir"
        install -d "$dest_dir"
        
        install -m 0644 "${MINICA_ROOT_DIR}$file" "$dest_file"
        bbdebug 1 "installing $dest_file"
	done
}

pkg_postinst_${PN}_class-target() {
#!/bin/sh
    if [ "x$D" != "x" ]; then
        # When installing to the target filesystem during the build, do nothing
        exit 0
    fi

    # Commands to run on the target system after package installation
    echo "Running post-install script for ${PN}"
    
    /usr/sbin/update-ca-certificates
}

PACKAGESPLITFUNCS:prepend = "gencert_populate_packages "

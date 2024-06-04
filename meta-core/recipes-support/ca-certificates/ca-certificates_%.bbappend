inherit minica

do_install_append() {
	files=$(find ${MINICA_ROOT_DIR} -type f -name '*.crt' | sed "s|^${MINICA_ROOT_DIR}||" | sort)
	
	for file in $files; do
        dest_dir="${D}${datadir}/ca-certificates/${file}"
        
        mkdir -p "$(dirname "$dest_dir")"
        install -m 0644 "${MINICA_ROOT_DIR}$file" "$dest_dir"
	done
	
    find ${MINICA_ROOT_DIR} -type f -name '*.crt' | sed "s|^${MINICA_ROOT_DIR}||" | sort >> ${D}${sysconfdir}/ca-certificates.conf
}
inherit go

DEPENDS_GORICE ?= ""
DEPENDS_GORICE_class-target = "go-rice-native"
DEPENDS_GORICE_class-native = "go-rice-native"

DEPENDS:append = " ${DEPENDS_GORICE}"

export RICE = "${STAGING_BINDIR_NATIVE}/rice"

RICE_ARGS ?= ''

# Either 'go' || 'syso'
GO_RICE_EMBEDTYPE ?= ''

# Non-empty triggers zip-append.
GO_RICE_APPEND ?= ''
GO_RICE_FILTEROUT ?= '.a$|.so$'

go_list_executables() {
	${GO} list -f '{{.Target}}' ${GOBUILDFLAGS} ${GO_INSTALL} | \
		egrep -v '${GO_RICE_FILTEROUT}' | \
		awk '{ print $1}'
}

go_do_compile:prepend() {
	# Issue a `go get` with the proper modcache before executing rice.
	export TMPDIR="${GOTMPDIR}"
	if [ -n "${GO_INSTALL}" ]; then
		if [ -n "${GO_LINKSHARED}" ]; then
			${GO} get -d ${GOBUILDFLAGS} `go_list_packages`
			rm -rf ${B}/bin
		fi
		${GO} get -d ${GO_LINKSHARED} ${GOBUILDFLAGS} `go_list_packages`
	fi

        # Execute rice to generate boxes for the packages
	if [ -n "${GO_RICE_EMBEDTYPE}" ]; then
		${RICE} ${RICE_ARGS} -i ${GO_IMPORT} embed-${GO_RICE_EMBEDTYPE}
	fi
	
}

go_do_compile:append() {
	if [ -n "${GO_RICE_APPEND}" ]; then
		go_list_executables | while read app; do
			${RICE} ${RICE_ARGS} -i ${GO_IMPORT} append --exec $app
		done
	fi
}


# Appends the forfunQ88 kernel configurations to the SRC_URI.
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_forfun-q88-tablet = " file://drm.cfg file://axp20x.cfg file://pcf8563.cfg file://rtl8188.cfg "

KERNEL_MODULE_AUTOLOAD_append_forfun-q88-tablet = "bluetooth rtl8188eu"
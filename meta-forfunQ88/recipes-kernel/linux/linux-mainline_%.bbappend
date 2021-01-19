# Appends the forfunQ88 kernel configurations to the SRC_URI.
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_forfun-q88-tablet = " \
    file://drm.cfg \
    file://axp20x.cfg \
    file://pcf8563.cfg \
    file://rtl8188.cfg \
    file://backlight-pwm.cfg \
    file://touchscreen.cfg \
"

#    file://forfun-q88-tablet.dts;subdir=linux-${PV}/arch/${ARCH}/boot/dts/

KERNEL_MODULE_AUTOLOAD_append_forfun-q88-tablet = " bluetooth rtl8188eu "
KERNEL_MODULE_PROBECONF_append_forfun-q88-tablet = " bluetooth rtl8188eu "

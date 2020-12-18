FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# overlays are activated in bsp bootfiles, by appending dtoverlay to the config.
SRC_URI += "file://hx711.cfg \
            file://hx711-rocketstand-overlay.dts;subdir=git/arch/${ARCH}/boot/dts/overlays \
"

KERNEL_DEVICETREE += "overlays/hx711-rocketstand.dtbo "

KERNEL_MODULE_AUTOLOAD  += "iio-trig-sysfs "
KERNEL_MODULE_PROBECONF += "iio-trig-sysfs "

KERNEL_MODULE_AUTOLOAD += "bcm2835-v4l2 "
KERNEL_MODULE_PROBECONF += "bcm2835-v4l2 "

KERNEL_MODULE_AUTOLOAD += "iio_hwmon "
KERNEL_MODULE_PROBECONF += "iio_hwmon "

KERNEL_MODULE_AUTOLOAD += "i2c-dev "
KERNEL_MODULE_PROBECONF += "i2c-dev "



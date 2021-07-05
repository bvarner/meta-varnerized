FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# overlays are activated using the bsp bootfiles, where the config.txt gets appended.
SRC_URI += "file://pigaragedoor-overlay.dts;subdir=git/arch/${ARCH}/boot/dts/overlays \
"

KERNEL_DEVICETREE += "overlays/pigaragedoor.dtbo "

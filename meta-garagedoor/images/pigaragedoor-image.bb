SUMMARY = "PiGarageDoor - A relay controlled raspberry pi garage door opener."
HOMEPAGE = "https://github.com/bvarner/meta-varnerized"
LICENSE = "MIT"

KERNEL_DEVICETREE += " overlays/pigaragedoor.dtbo"

include images/varnerized-raspberrypi.bb

IMAGE_INSTALL += " \
    pigaragedoor \
"

export IMAGE_BASENAME = "pigaragedoor-image"

SUMMARY = "PiGarageDoor - A relay controlled raspberry pi garage door opener."
HOMEPAGE = "https://github.com/bvarner/meta-varnerized"
LICENSE = "MIT"

include images/core-image-raspberrypi0-wifi.bb

IMAGE_INSTALL += " \
    pigaragedoor \
"

export IMAGE_BASENAME = "pigaragedoor-image"

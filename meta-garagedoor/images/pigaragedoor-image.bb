SUMMARY = "PiGarageDoor - A relay controlled raspberry pi garage door opener."
HOMEPAGE = "https://github.com/bvarner/meta-varnerized"
LICENSE = "MIT"

include images/varnerized-raspberrypi.bb

IMAGE_INSTALL += " \
	udev-rules-garagedoor \
    pigaragedoor \
"

export IMAGE_BASENAME = "pigaragedoor-image"

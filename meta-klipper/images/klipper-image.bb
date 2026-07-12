SUMMARY = "Raspberry Pi 4B image with Klipper host and MCU firmware tooling"
HOMEPAGE = "https://github.com/bvarner/meta-varnerized"
LICENSE = "MIT"

SDIMG_ROOTFS_TYPE = "ext4"
IMAGE_FSTYPES += "wic wic.bmap"
WKS_FILE = "sdimage-raspberrypi.wks"

DEPENDS += "rpi-bootfiles"

IMAGE_FEATURES += "ssh-server-openssh"
EXTRA_IMAGE_FEATURES += "debug-tweaks"

include recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += " \
    ${MACHINE_EXTRA_RRECOMMENDS} \
    git \
    iw \
    klipper \
    klipper-mcu-tools \
    nano \
    python3-cffi \
    python3-curses \
    python3-greenlet \
    python3-jinja2 \
    python3-pyserial \
    tzdata \
    udev \
    udev-rules-rpi \
    wpa-supplicant \
    wireless-regdb \
    packagegroup-core-sdk \
"

export IMAGE_BASENAME = "klipper-image"

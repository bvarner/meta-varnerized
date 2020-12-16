SUMMARY = "An image to bring up a Raspberry Pi Zero W With decent driver support."
HOMEPAGE = "https://github.com/bvarner/meta-varnerized"
LICENSE = "MIT"

SDIMG_ROOTFS_TYPE = "ext4"
IMAGES_FSTYPES += "wic wic.bmap"
WKS_FILE = "sdimage-raspberrypi-persistentvar.wks"

DEPENDS += "bootfiles"

IMAGE_LINGUAS = "en-us"

IMAGE_FEATURES += "read-only-rootfs"
IMAGE_FEATURES_remove += "splash"

include recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += " \
    ${MACHINE_EXTRA_RRECOMMENDS} \
    kernel-modules \
    udev-rules-rpi \
    tzdata \
    iw \
    linux-firmware-ralink \
    linux-firmware-rtl8192ce \
    linux-firmware-rtl8192cu \
    linux-firmware-rtl8192su \
    wpa-supplicant \
    wireless-regdb \    
"

disable_gettys() {
    echo "foo is : ${foo}"
}

set_local_timezone() {
    ln -sf /usr/share/zoneinfo/UTC ${IMAGE_ROOTFS}/etc/localtime
}

# Sets up an /etc/wpa_supplicant directory, where you can put configurations for 
# wpa_supplicant for your network devices. 
# Enables wpa_supplicant for 802.11 on wlan0
setup_wpa_supplicant() {
    # Configure the systemd unit
    mkdir -p ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants
    ln -sf /lib/systemd/system/wpa_supplicant-nl80211@.service ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/wpa_supplicant-nl80211@wlan0.service
    
    # Create the config directory and seed with a proper nl80211-wlan0 conf.
    mkdir -p ${IMAGE_ROOTFS}/etc/wpa_supplicant
    
    echo 'ctrl_interface=/var/run/wpa_supplicant' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
    echo 'ctrl_interface_group=0' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
    echo 'update_config=1' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
}

setup_certs() {
    echo "installing SSL certs..."
    mkdir -p ${IMAGE_ROOTFS}/etc/ssl/certs
    
    # Copy local pem files to....
    #cp /path/tofile/on/your/machine ${IMAGE_ROOTFS}/etc/ssl/certs/pi-launch-control.pem
    #cp /path/tofile/on/your/machine ${IMAGE_ROOTFS}/etc/ssl/certs/pi-launch-control-key.pem
}

ROOTFS_POSTPROCESS_COMMAND += " \
    set_local_timezone ; \
    setup_wpa_supplicant ; \
    disable_gettys ; \
    setup_certs ;\
"

export IMAGE_BASENAME = "core-image-raspberrypi0-wifi-image"


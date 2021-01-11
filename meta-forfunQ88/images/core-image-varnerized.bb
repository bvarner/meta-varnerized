SUMMARY = "An image to bring up a Device with some additional packages"
HOMEPAGE = "https://github.com/bvarner/meta-varnerized"
LICENSE = "MIT"

IMAGE_LINGUAS = "en-us"

include recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += " \
    ${MACHINE_EXTRA_RRECOMMENDS} \
    ${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS} \
    kernel-modules \
    tzdata \
    iw \
    wpa-supplicant \
    wireless-regdb \
    dtc \
    i2c-tools \
    usbutils \
"

disable_gettys() {
    if [ -n "${disable_getty}" ]; then
        echo "Disabling getty via /etc/systemd/logind.conf"
        echo 'NAutoVTs=0' >> ${IMAGE_ROOTFS}/etc/systemd/logind.conf
        echo 'ReserveVT=0' >> ${IMAGE_ROOTFS}/etc/systemd/logind.conf
    fi
}

set_local_timezone() {
    if [ -n "${timezone}" ]; then
        echo "Setting timezone to: ${timezone}"
        ln -sf "/usr/share/zoneinfo/${timezone}" ${IMAGE_ROOTFS}/etc/localtime
    else
        ln -sf /usr/share/zoneinfo/UTC ${IMAGE_ROOTFS}/etc/localtime
    fi
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
    cp ${IMAGE_ROOTFS}/etc/wpa_supplicant.conf ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
    
    # If there are environment variables set for the ssid and psk, use them.
    if [ -n "${ssid}" ] && [ -n "{psk}" ]; then
        echo 'network={' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    ssid="${ssid}"' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    psk="${psk}"' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '}' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
    fi    
}

setup_certs() {
    echo "installing SSL certs..."
    mkdir -p ${IMAGE_ROOTFS}/etc/ssl/certs
}

ROOTFS_POSTPROCESS_COMMAND += " \
    set_local_timezone ; \
    setup_wpa_supplicant ; \
    disable_gettys ; \
    setup_certs ; \
"

export IMAGE_BASENAME = "core-image-varnerized"


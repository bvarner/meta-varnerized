SUMMARY = "An image for running Pi-Launch-Control on a raspberry Pi Zero W."
HOMEPAGE = "https://bvarner.github.io"
LICENSE = "MIT"

KERNEL_MODULE_AUTOLOAD += "bcm2835-v4l2"
KERNEL_MODULE_PROBECONF += "bcm2835-v4l2"
KERNEL_DEVICETREE += " overlays/hx711-rocketstand.dtbo"

include images/varnerized-raspberrypi.bb

IMAGE_INSTALL += " \
    i2c-tools \
    v4l-utils \
    pi-launch-control \
"

# Sets up an /etc/wpa_supplicant directory, where you can put configurations for 
# wpa_supplicant for your network devices. 
# Enables wpa_supplicant for 802.11 on wlan0
setup_wpa_supplicant_append() {
    # Hackup the wlan.network to setup wlan0 as a DHCP server with a static IP address.
    rm ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    touch ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network

    echo '[Match]' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'Name=wlan0' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo '[Network]' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'Address=192.168.1.1/24' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'DHCPServer=yes' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo '[DHCPServer]' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'PoolOffset=50' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'PoolSize=50' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'DefaultLeaseTimeSec=900s' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'EmitDNS=no' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'EmitNTP=no' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'EmitRouter=no' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network
    echo 'EmitTimezone=no' >> ${IMAGE_ROOTFS}/etc/systemd/network/wlan.network

    # If the SSID is null, setup a "RocketStand" adhoc network.
    if [ -z "${ssid}" ]; then
        # Update the wpa_supplicant to create an adhoc base station known as 'RocketStand'.
        echo 'ap_scan=2' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo 'network={' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    ssid="RocketStand"' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    mode=2' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    frequency=2432' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    proto=RSN' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    key_mgmt=WPA-PSK' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    pairwise=CCMP' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        echo '    group=CCMP' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
    
        # Use the psk if set, otherwise, use 'ignition'.
        if [ -n "${psk}" ]; then
            echo '    psk="${psk}"' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        else
            echo '    psk="ignition"' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
        fi
        echo '}' >> ${IMAGE_ROOTFS}/etc/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
    fi
}

export IMAGE_BASENAME = "rocketstand-image"

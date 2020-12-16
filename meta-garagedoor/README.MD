# Pigaragedoor - Controll a Garage Door from a Raspberry Pi.
A bit bake layer to build a [Raspberry Pi Garage Door Opener](https://www.instructables.com/Raspberry-Pi-Garage-Door-Opener/) from this instructable,
with some improved software with this [pigaragedoor daemon](https://github.com/bvarner/pigaragedoor), and a more robust embedded linux distro at the base.

## Quick Build Instructions (Ubuntu 20.10)

For a raspberrypi0-wifi machine: (other machines can use other kas files or you can customize your own)

1. Get the tools
   ```
   sudo apt-get install bmap-tools git python3-pip
   sudo pip3 install kas
   mkdir /opt/kas-builds
   cd /opt/kas-builds
   ```
2. Get the repo & Configure your network `ssid` and `psk`
   ```
   git clone https://github.com/bvarner/meta-varnerized.git
   nano meta-varnerized/kas/local-env.yml
   ```
3. Run the build:
   ```
   kas build meta-varnerized/kas/pigaragedoor-raspberrypi0-wifi-kas.yml
   ```
5. Copy the Image to an SD card (device `/dev/mmcblk0`)
   ```
   sudo umount /dev/mmcblk0p*
   cd build/tmp/deploy/images/raspberrypi0-wifi
   sudo bmaptool copy --bmap pigaragedoor-image-raspberrypi0-wifi.wic.bmap pigaragedoor-image-raspberrypi0-wifi.wic.bz2 /dev/mmcblk0
   ```

## Configuration options
`../kas/local-env.yml` contains the following entries that may be of interest.

* `ssid` - The name of a wireless network to join.
* `psk`  - The passphrase for that network.
* `timezone` - The name of a timezone from the [tz database](https://en.wikipedia.org/wiki/List_of_tz_database_time_zones)

If both `ssid` and `psk` are defined, wpa supplicant will be configured to join that network with DHCP.
If a wired ethernet port is connected, DHCP will be used for that as well.

### Setting the hostname
By default mDNS will be setup for `garage-door.local` and advertised on the local network link.

The hostname can be set to an alternative value by editing `../kas/pigaragedoor-raspberrypi-kas.yml` and changing the following line:
```
    hostname_pn_base-files = "garage-door"
```

For example:
```
    hostname_pn_base-files = "shop-door-bay1"
```

## Building
Build this project using `kas`, as described in the [meta-varnerized README](../README.md), using any of the `pigaragedoor-<machine>-kas.yml` files and customizing to your liking.

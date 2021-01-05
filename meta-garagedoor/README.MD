# Pigaragedoor - Controll a Garage Door from a Raspberry Pi.
A bit bake layer to build a [Raspberry Pi Garage Door Opener](https://www.instructables.com/Raspberry-Pi-Garage-Door-Opener/) from this instructable,
with some improved software with this [pigaragedoor daemon](https://github.com/bvarner/pigaragedoor), and a more robust embedded linux distro at the base.

## Quick Build Instructions (Tested with Ubuntu 20.10)

For a raspberrypi0-wifi machine: (other machines can use other kas files or you can customize your own)

1. Get the tools
   ```
   sudo apt-get install chrpath diffstat bmap-tools git python3-pip
   sudo pip3 install kas
   sudo mkdir /opt/kas-builds
   sudo chmod a+rw /opt/kas-builds
   cd /opt/kas-builds
   ```
2. Get the repo & Configure your network `ssid`, `psk`, and `timezone`. Tell git to not track local changes.
   ```
   git clone https://github.com/bvarner/meta-varnerized.git
   nano meta-varnerized/kas/local-env.yml
   cd meta-varnerized
   git update-index --skip-worktree kas/local-env.yml
   cd ..
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
* `PIGARAGEDOOR_CERT` - Absolute path to a certificate file for SSL.
* `PIGARAGEDOOR_CERT_KEY` - Absolute path to the keyfile for SSL cert.

If both `ssid` and `psk` are defined, wpa supplicant will be configured to join that network with DHCP.
If a wired ethernet port is connected, DHCP will be used for the wired network as well.

## SSL Configuration
1. Create a cert / key pair with your favorite tool of choice. 
   I highly recommend [minica](https://github.com/jsha/minica), which you can use as follows:
   ```
   sudo apt-get install golang
   cd /ANY/PATH
   git clone https://github.com/jsha/minica.git
   go install
   minica --domains garage-door.local
   ```
2. Update the `kas/local-env.yml` with the absolute paths to your cert & key.
   ```
   cd /opt/kas-builds
   nano meta-varnerized/kas/local-env.yml
   ```
   Adding the lines like this:
   ```
     PIGARAGEDOOR_CERT: "/ANY/PATH/garage-door.local/cert.pem"
     PIGARAGEDOOR_CERT_KEY: "/ANY/PATH/garage-door.local/key.pem"
   ```
3. Run the kas build and copy the Image as in the Quick Build Instructions.
4. You'll need to setup your devices to trust the 'root' certificate in `/ANY/PATH/minica.pem`. For some systems, this may require the root certificate to be in a DER format CRT file.
   ```
   sudo apt-get install openssl
   openssl x509 -outform der -in /ANY/PATH/minica.pem -out /ANY/PATH/minica.crt
   ```
   On iOS devices, visiting the URL will enable you to 'trust' the certificate presented when you access `https://garage-door.local`.

### Setting the hostname
By default mDNS will be setup for `garage-door.local` and advertised on the local network link.

The hostname can be set to an alternative value by editing `../kas/pigaragedoor-raspberrypi-kas.yml` and changing the following line:
```
    hostname_pn-base-files = "garage-door"
```

For example:
```
    hostname_pn-base-files = "shop-door"
```

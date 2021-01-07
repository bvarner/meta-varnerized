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
4. Copy the Image to an SD card (device `/dev/mmcblk0`)
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
* `pigaragedoor_CERTDIR` - Absolute path to a directory containing a cert.pem and key.pem pair for SSL.

If both `ssid` and `psk` are defined, wpa supplicant will be configured to join that network with DHCP.
If a wired ethernet port is connected, DHCP will be used for the wired network as well.

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

### SSL Configuration
Keep in mind that the SSL certificate will need to match the hostname. In the example below, we assume the default `garage-door` hostname is being used, and will result in being advertised on your local network as `garage-door.local`.
1. Create a cert / key pair with your favorite tool of choice. 
   I highly recommend [minica](https://github.com/jsha/minica), which can be used as follows.
   ```
   sudo apt-get install golang
   cd ~
   git clone https://github.com/jsha/minica.git
   cd minica
   go install
   mkdir ~/certificates
   cd ~/certificates
   ~/go/bin/minica --domains garage-door.local
   ```
   This gets the `go` lang tools, gets the source for minica, and builds / installs the tool into `~/go/bin`. Then we create a directory to store certificates in within the home directory, and execute the command there to create a root cert (you can trust on all your devices) and then issue a new purpose-specific cert from it.
2. Update the `kas/local-env.yml` with the absolute path to the directory containing your generated cert.pem and key.pem.
   ```
   cd /opt/kas-builds
   nano meta-varnerized/kas/local-env.yml
   ```
   Include a line such as:
   ```
     pigaragedoor_CERTDIR: "/home/YOUR_USERNAME/certificates/garage-door.local"
   ```
   The path _must_ be absolute, or this will not work properly.
3. Run the kas build and copy the Image as in the Quick Build Instructions steps 3 and 4. 
4. You may need to setup your devices to trust the 'root' certificate in `/home/YOUR_USERNAME/certificates/minica.pem`. For some systems, this may require the root certificate to be in a DER format CRT file.
   ```
   sudo apt-get install openssl
   cd ~/certificates
   openssl x509 -outform der -in ./minica.pem -out ./minica.crt
   ```
   On iOS devices, visiting the URL will enable you to 'trust' the certificate presented when you access `https://garage-door.local`, which should be sufficient for this use.

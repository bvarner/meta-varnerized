# Rocketstand - Raspberry Pi rocket engine testing.
This is a bitbake layer to build images for running [pi-launch-control](https://github.com/bvarner/pi-launch-control).

pi-launch-control exposes REST endpoints and a basic web-based UI for controlling, measuring, and documenting model rocket engine tests.


The recipes in this layer configure a `rocketstand-image` that will self-publish itself on a wifi network as `https://rocketstand.local`.
Network configuration can be set to join an existing wifi network, or in the case that your test location may not be in range of a base-station, the device itself can setup an adhoc network and dhcp server, allowing control devices (phone, tablet, laptop) to connect directly to the devices wifi. You get to choose how this works at build-time.

In practice I've found it highly beneficial to have the ad-hoc network settings so that I can keep the engine and control box at a safe distance. :-)

## Quick Build Instructions (Tested with Ubuntu 20.10)
These directions assume you'll be using [minica](https://github.com/jsha/minica) to manage self-signed SSL certificates for your image.

1. Get the tools, create the SSL certs (feel free to skip the steps about minica and certificates if you don't want SSL)
   ```
   sudo apt-get instlal chrpath diffstat bmap-tools git python3-pip golang
   sudo pip3 install kas
   sudo mkdir /opt/kas-builds
   sudo chmod a+rw /opt/kas-builds
   cd /opt/kas-builds

   git clone https://github.com/jsha/minica.git
   cd /opt/kas-builds/minica
   go install

   mkdir -p /opt/kas-builds/certificates
   cd /opt/kas-builds/certificates
   ~/go/bin/minica --domains rocketstand.local
   ```
2. Fetch the meta-varnerized layer repository, and customize your `kas/local-env.yml`.
   You'll need to add things like:
       * The absolute path to the directory containing the certs created at a the end of step 1.
       * Any SSID or PSK for your wifi network if you want to join a network vs. create one at the test stand location.
       * Your timezone for the device image.
   ```
   cd /opt/kas-builds
   git clone https://github.com/bvarner/meta-varnerized.git
   nano meta-varnerized/kas/local-env.yml
   ```
   To configure the SSL certs, add a line such as:
   ```
   rocketstand_CERTDIR: "/opt/kas-builds/certificates/rocketstand.local"
   ```
   *Optional*: Tell git to ignore tracking changes for your local-env.
   ```
   cd meta-varnerized
   git update-index --skip-worktree kas/local-env.yml
   ```
3. Run the build:
   ```
   cd /opt/kas-builds
   kas build meta-varnerized/kas/rocketstand-kas.yml
   ```
4. Copy the resulting image to an SD card (device `/dev/mmcblk0` in this example)
   ```
   sudo umount /dev/mmcblk0*
   sudo bmaptool copy --bmap build/tmp/deploy/images/raspberrypi0-wifi/rocketstand-image-raspberrypi0-wifi.wic.bmap build/tmp/deploy/images/raspberrypi0-wifi/rocketstand-image-raspberrypi0-wifi.wic.bz2 /dev/mmcblk0
   ```


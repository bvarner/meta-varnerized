# Pigaragedoor - Controll a Garage Door from a Raspberry Pi.
A bit bake layer to build a [Raspberry Pi Garage Door Opener](https://www.instructables.com/Raspberry-Pi-Garage-Door-Opener/) from this instructable,
with some improved software with this [pigaragedoor daemon](https://github.com/bvarner/pigaragedoor), and a more robust embedded linux distro at the base.

## Configuration options `../kas/local-env.yml`
The following entries in the `local-env.yml` can be customized for this image.

* `ssid` - The name of a wireless network to join.
* `psk`  - The passphrase for that network.

If both `ssid` and `psk` are defined, the wireless adapter on your raspberry pi (if one is available) will join the network, obtain an address with DHCP, and advertise itself on the network.

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

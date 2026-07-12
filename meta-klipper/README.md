# meta-klipper

Klipper host and MCU firmware tooling for kirkstone-based Raspberry Pi images.

## What this layer provides

- A Klipper host package that installs the upstream source tree and a systemd service.
- An MCU helper command for `make menuconfig`, `make`, and flashing the built firmware.
- A Pi 4 kirkstone image recipe that pulls in the host stack and the MCU build toolchain.

## Intended use

This layer is meant to work with:

- `meta-raspberrypi`
- `meta-openembedded` (`meta-python`, `meta-networking`, `meta-oe`)
- `meta-arm` (`meta-arm-toolchain`)
- `poky`

The default image target is `raspberrypi4-64`.

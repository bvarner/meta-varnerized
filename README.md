# meta-varnerized
A bitbake meta-layer for projects I cook up.

## Building things you see here
I maintain [kas](https://kas.readthedocs.io) configurations to build the various projects in this repo, targeting the hardware I use.

If you'd like to use those as a basis for your own customizations, I wholly endorse that.

The process looks something like this:
1. Create a directory to work within, and install Kas from your distribution package manager, or from PiPy.
   ```
   mkdir kas-builds
   cd kas-builds
   sudo pip3 install kas
   ```
2. Clone this repo.
   ```
   git clone https://github.com/bvarner/meta-varnerized
   ```
3. Customize your `kas/local-env.yml` to set ssids, psk passphrases, etc. and tell git to ignore those changes.
   It's totally OK to use your favorite text editor here, instead of nano.
   ```
   nano meta-varnerized/kas/local-env.yml
   git update-index --skip-worktree meta-varnerized/kas/local-env.yml
   ```
4. Run the build.
   ```
   kas build meta-varnerized/kas/the-file-you-want-to-build.yml
   ```
5. Copy the resulting image to an SD card.
   ```
   cd build/tmp/deploy/images/<machine>/
   bmaptool copy --bmap <image_filename>.bmap <image_filename>.bz2 /path/to/mmc/block/device
   ```

** Further details and documentation may be found in the `meta-<project>` directories.**

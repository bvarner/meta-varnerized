# meta-varnerized
A bitbake meta-layer for projects I cook up.

## Building things you see here
I maintain [kas](https://kas.readthedocs.io) configurations to build the various projects in this repo, targeting the hardware I use.

If you'd like to use those as a basis for your own customizations, I wholly endorse that.

I build things in docker containers, with my volumes mapped, and my sshd agent mapped so that I don't have to do strange things to get my ssh keys to work for git remotes.

The process looks something like this:
1. Create a directory to work within, and install Kas from your distribution package manager, or from PiPy.
   ```
   mkdir kas-builds
   cd kas-builds
   ```
2. Clone this repo. I tend to prefix my repos with a number so I can set a specific include precedence in kas. I've kinda settled on 12 for this one.
   ```
   git clone https://github.com/bvarner/meta-varnerized 12-meta-varnerized
   ```
3. Customize your `kas/local-env.yml` to set ssids, psk passphrases, etc. and tell git to ignore those changes.
   It's totally OK to use your favorite text editor here, instead of nano.
   ```
   nano 12-meta-varnerized/kas/local-env.yml
   git update-index --skip-worktree 12-meta-varnerized/kas/local-env.yml
   ```
4. Build the container. Yes, I use podman.
   ```
   podman build -f 12-meta-varnerized/kas/dockerfile -t kasbuild:latest --build-arg USER_NAME=$(whoami) --build-arg host_uid=$(id -u) --build-arg host_gid=$(id -g) ./20-meta-spcd/kas
   ```
5. Run the container.
   ```
   podman run -it --rm --name kasbuild --workdir /opt/kas-build --userns=keep-id -v "$HOME:/home/$(whoami)/host_home:Z" -v "/opt/kas-build:/opt/kas-build:Z" -v "$SSH_AUTH_SOCK:/run/ssh-agent.sock:Z" -e SSH_AUTH_SOCK="/run/ssh-agent.sock" kasbuild:latest
   ```
6. Run the build.
   ```
   kas build 12-meta-varnerized/kas/the-file-you-want-to-build.yml
   ```
7. Copy the resulting image to an SD card.
   ```
   cd build/tmp/deploy/images/<machine>/
   bmaptool copy --bmap <image_filename>.bmap <image_filename>.bz2 /path/to/mmc/block/device
   ```

**Further details and documentation may be found in the `meta-<project>` directories.**

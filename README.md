# FixPhantoms
A simple plugin to prevent phantoms from spawning in mushroom biomes.

[SpigotMC plugin page](https://www.spigotmc.org/members/lordcre_.21806/)

## Configuration
``` yaml
# Whitelist of worlds to enable the plugin in. Includes "world" by default.
enabledWorlds:
- world
```

## Commands
`/fixphantoms reload`: Reload options from the configuration file

## Permissions
``` yaml
fp.reload:
  description: Reload the plugin config
  default: op
```

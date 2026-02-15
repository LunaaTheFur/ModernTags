# ModernTags

![Failed to load image](images/preview.png)

A modern plugin for displaying custom tags above player heads using Text Display entities.

## Core Features

- **Tag Customization** - full control over tag appearance (color, shadows, transparency, alignment, size, etc.)
- **Animated Tags** - support for multiple frames with configurable transition speed
- **Priority System** - automatic tag selection based on player permissions with priority consideration
- **PlaceholderAPI Integration** - support for any PAPI placeholders
- **Vault Integration** - automatic prefix and suffix insertion
- **Flexible Update Settings** - separate configuration for frame and placeholder update rates

## Technical Advantages

The plugin operates fully asynchronously in Netty threads via PacketEvents. All data is cached in memory for fast
access during repeated operations, ensuring high performance even on servers with a large number of players.

## Commands and Permissions

### Commands

- `/moderntags` - reload plugin configuration

### Permissions

- `moderntags.tag.<name>` - permission to use a specific tag (generated automatically)
- `moderntags.see.own` - visibility of own tag
- `moderntags.see.other` - visibility of other players' tags
- `moderntags.reload` - access to reload command

## Placeholders

The plugin supports the following built-in placeholders:

- `<placeholder:prefix>` - player prefix from Vault
- `<placeholder:suffix>` - player suffix from Vault
- `<placeholder:name>` - player name
- `<placeholder:display_name>` - player display name
- `<placeholder:health>` - player health

### PlaceholderAPI

To use placeholders from PlaceholderAPI, use the format:

```
<placeholder:EXPANSION_PLACEHOLDER>
```

Examples:

- `<placeholder:player_level>` - player level
- `<placeholder:vault_rank>` - player rank
- `<placeholder:player_ping>` - player ping

## Configuration

### General Settings

```yaml
# Use MiniMessage for formatting prefixes and suffixes from Vault
use-minimessage-colorizer-for-prefixes-and-suffixes: false

# Hide tag when player has passengers (e.g., another player)
hide-tag-when-has-passenger: false
```

### Tag Configuration

```yaml
name-tags:
  default: # Default tag (required)
    frame-update-rate: 10  # Frame transition rate (in ticks), -1 to disable
    placeholders-update-rate: 10  # Placeholder update rate (in ticks)
    priority: 0  # Tag priority (higher value = higher priority)
    frames:
      0: # First frame
        text: |-
          <placeholder:prefix><white><placeholder:name><placeholder:suffix>
          <red>❤ <white><placeholder:health>
        # Display parameters
        shadowed: true
        y-offset: 0.2
        background-color: "00000000"

  vip: # Additional tag for VIP players
    frame-update-rate: 20
    placeholders-update-rate: 5
    priority: 10  # Higher priority than default
    frames:
      0:
        text: "<gold>⭐ VIP ⭐<newline><placeholder:name>"
        shadowed: true
        scale: 1.2
```

### Frame Parameters

#### Text and Positioning

- `text` - tag text (supports MiniMessage formatting and placeholders)
- `x-offset` - X-axis offset (number)
- `y-offset` - Y-axis offset (number, default: 0.2)
- `z-offset` - Z-axis offset (number)
- `scale` - tag scale (number, default: 1.0)

#### Visual Effects

- `shadowed` - text shadow (true/false)
- `shadow-radius` - shadow radius (number)
- `shadow-strength` - shadow intensity (number)
- `see-through` - visibility through blocks (true/false)
- `background-color` - background color in HEX format (#RRGGBB or #AARRGGBB) or number
- `text-opacity` - text opacity (number from -1 to 255)
- `default-background` - standard Minecraft background (true/false)

#### Display Settings

- `alignment` - text alignment (LEFT, CENTER, RIGHT)
- `line-width` - maximum line width (number, default: 200)
- `vertical-billboard` - vertical billboard (true/false)
- `view-range` - view distance (number, default: 1.0)
- `brightness` - brightness in "type-strength" format, for example:
    - `block-15` - maximum block brightness
    - `sky-15` - maximum sky brightness

### Animated Tag Example

```yaml
name-tags:
  animated:
    frame-update-rate: 20  # Frame change every second
    placeholders-update-rate: 5
    priority: 5
    frames:
      0:
        text: "<gradient:red:yellow>✦ <placeholder:name> ✦"
        scale: 1.0
      1:
        text: "<gradient:yellow:red>✧ <placeholder:name> ✧"
        scale: 1.1
      2:
        text: "<gradient:red:yellow>✦ <placeholder:name> ✦"
        scale: 1.0
```

## Dependencies

### Required

- **PacketEvents** - for packet handling

### Optional

- **PlaceholderAPI** - for using placeholders from other plugins
- **Vault** - for working with prefixes and suffixes

## Support

- **Minecraft versions**: 1.19.4+
- **Platforms**: Paper, Purpur and Paper forks

## License

[Apache License Version 2.0, January 2004](LICENSE)

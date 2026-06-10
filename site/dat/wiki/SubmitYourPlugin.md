# Submitting Your Plugin to the Marketplace

If you have published a JMeter plugin and want it listed in the
[Plugins Manager](/wiki/PluginsManager/) catalogue, send a pull request to the
[project repository](https://github.com/undera/jmeter-plugins). You add a small descriptor
that points at your released artifact — the plugin code itself stays in your own repository.

## What to change

- **New plugin:** append one entry to `site/dat/repo/various.json` (the array of community
  plugins). Do not create a new `.json` file.
- **New version of a plugin already listed:** add a key to that plugin's existing `versions`
  map.

For the full list of descriptor fields and a complete example, see
[Plugin Repository Descriptor Format](/wiki/PluginRepositoryDescriptorFormat/).

## Requirements

- **Stable download location.** Each version's `downloadUrl` must point at
  [Maven Central](https://central.sonatype.com/) or your project's GitHub releases — a public,
  permanent URL.
- **Version key matches the artifact.** The key in the `versions` map must match the version in
  the JAR filename, e.g. `1.0.4` ↔ `…/my-plugin-1.0.4.jar`.
- **Formatting.** Run `python3 format_repo.py` before committing. CI verifies this with
  `python3 format_repo.py --check`; the JSON must use 2-space indentation and end with a
  newline.
- **Real, reusable value.** The plugin should do something not already covered by a built-in
  JMeter feature or a plugin already in the catalogue.

Contributing plugin *code* to this project (rather than listing your own) is covered by the
[Developers Guidelines](/wiki/DeveloperGuide/).

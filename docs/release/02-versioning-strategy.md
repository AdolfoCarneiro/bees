# 02 — Versioning Strategy

## 1. Purpose

This document defines how Curious Bees versions should be named and interpreted.

The goal is clarity for:

```txt
- players
- modpack authors
- testers
- GitHub releases
- Modrinth/CurseForge uploads
- future Fabric support
```

## 2. Recommended Format

Use semantic-version-inspired versions with pre-release labels.

Pattern:

```txt
MAJOR.MINOR.PATCH[-stage.N]
```

Examples:

```txt
0.1.0-alpha.1
0.1.0-alpha.2
0.2.0-beta.1
0.2.1-beta.1
1.0.0
```

## 3. Meaning

### MAJOR

Increase when there are major breaking changes.

Example:

```txt
1.x -> 2.x if genome save format changes incompatibly after stable release
```

Before stable release, major should stay:

```txt
0
```

### MINOR

Increase when adding meaningful features.

Examples:

```txt
0.1.0 -> first genetics loop
0.2.0 -> production MVP
0.3.0 -> tech apiary MVP
0.4.0 -> data-driven content
```

### PATCH

Increase for bug fixes, balance adjustments, and small improvements.

Examples:

```txt
0.2.0 -> 0.2.1
```

### stage

Recommended stage labels:

```txt
alpha
beta
rc
```

Examples:

```txt
0.1.0-alpha.1
0.1.0-alpha.2
0.2.0-beta.1
1.0.0-rc.1
```

## 4. Suggested Version Milestones

### 0.1.0-alpha.1

First internal or public genetics proof.

Could include:

```txt
- NeoForge 1.21.1
- genome storage
- wild genomes
- breeding inheritance
- mutation
- analyzer text output
```

### 0.2.0-alpha.1

Basic production appears.

Could include:

```txt
- comb items
- simple species output
- productivity modifier
- debug production validation
```

### 0.3.0-beta.1

Tech apiary starts.

Could include:

```txt
- genetic apiary block
- basic frames
- controlled breeding/production
```

### 0.4.0-beta.1

Data-driven content foundation.

Could include:

```txt
- JSON-ready definitions
- built-in content migration
- validation layer
```

### 0.5.0-beta.1

Fabric experimental support.

Could include:

```txt
- Fabric module
- cross-loader parity work
```

### 1.0.0

Stable NeoForge release.

Requires:

```txt
- stable save/load behavior
- usable analyzer
- basic production
- known issues under control
- release docs complete
```

## 5. Minecraft Version in Artifact Names

The mod version should not need Minecraft version embedded in semantic version, but the jar file should include it.

Recommended:

```txt
curious-bees-neoforge-1.21.1-0.1.0-alpha.1.jar
```

## 6. Loader in Artifact Names

Include loader in artifact names once multiple loaders exist.

Recommended:

```txt
curious-bees-neoforge-1.21.1-0.1.0-alpha.1.jar
curious-bees-fabric-1.21.1-0.5.0-beta.1.jar
```

## 7. Version Compatibility Notes

Every release should explicitly state:

```txt
Minecraft version
loader
loader version if known
world compatibility notes
known migration risks
```

Example:

```txt
This version targets Minecraft 1.21.1 and NeoForge.
Existing worlds from earlier alpha builds may not be fully compatible.
Back up worlds before testing.
```

## 8. Breaking Changes

Before version 1.0.0, breaking changes are allowed but must be documented.

After 1.0.0:

```txt
- avoid breaking genome serialization
- provide migration when possible
- document any incompatible save changes loudly
```

## 9. Release Tags

Git tags should match mod versions.

Examples:

```txt
v0.1.0-alpha.1
v0.2.0-beta.1
v1.0.0
```

## 10. Changelog Categories

Use consistent categories:

```txt
Added
Changed
Fixed
Removed
Known Issues
Migration Notes
```

Optional:

```txt
Internal
Developer Notes
```

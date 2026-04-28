# 03 — Platform Publishing Plan

## 1. Purpose

This document defines how Curious Bees should be published on GitHub Releases, Modrinth, and CurseForge.

This is not an automation document. It is a publishing process and metadata plan.

## 2. Publishing Order

Recommended order:

```txt
1. GitHub Release
2. Modrinth Version
3. CurseForge File
4. Update README links/badges if needed
5. Announce release
```

Reason:

```txt
GitHub should be the canonical technical release.
Modrinth and CurseForge should mirror the released artifact and changelog.
```

## 3. GitHub Releases

Use GitHub Releases for:

```txt
- release tag
- jar attachment
- release notes
- known issues
- source code transparency
```

GitHub releases are based on Git tags and can include release notes and binary assets. GitHub also supports draft releases and attaching compiled binaries to a release.

## 4. Modrinth Publishing

Use Modrinth for player-facing distribution.

Required metadata to prepare:

```txt
project name
slug
summary
description
license
categories
Minecraft version
loader
mod version
jar file
changelog
dependencies
client/server side metadata
```

Important Modrinth version metadata includes fields such as version name, version number, changelog, dependencies, and files. Version numbers should ideally follow semantic versioning.

## 5. CurseForge Publishing

Use CurseForge for player-facing distribution and modpack ecosystem compatibility.

Required metadata to prepare:

```txt
project name
summary
description
license
categories
logo
Minecraft version
loader tag
jar file
release type
changelog
related projects/dependencies
```

CurseForge distinguishes file release types such as Release, Beta, and Alpha. Release files sync to the CurseForge App by default; Beta and Alpha have different visibility/sync behavior. CurseForge file uploads include fields such as release type, changelog, supported game version, and related projects/dependencies.

## 6. Project Page Copy

The project page should make clear:

```txt
Curious Bees is a new bee genetics mod.
It is inspired by Forestry-style genetics.
It uses vanilla-style bees and breeding.
It is not a Forestry port.
It is not a Productive Bees fork.
It does not start as a resource bee mod.
```

## 7. Initial Platform Release Type

For the first public upload:

```txt
GitHub: Pre-release
Modrinth: Alpha
CurseForge: Alpha
```

Move to Beta only when:

```txt
- basic loop works reliably
- save/load is reasonably stable
- analyzer is usable
- known issues are documented
```

Move to Release only when:

```txt
- general users/modpack authors can reasonably rely on the mod
```

## 8. Dependencies

Initial dependency strategy:

```txt
NeoForge: required by platform
No required mod dependencies beyond loader/Minecraft initially
```

Do not claim optional compatibility until implemented and tested.

## 9. Client/Server Side

Curious Bees likely affects server gameplay and persistent entity data.

Initial metadata should assume:

```txt
Required on both client and server
```

Reason:

```txt
The mod adds gameplay logic and likely item/visual client data.
```

This can be refined later if parts are server-only or client-optional.

## 10. Categories / Tags

Recommended categories:

```txt
Technology
Magic? no, unless future content supports it
World Gen? no, unless spawn changes become significant
Utility? no
Food/Farming? maybe
Adventure? no
```

Better initial category language:

```txt
Technology
Farming
Mobs
Genetics/Breeding if platform supports it
```

Do not over-tag.

## 11. Screenshots and Images

Initial public release should ideally include:

```txt
project logo/icon
analyzer output screenshot
bee breeding screenshot
initial species/mutation diagram
basic production screenshot if implemented
```

Screenshots are not required for every alpha, but they help communicate the concept.

## 12. Moderation Readiness

Before submitting to CurseForge/Modrinth:

```txt
- description is clear
- license is selected
- no copyrighted assets are included
- no misleading claims
- mod works on the selected Minecraft/loader version
- file name and metadata match
```

## 13. Publishing Checklist Summary

```txt
1. Build jar.
2. Run smoke tests.
3. Create Git tag.
4. Draft GitHub release.
5. Upload jar.
6. Copy release notes to Modrinth.
7. Upload version on Modrinth.
8. Copy release notes to CurseForge.
9. Upload file on CurseForge.
10. Mark known issues.
11. Update README links.
```

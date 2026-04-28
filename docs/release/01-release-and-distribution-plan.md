# 01 — Release and Distribution Plan

## 1. Purpose

This document defines how Curious Bees should be distributed once the mod has a playable MVP.

Distribution is intentionally not part of the earliest implementation phases. The project should first prove:

```txt
genetics core -> initial content -> NeoForge bee storage -> vanilla breeding -> analyzer -> basic production
```

Only then should the mod be packaged for external users.

## 2. Release Philosophy

Curious Bees is a systems-first mod.

The first public release should not try to impress players with a huge amount of content. It should prove that the genetics loop works and that the project has a clear direction.

The first public release should be honest:

```txt
This is an early alpha/beta focused on the genetic breeding loop.
Content is intentionally small.
Resource bees are not part of the initial release.
Fabric support is planned for later.
```

## 3. Distribution Channels

Recommended channels:

```txt
GitHub Releases
Modrinth
CurseForge
```

### GitHub Releases

GitHub Releases should be used as the technical source of release history.

Use GitHub Releases for:

```txt
- release tags
- compiled jar attachment
- changelog
- known issues
- source transparency
- issue tracker links
```

### Modrinth

Modrinth should be one of the primary player-facing distribution platforms.

Use Modrinth for:

```txt
- public downloads
- project description
- loader/game version metadata
- dependency metadata
- changelog per version
```

### CurseForge

CurseForge should also be supported because many Minecraft players and modpacks still use it.

Use CurseForge for:

```txt
- public downloads
- modpack ecosystem support
- CurseForge App availability
- dependency metadata
- changelog per file
```

## 4. Recommended Release Stages

### Stage 0 — Internal Dev Builds

Audience:

```txt
developer only
```

Distribution:

```txt
local build artifacts
```

Purpose:

```txt
verify that the game launches and the mod loads
```

No public upload.

### Stage 1 — Private Test Builds

Audience:

```txt
developer + trusted testers
```

Distribution:

```txt
GitHub prerelease or direct jar sharing
```

Purpose:

```txt
validate basic gameplay loop
```

Do not publish to Modrinth/CurseForge yet unless the project page is intentionally hidden/experimental.

### Stage 2 — Public Alpha

Audience:

```txt
early testers
```

Distribution:

```txt
GitHub Releases
Modrinth
CurseForge
```

Purpose:

```txt
prove the mod concept and gather feedback
```

Recommended release type:

```txt
Alpha
```

Expected content:

```txt
- NeoForge 1.21.1 only
- five initial species
- basic genetics
- vanilla breeding integration
- analyzer MVP
- basic production
- known limitations clearly listed
```

### Stage 3 — Public Beta

Audience:

```txt
players willing to use the mod in small worlds/modpacks
```

Recommended release type:

```txt
Beta
```

Expected improvements:

```txt
- fewer known bugs
- better analyzer UX
- better production loop
- migration-safe genome serialization
- better config/logging
- no major world-corrupting issues known
```

### Stage 4 — Stable Release

Audience:

```txt
general players and modpacks
```

Recommended release type:

```txt
Release
```

Expected qualities:

```txt
- save/load stable
- update path documented
- core loop balanced enough
- major crashes fixed
- user-facing description and screenshots ready
```

Do not rush this stage.

## 5. First Public Alpha Definition

The first public alpha may release when all are true:

```txt
- NeoForge build runs on Minecraft 1.21.1.
- Vanilla bees can store genomes.
- Wild bees receive starter genomes.
- Breeding two bees creates a child with inherited genome.
- Mutations can produce Cultivated and Hardy.
- Analyzer can inspect active/inactive species and traits.
- Basic production behavior exists or is clearly marked experimental.
- Known issues are documented.
- License is selected.
- Project page description is written.
```

## 6. What Must Not Be Promised in the First Release

Do not promise:

```txt
- Fabric support in the first release
- resource bees
- huge species tree
- polished GUI
- advanced apiary automation
- compatibility with every major tech mod
- data-driven content authoring for players
- stable world migration guarantees before testing
```

If mentioned, these should be clearly labeled as future plans.

## 7. Release Artifact Naming

Recommended jar naming pattern:

```txt
curious-bees-neoforge-<minecraft_version>-<mod_version>.jar
```

Example:

```txt
curious-bees-neoforge-1.21.1-0.1.0-alpha.1.jar
```

Future Fabric example:

```txt
curious-bees-fabric-1.21.1-0.3.0-beta.1.jar
```

## 8. Release Metadata Needed

Each release should define:

```txt
mod version
Minecraft version
loader
loader version range
release type
changelog
known issues
breaking changes
dependencies
incompatibilities
download artifact
```

## 9. Required Pre-Release Review

Before any public upload:

```txt
1. Run tests.
2. Launch client.
3. Create a world.
4. Spawn/find bees.
5. Validate genome storage.
6. Breed bees.
7. Validate child genome.
8. Trigger/verify mutation with debug helpers.
9. Use analyzer.
10. Save/reload world.
11. Verify no obvious data loss.
12. Build final jar.
13. Draft release notes.
14. Mark release as alpha/beta honestly.
```

## 10. Distribution Go / No-Go Gate

### Go

Release can proceed if:

```txt
- build is reproducible
- game launches
- core loop works
- known issues are documented
- release type matches quality level
```

### No-Go

Do not release if:

```txt
- world save/load corrupts genome data
- breeding commonly crashes
- analyzer cannot inspect bees
- startup crashes on clean install
- version metadata is wrong
- jar includes dev-only files accidentally
```

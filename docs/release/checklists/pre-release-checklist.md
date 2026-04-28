# Pre-Release Checklist

Use before any public release.

## Build

```txt
- Version updated.
- Changelog updated.
- Tests pass.
- Jar builds.
- Jar name matches version, loader, and Minecraft version.
```

## Game Validation

```txt
- Client launches.
- Clean world loads.
- Existing test world loads.
- Mod appears in mod list.
- No startup crash.
```

## Curious Bees Validation

```txt
- Bee genomes can exist.
- Wild bee initialization works.
- Save/load preserves genome.
- Breeding works.
- Baby genome is assigned.
- Mutation can occur or be forced for testing.
- Analyzer works.
- Basic production works if included in release.
```

## Metadata

```txt
- Release notes written.
- Known issues written.
- Dependencies listed.
- Minecraft version listed.
- Loader listed.
- Release type selected.
```

## No-Go

Do not release if:

```txt
- startup crashes on clean install
- breeding commonly crashes
- save/load loses genome data unexpectedly
- release notes are missing
- artifact version does not match metadata
```

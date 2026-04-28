# 06 — Changelog and Release Notes

## 1. Purpose

This document defines how Curious Bees release notes and changelog entries should be written.

Players need clear release notes. Modpack authors need compatibility and migration notes. Developers need traceability.

## 2. Changelog Source

Recommended source of truth:

```txt
CHANGELOG.md
```

Each release should have a section:

```md
## [0.1.0-alpha.1] - YYYY-MM-DD
```

## 3. Categories

Use:

```txt
Added
Changed
Fixed
Removed
Known Issues
Migration Notes
Developer Notes
```

Not every release needs every section.

## 4. Alpha Release Notes Style

Alpha notes should be honest and clear.

Example:

```md
# Curious Bees 0.1.0-alpha.1

This is the first public alpha of Curious Bees.

It focuses on proving the core genetics loop:
- bees can carry genomes;
- wild bees can receive starter species;
- breeding can create inherited child genomes;
- early mutations can occur;
- analyzer output can inspect bee genetics.

## Known Issues

- Content is intentionally small.
- Balance is temporary.
- Existing worlds should be backed up before testing.
- Fabric support is not available yet.
```

## 5. Technical Release Notes

For GitHub, include more technical detail:

```txt
- internal architecture changes
- tests added
- serialization changes
- known data migration issues
- contributor notes
```

## 6. Player-Facing Release Notes

For Modrinth/CurseForge, focus on:

```txt
- what changed
- what players can do
- compatibility
- known issues
- whether worlds need backup
```

## 7. Breaking Changes

Always include a clear section if there are breaking changes.

Example:

```md
## Breaking Changes

- Genome serialization changed.
- Bees from earlier alpha worlds may lose genetics.
- Back up worlds before updating.
```

## 8. Migration Notes

Use migration notes whenever save data, config, content ids, or item ids change.

Example:

```md
## Migration Notes

This version renames the internal species id `bee_genetics:dry` to `curiousbees:arid`.
Existing worlds from earlier alpha builds may not migrate automatically.
```

## 9. Changelog Entry Template

```md
## [VERSION] - YYYY-MM-DD

### Added

- ...

### Changed

- ...

### Fixed

- ...

### Removed

- ...

### Known Issues

- ...

### Migration Notes

- ...
```

## 10. Release Notes Checklist

Before publishing:

```txt
- version matches jar metadata
- Minecraft version is listed
- loader is listed
- release type is honest
- known issues are listed
- breaking changes are listed
- dependencies are listed
- changelog is copied consistently across platforms
```

# 12 — Release Smoke Test

## 1. Purpose

This checklist should be run before sharing a build with anyone else.

It is not a full QA pass. It is a quick confirmation that the build is not obviously broken.

## 2. Build Checklist

```text
[ ] Clean build succeeds.
[ ] Mod jar is generated.
[ ] Version number is correct.
[ ] Mod metadata is correct.
[ ] No accidental debug-only files are packaged unless intended.
[ ] License file exists if publishing.
[ ] Changelog exists if publishing.
```

## 3. Launch Checklist

```text
[ ] Minecraft launches with the mod installed.
[ ] No crash during loading.
[ ] Main menu opens.
[ ] New world can be created.
[ ] Existing test world can be opened.
[ ] Logs do not show obvious fatal errors.
```

## 4. Core Gameplay Smoke Test

```text
[ ] Wild bee can exist with genome.
[ ] Genome can be inspected by debug command or analyzer.
[ ] Bee genome persists through save/load.
[ ] Two bees can breed.
[ ] Baby bee receives genome.
[ ] Mutation can occur if forced/debugged.
[ ] Analyzer shows readable genetics.
```

## 5. Production Smoke Test

If production is included:

```text
[ ] MVP comb/product items exist.
[ ] Production resolver/debug roll works.
[ ] No resource bees accidentally included in MVP.
```

## 6. Asset Smoke Test

```text
[ ] Missing texture blocks/items are not present unless intentionally placeholder.
[ ] Placeholder assets load.
[ ] Block/item model JSON paths are valid.
[ ] No purple-black missing texture where avoidable.
```

## 7. Regression Smoke Test

```text
[ ] No crash when saving world.
[ ] No crash when reloading world.
[ ] No crash when breeding bees.
[ ] No crash when inspecting bees.
[ ] No major log spam.
```

## 8. Release Go / No-Go

Release is allowed only when:

```text
- build succeeds;
- game launches;
- core loop works;
- save/load works;
- known limitations are documented;
- no serious crash is known.
```

## 9. Known Limitations Section

Before release, document:

```text
Known limitations:
- [example] Analyzer is text-only.
- [example] Assets are placeholders.
- [example] Production is minimal.
- [example] Fabric not supported yet.
```

# 04 — Release Automation Plan

## 1. Purpose

This document defines a staged approach for release automation.

Do not automate publishing too early. First make releases manual and repeatable. Automate only after the project has a stable build process.

## 2. Automation Stages

### Stage 0 — Manual Local Build

Use during early development.

```txt
./gradlew test
./gradlew build
```

Manual checks:

```txt
- game launches
- jar exists
- version metadata is correct
```

### Stage 1 — Manual GitHub Release

Use when producing early alpha builds.

Steps:

```txt
1. Run tests locally.
2. Build jar locally.
3. Create git tag.
4. Create GitHub Release manually.
5. Attach jar.
6. Mark prerelease.
```

### Stage 2 — CI Build Only

Add GitHub Actions to:

```txt
- run tests
- build jar
- upload build artifact
```

Do not publish to Modrinth/CurseForge yet.

### Stage 3 — GitHub Release Automation

Automate:

```txt
- build on tag push
- create draft release
- attach jar
```

Keep release publishing manual until confidence is high.

### Stage 4 — Modrinth/CurseForge Upload Automation

Only after manual releases are stable.

Possible automation:

```txt
- upload to Modrinth
- upload to CurseForge
- reuse changelog from release notes
```

This requires platform tokens/secrets and should be handled carefully.

## 3. Secrets and Tokens

Do not commit tokens.

Use GitHub Actions secrets for:

```txt
MODRINTH_TOKEN
CURSEFORGE_TOKEN
```

Potentially:

```txt
GITHUB_TOKEN
```

Never include tokens in docs, commits, screenshots, or AI prompts.

## 4. Build Artifact Rules

Release artifacts should be:

```txt
- reproducible
- named consistently
- built from tagged source
- not manually edited after build
```

Recommended artifact name:

```txt
curious-bees-neoforge-1.21.1-0.1.0-alpha.1.jar
```

## 5. Version Source of Truth

Pick one primary version source.

Options:

```txt
gradle.properties
mod metadata file
build script property
```

Recommended:

```txt
gradle.properties
```

Example:

```properties
mod_version=0.1.0-alpha.1
minecraft_version=1.21.1
mod_loader=neoforge
```

## 6. Changelog Source of Truth

Recommended source:

```txt
CHANGELOG.md
```

Release notes can be copied from:

```txt
CHANGELOG.md section for version
```

## 7. Automated Validation

Before release automation publishes anything:

```txt
- unit tests pass
- jar builds
- generated jar exists
- metadata contains expected mod id/version
```

Later:

```txt
- run headless or smoke launch if feasible
- verify no dev-only resources included
```

## 8. No-Go Conditions for Automation

Do not enable automatic public uploads if:

```txt
- versioning is inconsistent
- changelog is missing
- jars are not named correctly
- tests are flaky
- release process still requires manual edits to the jar
- project has not completed at least one manual public release
```

## 9. Recommended First GitHub Action

First CI should only run tests and build.

Pseudo workflow:

```yaml
name: Build

on:
  pull_request:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - checkout
      - setup java
      - run ./gradlew test
      - run ./gradlew build
      - upload artifacts
```

Do not add publishing in the first workflow.

## 10. Automation Decision Gate

Before Stage 4 automation, review:

```txt
- Are manual releases painless?
- Are platform metadata fields stable?
- Are secrets configured safely?
- Is the changelog process reliable?
- Are release failures recoverable?
```

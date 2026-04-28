# Curious Bees Release and Distribution Docs

This folder defines how Curious Bees should be packaged, versioned, published, and communicated.

The goal is not to publish too early. The goal is to make releases repeatable once the mod has a playable NeoForge MVP.

## Documents

```txt
docs/release/
├── README.md
├── 01-release-and-distribution-plan.md
├── 02-versioning-strategy.md
├── 03-platform-publishing-plan.md
├── 04-release-automation-plan.md
├── 05-project-page-and-branding.md
├── 06-changelog-and-release-notes.md
├── 07-license-and-source-policy.md
├── 08-beta-feedback-and-issue-triage.md
├── checklists/
│   ├── pre-release-checklist.md
│   ├── modrinth-release-checklist.md
│   ├── curseforge-release-checklist.md
│   └── post-release-checklist.md
└── templates/
    ├── release-notes-template.md
    ├── changelog-entry-template.md
    ├── github-release-template.md
    ├── modrinth-description-draft.md
    └── curseforge-description-draft.md
```

## Release Principle

Curious Bees should release only after the player can experience the core loop:

```txt
wild bee genome -> breeding -> inherited baby genome -> mutation chance -> analyzer -> basic production
```

Do not publish a public release just because the genetics core compiles.

## Initial Distribution Recommendation

Recommended first public distribution:

```txt
Platform: Modrinth + CurseForge + GitHub Releases
Loader: NeoForge only
Minecraft: 1.21.1
Release type: Alpha or Beta, not Stable Release
```

Fabric support should be published later as a separate loader build once the NeoForge MVP is stable.

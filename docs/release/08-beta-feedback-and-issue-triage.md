# 08 — Beta Feedback and Issue Triage

## 1. Purpose

This document defines how to handle player feedback after public alpha/beta releases.

The goal is to learn from players without letting feedback derail the roadmap.

## 2. Feedback Channels

Recommended:

```txt
GitHub Issues
Modrinth comments/discussions if available
CurseForge comments
Discord later if community grows
```

Primary technical tracker:

```txt
GitHub Issues
```

## 3. Issue Labels

Recommended labels:

```txt
bug
crash
data-loss
balance
content
ux
analyzer
breeding
mutation
production
neoforge
fabric-future
needs-repro
wontfix-for-now
post-mvp
```

## 4. Bug Report Template

Required fields:

```txt
Minecraft version
NeoForge version
Curious Bees version
other mods
crash log/latest.log
steps to reproduce
expected behavior
actual behavior
world backup available?
```

## 5. Priority Rules

### Critical

```txt
world corruption
save/load data loss
startup crash on clean install
server crash
breeding crash with common flow
```

### High

```txt
genome disappears
mutations impossible due to bug
analyzer crashes
production duplicates items
```

### Medium

```txt
balance issues
unclear analyzer output
rare edge-case bugs
```

### Low

```txt
cosmetic issues
wording issues
future feature requests
```

## 6. Feedback That Should Not Derail MVP

Do not immediately add:

```txt
resource bees
huge species tree
Fabric support
full GUI
apiary automation
compatibility with major tech mods
```

unless the planned phase is ready.

Move those to:

```txt
post-MVP
Phase 9
Fabric phase
```

## 7. Reproduction Process

For bugs:

```txt
1. Confirm version.
2. Ask for logs if missing.
3. Try to reproduce in minimal environment.
4. Try with only Curious Bees + NeoForge.
5. If reproducible, create internal task.
6. If not reproducible, mark needs-repro.
```

## 8. Balance Feedback

Balance feedback should be gathered but not overreacted to.

Useful questions:

```txt
Is mutation too rare?
Is analyzer too hard to understand?
Are hybrids useful?
Is purebred stabilization satisfying?
Is production rewarding enough?
```

Do not rebalance everything from one report.

## 9. Release Feedback Review

After each alpha/beta release, create a short review:

```txt
What worked?
What broke?
What confused players?
What should be fixed before next release?
What should be deferred?
```

## 10. Go / No-Go for Next Release

Do not release next version if:

```txt
critical crashes are unresolved
known data loss has no warning
release notes are missing migration notes
```

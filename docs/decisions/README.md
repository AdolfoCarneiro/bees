# Curious Bees — Architecture Decision Records

This folder contains Architecture Decision Records (ADRs) for Curious Bees.

ADRs are used to preserve important project decisions, their context, the reasoning behind them, and their consequences.

They should help both humans and AI coding agents understand why the project follows a specific direction.

## ADR Index

| ADR | Title | Status |
|---|---|---|
| 0001 | Use NeoForge 1.21.1 as the Initial Loader | Accepted |
| 0002 | Keep the Genetics Core Pure Java | Accepted |
| 0003 | Start with Hardcoded Built-In Content Before JSON/Data-Driven Loading | Accepted |
| 0004 | Use Vanilla Bee Breeding as the First Gameplay Loop | Accepted |
| 0005 | Use Placeholder Assets Before Polished Assets | Accepted |
| 0006 | Add Fabric Support After the NeoForge MVP | Accepted |
| 0007 | Keep Resource Bees Out of the MVP | Accepted |
| 0008 | Use Detailed Local Specs for AI Implementation Guidance | Accepted |

## ADR Template

Each ADR follows this structure:

```md
# ADR-0000 — Title

## Status

Accepted / Proposed / Superseded / Deprecated

## Date

YYYY-MM-DD

## Context

What situation or problem led to this decision?

## Decision

What did we decide?

## Consequences

What becomes easier, harder, or constrained because of this decision?

## Applies To

Which parts of the project are affected?

## Related Documents

Which specs or docs support this decision?
```

## Usage Rules

- ADRs should be short enough to read quickly.
- ADRs should not be rewritten casually.
- If a decision changes, create a new ADR that supersedes the old one.
- AI agents should read relevant ADRs before implementing related tasks.

# ADR-0008 — Use Detailed Local Specs for AI Implementation Guidance

## Status

Accepted

## Date

2026-04-28

## Context

The project will be developed with heavy AI assistance using tools such as Claude Code, Codex, Cursor, and ChatGPT.

The backlog is useful for visual planning, but backlog cards become hard to use if they contain very large implementation instructions.

At the same time, short backlog tasks are not enough for reliable AI coding. AI agents need detailed scope, non-goals, acceptance criteria, expected tests, and prompts.

## Decision

Use local Markdown implementation specs as the primary detailed instructions for AI coding agents.

The backlog should remain concise and visual. It should point to detailed local docs where needed.

Recommended structure:

```txt
docs/implementation/
├── 00-phase-0-documentation-and-decisions.md
├── 01-genetics-core-implementation.md
├── 02-initial-content-implementation.md
├── 03-neoforge-entity-integration.md
├── 04-vanilla-breeding-integration.md
├── 05-analyzer-implementation.md
├── 06-production-mvp.md
├── 07-tech-apiary-and-automation.md
├── 08-data-driven-content.md
├── 09-expanded-content-roadmap.md
└── 10-fabric-support-implementation.md
```

## Consequences

### Positive

- Detailed specs are versioned with code.
- Claude Code can read the docs directly from the repository.
- Reviews can include documentation changes.
- Backlog stays clean.
- AI agents have enough detail to implement small tasks safely.

### Negative

- Specs must be maintained as the project changes.
- Some duplication may exist between backlog and docs.
- The developer must ensure Claude Code reads the relevant doc before implementation.

### Constraints

- Implementation docs should be in English.
- User-facing planning discussion can happen in Portuguese.
- Specs should include objective, scope, non-goals, suggested files, acceptance criteria, expected tests, prompt, and definition of done.
- Do not rely only on a task title or short backlog note when asking an AI agent to code.

## Applies To

- Claude Code workflow;
- Cursor workflow;
- backlog design;
- documentation structure;
- implementation planning.

## Related Documents

- `CLAUDE.md`
- `docs/06-ai-coding-guidelines.md`
- `docs/implementation/00-phase-0-documentation-and-decisions.md`
- `docs/implementation/01-genetics-core-implementation.md`

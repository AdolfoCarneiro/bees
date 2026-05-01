# Phase 0 — Documentation, Decisions, and AI Workflow Implementation Spec

## 1. Purpose

Phase 0 is the foundation phase for **Curious Bees**.

This phase does not implement gameplay code. Instead, it creates the documentation, project rules, decision records, task structure, and AI-agent workflow required to make later implementation safe, incremental, and reviewable.

Curious Bees is expected to be developed with heavy AI assistance using tools such as Claude Code, Codex, Cursor, and ChatGPT. Because of that, Phase 0 must be treated as an implementation phase, not as a casual planning step.

The output of Phase 0 should make it possible for an AI coding agent to answer:

```text
What is this mod?
What is not part of the MVP?
What should be implemented first?
What must not be implemented yet?
Where should code live?
Which docs should be read before coding?
How should each task be executed?
How should assets be handled?
How should future Fabric support be protected?
```

## 2. Phase 0 Goals

Phase 0 should establish:

- project vision;
- non-goals;
- roadmap;
- architecture rules;
- genetics system specification;
- breeding and mutation specification;
- content design specification;
- AI coding guidelines;
- backlog/task strategy;
- local documentation structure;
- future asset pipeline strategy;
- Claude Code instructions;
- decision records;
- review/checklist process.

## 3. Phase 0 Non-goals

Do not implement any gameplay code during Phase 0.

Do not implement:

- genetics classes;
- NeoForge integration;
- Fabric integration;
- items;
- blocks;
- textures;
- models;
- analyzer item;
- GUI;
- apiary;
- resource bees;
- JSON/datapack loading;
- production mechanics;
- Blockbench automation;
- MCP integrations.

Phase 0 can define how these things will be approached later, but it must not attempt to build them.

## 4. Relationship to Existing Docs

The repository already has a strong initial documentation set:

```text
CLAUDE.md
README.md
docs/mvp/01-product-vision-and-roadmap.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
docs/mvp/05-content-design-spec.md
docs/mvp/06-ai-coding-guidelines.md
docs/mvp/07-initial-backlog.md
```

Phase 0 should refine and connect these docs instead of replacing them blindly.

The existing docs already define the central direction:

- the mod brings Forestry-inspired bee genetics to modern vanilla-style bees;
- NeoForge 1.21.1 is the initial target;
- Fabric support is a future target;
- the genetics core must be pure Java;
- Minecraft integration should call the core, not contain the core logic;
- the MVP should start with a small species set;
- AI agents should work on small, testable slices.

## 5. Recommended Repository Documentation Layout

The local repository should use this documentation structure:

```text
curious-bees/
├── CLAUDE.md
├── README.md
├── docs/
│   ├── 01-product-vision-and-roadmap.md
│   ├── 02-technical-architecture.md
│   ├── 03-genetics-system-spec.md
│   ├── 04-breeding-and-mutation-spec.md
│   ├── 05-content-design-spec.md
│   ├── 06-ai-coding-guidelines.md
│   ├── 07-initial-backlog.md
│   ├── decisions/
│   │   ├── 0001-initial-loader-neoforge-1211.md
│   │   ├── 0002-pure-java-genetics-core.md
│   │   ├── 0003-vanilla-first-breeding-loop.md
│   │   ├── 0004-hardcoded-content-before-json.md
│   │   └── 0005-placeholder-assets-before-art-pipeline.md
│   ├── implementation/
│   │   ├── 00-phase-0-documentation-and-decisions.md
│   │   ├── 01-genetics-core-implementation.md
│   │   ├── 02-initial-content-implementation.md
│   │   ├── 03-neoforge-entity-integration.md
│   │   ├── 04-vanilla-breeding-integration.md
│   │   ├── 05-analyzer-implementation.md
│   │   ├── 06-production-mvp.md
│   │   └── 07-assets-and-art-pipeline.md
│   └── prompts/
│       ├── claude-code-task-template.md
│       ├── claude-code-review-template.md
│       └── research-spike-template.md
├── common/
├── neoforge/
└── fabric/
```

If the project is not using a full multiloader structure yet, the docs should still use this future-facing structure. The docs should not force all modules to exist immediately.

## 6. Documentation Files and Responsibilities

### 6.1 `CLAUDE.md`

Purpose:

```text
Short, strict, always-read instructions for Claude Code and other coding agents.
```

It should contain:

- project summary;
- target platform;
- critical architecture rules;
- docs to read before coding;
- initial MVP scope;
- non-goals;
- coding behavior rules;
- first recommended implementation prompt.

It should not contain every detailed implementation instruction. Detailed instructions should live in `docs/implementation/`.

Recommended length:

```text
Short enough for an agent to follow every time.
Long enough to prevent architecture drift.
```

### 6.2 `README.md`

Purpose:

```text
Human-facing project overview.
```

It should explain:

- what Curious Bees is;
- what it is not;
- target loader;
- documentation entry points;
- first development step.

### 6.3 `docs/mvp/01-product-vision-and-roadmap.md`

Purpose:

```text
Product vision and long-term roadmap.
```

It should answer:

- what fantasy the mod delivers;
- why genetics matters;
- what the MVP proves;
- how the roadmap evolves;
- which risks should be avoided.

### 6.4 `docs/mvp/02-technical-architecture.md`

Purpose:

```text
Architecture boundaries and module responsibilities.
```

It should define:

- pure genetics core;
- common content definitions;
- common gameplay orchestration;
- platform adapters;
- NeoForge-specific implementation;
- future Fabric-specific implementation;
- anti-patterns.

### 6.5 `docs/mvp/03-genetics-system-spec.md`

Purpose:

```text
Core genetics rules.
```

It should define:

- genome;
- chromosome;
- allele;
- gene pair;
- active/inactive allele;
- dominance;
- purebred/hybrid;
- Mendelian inheritance;
- initial chromosome types;
- random abstraction;
- testability requirements.

### 6.6 `docs/mvp/04-breeding-and-mutation-spec.md`

Purpose:

```text
Vanilla-style breeding and mutation behavior.
```

It should define:

- player-facing breeding flow;
- system breeding flow;
- missing genome fallback;
- inheritance timing;
- mutation timing;
- mutation definition structure;
- partial vs full mutation;
- environment context;
- mutation feedback.

### 6.7 `docs/mvp/05-content-design-spec.md`

Purpose:

```text
Initial species, traits, mutations, products, and future content categories.
```

It should define:

- Meadow;
- Forest;
- Arid;
- Cultivated;
- Hardy;
- initial traits;
- initial mutation tree;
- production philosophy;
- content loading strategy;
- future categories.

### 6.8 `docs/mvp/06-ai-coding-guidelines.md`

Purpose:

```text
Rules for using AI agents safely.
```

It should define:

- one task at a time;
- required reading;
- agent workflow;
- what agents must not do;
- task size guidelines;
- review checklist;
- failure modes.

### 6.9 `docs/mvp/07-initial-backlog.md`

Purpose:

```text
Initial epic/story/task map.
```

It should remain high-level enough to guide planning, but not become the only source of implementation detail.

### 6.10 `docs/implementation/*.md`

Purpose:

```text
Detailed implementation specs for each phase.
```

These docs should contain the heavy instructions that would otherwise make Notion cards too noisy.

Each implementation spec should include:

- objective;
- scope;
- non-goals;
- required concepts;
- expected classes/files;
- implementation order;
- acceptance criteria;
- expected tests;
- coding prompts;
- review checklist;
- definition of done.

### 6.11 `docs/decisions/*.md`

Purpose:

```text
Small architectural decision records.
```

Every important project decision should be recorded here so it does not exist only in chat history.

Recommended format:

```text
# Decision: [Title]

## Status
Accepted | Proposed | Superseded

## Context
Why this decision exists.

## Decision
What was decided.

## Consequences
What this enables and what trade-offs it creates.
```

### 6.12 `docs/prompts/*.md`

Purpose:

```text
Reusable AI prompts.
```

Prompts should be versioned and reused instead of rewritten from memory each time.

## 7. Required Phase 0 Decision Records

Phase 0 should create these decision records.

## 7.1 Decision 0001 — Initial Loader is NeoForge 1.21.1

Status:

```text
Accepted
```

Decision:

```text
Curious Bees starts with NeoForge 1.21.1 as the first implementation target.
```

Reason:

```text
The first playable version needs one stable target before considering multiloader complexity.
```

Consequence:

```text
NeoForge integration may be implemented first, but core logic must remain loader-independent.
```

## 7.2 Decision 0002 — Genetics Core is Pure Java

Status:

```text
Accepted
```

Decision:

```text
The genetics core must not depend on Minecraft, NeoForge, Fabric, events, registries, entities, ItemStack, NBT, data components, attachments, or mixins.
```

Reason:

```text
The genetics engine is the hardest logic in the mod and must be unit-testable outside Minecraft.
```

Consequence:

```text
All Minecraft integration must call services from the core instead of embedding logic in event handlers.
```

## 7.3 Decision 0003 — MVP Breeding Uses Vanilla Interaction

Status:

```text
Accepted
```

Decision:

```text
The first player-facing breeding loop should use vanilla-style bee breeding: two bees, flowers, baby bee.
```

Reason:

```text
The mod should feel grounded in modern vanilla bees while adding Forestry-like genetics under the hood.
```

Consequence:

```text
A Forestry-style apiary is future tech progression, not the first breeding foundation.
```

## 7.4 Decision 0004 — Built-in Content Before JSON

Status:

```text
Accepted
```

Decision:

```text
Initial species, traits, mutations, and products can be hardcoded in centralized built-in definitions before JSON/datapack loading exists.
```

Reason:

```text
Implementing data-driven loading too early can slow down the core and force unstable schemas.
```

Consequence:

```text
Definitions must still be centralized and shaped so they can move to JSON later.
```

## 7.5 Decision 0005 — Placeholder Assets Before Asset Pipeline

Status:

```text
Accepted
```

Decision:

```text
Assets are not part of the critical path until Analyzer MVP / Basic Production. Early development should use no assets or placeholders.
```

Reason:

```text
The first phases are mostly pure code and tests. Blockbench/MCP/asset generation should not block genetics or breeding implementation.
```

Consequence:

```text
A future asset pipeline document is useful, but Blockbench automation is not required for Phase 0 or Phase 1.
```

## 8. Phase 0 Task Breakdown

## TASK 0.1 — Audit Existing Documentation

### Objective

Review existing documentation and identify missing decisions, contradictions, and areas where instructions are too vague.

### Scope

Audit:

```text
CLAUDE.md
README.md
docs/mvp/01-product-vision-and-roadmap.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
docs/mvp/05-content-design-spec.md
docs/mvp/06-ai-coding-guidelines.md
docs/mvp/07-initial-backlog.md
```

### Non-goals

Do not rewrite all docs from scratch.

Do not implement code.

### Acceptance Criteria

- Missing or vague areas are listed.
- Contradictions are listed.
- Follow-up doc edits are identified.
- No code changes are made.

### Prompt

```text
Read CLAUDE.md and all files in docs/.

Focus only on auditing Phase 0 documentation.

Do not implement code.

Return:
1. missing decisions;
2. contradictions;
3. vague areas;
4. recommended doc updates;
5. whether the docs are ready for Phase 1 implementation.
```

## TASK 0.2 — Create Implementation Specs Folder

### Objective

Create the `docs/implementation/` folder and the phase implementation spec files.

### Scope

Create:

```text
docs/implementation/00-phase-0-documentation-and-decisions.md
docs/implementation/01-genetics-core-implementation.md
docs/implementation/02-initial-content-implementation.md
docs/implementation/03-neoforge-entity-integration.md
docs/implementation/04-vanilla-breeding-integration.md
docs/implementation/05-analyzer-implementation.md
docs/implementation/06-production-mvp.md
docs/implementation/07-assets-and-art-pipeline.md
```

Only Phase 0 must be fully detailed immediately.

The other files may start as structured placeholders with clear headings.

### Non-goals

Do not implement code.

Do not fill every future phase completely yet unless explicitly requested.

### Acceptance Criteria

- Folder exists.
- Files exist.
- Phase 0 file is detailed.
- Future phase files have placeholders and intended sections.

### Prompt

```text
Read CLAUDE.md and all docs.

Create docs/implementation/ and the phase implementation spec files.

Fully write docs/implementation/00-phase-0-documentation-and-decisions.md.

For future phase files, create structured placeholders with headings only.

Do not implement code.
```

## TASK 0.3 — Create Decision Records

### Objective

Create initial decision records for foundational project decisions.

### Scope

Create:

```text
docs/decisions/0001-initial-loader-neoforge-1211.md
docs/decisions/0002-pure-java-genetics-core.md
docs/decisions/0003-vanilla-first-breeding-loop.md
docs/decisions/0004-hardcoded-content-before-json.md
docs/decisions/0005-placeholder-assets-before-art-pipeline.md
```

### Non-goals

Do not over-document tiny decisions.

Do not create decisions for unresolved future topics.

### Acceptance Criteria

Each decision record includes:

```text
Title
Status
Context
Decision
Consequences
```

### Prompt

```text
Read CLAUDE.md and docs/mvp/01-product-vision-and-roadmap.md.

Create docs/decisions/ with the initial decision records listed in Phase 0.

Use a concise ADR-style format:
- Status
- Context
- Decision
- Consequences

Do not implement code.
```

## TASK 0.4 — Create Prompt Templates

### Objective

Create reusable prompt templates for AI-assisted development.

### Scope

Create:

```text
docs/prompts/claude-code-task-template.md
docs/prompts/claude-code-review-template.md
docs/prompts/research-spike-template.md
```

### Required Template: Task Implementation

Must include:

```text
Read CLAUDE.md
Read relevant docs
Focus only on one task
Summarize understanding
List expected files
Implement smallest complete version
Run or describe tests
Summarize changes
List follow-ups
```

### Required Template: Review

Must include checks for:

```text
scope creep
missing tests
platform imports in common code
unnecessary abstractions
hardcoded content in wrong places
future Fabric compatibility
```

### Required Template: Research Spike

Must include:

```text
question
context
sources to check
alternatives
recommendation
risks
next implementation task
```

### Non-goals

Do not make prompts overly broad.

Do not create prompts that ask agents to implement multiple systems at once.

### Acceptance Criteria

- Three prompt template files exist.
- Templates are reusable.
- Templates reinforce one-task-at-a-time development.

### Prompt

```text
Read docs/mvp/06-ai-coding-guidelines.md.

Create docs/prompts/ with three reusable prompt templates:
- claude-code-task-template.md
- claude-code-review-template.md
- research-spike-template.md

Do not implement code.
```

## TASK 0.5 — Update CLAUDE.md to Point to Implementation Specs

### Objective

Ensure Claude Code knows where detailed implementation specs live.

### Scope

Update `CLAUDE.md` to mention:

```text
docs/implementation/
docs/decisions/
docs/prompts/
```

It should instruct Claude Code to read the relevant implementation spec before coding a task.

### Non-goals

Do not make `CLAUDE.md` huge.

Do not duplicate all implementation details inside `CLAUDE.md`.

### Acceptance Criteria

- `CLAUDE.md` points to implementation specs.
- It still remains concise.
- It reinforces pure Java genetics core boundaries.
- It reinforces one-task-at-a-time development.

### Prompt

```text
Read CLAUDE.md and docs/implementation/00-phase-0-documentation-and-decisions.md.

Update CLAUDE.md so it points to:
- docs/implementation/
- docs/decisions/
- docs/prompts/

Keep CLAUDE.md concise.
Do not duplicate the full implementation specs.
Do not change the project direction.
```

## TASK 0.6 — Add Asset Pipeline Placeholder Document

### Objective

Document the current asset strategy without making assets part of the critical path.

### Scope

Create:

```text
docs/implementation/07-assets-and-art-pipeline.md
```

This file should explain:

- assets are not needed for Phase 1 genetics core;
- placeholders are acceptable for early analyzer/items/products;
- Blockbench is useful later for custom blocks/machines;
- Blockbench/MCP automation is future optional tooling;
- security concerns should be considered before allowing MCP tools to execute scripts;
- asset work should not block genetics, storage, breeding, or analyzer MVP.

### Non-goals

Do not create actual assets.

Do not install Blockbench plugins.

Do not create MCP configuration.

### Acceptance Criteria

- Asset strategy is documented.
- Placeholder-first approach is clear.
- Blockbench is treated as future workflow, not MVP blocker.

### Prompt

```text
Read docs/mvp/01-product-vision-and-roadmap.md and docs/mvp/06-ai-coding-guidelines.md.

Create docs/implementation/07-assets-and-art-pipeline.md.

Document a placeholder-first asset strategy.
Explain when assets become necessary and when Blockbench should be considered.
Do not create assets or install tools.
```

## TASK 0.7 — Create Phase 1 Readiness Checklist

### Objective

Create a checklist that tells whether the project is ready to begin Phase 1 implementation.

### Scope

Create a section or file:

```text
docs/implementation/phase-1-readiness-checklist.md
```

or add a checklist section to this document.

### Checklist

Phase 1 can start when:

```text
CLAUDE.md exists and is current.
Product vision exists.
Architecture doc exists.
Genetics spec exists.
Breeding/mutation spec exists.
AI coding guidelines exist.
Implementation specs folder exists.
Phase 0 decisions are recorded.
Prompt templates exist.
Phase 1 implementation spec exists or is ready to be written.
The first task is clearly identified.
```

### Non-goals

Do not require asset docs to be perfect before Phase 1.

Do not require Notion backlog to be perfect before Phase 1.

### Acceptance Criteria

- Checklist exists.
- It is clear which items are required vs optional.
- It points to the first Phase 1 task.

### Prompt

```text
Create a Phase 1 readiness checklist based on the existing docs and Phase 0 implementation spec.

Do not implement code.

The checklist should clearly indicate what must be ready before starting the pure Java genetics core.
```

## 9. Phase 0 Acceptance Criteria

Phase 0 is complete when:

```text
- CLAUDE.md exists and is up to date.
- README.md exists.
- docs/mvp/01-product-vision-and-roadmap.md exists.
- docs/mvp/02-technical-architecture.md exists.
- docs/mvp/03-genetics-system-spec.md exists.
- docs/mvp/04-breeding-and-mutation-spec.md exists.
- docs/mvp/05-content-design-spec.md exists.
- docs/mvp/06-ai-coding-guidelines.md exists.
- docs/mvp/07-initial-backlog.md exists.
- docs/implementation/ exists.
- docs/implementation/00-phase-0-documentation-and-decisions.md exists.
- docs/implementation/01-genetics-core-implementation.md exists or is planned.
- docs/decisions/ exists with the initial foundational decisions.
- docs/prompts/ exists with reusable AI prompts.
- Asset strategy is documented as placeholder-first.
- The first Phase 1 implementation task is clearly identified.
- No gameplay code has been implemented prematurely.
```

## 10. Phase 0 Review Checklist

Before marking Phase 0 done, review:

```text
Are the docs consistent with each other?
Is the MVP still small?
Is NeoForge still the first target?
Is Fabric still future scope?
Is the genetics core still pure Java?
Are resource bees still out of MVP?
Is the first implementation task small enough?
Are AI agents instructed not to implement too much?
Are future assets documented without becoming blockers?
Are decisions recorded outside chat history?
```

## 11. First Phase 1 Task

After Phase 0, the first real implementation task should be:

```text
TASK — Create genetics core package structure
```

Then:

```text
TASK — Implement Allele, Dominance and ChromosomeType
TASK — Implement GenePair active/inactive resolution
TASK — Implement Genome aggregate
TASK — Implement BreedingService
TASK — Implement MutationDefinition and MutationService
```

## 12. Recommended Phase 0 Handoff Prompt for Claude Code

Use this prompt when asking Claude Code to create or update Phase 0 docs locally:

```text
Read CLAUDE.md and all files in docs/.

Do not implement code.

Focus only on Phase 0 documentation and decisions.

Create or update:
- docs/implementation/00-phase-0-documentation-and-decisions.md
- docs/implementation/01-genetics-core-implementation.md as a placeholder if missing
- docs/implementation/07-assets-and-art-pipeline.md as a placeholder if missing
- docs/decisions/ initial decision records
- docs/prompts/ reusable prompt templates

Do not change the project direction.
Do not introduce gameplay implementation.
Do not generate assets.
Do not install tools.

After editing, summarize:
1. files created;
2. files changed;
3. decisions recorded;
4. whether Phase 1 is ready to start;
5. remaining documentation gaps.
```

## 13. Notes on Notion vs Local Docs

Use Notion for:

```text
Backlog visualization
Task status
Dependency tracking
High-level planning
```

Use local docs for:

```text
Detailed implementation instructions
Claude Code prompts
Architecture rules
Decision records
Versioned specs
```

The backlog should not carry all implementation detail.

The local docs should carry the heavy implementation specs.

## 14. Notes on Assets and Blockbench

Assets should be considered, but not prioritized too early.

Phase 0 should record the strategy:

```text
No assets required for pure genetics core.
Placeholders are acceptable for early items/products.
Blockbench is useful later for custom blocks and machines.
Blockbench/MCP automation should be treated as optional future tooling.
Security and sandboxing should be considered before enabling MCP tools that can execute scripts.
```

Recommended asset progression:

```text
Phase 1: no assets
Phase 2: no assets or placeholders only
Phase 3: no assets required, debug commands acceptable
Phase 4: no assets required, mutation feedback can be debug/log first
Phase 5: placeholder analyzer item texture
Phase 6: placeholder comb item textures
Phase 7: Blockbench becomes useful for custom machines/apiary
```

Do not block genetics, storage, breeding, or analyzer MVP on polished art.


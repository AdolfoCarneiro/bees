> Status: MVP foundation document.
>
> This document describes the original MVP design used to validate the Curious Bees core loop.
> It is preserved as historical and architectural context.
> For the current post-MVP direction, see:
> `docs/post-mvp/11-post-mvp-productization-roadmap.md`.

# 06 — AI Coding Guidelines

## 1. Purpose

This project will be developed with heavy AI assistance using tools such as Claude Code, Codex, Cursor, and similar coding agents.

This document defines how AI agents should be used so the project does not become chaotic.

## 2. Core Principle

Do not ask an AI agent to build the whole mod at once.

Ask it to build small, testable slices.

Good:

```text
Implement GenePair active/inactive resolution with tests.
```

Bad:

```text
Make the whole bee genetics mod.
```

## 3. Documentation Layers

AI agents must understand the difference between planning docs and execution docs.

```text
CLAUDE.md              = agent rules and guardrails
Core docs              = product/domain/architecture reference
docs/implementation/   = detailed execution specs
docs/decisions/        = architectural decisions
docs/quality/          = validation and test plans
docs/art/              = asset workflow and placeholder strategy
docs/release/          = release and distribution planning
Backlog/Notion         = status tracking and task list
```

Backlog items may be short. Implementation docs should be detailed.

Do not rely only on a backlog title.

## 4. Required Reading Before Coding

Before coding, the agent should read:

```text
CLAUDE.md
docs/mvp/01-product-vision-and-roadmap.md
docs/mvp/02-technical-architecture.md
```

Then read the relevant implementation spec:

```text
docs/implementation/00-phase-0-documentation-and-decisions.md
docs/implementation/01-genetics-core-implementation.md
docs/implementation/02-initial-content-implementation.md
docs/implementation/03-neoforge-entity-integration.md
docs/implementation/04-vanilla-breeding-integration.md
docs/implementation/05-analyzer-implementation.md
docs/implementation/06-production-mvp.md
docs/implementation/07-tech-apiary-and-automation.md
docs/implementation/08-data-driven-content.md
docs/implementation/09-expanded-content-roadmap.md
docs/implementation/10-fabric-support-implementation.md
```

Read domain specs as needed:

```text
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
docs/mvp/05-content-design-spec.md
```

Read ADRs if a task touches a decision:

```text
docs/decisions/
```

Read test plans if a task changes behavior:

```text
docs/quality/
```

Read art docs if a task touches textures/models/assets:

```text
docs/art/
```

## 5. Agent Workflow

Every coding task should follow this pattern:

```text
1. Read relevant docs.
2. Restate the task scope.
3. Identify files to change.
4. Identify assumptions and risks.
5. Implement the smallest useful slice.
6. Add or update tests.
7. Run tests or explain why they cannot run.
8. Summarize changes.
9. List follow-up tasks.
```

## 6. What Agents Must Not Do Without Explicit Request

Do not let an agent casually add:

```text
- resource bees;
- large species trees;
- complex GUI;
- full apiary system;
- Fabric support;
- mod compatibility;
- datapack JSON system;
- advanced networking;
- complex client rendering;
- custom bee entity replacement;
- polished assets as blockers;
- Blockbench/MCP automation as a requirement.
```

These are future tasks, not first tasks.

## 7. Recommended Prompt: First Implementation

Use the detailed Phase 1 implementation spec.

```text
Read CLAUDE.md.
Read docs/implementation/01-genetics-core-implementation.md.
Read docs/mvp/03-genetics-system-spec.md.

Focus only on Task 1 from the Phase 1 implementation spec:
Create genetics core package structure.

Do not implement alleles, genomes, breeding, mutation, Minecraft integration, NeoForge integration, Fabric support, items, blocks or UI.

Before coding:
1. summarize your understanding;
2. list the files/directories you expect to create or modify;
3. list assumptions and risks.

Then implement the smallest complete version.
```

After Task 1 is reviewed, proceed to Task 2 from `docs/implementation/01-genetics-core-implementation.md`.

## 8. Recommended Prompt: Task Execution Template

```text
Read CLAUDE.md first.

Then read:
- [relevant implementation spec]
- [relevant domain spec]
- [relevant ADRs]
- [relevant quality/test plan]

Focus only on:
[TASK NAME]

Task/spec details:
[PASTE TASK SECTION IF NEEDED]

Do not implement unrelated features.
Do not implement future backlog items early.
Do not introduce Minecraft, NeoForge, or Fabric dependencies into common genetics code.

Before coding:
1. summarize your understanding;
2. list the files you expect to create or modify;
3. list assumptions and risks.

Then implement the smallest complete version of this task.

After coding:
1. run or describe tests;
2. summarize what changed;
3. list any follow-up tasks;
4. mention any deviation from the docs/specs.
```

## 9. Recommended Prompt: Review Core Before Minecraft Integration

Use only after Phase 1 is implemented.

```text
Read CLAUDE.md, docs/implementation/01-genetics-core-implementation.md, docs/mvp/03-genetics-system-spec.md, and docs/quality/02-genetics-core-test-plan.md.

Review the implemented genetics core.

Check specifically:
- Does common genetics import Minecraft, NeoForge, or Fabric classes?
- Are active/inactive alleles persisted?
- Is randomness injectable/testable?
- Are tests covering Mendelian inheritance?
- Are species definitions centralized or ready to be centralized?
- Is mutation logic independent from Minecraft entities?

Do not change files yet. First produce a review with recommended fixes.
```

## 10. Recommended Prompt: NeoForge Bee Genome Storage

Use only after core tests pass.

```text
Read CLAUDE.md.
Read docs/implementation/03-neoforge-entity-integration.md.
Read relevant ADRs and quality plans.

Implement only the selected NeoForge-side genome storage task.

Requirements:
- Store a Genome on Bee entities.
- Persist genome through save/load.
- Keep NeoForge-specific code isolated from common genetics.
- Do not put genetic logic inside event handlers.

Do not implement breeding yet.
Do not implement analyzer GUI yet.
```

## 11. Recommended Prompt: Vanilla Breeding Integration

Use only after bee genome storage works.

```text
Read CLAUDE.md.
Read docs/implementation/04-vanilla-breeding-integration.md.
Read docs/mvp/04-breeding-and-mutation-spec.md.
Read relevant quality plans.

Implement only the requested vanilla breeding integration task.

Requirements:
- When two vanilla bees produce a baby, read both parent genomes.
- Generate the child genome using common services.
- Apply mutation using common services.
- Store the final genome on the baby bee.
- Handle missing parent genomes safely.
- Keep event handlers thin.

Do not implement apiary, resource bees, advanced GUI, or Fabric support.
```

## 12. Recommended Prompt: Analyzer MVP

```text
Read CLAUDE.md.
Read docs/implementation/05-analyzer-implementation.md.
Read docs/mvp/05-content-design-spec.md.

Implement only the requested Analyzer MVP task.

Requirements:
- Use on a bee to display its genome.
- Show active/inactive species.
- Show purebred/hybrid status.
- Show Lifespan, Productivity, Fertility, and FlowerType.
- Start with chat output or tooltip if GUI is too large.

Do not implement advanced research/discovery UI yet.
```

## 13. Recommended Prompt: Asset Tasks

Use only when a task actually touches assets.

```text
Read docs/art/README.md and the relevant asset pipeline document.

Use placeholder assets unless the task explicitly asks for polished assets.

Do not introduce Blockbench/MCP automation unless the task explicitly asks for it.
```

## 14. How to Keep Agents Focused

When asking an AI to work, include:

```text
Scope:
Out of scope:
Files likely involved:
Tests required:
Do not:
```

Example:

```text
Scope:
Implement GenePair dominance resolution.

Out of scope:
BreedingService, MutationService, Minecraft integration, items, blocks.

Files likely involved:
common/genetics/model/GenePair.java
common/genetics/model/Allele.java
common/genetics/model/Dominance.java
common/genetics/model/GenePairTest.java

Tests required:
Dominant beats recessive.
Same dominance chooses active via random and persists result.

Do not:
Import Minecraft classes.
```

## 15. Good Task Size

A good AI coding task should usually be one of:

```text
- one model class plus tests;
- one service plus tests;
- one platform integration hook;
- one item with simple behavior;
- one refactor with a narrow purpose;
- one research spike producing a decision note.
```

A risky task is:

```text
- multiple systems at once;
- many files across common + NeoForge + Fabric;
- UI + networking + data + gameplay in one prompt;
- asset workflow + code + release in one prompt.
```

## 16. Review Checklist

After every AI-generated implementation, check:

```text
- Did it follow the docs?
- Did it implement only the requested scope?
- Did it add tests where appropriate?
- Did it invent mechanics?
- Did it import platform APIs into common genetics?
- Did it hardcode content in random places?
- Did it add unnecessary abstractions?
- Did it break future Fabric support?
- Did it produce code that can be explained simply?
```

## 17. Commit Strategy

Recommended commit sequence:

```text
1. docs: add project vision and architecture docs
2. docs: add implementation specs and ADRs
3. core: add genetics primitives
4. core: add gene pair and genome model
5. core: add breeding service
6. core: add mutation service
7. core: add built-in species definitions
8. neoforge: attach genome data to bees
9. neoforge: initialize wild bee genomes
10. neoforge: apply genetics to vanilla breeding
11. gameplay: add basic bee analyzer
12. gameplay: add basic species production
```

## 18. AI Failure Modes to Watch

### 18.1 The Agent Builds Too Much

Symptom:

```text
It adds apiary, resource bees, GUI, textures, JSON loading, and Fabric code in one response.
```

Fix:

```text
Reject or revert. Ask for a smaller implementation.
```

### 18.2 The Agent Puts Genetics in Events

Symptom:

```text
Breeding logic lives directly inside NeoForge event handler.
```

Fix:

```text
Move logic into common BreedingService and MutationService.
```

### 18.3 The Agent Makes Deterministic Recipes

Symptom:

```text
Meadow + Forest always equals Cultivated.
```

Fix:

```text
Mutation must be probabilistic.
```

### 18.4 The Agent Ignores Inactive Alleles

Symptom:

```text
Only active species is stored.
```

Fix:

```text
Every chromosome must store two alleles and active/inactive result.
```

### 18.5 The Agent Recalculates Active Allele

Symptom:

```text
Active allele changes when reading the genome.
```

Fix:

```text
Resolve active/inactive only when creating the gene pair and persist it.
```

### 18.6 The Agent Treats Backlog as Full Spec

Symptom:

```text
It implements based only on a short task title.
```

Fix:

```text
Point it to docs/implementation/<phase>.md and rerun with a narrow prompt.
```

## 19. Final Rule

The project should grow like this:

```text
Correct core -> small content -> NeoForge storage -> breeding -> analyzer -> production -> tech -> JSON -> expanded content -> Fabric
```

Do not invert this order without a clear reason and a documented decision.

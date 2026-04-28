# 06 — AI Coding Guidelines

## 1. Purpose

This project will be developed with heavy AI assistance using tools such as Claude Code, Codex, Cursor, and similar coding agents.

This document defines how AI agents should be used so the project does not become chaotic.

## 2. Core Principle

Do not ask an AI agent to build the whole mod at once.

Ask it to build small, testable slices.

Good:

```txt
Implement the pure Java genetics core and unit tests.
```

Bad:

```txt
Make the whole bee genetics mod.
```

## 3. Required Reading Before Coding

Before coding, the agent should read:

```txt
CLAUDE.md
docs/01-product-vision-and-roadmap.md
docs/02-technical-architecture.md
docs/03-genetics-system-spec.md
docs/04-breeding-and-mutation-spec.md
```

For content tasks:

```txt
docs/05-content-design-spec.md
```

For planning tasks:

```txt
docs/07-initial-backlog.md
```

## 4. Agent Workflow

Every coding task should follow this pattern:

```txt
1. Read relevant docs.
2. Restate the task scope.
3. Identify files to change.
4. Implement the smallest useful slice.
5. Add or update tests.
6. Run tests or explain why they cannot run.
7. Summarize changes.
8. List any follow-up tasks.
```

## 5. What Agents Must Not Do Without Explicit Request

Do not let an agent casually add:

```txt
- Resource bees
- Large species trees
- Complex GUI
- Full apiary system
- Fabric support
- Mod compatibility
- Datapack JSON system
- Advanced networking
- Complex client rendering
- Custom bee entity replacement
```

These are future tasks, not first tasks.

## 6. Recommended Prompt: First Implementation

```txt
Read CLAUDE.md and all docs in the docs folder.

Implement only the pure Java genetics core.

Do not use Minecraft, NeoForge, Fabric, NBT, components, entities, blocks, items, registries, or events.

Create:
- Allele
- Dominance
- GenePair
- Genome
- ChromosomeType
- BreedingService
- BreedingResult
- MutationDefinition
- MutationService
- MutationResult
- GeneticRandom

Add unit tests for:
- Dominance resolution
- Active/inactive allele persistence
- Mendelian inheritance
- Purebred detection
- Hybrid detection
- Mutation with 0% chance
- Mutation with 100% chance
- Approximate distribution over many crosses

Do not implement Minecraft integration yet.
```

## 7. Recommended Prompt: Review Core Before Minecraft Integration

```txt
Read the implemented genetics core and the docs.

Review whether the core violates any architecture rule.

Check specifically:
- Does common genetics import Minecraft, NeoForge, or Fabric classes?
- Are active/inactive alleles persisted?
- Is randomness injectable/testable?
- Are tests covering Mendelian inheritance?
- Are species definitions centralized?
- Is mutation logic independent from Minecraft entities?

Do not change files yet. First produce a review with recommended fixes.
```

## 8. Recommended Prompt: NeoForge Bee Genome Storage

Use only after core tests pass.

```txt
Read CLAUDE.md and the architecture docs.

Implement NeoForge-side genome storage for vanilla Bee entities.

Requirements:
- Store a Genome on Bee entities.
- Initialize missing genome data for naturally spawned bees.
- Persist genome through save/load.
- Do not put genetic logic inside event handlers.
- Event handlers should call common services.
- Add a debug command to inspect the genome of the bee the player is looking at, if feasible.

Do not implement breeding yet.
Do not implement analyzer GUI yet.
```

## 9. Recommended Prompt: Vanilla Breeding Integration

Use only after bee genome storage works.

```txt
Read CLAUDE.md and the breeding/mutation spec.

Implement vanilla bee breeding integration for NeoForge.

Requirements:
- When two vanilla bees produce a baby, read both parent genomes.
- Generate the child genome using BreedingService.
- Apply MutationService with environment context.
- Store the final genome on the baby bee.
- Handle missing parent genomes safely.
- Add debug logs or minimal feedback when mutation occurs.

Do not implement apiary, resource bees, or advanced GUI.
```

## 10. Recommended Prompt: Analyzer MVP

```txt
Read CLAUDE.md and the genetics/content specs.

Implement a basic Bee Analyzer item.

Requirements:
- Use on a bee to display its genome.
- Show active/inactive species.
- Show purebred/hybrid status.
- Show Lifespan, Productivity, Fertility, and FlowerType.
- Clearly distinguish active and inactive alleles.
- Start with chat output or tooltip if GUI is too large.

Do not implement advanced research/discovery UI yet.
```

## 11. Recommended Prompt: Content JSON Migration

Use only after hardcoded definitions are stable.

```txt
Read the content design spec.

Refactor built-in species and mutation definitions to support future JSON loading.

Do not remove hardcoded built-ins yet.
Create model classes and validation that can be used by both built-in definitions and future JSON data.

Do not implement a full datapack reload system unless explicitly requested.
```

## 12. How to Keep Agents Focused

When asking an AI to work, include:

```txt
Scope:
Out of scope:
Files likely involved:
Tests required:
Do not:
```

Example:

```txt
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

## 13. Good Task Size

A good AI coding task should usually be one of:

```txt
- One model class plus tests.
- One service plus tests.
- One platform integration hook.
- One item with simple behavior.
- One refactor with a narrow purpose.
```

A risky task is:

```txt
- Multiple systems at once.
- Many files across common + NeoForge + Fabric.
- UI + networking + data + gameplay in one prompt.
```

## 14. Review Checklist

After every AI-generated implementation, check:

```txt
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

## 15. Commit Strategy

Recommended commit sequence:

```txt
1. docs: add project vision and architecture docs
2. core: add genetics model
3. core: add breeding service
4. core: add mutation service
5. core: add built-in species definitions
6. neoforge: attach genome data to bees
7. neoforge: initialize wild bee genomes
8. neoforge: apply genetics to vanilla breeding
9. gameplay: add basic bee analyzer
10. gameplay: add basic species production
```

## 16. AI Failure Modes to Watch

### 16.1 The Agent Builds Too Much

Symptom:

```txt
It adds apiary, resource bees, GUI, textures, JSON loading, and Fabric code in one response.
```

Fix:

```txt
Reject or revert. Ask for a smaller implementation.
```

### 16.2 The Agent Puts Genetics in Events

Symptom:

```txt
Breeding logic lives directly inside NeoForge event handler.
```

Fix:

```txt
Move logic into common BreedingService and MutationService.
```

### 16.3 The Agent Makes Deterministic Recipes

Symptom:

```txt
Meadow + Forest always equals Cultivated.
```

Fix:

```txt
Mutation must be probabilistic.
```

### 16.4 The Agent Ignores Inactive Alleles

Symptom:

```txt
Only active species is stored.
```

Fix:

```txt
Every chromosome must store two alleles and active/inactive result.
```

### 16.5 The Agent Recalculates Active Allele

Symptom:

```txt
Active allele changes when reading the genome.
```

Fix:

```txt
Resolve active/inactive only when creating the gene pair and persist it.
```

## 17. Final Rule

The project should grow like this:

```txt
Correct core -> small content -> NeoForge storage -> breeding -> analyzer -> production -> tech -> JSON -> Fabric
```

Do not invert this order without a clear reason.

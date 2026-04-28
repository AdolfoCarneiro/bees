# Curious Bees

Curious Bees is a Minecraft mod project focused on bringing Forestry-inspired bee genetics to modern vanilla-style bees.

The goal is not to port Forestry and not to fork Productive Bees. The goal is to build a new mod from scratch where vanilla bees can carry genetic data, reproduce through Minecraft's natural breeding flow, mutate into new species, and gradually support a deeper tech-oriented bee progression.

## Target

- Initial loader: **NeoForge 1.21.1**
- Future loader: **Fabric**
- Development style: **AI-assisted coding** with Claude Code, Codex, Cursor, and similar tools
- Main design rule: **implement the genetics core first, isolated from Minecraft APIs**

## Core Fantasy

The player should feel that every generation matters:

- a baby bee may inherit a useful recessive trait;
- a hybrid may carry hidden potential;
- a rare mutation may appear after several attempts;
- stabilizing a purebred lineage should feel like an achievement;
- later tech should improve control and throughput, not replace the genetic loop.

## What This Project Is Not

Curious Bees is not:

- a Forestry port;
- a Productive Bees fork;
- a deterministic `A + B = C` recipe system;
- a resource-bee mod as its first priority;
- a project that should start by implementing dozens of species;
- a mod that should begin with GUIs, machines, custom blocks, or polished assets.

## Documentation Map

The project documentation is split into layers.

### Core planning docs

```text
docs/
├── 01-product-vision-and-roadmap.md
├── 02-technical-architecture.md
├── 03-genetics-system-spec.md
├── 04-breeding-and-mutation-spec.md
├── 05-content-design-spec.md
├── 06-ai-coding-guidelines.md
└── 07-initial-backlog.md
```

These explain the product, architecture, core genetic model, breeding/mutation rules, initial content direction, AI usage rules, and high-level backlog.

### Implementation specs

```text
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

These are the execution specs. When an AI coding agent implements a task, it should read the relevant implementation document first.

### Decision records

```text
docs/decisions/
```

Architectural Decision Records (ADRs) explain why important decisions were made, such as starting with NeoForge, keeping the genetics core pure Java, using hardcoded built-ins before JSON, and delaying Fabric until after the NeoForge MVP.

### Quality and test plans

```text
docs/quality/
```

These files define test plans, validation gates, manual playtest checklists, and release smoke tests.

### Art and asset pipeline

```text
docs/art/
```

This contains the asset pipeline plan, placeholder strategy, Blockbench workflow, optional future AI/skill tooling, and templates for asset requests.

### Release and distribution

```text
docs/release/
```

These files define release strategy, versioning, changelog conventions, platform publishing, GitHub/Modrinth/CurseForge plans, and release checklists.

## Recommended Repository Layout

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
│   ├── implementation/
│   ├── decisions/
│   ├── quality/
│   ├── art/
│   └── release/
├── common/
├── neoforge/
├── fabric/
├── build.gradle
├── settings.gradle
└── gradle.properties
```

If the initial build setup is not multiloader yet, the same documentation rules still apply. Keep package boundaries clean even in a temporary single-loader layout.

## Source of Truth Rules

Use the right document for the right kind of decision:

```text
README.md                    = project map and onboarding
CLAUDE.md                    = AI agent guardrails
Core docs                    = product and architecture reference
Implementation docs          = task execution source
ADRs                         = architectural decision source
Quality docs                 = validation and test source
Art docs                     = asset workflow source
Release docs                 = publishing source
Notion/backlog               = planning/status tracking
```

If the backlog and implementation docs disagree, do not guess. Review and update the docs before coding.

## Recommended Development Flow

```text
1. Read CLAUDE.md.
2. Read the relevant implementation phase document.
3. Read related specs and ADRs.
4. Pick one small task.
5. Implement only that task.
6. Run or describe tests.
7. Review the diff.
8. Update backlog/status.
9. Create follow-up tasks when needed.
```

## First Development Focus

Do not start with blocks, items, GUIs, textures, hives, or Minecraft event integration.

Start with the pure Java genetics core:

- `Allele`
- `Dominance`
- `ChromosomeType`
- `GenePair`
- `Genome`
- `GeneticRandom`
- `BreedingService`
- `MutationDefinition`
- `MutationService`
- Unit tests for dominance, active/inactive persistence, inheritance distribution, hybrid/purebred detection, and mutation probability

The detailed execution plan for this is in:

```text
docs/implementation/01-genetics-core-implementation.md
```

## MVP Definition

The MVP is complete when:

```text
- A vanilla bee can have a genome.
- Wild bees receive starting species.
- Two vanilla bees can breed in the world.
- The baby bee inherits genetics from both parents.
- Mutation can produce Cultivated or Hardy.
- The player can inspect the result.
- Species identity affects at least one basic production behavior.
```

## Important Guardrail

Do not let AI agents implement large unrelated systems in one pass.

Good:

```text
Implement only GenePair active/inactive resolution with tests.
```

Bad:

```text
Implement genetics, NeoForge storage, breeding, analyzer, apiary, assets, and Fabric support.
```

## Local Setup (Windows)

### Prerequisites

- JDK 21 installed (Temurin 21 recommended)
- Git installed

Check versions:

```powershell
java -version
git --version
```

If `java -version` does not show 21, set Java 21 for the current terminal session:

```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
java -version
```

### Run Gradle

From repository root:

```powershell
.\gradlew.bat tasks
.\gradlew.bat build
```

### Open the game client

```powershell
.\gradlew.bat runClient
```

The first run downloads Minecraft/NeoForge assets and can take several minutes.

### Troubleshooting

- `Unsupported class file major version 69`: you are running with Java 25; switch to Java 21 in the terminal and retry.
- `JAVA_HOME` points to wrong JDK: update `JAVA_HOME` and prepend `%JAVA_HOME%\bin` to `PATH`.
- Wrapper or dependency issues: delete `.gradle` in the project root and retry `.\gradlew.bat build`.
- Slow or failing first client run: rerun `.\gradlew.bat runClient` after assets finish downloading.

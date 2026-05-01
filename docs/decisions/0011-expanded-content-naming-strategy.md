# ADR-0011 - Expanded Content Naming Strategy

## Status

Accepted

## Date

2026-04-29

## Context

Phase 9 defines the roadmap for future Curious Bees content after the MVP and data-driven content pipeline.

Future content may include biome bees, managed/tech bees, harsh-environment bees, resource foundation bees, resource bees, compatibility bees, and exotic/endgame bees.

Names must support two competing goals:

- help players understand what a bee does;
- give Curious Bees its own identity instead of feeling like a Productive Bees-style resource list.

The MVP species remain simple and unchanged:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

This decision applies to future species, especially post-MVP and resource-adjacent content.

## Options Considered

### Option A - Direct Names

Examples:

```text
Iron Bee
Copper Bee
Gold Bee
Redstone Bee
Diamond Bee
Emerald Bee
```

Pros:

- immediately understandable;
- searchable;
- friendly to modpack players;
- easy to connect to production outputs.

Cons:

- feels close to existing resource bee mods;
- encourages thinking of bees as output recipes;
- weakens the genetics-first identity;
- can make early content feel like a checklist.

### Option B - Thematic Names

Examples:

```text
Ferric
Cupric
Aureate
Resonant
Crystalline
Verdant
```

Pros:

- stronger identity;
- better fantasy fit for genetic lineages;
- less directly tied to deterministic resource production;
- supports species that have roles beyond item output.

Cons:

- less immediately clear;
- needs analyzer/tooltips/guide text to avoid confusion;
- can become too obscure if overused.

### Option C - Hybrid Names

Examples:

```text
Ferric Bee (Iron)
Cupric Bee (Copper)
Resonant Bee (Redstone)
Crystalline Bee (Diamond)
```

Pros:

- thematic while remaining understandable;
- works well in analyzer output and guide entries;
- keeps species names distinct from output item names;
- helps modpack authors and players search for resource meaning.

Cons:

- display text can become longer;
- requires consistent formatting rules;
- needs localization discipline later.

## Decision

Use a hybrid naming strategy for resource-adjacent and late-game content:

```text
The species name should be thematic.
Player-facing explanatory text may include the plain meaning in parentheses or a tooltip.
```

Examples:

```text
Ferric Bee
Analyzer/guide hint: associated with iron-rich combs

Cupric Bee
Analyzer/guide hint: associated with copper-rich combs

Resonant Bee
Analyzer/guide hint: associated with redstone-like energy
```

For non-resource categories, prefer descriptive thematic names without parentheses when the meaning is already clear:

```text
Mire
Tropical
Alpine
Cavern
Managed
Industrious
Blazing
Void
```

Direct names are allowed only when clarity is more important than distinctiveness, but they should not be the default for resource bees.

## Naming Rules

### General Species Names

Species names should be:

- short enough for analyzer output;
- readable in tooltips;
- thematic but not opaque;
- distinct from existing MVP species;
- distinct from major existing bee mods where practical;
- tied to a species role, not only an item output.

### Biome And Environment Names

Use names that imply habitat or adaptation:

```text
Mire
Tropical
Frost
Alpine
Briny
Cavern
Nocturnal
```

Avoid names that are only biome labels if a stronger species identity exists.

### Managed And Tech Names

Use names that imply domestication, selection, stability, or work:

```text
Managed
Diligent
Industrious
Ordered
Stable
Refined
```

Avoid names that imply a simple linear upgrade unless the bee has real tradeoffs.

### Resource Foundation Names

Use broad material-theme names before direct resource names:

```text
Mineral
Metallic
Crystalline
Conductive
Resonant
Geologic
Deep
```

These should unlock later resource paths rather than produce major resources immediately.

### Resource Bee Names

Prefer thematic species names with clear explanation:

```text
Ferric - iron
Cupric - copper
Aureate - gold
Resonant - redstone
Crystalline - diamond
Verdant - emerald
```

Analyzer, guidebook, tooltip, or lang text should make the resource association clear.

### Compatibility Names

Compatibility names should fit both Curious Bees and the target mod without hardcoding external APIs into common genetics.

Avoid making compatibility species mandatory for the base mod.

## Consequences

### Positive

- Future species can feel like genetic lineages instead of item recipes.
- Resource bees remain understandable without copying direct naming patterns too closely.
- Analyzer and guide text can carry clarity while names carry flavor.
- Naming supports the "species identity first" design pillar.

### Negative

- Thematic names require better player-facing explanation.
- Some players may search for direct resource names and need tooltip/guide support.
- Localization will need consistent formatting later.

### Constraints

- Do not rename MVP species as part of this decision.
- Do not add resource bees as part of this decision.
- Do not use obscure names without analyzer/guide/tooltips to explain them.
- Do not let output item names replace species identity.

## Applies To

- expanded content roadmap;
- future species definitions;
- analyzer display text;
- guidebook/tooltips when added;
- future resource bee planning;
- localization conventions.

## Related Documents

- `docs/implementation/09-expanded-content-roadmap.md`
- `docs/implementation/09a-expanded-content-categories.md`
- `docs/mvp/05-content-design-spec.md`
- `docs/decisions/0007-no-resource-bees-in-mvp.md`

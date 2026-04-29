# 05 — Tech Apiary Assets (Phase 7I)

## 1. Purpose

This document defines the Phase 7I asset workflow for Tech Apiary features.

It keeps gameplay implementation unblocked while providing a clear path from
placeholder assets to final Blockbench-based assets.

## 2. Current Implementation State

The first Genetic Apiary implementation is already functional on NeoForge:

```text
- block registration and placement
- block entity with persistent output inventory
- production roll integration
- basic automation hooks
```

Visual polish is intentionally deferred.

## 3. Phase 7I Rule

Use placeholders first. Do not block gameplay tasks on polished art.

This applies to:

```text
- Genetic Apiary block visuals
- frame item visuals
- optional future machine visuals
```

## 4. Asset Scope for This Stage

### In Scope (Now)

```text
- document required runtime paths
- keep placeholder strategy explicit
- define Blockbench source layout
- define validation checklist
```

### Out of Scope (Now)

```text
- final polished textures
- animation pipeline
- full art pass across all items/blocks
- release-quality rendering polish
```

## 5. Runtime Paths (Canonical)

Assuming namespace `curiousbees`:

```text
neoforge/src/main/resources/assets/curiousbees/
├── blockstates/
│   └── genetic_apiary.json
├── models/
│   ├── block/
│   │   └── genetic_apiary.json
│   └── item/
│       └── genetic_apiary.json
├── textures/
│   ├── block/
│   │   └── genetic_apiary.png
│   └── item/
│       ├── basic_frame.png
│       ├── mutation_frame.png
│       └── productivity_frame.png
└── lang/
    └── en_us.json
```

If placeholders are used, keep the final file names/paths to avoid code changes later.

## 6. Blockbench Source Layout

Store editable source files outside runtime resources:

```text
assets-source/
└── blockbench/
    ├── genetic_apiary.bbmodel
    └── future_centrifuge.bbmodel
```

Never commit only exported JSON without committing the `.bbmodel` source.

## 7. Placeholder Strategy

### Genetic Apiary Block

Allowed placeholder levels:

```text
- simple cube model
- temporary neutral texture
- basic item model pointing to block model
```

### Frame Items (Future 7F)

Allowed placeholder levels:

```text
- generated flat textures
- simple color-coded placeholders
```

## 8. Validation Checklist

Before considering 7I complete:

```text
- gameplay remains unblocked by art tasks
- placeholder/final paths are namespaced correctly
- source and runtime assets are separated
- no missing-texture errors in dev client
- Blockbench source policy is documented
```

## 9. Follow-up Tasks

```text
TASK — Create genetic_apiary.bbmodel source
TASK — Export first custom apiary model JSON
TASK — Create placeholder frame textures for 7F
TASK — Validate all Phase 7 runtime asset references in-game
```

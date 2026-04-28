# Blockbench Model Brief Template

## Model Name

```text
[model_id]
```

## Purpose

```text
What gameplay object does this model represent?
```

## Phase

```text
[Phase number/name]
```

## Visual Style

```text
vanilla-adjacent
low-poly
Minecraft-readable
tech/apiculture themed
```

## Materials / Palette

```text
wood
wax/honey
copper/bronze
glass
dark slots
```

## Required Views

```text
world block
inventory item
GUI icon, if needed
```

## Source File

```text
assets-source/blockbench/[model_id].bbmodel
```

## Export Targets

```text
assets/<modid>/models/block/[model_id].json
assets/<modid>/textures/block/[model_id].png
assets/<modid>/blockstates/[model_id].json
```

## Constraints

```text
- keep geometry simple;
- avoid excessive nesting;
- texture paths must be relative/correct;
- exported model must load in Minecraft Java.
```

## Acceptance Criteria

```text
- [ ] .bbmodel exists.
- [ ] exported JSON exists.
- [ ] texture exists.
- [ ] blockstate exists.
- [ ] model loads in-game.
- [ ] no missing texture.
```

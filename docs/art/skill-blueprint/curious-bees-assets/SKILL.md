# Curious Bees Assets Skill Blueprint

This is a blueprint for a future Claude Code skill.

Do not activate this skill until asset work becomes frequent enough to justify it.

To activate later, copy this folder to:

```text
.claude/skills/curious-bees-assets/
```

## Purpose

Help Claude Code plan, wire, and validate Curious Bees assets without blocking gameplay implementation.

## When to Use

Use this skill when the user asks for:

```text
asset planning
placeholder item assets
Minecraft model JSON creation
blockstate JSON creation
lang key updates
asset path validation
Blockbench export review
```

## Project Rules

- Do not add polished art requirements to early genetics phases.
- Do not block implementation on final assets.
- Prefer placeholders for Analyzer MVP and Production MVP.
- Use Blockbench for custom blocks/machines, not for pure genetics code.
- Never claim visual quality has been validated unless the user or game test confirmed it.
- Do not create resource bee assets unless explicitly requested and the project is in expanded content phase.

## Expected Repository Structure

Source assets:

```text
assets-source/
├── blockbench/
├── textures-source/
└── references/
```

Runtime assets:

```text
src/main/resources/assets/<modid>/
├── textures/
│   ├── item/
│   └── block/
├── models/
│   ├── item/
│   └── block/
├── blockstates/
└── lang/
```

## Default Item Model Template

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "<modid>:item/<item_id>"
  }
}
```

## Asset Review Checklist

Before finishing an asset task, check:

```text
- texture exists;
- model JSON exists;
- model references the correct texture;
- lang key exists;
- namespace is consistent;
- no uppercase filenames;
- source file exists when applicable;
- placeholders are clearly tracked.
```

## Workflow: Create Placeholder Item Asset

1. Confirm item id and namespace.
2. Create or expect placeholder PNG path.
3. Create item model JSON.
4. Add lang entry.
5. Do not create gameplay code unless explicitly requested.
6. Report missing PNGs as TODOs if the image file does not exist.

## Workflow: Review Blockbench Export

1. Check `.bbmodel` source path.
2. Check exported JSON path.
3. Check texture references.
4. Check blockstate reference.
5. Check item model if block appears in inventory.
6. Ask the user to verify the visual result in-game.

## Non-Goals

Do not:

```text
- design the full visual identity alone;
- use external MCP tools without user approval;
- overwrite source art without review;
- add new gameplay features;
- add future bee species or resource bee assets casually.
```

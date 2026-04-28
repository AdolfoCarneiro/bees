# Asset Review Checklist

Use this checklist before marking an asset-related task as done.

## General

```text
[ ] Asset id uses lowercase snake_case.
[ ] Namespace is correct and consistent.
[ ] Runtime path matches expected Minecraft resource structure.
[ ] Source file is stored when applicable.
[ ] Placeholder/final status is clear.
[ ] No unrelated assets were added.
```

## Item Assets

```text
[ ] Texture exists under textures/item/.
[ ] Model JSON exists under models/item/.
[ ] Model JSON references the correct texture.
[ ] Lang entry exists.
[ ] Item loads in-game without missing texture.
```

## Block Assets

```text
[ ] Texture exists under textures/block/.
[ ] Block model exists under models/block/.
[ ] Blockstate exists under blockstates/.
[ ] Block item model exists if needed.
[ ] Lang entry exists.
[ ] Block loads in-game without missing texture.
[ ] Inventory representation is acceptable.
```

## Blockbench Assets

```text
[ ] .bbmodel source file is committed.
[ ] Exported JSON is committed.
[ ] Texture references are relative/correct.
[ ] Model follows Java block/item model constraints.
[ ] Visual result checked in-game.
```

## AI/Automation

```text
[ ] Claude did not claim visual validation without actual review.
[ ] Generated files were reviewed.
[ ] No script or MCP overwrote source files unexpectedly.
[ ] Follow-up polish tasks were created if placeholder is temporary.
```

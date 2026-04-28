# 04 — AI Asset Tooling Plan

## 1. Purpose

This document decides how Curious Bees should use AI tools for asset work.

It specifically addresses whether asset work should use:

```text
Claude Code prompts
Claude Code slash commands
Claude Code skills
Cursor
Blockbench
Blockbench MCP
other image/model generation tools
```

## 2. Current Recommendation

Do not create a complex Blockbench MCP workflow yet.

Start with:

```text
manual placeholders
Claude Code for resource file wiring
Cursor for review
Blockbench manually when custom blocks begin
```

Add automation only after the workflow repeats enough to justify it.

## 3. Tooling Ladder

Use the simplest tool that works.

### Level 1 — Plain Prompt

Use when the task is rare or experimental.

Example:

```text
Create JSON model files for these placeholder item textures.
```

### Level 2 — Claude Code Slash Command

Use when the task is repeated but simple.

Good candidates:

```text
/asset-check
/create-placeholder-item
/create-item-model
```

Slash commands are good for one-file reusable prompt templates.

### Level 3 — Claude Code Skill

Use when the workflow is complex and needs structured reference material.

Good candidate:

```text
Curious Bees Asset Pipeline Skill
```

Could include:

```text
asset naming rules
resource folder rules
model JSON templates
blockstate templates
review checklist
validation script
Blockbench export guide
```

### Level 4 — MCP Integration

Use when Claude needs to interact with an external app/service.

Possible future use:

```text
Blockbench MCP
image generation service MCP
asset validation MCP
```

Only use after permissions and risks are understood.

## 4. Why Not Start with MCP?

MCP can be powerful, but it adds:

```text
setup complexity
security/permission questions
tool reliability issues
workflow coupling
harder debugging
```

For early Curious Bees, most asset needs are simple placeholders.

A full MCP flow is overkill until custom block/machine models are frequent.

## 5. When a Skill Makes Sense

A Claude Code skill makes sense when the project needs a repeated workflow such as:

```text
Given an asset ID, create:
- item model JSON
- optional block model JSON
- blockstate JSON
- lang entry
- placeholder path
- checklist for missing textures
```

This workflow is useful, repeatable, and project-specific.

## 6. Recommended Skill Scope

Future skill name:

```text
curious-bees-assets
```

Location if activated:

```text
.claude/skills/curious-bees-assets/SKILL.md
```

Skill responsibilities:

```text
- explain Curious Bees asset conventions;
- generate asset integration plans;
- create item/block model JSON;
- update lang entries;
- validate resource path consistency;
- create TODOs for missing textures;
- warn when Blockbench is required;
- avoid generating polished art claims.
```

Skill non-goals:

```text
- do not claim to visually validate art;
- do not create complex models without Blockbench/source files;
- do not overwrite source assets without review;
- do not introduce gameplay code;
- do not add resource bees or future assets unless requested.
```

## 7. Recommended Slash Commands

Before a full skill, these project slash commands may be enough:

```text
.claude/commands/asset-plan.md
.claude/commands/asset-check.md
.claude/commands/create-item-model.md
```

### `/asset-plan`

Purpose:

```text
Turn a feature/task into a list of required assets and placeholders.
```

### `/asset-check`

Purpose:

```text
Check existing asset paths, model JSON references, lang keys, and likely missing textures.
```

### `/create-item-model`

Purpose:

```text
Given an item id, create the basic generated item model JSON and lang entry.
```

## 8. Cursor Usage

Cursor is probably better than Claude Code for quick visual resource-tree editing.

Use Cursor for:

```text
checking file paths
opening JSONs side by side
reviewing diffs
quick renames
manual corrections
```

Use Claude Code for:

```text
multi-file repetitive updates
explaining expected structure
generating prompts/checklists
running scripted validation
```

## 9. Asset Validation Script Idea

Future script:

```text
scripts/validate-assets.py
```

Checks:

```text
- every item model references an existing texture;
- every blockstate references an existing model;
- every block model references existing textures;
- every registered item/block has a lang key;
- no accidental namespace mismatch;
- no uppercase file names;
- no spaces in asset file names.
```

This script would be useful for a Claude Code skill.

## 10. Skill Activation Decision

Do not activate the skill immediately unless you want Claude Code to use it.

Safer approach:

```text
1. Keep skill blueprint in docs/art/skill-blueprint/.
2. Use it manually as reference.
3. After repeated asset work, copy it to .claude/skills/curious-bees-assets/SKILL.md.
4. Add validation scripts only when resources exist.
```

## 11. Decision Gates

### Gate 1 — Simple Prompt or Skill?

Use a simple prompt if:

```text
only one asset
one-time task
no validation script needed
```

Use a skill if:

```text
asset work becomes repeated
multiple file types are involved
validation checklists are useful
Claude should discover the workflow automatically
```

### Gate 2 — Skill or MCP?

Use a skill if:

```text
Claude only needs repo files and docs.
```

Use MCP if:

```text
Claude needs to interact with Blockbench or another external tool.
```

### Gate 3 — Manual Blockbench or MCP Blockbench?

Use manual Blockbench if:

```text
you are designing visually
you need creative control
the model is important
```

Use MCP only if:

```text
the workflow is repetitive
the operation is low-risk
permission boundaries are clear
the output is reviewed in git
```

## 12. Final Recommendation

For Curious Bees:

```text
Now:
- document the pipeline;
- use placeholders;
- no MCP.

Analyzer/Production MVP:
- add simple item textures and generated item models;
- maybe use slash commands.

Tech Apiary:
- use Blockbench manually;
- consider skill for asset wiring/checking.

After repeated asset tasks:
- activate Curious Bees asset skill.

Only much later:
- evaluate Blockbench MCP.
```

# Asset Generation Guidelines

How to create, request, and commit visual / audio assets for Curious Bees. Hard rule first, process second, prompt template last.

> Related: [`requirements.md` §7](requirements.md#7-asset-requirements), [`decisions.md` → ADR-0005](decisions.md), [`TASKS.md` → E1.A / E5](TASKS.md).

---

## 1. Hard rules

- **HR-1 — No silent placeholder-as-final.** Every dev placeholder must be tagged `DEV-PLACEHOLDER` in code or a model `_comment` so a single grep finds it before a release. No texture/sound that looks finished may be committed unless it **is** the finished asset.
- **HR-2 — Never crash on missing assets.** Code paths that read a texture / lang key / sound must fall back **visibly** (vanilla bee texture, generic icon, silent) and **log a WARNING**.
- **HR-3 — Genetics core stays asset-free.** No `common/genetics` code may reference textures, models, sounds, or `ResourceLocation`. Asset wiring lives under `neoforge/` (and future `fabric/`).
- **HR-4 — Don't block early phases on art.** Per [`decisions.md` → ADR-0005](decisions.md), polished assets are not on the critical path before the loop they support is playable.
- **HR-5 — Resource pack hygiene.** All filenames follow [`decisions.md` → ADR-0011 (naming)](decisions.md). No mixed casing, no spaces, no platform-specific separators in asset paths.

## 2. Asset categories & where they live

| Category | Path | Notes |
|----------|------|-------|
| **Living-bee textures** | `neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/<species_id>.png` | Single 64×64 sheet; resolved via species → texture key map (see [`architecture.md` §7](architecture.md)). |
| **Item icons** (combs, frames, analyzer, machines) | `assets/curiousbees/textures/item/<item_id>.png` | 16×16. |
| **Block textures** (nests, apiary parts, centrifuge) | `assets/curiousbees/textures/block/<block_id>/...` | Folder per block; matches model files. |
| **Block models / blockstates** | `assets/curiousbees/models/block/...`, `assets/curiousbees/blockstates/...` | Built in Blockbench when custom geometry is needed. |
| **GUI backgrounds** | `assets/curiousbees/textures/gui/<screen_id>.png` | Replace dev placeholders during E5. |
| **Sounds** | `assets/curiousbees/sounds/...` + `assets/curiousbees/sounds.json` | OGG; non-grating volume; distinct enough between machines. |
| **Lang keys** | `assets/curiousbees/lang/<locale>.json` | English required; other locales optional. Missing keys → WARNING + fallback to key name. |

## 3. When you may use a placeholder

Placeholders are **encouraged** during structural work and **forbidden** in a release-tagged build for an area whose phase has been marked complete in [`roadmap.md`](roadmap.md).

Acceptable placeholder forms:

- a tinted vanilla texture (with `_comment` in the model JSON);
- a flat-color sprite (with `DEV-PLACEHOLDER` in the file name or in an adjacent code comment);
- a vanilla GUI reused as background (with a `DEV PLACEHOLDER` Java comment over the texture constant).

Unacceptable forms:

- art that looks "good enough" but was never reviewed;
- AI-generated sprites committed without a tag, manifest entry, or PR description noting the source;
- "TODO replace" comments without the `DEV-PLACEHOLDER` token.

## 4. Process for a new asset

### 4.1 Trigger

You're opening a task in [`TASKS.md`](TASKS.md) (typically E1.A, E2.B, E3.A, E3.B, E4.A, E5.\*) that needs an asset that does not exist or is currently a placeholder.

### 4.2 Steps

1. **Check structure first.** If the consumer code (renderer, model, screen) is not yet ready to receive a real asset, ship the structural change with a labeled placeholder; do not commission art ahead of consumer code.
2. **Write a prompt / request** using the template in §6. It can live in a GitHub issue, a PR description, or a temporary scratch file — there is **no** fixed `docs/art/` folder in this repo right now (see [`decisions.md` → ADR-0005](decisions.md)).
3. **Generate or commission.** Blockbench for custom blocks/machines; pixel art tools for sprites; AI tools allowed if the prompt is recorded and the maintainer reviews the result.
4. **Drop into the right asset path** (see §2). Naming follows ADR-0011.
5. **Wire the asset.** Add to model / blockstate / lang as needed. Add to the species → texture key map for entities.
6. **Remove the `DEV-PLACEHOLDER` tag** for the asset you just replaced. Run `rg DEV-PLACEHOLDER` (or `Grep`) in the PR to confirm scope.
7. **Test multiplayer.** Texture/atlas issues often only show on dedicated servers or with mods reordering registries.
8. **Commit** with prefix `client:` (assets + screen wiring), `neoforge:` (block/item registration), or `docs:` (only if updating these guidelines).

### 4.3 Definition of done for an asset task

- File present in the right path with the right name.
- Consumer code wired (model JSON, screen constant, lang key).
- No `DEV-PLACEHOLDER` left for that asset (`rg DEV-PLACEHOLDER` clean for the affected files).
- Renders correctly in single-player **and** on a dedicated server (smoke test).
- License / source noted in the PR description if external (commissioned, AI-generated, or third-party).

## 5. Style guidance

These are defaults, not laws. Override in a PR description when a specific asset needs to break style on purpose.

- **Pixel scale.** 16 px per block face for items/blocks unless a screen layout requires more.
- **Bee entity.** 64×64 atlas matching vanilla bee UV layout. Keep silhouette readable from 4-block distance.
- **Palette.** Per species, define 3–5 hex codes; list them in the prompt so re-renders stay consistent.
- **Identity.** Avoid colors / shapes that read as a Productive Bees / Forestry sprite at a glance — Curious Bees has its own face (see [`project-guide.md`](project-guide.md)).
- **GUIs.** Match the genetic apiary's visual language across screens (apiary, analyzer, centrifuge). Do not introduce a new GUI style per screen.
- **Sound.** Short loops; never grating; respect player ambient volume.

## 6. Prompt template

Copy this into your issue / PR / scratch note. Fill every section before commissioning the asset.

```text
Asset:        <species_id / item_id / block_id / gui_id / sound_id>
Target path:  assets/curiousbees/<...>/<file>.<png|ogg|json>
Size:         <e.g. 16x16 / 64x64 / 256x256 / N seconds OGG mono>
Style notes:  <pixel art / hand-drawn / vanilla-leaning / etc.>
Palette:      <list of hex codes — 3 to 5 entries>
References:   <vanilla bee, vanilla beehive, in-repo neighbor file, etc.>
Negative:     <what it must NOT look like — e.g. PB clone, photographic, etc.>
Status:       PENDING / IN-REVIEW / FINAL
Source:       <hand / Blockbench / AI tool name + prompt summary / commission>
License:      <CC0 / mod-internal / other — required if external>
```

## 7. Removal & cleanup

- Before tagging a release, run a placeholder sweep: `rg DEV-PLACEHOLDER` across the repo. If any hit lives in an area whose phase exit in [`roadmap.md`](roadmap.md) is complete, fix it or downgrade the release.
- Old / unused assets must be removed from the resource folder when their consumer code is removed; orphan textures bloat the jar.

## 8. Things that are out of scope here

- Marketing assets (promo images, mod page hero shots, trailers) — handle in the release process, not in this file.
- Fabric-specific asset paths — covered when DR-010 is greenlit ([`decisions.md` → DR-010](decisions.md)).
- A bundled asset manifest under `docs/` — intentionally not in this repo right now; track via PR descriptions / issues.

_Last updated: 2026-05-04._

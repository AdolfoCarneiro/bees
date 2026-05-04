> **Obsolete checklist.** This file described an older docs layout. **Current direction:** [`docs/post-mvp/gameplay-direction.md`](../../post-mvp/gameplay-direction.md) and [`docs/ROADMAP.md`](../../ROADMAP.md). Kept for historical context only.

# Docs Cleanup Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Remover tudo que foi necessário para validar o MVP mas não tem mais utilidade para evoluir o produto — deixando apenas o que um agente ou desenvolvedor precisa para continuar buildando.

**Architecture:** Phases 11–15 do roadmap pós-MVP estão implementadas. O projeto entra agora na era de expansão de conteúdo (Phase 16/17+). A documentação ainda carrega ~30 arquivos de planejamento/execução do MVP que já são história. Limpeza por deleção direta (não archiving — o git guarda o histórico).

**Tech Stack:** Markdown, PowerShell/Bash file ops, git

---

## Contexto: o que foi feito vs. o que vem a seguir

Feito (confirmado por git log):
- Phase 13 — Analyzer UX and Progression
- Phase 14 — Genetic Apiary GUI and Bee Display
- Phase 15 — Content and Asset Pipeline
- Phase 12 — Visual Species System (dependência das anteriores, portanto feita)
- Phase 11.5 — Species Bee Nests and Habitat System (idem)

Próximo:
- Phase 16 — Frame Behavior (frames com efeitos reais)
- Phase 17 — First Expanded Species Branch (Cultivated/Managed)
- Phase 11.6 — Custom Bee Entity Architecture (ADR/design only)

---

## Task 1: Deletar docs MVP completados (implementation/ phases 00–10 + review files)

**Files:**
- Delete: `docs/implementation/00-phase-0-documentation-and-decisions.md`
- Delete: `docs/implementation/01-genetics-core-implementation.md`
- Delete: `docs/implementation/02-initial-content-implementation.md`
- Delete: `docs/implementation/03-neoforge-entity-integration.md`
- Delete: `docs/implementation/04-vanilla-breeding-integration.md`
- Delete: `docs/implementation/05-analyzer-implementation.md`
- Delete: `docs/implementation/06-production-mvp.md`
- Delete: `docs/implementation/07-tech-apiary-and-automation.md`
- Delete: `docs/implementation/07j-phase-7-review-and-balancing.md`
- Delete: `docs/implementation/08-data-driven-content.md`
- Delete: `docs/implementation/08k-phase-8-exit-review.md`
- Delete: `docs/implementation/09-expanded-content-roadmap.md`
- Delete: `docs/implementation/09a-expanded-content-categories.md`
- Delete: `docs/implementation/09c-expanded-content-asset-roadmap.md`
- Delete: `docs/implementation/09d-phase-9-exit-review.md`
- Delete: `docs/implementation/10-fabric-support-implementation.md`

Nota: `09b` e `bee-species-nest-targeting-and-nectar.md` são tratados em tasks separadas.

- [ ] **Step 1: Confirmar que nenhum dos arquivos é referenciado em CLAUDE.md de forma que quebraria a leitura**

Verificar via grep:
```powershell
Select-String -Path "C:\Users\Adolfo\source\repos\bees\CLAUDE.md" -Pattern "00-phase|01-genetics-core-impl|02-initial-content-impl|03-neoforge-entity|04-vanilla-breeding|05-analyzer-impl|06-production-mvp|07-tech-apiary|08-data-driven|09-expanded|10-fabric-support"
```
Esperado: matches apenas em seções já marcadas para remoção no Task 5 (CLAUDE.md update).

- [ ] **Step 2: Deletar os 16 arquivos**

```powershell
$base = "C:\Users\Adolfo\source\repos\bees\docs\implementation"
@(
  "00-phase-0-documentation-and-decisions.md",
  "01-genetics-core-implementation.md",
  "02-initial-content-implementation.md",
  "03-neoforge-entity-integration.md",
  "04-vanilla-breeding-integration.md",
  "05-analyzer-implementation.md",
  "06-production-mvp.md",
  "07-tech-apiary-and-automation.md",
  "07j-phase-7-review-and-balancing.md",
  "08-data-driven-content.md",
  "08k-phase-8-exit-review.md",
  "09-expanded-content-roadmap.md",
  "09a-expanded-content-categories.md",
  "09c-expanded-content-asset-roadmap.md",
  "09d-phase-9-exit-review.md",
  "10-fabric-support-implementation.md"
) | ForEach-Object { Remove-Item "$base\$_" -Confirm:$false }
```
Esperado: nenhum erro; arquivos sumidos.

- [ ] **Step 3: Confirmar deleção**

```powershell
Get-ChildItem "C:\Users\Adolfo\source\repos\bees\docs\implementation" | Select-Object Name
```
Esperado: apenas `11`, `12`, `13`, `14`, `15`, `bee-species-nest-targeting-and-nectar.md` restantes.

- [ ] **Step 4: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add -A docs/implementation/
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: remove completed MVP implementation specs (phases 00–10)"
```

---

## Task 2: Deletar docs MVP completados (docs/mvp/ — 01, 06, 07)

**Files:**
- Delete: `docs/mvp/01-product-vision-and-roadmap.md` (supersedido pelo post-mvp roadmap)
- Delete: `docs/mvp/06-ai-coding-guidelines.md` (conteúdo já está em CLAUDE.md)
- Delete: `docs/mvp/07-initial-backlog.md` (todas as tasks MVP concluídas)
- Keep: `docs/mvp/02`, `03`, `04`, `05` (fundação arquitetural ainda válida)

- [ ] **Step 1: Deletar os 3 arquivos**

```powershell
$base = "C:\Users\Adolfo\source\repos\bees\docs\mvp"
Remove-Item "$base\01-product-vision-and-roadmap.md" -Confirm:$false
Remove-Item "$base\06-ai-coding-guidelines.md" -Confirm:$false
Remove-Item "$base\07-initial-backlog.md" -Confirm:$false
```

- [ ] **Step 2: Verificar o que sobrou em docs/mvp/**

```powershell
Get-ChildItem "C:\Users\Adolfo\source\repos\bees\docs\mvp" | Select-Object Name
```
Esperado: apenas `02`, `03`, `04`, `05`.

- [ ] **Step 3: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add -A docs/mvp/
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: remove completed MVP vision, backlog, and AI guidelines"
```

---

## Task 3: Deletar test plans do MVP (docs/quality/ — 01–10)

**Files:**
- Delete: `docs/quality/01-testing-and-validation-plan.md`
- Delete: `docs/quality/02-genetics-core-test-plan.md`
- Delete: `docs/quality/03-initial-content-test-plan.md`
- Delete: `docs/quality/04-neoforge-entity-integration-test-plan.md`
- Delete: `docs/quality/05-vanilla-breeding-test-plan.md`
- Delete: `docs/quality/06-analyzer-test-plan.md`
- Delete: `docs/quality/07-production-mvp-test-plan.md`
- Delete: `docs/quality/08-tech-apiary-test-plan.md`
- Delete: `docs/quality/09-data-driven-content-test-plan.md`
- Delete: `docs/quality/10-fabric-support-test-plan.md`
- Keep: `README.md`, `11-manual-playtest-checklists.md`, `12-release-smoke-test.md`

- [ ] **Step 1: Deletar os 10 arquivos**

```powershell
$base = "C:\Users\Adolfo\source\repos\bees\docs\quality"
1..10 | ForEach-Object {
  $files = Get-ChildItem "$base\0$_-*.md","$base\$_-*.md" -ErrorAction SilentlyContinue
  $files | Remove-Item -Confirm:$false
}
```

- [ ] **Step 2: Verificar**

```powershell
Get-ChildItem "C:\Users\Adolfo\source\repos\bees\docs\quality" | Select-Object Name
```
Esperado: `README.md`, `11-manual-playtest-checklists.md`, `12-release-smoke-test.md`.

- [ ] **Step 3: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add -A docs/quality/
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: remove completed MVP phase test plans"
```

---

## Task 4: Mover docs de implementação pós-MVP concluídas (09b e bee-nest-targeting)

**Files:**
- Move: `docs/implementation/09b-first-post-mvp-mutation-branch.md` → `docs/post-mvp/17-cultivated-branch-design-input.md`
  - Razão: contém design útil para Phase 17 (First Expanded Species Branch), mas não é um "plano de implementação" em andamento — é input de design para uma fase futura.
- Move: `docs/implementation/bee-species-nest-targeting-and-nectar.md` → `docs/reference/bee-nest-targeting-behavior.md`
  - Razão: é uma nota técnica sobre comportamento vivo do sistema atual, não um plano de implementação. Merece uma localização mais apropriada.

- [ ] **Step 1: Criar o diretório docs/reference/ se não existir**

```powershell
New-Item -ItemType Directory -Force "C:\Users\Adolfo\source\repos\bees\docs\reference"
```

- [ ] **Step 2: Mover os dois arquivos**

```powershell
$base = "C:\Users\Adolfo\source\repos\bees"
Move-Item "$base\docs\implementation\09b-first-post-mvp-mutation-branch.md" `
          "$base\docs\post-mvp\17-cultivated-branch-design-input.md"
Move-Item "$base\docs\implementation\bee-species-nest-targeting-and-nectar.md" `
          "$base\docs\reference\bee-nest-targeting-behavior.md"
```

- [ ] **Step 3: Adicionar cabeçalho de contexto ao topo de 17-cultivated-branch-design-input.md**

Adicionar no topo do arquivo:
```markdown
> **Status:** Design input para Phase 17 — First Expanded Species Branch.
> Não implementado. Consulte `docs/post-mvp/11-post-mvp-productization-roadmap.md` (Phase 17) antes de executar.
```

- [ ] **Step 4: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add -A docs/implementation/ docs/post-mvp/ docs/reference/
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: move mutation branch design input and bee-nest behavior note to correct homes"
```

---

## Task 5: Deletar implementation docs de phases pós-MVP concluídas (12–15)

**Contexto:** Os arquivos `docs/implementation/12-15` foram os planos de execução das phases já buildadas. O código agora é a fonte da verdade. Os spec docs em `docs/post-mvp/12-15` (design intent) continuam existindo.

**Files:**
- Delete: `docs/implementation/12-visual-species-system-implementation.md`
- Delete: `docs/implementation/13-analyzer-ux-implementation.md`
- Delete: `docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md`
- Delete: `docs/implementation/15-content-and-asset-pipeline-implementation.md`

**Nota:** `docs/implementation/11-post-mvp-productization-foundation-implementation.md` — verificar antes de deletar (pode ter informações sobre o setup da phase 11 que ainda seja referência).

- [ ] **Step 1: Ler rapidamente o 11-post-mvp doc para decidir**

Ler as primeiras 30 linhas de `docs/implementation/11-post-mvp-productization-foundation-implementation.md`.
Se for apenas tasks de reorganização de docs (feitas), deletar. Se contiver regras arquiteturais ainda válidas, manter.

- [ ] **Step 2: Deletar os 4 implementation docs (+ 11 se confirmado como feito)**

```powershell
$base = "C:\Users\Adolfo\source\repos\bees\docs\implementation"
Remove-Item "$base\12-visual-species-system-implementation.md" -Confirm:$false
Remove-Item "$base\13-analyzer-ux-implementation.md" -Confirm:$false
Remove-Item "$base\14-genetic-apiary-gui-and-frames-implementation.md" -Confirm:$false
Remove-Item "$base\15-content-and-asset-pipeline-implementation.md" -Confirm:$false
# Adicionar 11 aqui se decidido no Step 1:
# Remove-Item "$base\11-post-mvp-productization-foundation-implementation.md" -Confirm:$false
```

- [ ] **Step 3: Verificar o que sobrou em docs/implementation/**

```powershell
Get-ChildItem "C:\Users\Adolfo\source\repos\bees\docs\implementation" | Select-Object Name
```
Esperado: apenas `bee-nest-targeting-behavior.md` foi movido, e o dir deve estar vazio (ou só com o 11 se mantido).

- [ ] **Step 4: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add -A docs/implementation/
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: remove completed post-MVP phase implementation plans (12–15)"
```

---

## Task 6: Atualizar CLAUDE.md

**Goal:** CLAUDE.md deve refletir onde o projeto está agora — phases 12-15 feitas, entrando em fase de expansão de conteúdo.

**Modify:** `CLAUDE.md`

Mudanças necessárias:

1. **"Required Reading Before Coding"** — remover referências a docs deletados; adicionar nota de phases concluídas.

2. **"Current Post-MVP Priorities"** — atualizar para refletir que visual, analyzer, apiary e content pipeline estão buildados; novo foco = frames, species expansion.

3. **"Suggested Post-MVP Implementation Order"** — marcar ou remover os items já feitos (steps 1–18 do roadmap original).

4. **Task-specific additions** — remover referências a implementation docs deletados (12-15 implementation).

5. **Adicionar seção "Current Phase Status"** — resumo rápido do que foi feito vs. o que vem a seguir.

- [ ] **Step 1: Ler CLAUDE.md completo para identificar todas as referências a deletar**

Buscar por:
- `docs/mvp/01`
- `docs/mvp/06`
- `docs/mvp/07`
- `docs/implementation/1[2345]`
- `docs/implementation/0[0-9]`
- Seção "Suggested Post-MVP Implementation Order"
- Seção "Current Post-MVP Priorities"

- [ ] **Step 2: Reescrever as seções afetadas**

**Seção "Current Post-MVP Priorities" — novo conteúdo:**
```markdown
## Current Post-MVP Priorities

Phases 12–15 are complete:
- Phase 12: visual species system (species textures on living bees)
- Phase 13: analyzer UX and progression (portable analyzer, analysis state)
- Phase 14: genetic apiary GUI (full apiary GUI with bee display)
- Phase 15: content and asset pipeline (species authoring foundation)

Current focus:
- Phase 16: frame behavior (frames with real genetic/production effects)
- Phase 17: first expanded species branch (Cultivated/Managed mutation tree)
- Phase 11.6: custom bee entity architecture (design/ADR only)

Do not start Phase 17 before Phase 16 is solid.
```

**Seção "Suggested Post-MVP Implementation Order" — substituir por:**
```markdown
## Suggested Next Steps

Phase 16 — Frame Behavior:
- Define frame effects (Productivity, Mutation, Stability, Fertility)
- Implement frame modifier calculations
- Connect frames to apiary production output

Phase 17 — First Expanded Species Branch:
- Read docs/post-mvp/17-cultivated-branch-design-input.md
- Design visual identity per new species before implementation
- Create asset prompt docs before any texture work
- Implement data + visual + habitat for each new species

Phase 11.6 — Custom Bee Entity Architecture (design only):
- Author the ADR at docs/post-mvp/11-6-custom-bee-entity-architecture.md
- Do not implement yet
```

**"Required Reading Before Coding" — task-specific additions — remover:**
```
Visual species work:
- docs/implementation/12-visual-species-system-implementation.md   ← REMOVE

Analyzer work:
- docs/implementation/13-analyzer-ux-implementation.md   ← REMOVE

Apiary/frames work:
- docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md   ← REMOVE

Content/assets work:
- docs/implementation/15-content-and-asset-pipeline-implementation.md   ← REMOVE
```

- [ ] **Step 3: Aplicar as edições no CLAUDE.md**

Usar Edit tool para cada mudança (ler o arquivo primeiro, identificar os blocos exatos, substituir).

- [ ] **Step 4: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add CLAUDE.md
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: update CLAUDE.md to reflect phases 12–15 complete and next priorities"
```

---

## Task 7: Atualizar docs/README.md e marcar phases concluídas no roadmap

**Files:**
- Modify: `docs/README.md`
- Modify: `docs/post-mvp/11-post-mvp-productization-roadmap.md`

- [ ] **Step 1: Ler docs/README.md**

Identificar se menciona MVP phases ou implementation docs deletados.

- [ ] **Step 2: Atualizar README.md**

Adicionar nota de status atual após a lista de leitura essencial:
```markdown
## Current Status (May 2026)

Phases 12–15 implemented. The mod has:
- species-specific textures on living bees
- portable analyzer with analysis state
- genetic apiary GUI
- content and asset pipeline foundation

Next: Phase 16 (frames) → Phase 17 (species expansion).
```

- [ ] **Step 3: Atualizar roadmap 11 — marcar phases concluídas**

No `docs/post-mvp/11-post-mvp-productization-roadmap.md`, adicionar marcadores de status nos cabeçalhos das phases:
- Phase 11 → `[DONE]`
- Phase 11.5 → `[DONE]`
- Phase 12 → `[DONE]`
- Phase 13 → `[DONE]`
- Phase 14 → `[DONE]`
- Phase 15 → `[DONE]`
- Phase 16 → `[NEXT]`
- Phase 17 → `[PLANNED]`

- [ ] **Step 4: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add docs/README.md "docs/post-mvp/11-post-mvp-productization-roadmap.md"
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: update README and roadmap to reflect phases 12–15 complete"
```

---

## Task 8 (Opcional): Criar skill `new-bee-species`

**Contexto:** Com o pipeline de conteúdo estabelecido, adicionar uma nova espécie requer múltiplas peças que precisam estar em sincronia. Uma skill garante que nenhuma peça seja esquecida.

**File:**
- Create: `.claude/plugins/local/skills/new-bee-species.md`

Conteúdo da skill:
```markdown
---
name: new-bee-species
description: Guia completo para adicionar uma nova espécie de abelha ao mod — data definition, texture prompt doc, habitat config, mutations, localization, e art manifest entry.
---

# New Bee Species Checklist

When adding a new bee species, ALL of the following must be created or updated:

## 1. Species Data Definition
File: `src/main/resources/data/curiousbees/species/<id>.json`
- id, display_name, dominance, default_traits
- visual profile (texture path)
- optional habitat definition

## 2. Texture Prompt Document
File: `docs/art/prompts/species/textures-entity-bee-<id>.md`
- Use template from `docs/art/templates/asset-request-template.md`
- Include: target path, size (64x64), style notes, palette with hex codes, UV reference
- DO NOT commit placeholder texture as final asset

## 3. Habitat Definition (if world-spawnable)
File: included in species JSON OR `data/curiousbees/habitat/<id>.json`
- biome tags for spawning
- nest block type

## 4. Mutation Entries
File: `src/main/resources/data/curiousbees/mutations/<mutation_id>.json`
- parent species IDs (order-independent)
- result species ID
- base mutation chance

## 5. Localization
File: `src/main/resources/assets/curiousbees/lang/en_us.json`
- `species.curiousbees.<id>` key with display name

## 6. Asset Manifest
File: `docs/art/asset-manifest.md`
- Add entry for the new texture (status: PENDING until asset provided)

## Guardrails
- Do NOT implement without a texture prompt doc ready
- Do NOT add resource-output species without explicit phase approval
- Read docs/mvp/05-content-design-spec.md for trait defaults reference
- Read docs/art/asset-prompt-workflow.md for the full asset workflow
```

- [ ] **Step 1: Verificar se o diretório .claude/plugins/local/skills/ existe**

```powershell
Test-Path "C:\Users\Adolfo\.claude\plugins\local\skills"
```

- [ ] **Step 2: Criar a skill file** (usar Write tool)

- [ ] **Step 3: Commit**

```powershell
git -C "C:\Users\Adolfo\source\repos\bees" add .claude/
git -C "C:\Users\Adolfo\source\repos\bees" commit -m "docs: add new-bee-species skill checklist"
```

---

## Self-Review

### Spec Coverage
- [x] Deletar docs MVP completos (impl 00-10) → Tasks 1, 2, 3
- [x] Deletar docs qualidade MVP (quality 01-10) → Task 3
- [x] Mover docs de lugar errado → Task 4
- [x] Deletar impl docs post-MVP concluídas (12-15) → Task 5
- [x] Atualizar CLAUDE.md → Task 6
- [x] Atualizar README e roadmap → Task 7
- [x] Criar skill para novo conteúdo → Task 8

### O que NÃO é tocado (intencionalmente)
- `docs/decisions/` — ADRs são histórico por natureza, ficam todos
- `docs/art/` — pipeline ativo, fica tudo
- `docs/release/` — estratégia de release futura, fica tudo
- `docs/content/` — guias de authoring, ficam todos
- `docs/mvp/02–05` — fundação arquitetural ainda 100% válida
- `docs/post-mvp/10-5, 12–15` — specs de design de referência, ficam todos

### Total de operações
- ~30 arquivos deletados
- ~3 arquivos atualizados
- ~2 arquivos movidos
- 1 skill criada
- 1 dir criado (docs/reference/)

<!-- generated-by: gsd-doc-writer -->
# Curious Bees — Referência de Features

> Ícones de status: ✅ Implementado | 🔧 Parcial / Em andamento | 📋 Planejado

---

## Conceito central

Curious Bees é um mod de **genética de abelhas para NeoForge 1.21.1**. A proposta é diferente de mods como Productive Bees ou Forestry: as abelhas são **entidades vivas vanilla** que carregam genomas — cruzamento Mendeliano, dominância alélica, e mutações probabilísticas acontecem no mundo, não em máquinas ou caixas de inventário. Nenhuma queen, princess, ou drone; nenhum ciclo de vida obrigatório.

O que diferencia o Curious Bees é a profundidade genética acessível. Genética sempre visível nas interfaces controladas do mod (colmeia, itens de captura), sem gate de análise. O Analisador existe como ferramenta opcional de inspeção, não como pré-requisito para ver os dados. O objetivo declarado é "logística de nível Productive Bees, curiosidade de nível Forestry."

A produção segue o fluxo: **abelhas no mundo → cruzamento → colmeia avançada → favos → centrífuga → mel + cera + subprodutos**. Resource bees (abelhas de minério) estão explicitamente fora de escopo até que uma série de pré-requisitos do ADR-0012 seja satisfeita.

---

## Sistema de genética

### Cromossomos ativos (MVP)

| Cromossomo | Efeito | Status |
|-----------|--------|--------|
| `SPECIES` | Identidade visual e de produção da abelha | ✅ Implementado |
| `PRODUCTIVITY` | Multiplicador de saída de produção | ✅ Implementado |
| `FLOWER_TYPE` | Tipo de flor aceita para polinização | ✅ Implementado |
| `LIFESPAN` | Presente no código, **removido do gameplay** (ADR-0017) | 🔧 Legado — aguarda remoção completa (PR-T10) |
| `FERTILITY` | Presente no código, **removido do gameplay** (ADR-0017) | 🔧 Legado — aguarda remoção completa (PR-T10) |

> ADR-0017: `LIFESPAN` e `FERTILITY` estão no enum `ChromosomeType` por compatibilidade com saves antigos, mas não devem aparecer em nenhuma UI, lógica de produção ou breeding. A remoção completa do código é futura.

### Herança e mutação

| Mecanismo | Descrição | Status |
|-----------|-----------|--------|
| Herança Mendeliana | Um alelo de cada pai por cromossomo; dominância resolve o alelo ativo | ✅ Implementado (`BreedingService`) |
| Mutação probabilística | `MutationService` aplica mutações após herança; nunca determinístico (A+B→C) | ✅ Implementado |
| Fallback sem genoma | Abelha sem genoma recebe espécie Common + WARNING no log; nunca crasha | ✅ Implementado |
| Persistência do genoma | Entity data attachment com Codec; active/inactive preservados no load | ✅ Implementado (`BeeGenomeStorage`, ADR-003) |
| Feedback visual de mutação | Partícula + som no `BeeBreedingEventHandler` quando mutação ocorre | ✅ Implementado (E1-T13) |
| Habitat discovery | Common + Common breeding → 3% chance de descobrir espécie local do bioma | 📋 Planejado (PR-T05) |

### Núcleo de genética (arquitetura)

O pacote `common/genetics` é **Java puro** — zero imports de Minecraft, NeoForge, Fabric, registries, NBT, eventos, UI ou mixins. Testável em JUnit sem nenhuma dependência de jogo. CI verifica esse limite via `ArchitectureBoundaryTest`. (ADR-0002)

---

## Espécies

MVP: 6 espécies. `Common` representa a abelha vanilla dentro do sistema genético. As demais 5 são as espécies de conteúdo do mod.

| Espécie | ID | Dominância | Habitat | Produto | Flor | Status |
|--------|----|-----------|---------|---------|------|--------|
| **Common** | `curiousbees:common` | Dominante (sobre todos) | Universal (spawn egg vanilla) | Normal | Flores | 📋 Planejado (PR-T03) |
| **Meadow** | `curious_bees:species/meadow` | Dominante | Planícies, prados, florestas de flores | Favo Meadow | Flores | ✅ Implementado |
| **Forest** | `curious_bees:species/forest` | Dominante | Florestas (tag `minecraft:is_forest`) | Favo Forest | Folhas | ✅ Implementado |
| **Arid** | `curious_bees:species/arid` | Recessivo | Savanas, desertos, badlands | Favo Arid | Cactos | ✅ Implementado |
| **Cultivated** | `curious_bees:species/cultivated` | Dominante | Sem habitat selvagem (reprodução) | Favo Cultivated | Flores | ✅ Implementado |
| **Hardy** | `curious_bees:species/hardy` | Recessivo | Sem habitat selvagem (reprodução) | Favo Hardy | Flores/Cactos | ✅ Implementado |

**Texturas de espécie:** DEV-PLACEHOLDER para todas as 5 espécies MVP (arquivos existem, arte final programada para P5/E5). `SpeciesTextureResolver` com fallback de 3 camadas: textura da espécie → fallback do mod → fallback vanilla.

**Spawn eggs:** implementados para todas as 5 espécies (`CuriousBeeSpeciesSpawnEggItem`). Spawn egg vanilla (`minecraft:bee_spawn_egg`) deve atribuir espécie Common — implementação pendente (PR-T03).

---

## Captura e transporte de abelhas

Implementação base presente; comportamento atômico (captura/liberação sem perda silenciosa) é o foco do PR-T01.

| Item | Tipo | Crafting | Comportamento | Status |
|------|------|---------|---------------|--------|
| **Bee Jar** (Pote de Abelha) | Single-use | Simples (garrafa de vidro + favo) | Captura 1 abelha; quebra ao liberar | 🔧 Parcial — item existe, atomicidade pendente (PR-T01) |
| **Bee Transporter** (Transportador) | Reutilizável | Difícil (ferro/ouro + frame) | Captura 1 abelha; retorna ao inventário ao liberar | 🔧 Parcial — item existe, atomicidade pendente (PR-T01) |

**Comportamento de captura (ADR-0014):**
- Clique direito em abelha no mundo → abelha removida e serializada no item
- Tooltip do item mostra espécie + relatório genético (sem gate de análise)
- Clique direito no ar ou bloco → abelha spawna no local
- Nenhuma abelha pode desaparecer silenciosamente — se a operação falhar, o estado reverte

**PR-T01 (todo):** garantir atomicidade completa — serializar genome ANTES de remover a abelha do mundo; spawnar a abelha ANTES de limpar o item. Rollback em falha.

---

## Advanced Beehive (Colmeia Avançada)

O bloco de produção principal do mod. O nome interno ainda usa `advanced_apiary`/`AdvancedApiaryBlock`; a renomeação para `advanced_beehive` é o PR-T02.

### Capacidade (ADR-0018)

| Configuração | Bee Slots | Frame Slots | Output Slots | Upgrade Slots |
|-------------|-----------|-------------|-------------|--------------|
| Sem Expansion Box | 3 | 3 | 9 | 0 |
| Com Beehive Expansion Box | 7 | 3 | 9 | 3 |

> Bee slots são **visuais**, não slots de IItemHandler — não expostos a hoppers/pipes.

### Status de implementação

| Componente | Status |
|-----------|--------|
| `AdvancedApiaryBlock` + `AdvancedApiaryBlockEntity` | ✅ Implementado (herda de `GeneticApiaryBlockEntity`) |
| Menu (`AdvancedApiaryMenu`) | ✅ Implementado |
| GUI com 3 bee slots visuais, 3 frame slots, 9 outputs | 📋 Planejado (PR-T06) |
| Genética visível nos bee slots sem gate de análise | 📋 Planejado (PR-T06, PR-T09) |
| Inserção via Bee Jar / Bee Transporter | 🔧 Parcial — menu existe, atomicidade pendente (PR-T01) |
| Entrada via AI vanilla | ✅ Implementado (herança de `BeehiveBlock`) |
| Renomeação para `advanced_beehive` no lang/recipes | 📋 Planejado (PR-T02) |
| Sided IO (output extract-only, frames não inseríveis pelo fundo) | ✅ Implementado (E0-T07) |
| Cache de entity scan (20 ticks) | ✅ Implementado (E0-T08) |

---

## Beehive Expansion Box (Caixa de Expansão)

Bloco opcional que expande a capacidade da Advanced Beehive. Nome interno atual: `apiary_extension`/`ApiaryExtensionBlock`.

| Regra | Detalhe | Status |
|-------|---------|--------|
| Placement | Diretamente **abaixo** da Advanced Beehive (não lateral, não acima) | 📋 Planejado (PR-T07) |
| Expansão | 3 → 7 bee slots + adiciona 3 upgrade slots | 📋 Planejado (PR-T07) |
| Remoção segura | Abelhas em excesso são liberadas no mundo; nunca deletadas silenciosamente | 📋 Planejado (PR-T07) |
| GUI compartilhada | Abrir a Expansion Box abre a mesma GUI da Advanced Beehive com layout expandido | 📋 Planejado (PR-T07) |
| Bloco existe no registro | `ApiaryExtensionBlock` + `ApiaryExtensionBlockEntity` registrados | ✅ Implementado |

---

## Frames (Quadros)

Itens colocados nos 3 slots de frame da colmeia para modular a produção.

| Frame | ID | Durabilidade | Efeito | Status |
|------|----|-------------|--------|--------|
| **Basic Frame** | `curiousbees:basic_frame` | 64 usos | Multiplicador base de produção | ✅ Implementado |
| **Mutation Frame** | `curiousbees:mutation_frame` | 32 usos | Aumenta taxa de mutação | ✅ Implementado |
| **Productivity Frame** | `curiousbees:productivity_frame` | 48 usos | Aumenta multiplicador de produção | ✅ Implementado |

**Implementação completa:**
- Itens registrados em `ModItems` com durabilidade configurada
- Inventário de 3 slots no `GeneticApiaryBlockEntity` (E3-T06)
- `FrameModifiers.combine()` agrega multiplicadores de múltiplos frames (E3-T07)
- `hurtAndBreak()` por ciclo de produção; slot limpo quando frame quebra (E3-T08)
- Tag `curiousbees:frames` usada para validação via `isFrameItem()` (E0-T07)

---

## Centrifuge (Centrífuga)

Máquina de processamento que converte favos em mel + cera + subprodutos por espécie.

### Slots atuais vs planejados

| Slot | Implementado agora | Meta PR-T08 |
|------|-------------------|-------------|
| Comb input | 1 (✅) | 1 |
| Output slots | **4** (🔧 — diverge do spec) | **9** |
| Bottle input | 1 (✅) | 1 |
| Honey bottle output | Incluso nos 4 outputs (🔧) | **1 slot dedicado** |
| Upgrade slots | 0 (📋) | 3 |

> O código atual (`OUTPUT_SLOTS = 4`) diverge da especificação ADR-0015 (9 result slots + 1 honey bottle output separado + 3 upgrade slots). PR-T08 corrige isso.

### Modelo de mel (ADR-0015)

- Centrífuga rastreia `honeyCounter` interno (0–5 porções) — sem registro de fluido
- Por ciclo: se `honeyCounter > 0` + `glass_bottle` disponível + slot de saída livre → consome garrafa + porção → produz `honey_bottle`
- Overflow (contador cheio, sem garrafa) é descartado em FINE — nunca bloqueia processamento
- Advanced Beehive não tem slot de mel — combs saem pelos output slots

### Status

| Componente | Status |
|-----------|--------|
| `CentrifugeBlock` + `CentrifugeBlockEntity` | ✅ Implementado |
| `CentrifugeMenu` + `CentrifugeScreen` | ✅ Implementado |
| Recipe type `curiousbees:centrifuge` + serializer | ✅ Implementado |
| Receitas stub para favos existentes (wax + honey + by-product) | ✅ Implementado |
| `honeyCounter` + bottling mecânico | ✅ Implementado (honeyCounter presente no código) |
| 9 output slots + 1 honey bottle slot dedicado + 3 upgrade slots | 📋 Planejado (PR-T08) |

---

## Ninhos selvagens e descoberta de habitat

### Ninhos selvagens (Wild Nests)

| Ninho | Espécie | Biomas | Variante visual | Status |
|------|---------|--------|-----------------|--------|
| `meadow_bee_nest` | Meadow | Plains, flower_forest, meadow | Padrão | ✅ Implementado |
| `forest_bee_nest` | Forest | Forest, birch_forest, dark_forest | Padrão | ✅ Implementado |
| `forest_bee_log_nest` | Forest | Florestas (variante tronco) | `NestVariant.LOG` | ✅ Implementado |
| `arid_bee_nest` | Arid | Desert, savanna, badlands | Padrão | ✅ Implementado |

**Framework de ninhos (E2.B):**
- `SpeciesBeeNestBlock` genérico — espécie definida por parâmetro, não por subclasse
- Variantes visuais (tronco, superfície, pendurado) sem novas classes Java
- `SpeciesBeeNestFeature` para worldgen via features de dados
- POI/hive targeting centralizado em `BeeSpeciesHiveTargetHandler`

**Ninhos gerados com ocupantes (ADR-0019):** cada ninho declara `occupant_species_pool`. Worldgen popula de 1–N abelhas do pool. Bioma não overrides espécie.

| Componente | Status |
|-----------|--------|
| Ninhos com ocupantes gerados via pool data-driven | 📋 Planejado (PR-T04) |
| Sem hardcode `if biome == desert → Arid` | 📋 Planejado (PR-T04) |
| Spawn handler atual | 🔧 Usa biome tags (comportamento a corrigir em PR-T03/T04) |

### Habitat discovery (descoberta de habitat)

Quando **duas abelhas Common** criam abelhas em um bioma com espécies de habitat definidas:

- **3% de chance base** de mutação de descoberta
- Resultado retirado do pool de habitat do bioma (data-driven)
- Resultado parcial (~95%): um alelo muda, outro fica Common
- Resultado completo (~5%): ambos os alelos viram a espécie descoberta
- Este é o caminho de progressão intencional — jogador cria abelhas vanilla no mundo e pode descobrir espécies locais

| Status |
|--------|
| 📋 Planejado (PR-T05 — depende de PR-T03) |

---

## Bee Analyzer (Analisador — inspetor opcional)

O Analisador **não é um gate de progressão** (ADR-0016). Genética visível em todas as UIs controladas sem necessidade de análise. O Analisador é uma ferramenta opcional de inspeção detalhada.

| Componente | Status |
|-----------|--------|
| `BeeAnalyzerItem` registrado | ✅ Implementado |
| `BeeAnalyzerScreen` mostrando `BeeGeneticReport` | ✅ Implementado (E1-T06) |
| Flag `isAnalyzed()` não bloqueia nenhuma UI | 📋 Planejado (PR-T09 — remoção do gate) |
| Relatório genético compartilhado com Advanced Beehive e tooltips | 📋 Planejado (PR-T09) |

**Dados do relatório:** Espécie (ativo/inativo/pureza), Produtividade, Flower Type. Sem Lifespan, sem Fertility. IDs internos de alelo nunca exibidos ao jogador.

---

## Favos e produtos

| Favo | Espécie | Status |
|-----|---------|--------|
| `meadow_comb` | Meadow | ✅ Implementado |
| `forest_comb` | Forest | ✅ Implementado |
| `arid_comb` | Arid | ✅ Implementado |
| `cultivated_comb` | Cultivated | ✅ Implementado |
| `hardy_comb` | Hardy | ✅ Implementado |

**`ProductionResolver`** gera outputs a partir da espécie ativa (e opcionalmente inativa) modulada por traits e frame modifiers. Definições em `BuiltinProductionDefinitions` (string IDs — sem imports de Minecraft no core).

Receitas de centrífuga por favo existem como stubs (wax + honey + placeholder de subproduto por espécie).

---

## Automação

| Contrato | Detalhe | Status |
|---------|---------|--------|
| Sided `IItemHandler` | `ApiaryCapabilities` — view por direção | ✅ Implementado (E0-T07) |
| Output extract-only | Hopper abaixo só extrai outputs, não insere frames | ✅ Implementado |
| Frame insert via lateral | Hopper lateral pode inserir frames | ✅ Implementado |
| Bee slots nunca expostos a hopper/pipe | Bee slots não são `IItemHandler` | ✅ (via design do Advanced Beehive) |
| Centrifuge automation view | Input insert + bottle insert + outputs extract-only | ✅ Implementado |

---

## Genetic Apiary (colmeia legada)

O `GeneticApiaryBlock` é o **piso de compatibilidade** (ADR-0009). Ainda existe no registro e funciona como colmeia de produção com frames e outputs. A interface principal ao jogador é a **Advanced Beehive**.

- `GeneticApiaryBlock extends BeehiveBlock` — abelhas entram por AI vanilla
- Produção aditiva ao mel vanilla; frames funcionais
- Menu e screen existentes (`GeneticApiaryMenu`, `GeneticApiaryScreen`)
- Não aceita inserção de Bee Jar/Transporter via GUI (reservado para Advanced Beehive)

---

## O que NÃO está neste mod

| Feature | Razão da exclusão | Referência |
|---------|-------------------|-----------|
| **Resource bees** (abelhas de minério) | Gate de pré-requisitos — lista completa no ADR-0012 | ADR-0007, ADR-0012 |
| **Lifecycle / death / larvae** | Contradiz o modelo de entidade viva vanilla | R-2.4 |
| **Temperature / humidity simulation** | Fora de escopo explícito | R-2.5 |
| **Fabric parity** | Pós-MVP; porta quando NeoForge estiver sólido | ADR-0006, DR-010 |
| **JEI/REI deep integration** | Phase 6 ou late P4; apenas quando receitas estabilizarem | roadmap P4/P6 |
| **Breeding dentro da colmeia** | Breeding permanece no mundo (entidade viva) | ADR-0009 |
| **Energia / power** | Sem power por padrão | DR-016, ADR-0009 |
| **Fluid honey** | Modelo de garrafa discreta na centrífuga | ADR-0015 |

---

## Resumo do roadmap por fase

| Fase | Objetivo | Status atual |
|------|---------|-------------|
| **Phase PR** (Product Reset) | Alinhar implementação com produto: Bee Jar atômico, renomear Advanced Beehive, Common species, ninhos com ocupantes, habitat discovery, GUI correta, Expansion Box, Centrifuge slots, remover analysis gate, remover Lifespan/Fertility de UIs | 🔧 **Em andamento** — todos os 10 tasks `todo` |
| **Phase 0** | Genetics core, breeding, production resolver, Genetic Apiary loop | ✅ **Concluído** (todos E0 tasks done) |
| **Phase 1** | Rendering por espécie, Analyzer UX, texturas DEV-PLACEHOLDER, feedback de mutação | ✅ **Concluído** (todos E1 tasks done) |
| **Phase 2** | Ninhos selvagens data-driven, habitat predicates, worldgen features, population caps | ✅ **Concluído** (todos E2 tasks done) |
| **Phase 3** | Advanced Beehive como interface primária, GUI de hive, frames como items, automation contract | ✅ **Concluído** (todos E3 tasks done) |
| **Phase 4** | Centrífuga — block + menu + recipes + honey bottling | ✅ **Concluído** (todos E4 tasks done exceto E4-T09 JEI opcional) |
| **Phase 5** | Arte e identidade: GUI backgrounds, sprites finais, sons, guia in-game | ✅ **Concluído** (todos E5 tasks done) |
| **Phase 6** | Fabric parity prep — audit do common module, parity de receitas, test world | ✅ **Concluído** (todos E6 tasks done) |

> **Prioridade atual:** Phase PR deve ser completada antes de qualquer novo conteúdo. A implementação existente tem a estrutura correta, mas vários comportamentos do produto (Bee Jar atômico, Common species, Advanced Beehive GUI correta, Centrifuge slots corretos) ainda não estão no estado especificado pelas ADRs mais recentes.

---

_Gerado em 2026-05-07 com base em `docs/requirements.md`, `docs/roadmap.md`, `docs/TASKS.md`, `docs/decisions.md`, e código em `neoforge/src/` e `common/src/`._

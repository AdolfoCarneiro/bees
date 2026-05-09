# Curious Bees — Plano de Testes In-Game

> Checklist manual para validar features antes de considerar um Epic concluído.
> Cada seção corresponde a uma task do Epic PR.
>
> **Status do Epic PR:** todas as tasks estão com status `done`. Execute este plano
> para regressão completa antes de qualquer release ou merge significativo.

---

## Como usar este plano

1. Abra o mundo de teste (veja **Setup recomendado** abaixo).
2. Navegue até a seção da task que acabou de ser implementada.
3. Execute cada cenário em ordem: marque `[x]` nos passos e anote falhas.
4. Um bloco inteiro passa quando todos os critérios **PASS** estiverem atendidos.
5. Antes de fazer merge de qualquer PR do Epic PR, execute também a seção
   **Testes de regressão**.

---

## Setup recomendado

- **Modo:** Criativo (`/gamemode creative`)
- **Cheats:** ativados
- **Dificuldade:** Pacífica (evita mobs interferentes)
- **Dia parado:** `/gamerule doDaylightCycle false` + `/time set noon`
- **Nenhum outro mod de abelhas** carregado (evita conflitos de spawn/POI)
- **Versão:** NeoForge 1.21.1
- **Log level:** adicione `-Djava.util.logging.level=FINE` ao JVM para ver logs de trace

---

## PR-T01 — Bee Jar + Bee Transporter (captura/release atômico)

**Referência:** ADR-0014, ADR-0016 · Requisitos R-4.7

### Pré-condição

- Bee Jar (`curiousbees:bee_jar`) e Bee Transporter (`curiousbees:bee_transporter`) no inventário.
- Pelo menos duas abelhas vivas no mundo com genoma (`/curiousbees debug set_bee_genome meadow`
  para garantir genoma conhecido).

---

### C1: Captura com Bee Jar

1. [ ] Segure o Bee Jar vazio na mão.
2. [ ] Clique com botão direito em uma abelha no mundo.
3. [ ] **Resultado esperado:** a abelha desaparece do mundo; o Bee Jar mostra o item carregado
       (nome ou ícone diferente do vazio).
4. [ ] Abra o tooltip do Bee Jar carregado — deve exibir a **espécie** da abelha capturada
       (ex.: "Meadow Bee") e dados genéticos sem exigir análise prévia.
5. [ ] **Resultado esperado:** nenhum ID interno bruto (ex.: `curiousbees:species/meadow`) aparece
       no tooltip; apenas o nome de exibição traduzido.

**Critério PASS:** abelha capturada; item mostra espécie e genética; sem ID bruto.

**O que falha se quebrado:** abelha desaparece sem dados serem salvos no item (perda silenciosa de abelha).

---

### C2: Release com Bee Jar

1. [ ] Com o Bee Jar carregado na mão, clique com botão direito em um bloco ou no ar.
2. [ ] **Resultado esperado:** uma abelha é spawnada na posição do clique com o mesmo genoma
       que foi capturado.
3. [ ] Confira a espécie com `/curiousbees debug inspect_bee` apontando para a abelha recém-spawnada.
4. [ ] **Resultado esperado:** o Bee Jar some do inventário (item de uso único — `shrink(1)`).

**Critério PASS:** abelha spawnada com genoma correto; Bee Jar consumido.

**O que falha se quebrado:** abelha spawnada sem genoma (genoma default/errado); Bee Jar não consumido.

---

### C3: Bee Jar já carregado — tentativa de segunda captura

1. [ ] Com o Bee Jar *já carregado* na mão, tente clicar com botão direito em outra abelha.
2. [ ] **Resultado esperado:** nada acontece (retorna `PASS`); a segunda abelha permanece no mundo;
       o Bee Jar continua com a primeira abelha.

**Critério PASS:** sem side effect; segunda abelha intacta; conteúdo do Bee Jar inalterado.

**O que falha se quebrado:** segunda abelha desaparece ou sobrescreve a primeira.

---

### C4: Bee Transporter (captura reusável)

1. [ ] Segure o Bee Transporter vazio na mão.
2. [ ] Clique com botão direito em uma abelha — captura deve funcionar como o Bee Jar.
3. [ ] **Resultado esperado:** abelha desaparece; Bee Transporter exibe espécie e genética no tooltip.

**Critério PASS:** captura idêntica ao Bee Jar; item permanece na mão (não some).

---

### C5: Release com Bee Transporter — volta vazio

1. [ ] Com o Bee Transporter carregado na mão, clique com botão direito em um bloco ou no ar.
2. [ ] **Resultado esperado:** abelha é spawnada com o genoma correto.
3. [ ] **Resultado esperado:** o Bee Transporter *volta para a mão* **sem** o dado de abelha capturada
       (componente `CAPTURED_BEE` removido) — tooltip mostra estado vazio.

**Critério PASS:** abelha spawnada corretamente; item reutilizável permanece no inventário sem dado residual.

**O que falha se quebrado:** item some como Bee Jar; ou item fica com dado fantasma da abelha anterior.

---

### C6: Abelha sem genoma — captura ignorada

1. [ ] Spawne uma abelha vanilla pura sem genoma (mundo legado ou `/summon minecraft:bee`).
2. [ ] Tente capturar com Bee Jar vazio.
3. [ ] **Resultado esperado:** nada acontece (retorna `PASS`); abelha permanece no mundo; Bee Jar permanece vazio.

**Critério PASS:** captura só ocorre em abelhas com genoma; sem crash.

---

### C7: Falha de spawn — atomicidade garantida

> **Nota:** `addFreshEntity` falhar dentro de um bloco sólido é difícil de acionar em-jogo pois
> o MC geralmente resolve a colisão. O contrato real é verificável via log:
> o item só deve ser consumido APÓS `addFreshEntity` retornar `true`.
> Verifique via log FINE (`-Djava.util.logging.level=FINE`) que a mensagem de
> "spawned bee" aparece ANTES da mensagem de "item consumed/cleared".

**Critério PASS (log-based):** ordem de eventos no log confirma spawn antes de consumo do item.

---

## PR-T02 — Naming reset (renomear Advanced Beehive)

**Referência:** ADR-0018 · Requisito R-4.6

### Pré-condição

- Modo criativo; tab criativa de Curious Bees visível.

---

### C1: Nome na tab criativa

1. [ ] Abra a tab criativa do mod.
2. [ ] **Resultado esperado:** bloco principal de colmeia aparece com o nome **"Advanced Beehive"**.
3. [ ] **Resultado esperado:** nenhuma entrada chamada "Apiary", "Genetic Apiary" ou "Advanced Apiary"
       aparece na tab criativa.

**Critério PASS:** apenas "Advanced Beehive" (e "Beehive Expansion Box" quando implementado) visíveis.

---

### C2: Nome no tooltip do item

1. [ ] Segure o item `curiousbees:advanced_beehive` na mão.
2. [ ] **Resultado esperado:** tooltip mostra "Advanced Beehive" (chave `block.curiousbees.advanced_beehive`).
3. [ ] **Resultado esperado:** nenhuma palavra "Apiary" ou "Genetic" aparece no tooltip.

**Critério PASS:** nome correto; ausência de nomes legados.

---

### C3: Bloco colocado no mundo

1. [ ] Coloque o bloco `curiousbees:advanced_beehive` no mundo.
2. [ ] Abra o menu (botão direito com mão vazia).
3. [ ] **Resultado esperado:** título da janela diz "Advanced Beehive" (ou equivalente traduzido).

**Critério PASS:** GUI abre; título correto; sem referência a "Apiary".

---

### C4: Receita de crafting

1. [ ] Abra a mesa de crafting.
2. [ ] Verifique se a receita do Advanced Beehive está disponível.
3. [ ] **Resultado esperado:** receita produz `curiousbees:advanced_beehive`; nenhuma receita
       produz um item legado de "Apiary" sem migração.

**Critério PASS:** receita funcional para o nome correto.

**O que falha se quebrado:** strings "Apiary" aparecem em qualquer UI de jogador; chaves de lang legadas visíveis.

---

## PR-T03 — Espécie Common + correção de atribuição de spawn

**Referência:** ADR-0019 · Requisito R-3.1

### Pré-condição

- Mundo com bioma variado (floresta, deserto, planície); overworld.

---

### C1: Spawn egg vanilla → espécie Common

1. [ ] Use `minecraft:bee_spawn_egg` para spawnar uma abelha.
2. [ ] Aponte para a abelha e execute `/curiousbees debug inspect_bee`.
3. [ ] **Resultado esperado:** espécie = `curiousbees:common` (ou nome de display "Common Bee").

**Critério PASS:** spawn egg vanilla sempre atribui espécie Common.

---

### C2: Spawn egg do mod → espécie declarada

1. [ ] Use um spawn egg específico do mod (ex.: `curiousbees:meadow_bee_spawn_egg`) para spawnar.
2. [ ] Inspecione com `/curiousbees debug inspect_bee`.
3. [ ] **Resultado esperado:** espécie = a espécie declarada pelo egg (ex.: `curiousbees:meadow`).
4. [ ] **Resultado esperado:** o bioma atual NÃO sobrescreve a espécie.

**Critério PASS:** spawn egg do mod atribui espécie correta independente do bioma.

---

### C3: Fallback seguro — abelha sem genoma

1. [ ] Force uma abelha sem genoma (possível via debug ou mundo legado).
2. [ ] **Resultado esperado:** genoma é atribuído como Common; WARNING no log;
       sem crash.
3. [ ] **Resultado esperado:** o bioma não é usado como fallback de espécie.

**Critério PASS:** fallback = Common; log de WARNING presente; sem crash.

**O que falha se quebrado:** abelha recebe espécie de bioma (ex.: Arid em deserto) via fallback genérico.

---

## PR-T04 — Wild nests geram com ocupantes (data-driven)

**Referência:** ADR-0019 · Requisito R-4.2

### Pré-condição

- Geração de mundo nova (seed qualquer); overworld com biomas variados.
- `/gamerule doMobSpawning true`

---

### C1: Ninho gerado com ocupantes

1. [ ] Explore o mundo até encontrar um ninho selvagem de Curious Bees (ex.: em floresta).
2. [ ] Observe as abelhas ao redor ou quebre o ninho com Silk Touch.
3. [ ] **Resultado esperado:** o ninho gerou com 1–3 abelhas ocupantes conforme o pool
       declarado no JSON do ninho (`occupant_species_pool`).

**Critério PASS:** ninho não está vazio ao gerar; abelhas têm espécie do pool declarado.

---

### C2: Bioma não sobrescreve espécie do ninho

1. [ ] Encontre um ninho em bioma de deserto; inspecione as abelhas.
2. [ ] **Resultado esperado:** espécie das abelhas = a declarada no pool do ninho (ex.: `arid`);
       **não** derivada diretamente do bioma por código hardcoded.

**Critério PASS:** sem `if biome == desert → Arid` no comportamento observado.

---

### C3: Tipos de ninhos diferentes por bioma

1. [ ] Visite floresta, planície e deserto — localize ninhos em cada bioma.
2. [ ] **Resultado esperado:** ninhos de tipos diferentes aparecem nos biomas corretos conforme
       `biome_tags` do JSON.

**Critério PASS:** placement de ninhos data-driven funcional por biome tag.

**O que falha se quebrado:** ninhos sempre vazios; ou espécie hardcoded por bioma no código.

---

## PR-T05 — Mutação habitat discovery (Common + Common → 3%)

**Referência:** ADR-0019 · Requisito R-3.3

### Pré-condição

- Dois pares de abelhas Common em uma área com flores (planície/floresta).
- Cheats on para monitorar resultados; `/gamerule doMobSpawning false` para isolar.

---

### C1: Cruzamento Common + Common sem mutação (caso base)

1. [ ] Permita que dois Common bees se cruzem (flores disponíveis).
2. [ ] Inspecione o filhote com `/curiousbees debug inspect_bee`.
3. [ ] **Resultado esperado (caso majoritário ~97%):** filhote é Common.

---

### C2: Mutação habitat discovery ocorre (~3%)

1. [ ] Repita cruzamentos Common + Common no mesmo bioma (mínimo 20–30 cruzamentos).
2. [ ] **Resultado esperado:** em ~3% dos casos, o filhote não é Common puro — uma ou ambas
       as alelos de espécie mudaram para uma espécie do pool de habitat do bioma.
3. [ ] **Resultado esperado (partial ~95% dos casos de mutação):** um alelo = espécie descoberta,
       outro = Common.
4. [ ] **Resultado esperado (full ~5% dos casos de mutação):** ambos os alelos = espécie descoberta.
5. [ ] **Resultado esperado:** efeito de partícula e/ou som ao ocorrer mutação.

> **Nota:** com 3% de chance, pode ser necessário ~50+ cruzamentos para observar o fenômeno.
> Use `/curiousbees debug roll_production` para verificar o sistema de mutação indiretamente.

**Critério PASS:** mutação ocorre em biomas com pool de habitat definido; fenômeno é probabilístico, não determinístico.

---

### C3: Sem mutação em bioma sem pool de habitat

1. [ ] Localize um bioma sem pool de habitat definido (ex.: oceano profundo, End, Nether — verificar JSONs em `data/curiousbees/`).
2. [ ] Realize 20+ cruzamentos Common + Common nesse bioma.
3. [ ] **Resultado esperado:** todos os filhotes são Common; nenhuma mutação para espécie desconhecida; sem crash.

**Critério PASS:** ausência de pool não causa crash; sem mutação para espécie inválida.

**O que falha se quebrado:** filhotes *sempre* mutam (determinístico); filhotes *nunca* mutam;
mutação ocorre fora do contexto Common + Common; crash quando pool de habitat está vazio.

---

## PR-T06 — Advanced Beehive GUI (3 bee slots, sem expansão)

**Referência:** ADR-0016, ADR-0018 · Requisito R-3.6

### Pré-condição

- Advanced Beehive colocado no mundo; Beehive Expansion Box *não* instalado.

---

### C1: Layout correto sem Expansion Box

1. [ ] Clique com botão direito no Advanced Beehive (mão vazia).
2. [ ] **Resultado esperado:** GUI abre com exatamente:
   - 3 slots visuais de abelha (não item slots comuns)
   - 3 slots de frame
   - 9 slots de output
   - 0 slots de upgrade

**Critério PASS:** contagem exata de slots conforme ADR-0018.

---

### C2: Inserção de abelha via Bee Jar no slot

1. [ ] Capture uma abelha com um Bee Jar (PR-T01 concluído).
2. [ ] Abra o GUI do Advanced Beehive com slot de abelha vazio.
3. [ ] Clique no slot de abelha com o Bee Jar na mão.
4. [ ] **Resultado esperado:** abelha é adicionada como ocupante; Bee Jar é consumido;
       o slot exibe espécie, pureza e resumo de traits da abelha.

**Critério PASS:** inserção via Bee Jar funcional; slot mostra dados genéticos sem gate de análise.

---

### C3: Slot de abelha mostra genética sem análise prévia

1. [ ] Insira uma abelha *nunca analisada* no hive.
2. [ ] **Resultado esperado:** slot mostra espécie, pureza e traits mesmo sem análise prévia.
3. [ ] **Resultado esperado:** nenhum ID bruto visível (ex.: `curiousbees:species/forest`).

**Critério PASS:** genética sempre visível em controlled UI (ADR-0016); sem ID bruto.

---

### C4: Slots de abelha não aceitam inserção via hopper

1. [ ] Coloque um hopper apontando para o Advanced Beehive.
2. [ ] Tente inserir um item qualquer pelo hopper.
3. [ ] **Resultado esperado:** slots de abelha *não* são item slots convencionais — hopper não
       consegue inserir abelhas ou itens nos slots de abelha.

**Critério PASS:** bee slots não expostos ao `IItemHandler` de automação.

### C5: Frame insertion e durabilidade

1. [ ] Abra o Advanced Beehive (sem Expansion Box).
2. [ ] Insira um frame no slot de frame (drag or click com frame item).
3. [ ] **Resultado esperado:** frame é aceito no slot; barra de durabilidade aparece abaixo do slot de frame.
4. [ ] Aguarde produção de combs com frame instalado.
5. [ ] **Resultado esperado:** frame perde durabilidade a cada output; ao chegar em 0, slot é esvaziado e log `debug` é emitido.

**Critério PASS:** frame funcional; durabilidade visível; frame quebrado limpa o slot.

---

### C6: Estado "output full" — aviso no painel de abelhas

1. [ ] Encha todos os 9 slots de output (use `/item give` ou aguarde produção intensa).
2. [ ] Observe o painel de abelhas (esquerda do GUI).
3. [ ] **Resultado esperado:** texto de aviso "Output Full" (ou equivalente) aparece no painel de abelhas.

**Critério PASS:** aviso visível quando outputs lotados; desaparece ao esvaziar pelo menos um slot.

**O que falha se quebrado:** GUI exibe slots errados; genética oculta sem análise; bee slots acessíveis por hopper.

---

## PR-T07 — Beehive Expansion Box (7 bee slots + 3 upgrade slots)

**Referência:** ADR-0018 · Requisito R-4.6

### Pré-condição

- Advanced Beehive colocado no mundo.
- Beehive Expansion Box disponível no inventário.

---

### C1: Instalação abaixo do Advanced Beehive

1. [ ] Coloque o Beehive Expansion Box diretamente **abaixo** do Advanced Beehive.
2. [ ] Clique com botão direito no Advanced Beehive.
3. [ ] **Resultado esperado:** GUI muda para layout expandido:
   - 7 slots visuais de abelha
   - 3 slots de frame
   - 9 slots de output
   - 3 slots de upgrade

**Critério PASS:** GUI expandido ao detectar Expansion Box abaixo.

---

### C2: Expansion Box ao lado ou acima — sem efeito

1. [ ] Coloque um Expansion Box na lateral ou acima do Advanced Beehive.
2. [ ] **Resultado esperado:** GUI continua com layout padrão (3 bee slots, 0 upgrade slots).

**Critério PASS:** expansão só funciona abaixo (regra de placement ADR-0018).

---

### C3: Remoção do Expansion Box com >3 abelhas ocupantes

1. [ ] Com layout expandido, insira 5–7 abelhas no hive.
2. [ ] Quebre o Beehive Expansion Box.
3. [ ] **Resultado esperado:** abelhas excedentes (além de 3) são liberadas no mundo — *nunca* deletadas silenciosamente.
4. [ ] **Resultado esperado:** abelhas liberadas conservam seus genomas.

**Critério PASS:** zero perda silenciosa de abelhas ao remover Expansion Box.

---

### C4: Remoção do Advanced Beehive com Expansion Box instalado

1. [ ] Com o Expansion Box instalado, quebre o Advanced Beehive.
2. [ ] **Resultado esperado:** Expansion Box para de funcionar (estado inerte); sem crash.

**Critério PASS:** Expansion Box sem hive não crasha o jogo.

### C5: Upgrade slots rejeitam itens sem tag

1. [ ] Com Expansion Box instalado, tente inserir um item qualquer (ex.: pedra, madeira) em um dos 3 slots de upgrade.
2. [ ] **Resultado esperado:** item NÃO é aceito no slot de upgrade.
3. [ ] Tente inserir um item com tag `curiousbees:beehive_upgrades` (quando disponível).
4. [ ] **Resultado esperado:** item com tag correta é aceito.

**Critério PASS:** `isItemValid` funcional; slots filtram por tag.

---

### C6: GUI aberta quando Expansion Box é removido (limitação conhecida)

1. [ ] Abra o GUI do Advanced Beehive com Expansion Box instalado (layout expandido visível).
2. [ ] Com o GUI aberto, peça a outro jogador (ou use /setblock) para remover o Expansion Box.
3. [ ] **Resultado esperado (comportamento atual — MVP):** GUI permanece aberto com o layout expandido até ser fechado e reaberto; sem crash.
4. [ ] Feche e reabra o GUI.
5. [ ] **Resultado esperado:** GUI reabre com layout padrão (3 bee slots, 0 upgrade slots).

> **Nota:** a atualização em tempo real do GUI ao remover a box não está implementada (limitação de MVP documentada no plano de PR-T07). O comportamento seguro (sem crash) é o critério.

**Critério PASS:** sem crash; GUI correto após reabrir.

**O que falha se quebrado:** abelhas deletadas silenciosamente ao remover Expansion Box; crash ao remover Advanced Beehive.

---

## PR-T08 — Centrifuge — 9 output slots + 3 upgrade slots

**Referência:** ADR-0015

### Pré-condição

- Centrifuge colocado no mundo; combs disponíveis.

---

### C1: Layout de slots correto

1. [ ] Abra o GUI da Centrifuge (botão direito).
2. [ ] **Resultado esperado:**
   - 1 slot de entrada de comb
   - 9 slots de output para itens
   - 1 slot de entrada de garrafa (glass bottle)
   - 1 slot de output dedicado para honey bottle
   - 3 slots de upgrade

**Critério PASS:** contagem exata conforme ADR-0015.

---

### C2: Honey counter visual (0–5)

1. [ ] Insira um comb que produza mel na Centrifuge sem garrafa vazia.
2. [ ] **Resultado esperado:** contador de mel sobe (0–5 porções) indicado visualmente no GUI.
3. [ ] **Resultado esperado:** quando o contador está em 5 e não há garrafa, overflow é descartado
       (FINE no log) — processamento NÃO para.

**Critério PASS:** counter funcional; overflow não bloqueia; processamento contínuo.

---

### C3: Engarrafamento de mel

1. [ ] Insira glass bottles no slot de garrafa e combs no input.
2. [ ] **Resultado esperado:** a Centrifuge consome 1 garrafa + 1 porção do honey counter e produz
       1 honey bottle no slot dedicado de saída.
3. [ ] **Resultado esperado:** honey bottle sai pelo slot dedicado, não pelos 9 slots de resultado.

**Critério PASS:** honey bottle no slot correto; garrafa consumida corretamente.

---

### C4: Automação — hopper abaixo extrai outputs

1. [ ] Coloque um hopper abaixo da Centrifuge.
2. [ ] **Resultado esperado:** hopper extrai itens dos slots de output (wax, by-products, honey bottles).
3. [ ] **Resultado esperado:** hopper NÃO consegue inserir itens nos slots de output.

**Critério PASS:** extração funcional; sem inserção nos outputs via automação.

**O que falha se quebrado:** slots de output incorretos; honey bottle no lugar errado; automação inserindo em outputs.

---

## PR-T09 — Remover analysis gate de todos os caminhos de UI

**Referência:** ADR-0016 · Requisito R-1.5, R-4.1

### Pré-condição

- Abelhas com e sem análise prévia disponíveis.
- Advanced Beehive, Bee Jar, Bee Transporter todos disponíveis.

---

### C1: Advanced Beehive — genética visível sem análise

1. [ ] Insira uma abelha *nunca analisada* no Advanced Beehive via Bee Jar.
2. [ ] **Resultado esperado:** slot da abelha mostra espécie, pureza e traits imediatamente.
3. [ ] **Resultado esperado:** nenhuma mensagem "não analisado" ou "analise primeiro".

**Critério PASS:** sem gate de análise na UI do Advanced Beehive.

---

### C2: Bee Jar tooltip — genética visível sem análise

1. [ ] Capture uma abelha *nunca analisada* com um Bee Jar.
2. [ ] **Resultado esperado:** tooltip mostra espécie e dados genéticos.
3. [ ] **Resultado esperado:** nenhum ID bruto (`curiousbees:*`) visível.

**Critério PASS:** tooltip exibe dados sem depender de `isAnalyzed()` (ADR-0016 implementado).

---

### C3: Bee Transporter tooltip — mesma regra

1. [ ] Repita o C2 com o Bee Transporter.
2. [ ] **Resultado esperado:** idêntico ao C2.

**Critério PASS:** comportamento consistente entre Bee Jar e Bee Transporter.

---

### C4: Analyzer continua funcional (opcional, não obrigatório)

1. [ ] Se o Bee Analyzer ainda existir no mod, use-o em uma abelha.
2. [ ] **Resultado esperado:** Analyzer produz o mesmo `BeeGeneticReport` que o GUI já exibe — não é
       o único caminho para ver genética.

**Critério PASS:** Analyzer é opcional; genética já visível em outros UIs antes de usar o Analyzer.

**O que falha se quebrado:** qualquer UI ainda oculta genética por falta de análise.

---

## PR-T10 — Remover Lifespan e Fertility da UI, conteúdo e breeding

**Referência:** ADR-0017 · Requisito R-6.1

### Pré-condição

- Advanced Beehive com abelha ocupante; Bee Jar com abelha capturada.

---

### C1: Lifespan ausente de todos os UIs

1. [ ] Abra o Advanced Beehive com abelha inserida.
2. [ ] **Resultado esperado:** nenhuma linha "Lifespan", "Vida útil" ou similar na UI.
3. [ ] Verifique tooltip do Bee Jar/Transporter.
4. [ ] **Resultado esperado:** nenhuma menção a Lifespan nos tooltips.

**Critério PASS:** zero menções a Lifespan em qualquer UI de jogador.

---

### C2: Fertility ausente de todos os UIs

1. [ ] Repita C1 procurando "Fertility" ou "Fertilidade".
2. [ ] **Resultado esperado:** nenhuma menção em nenhum UI.

**Critério PASS:** zero menções a Fertility em qualquer UI de jogador.

---

### C3: Saves antigos com LIFESPAN/FERTILITY não crasham

1. [ ] Carregue um mundo salvo que tenha abelhas com genomas contendo lifespan/fertility
       (ou crie manualmente via NBT editor se disponível).
2. [ ] **Resultado esperado:** jogo carrega sem crash; abelhas funcionam normalmente;
       campos ignorados silenciosamente.

**Critério PASS:** retrocompatibilidade de save; sem crash ao desserializar campos removidos.

---

### C4: Species JSON não define lifespan/fertility

1. [ ] Verifique os arquivos JSON de espécies em `data/curiousbees/species/`.
2. [ ] **Resultado esperado:** nenhuma chave `lifespan` ou `fertility` nos arquivos de espécie.

**Critério PASS:** dados de conteúdo limpos dos campos removidos.

**O que falha se quebrado:** qualquer UI exibe Lifespan ou Fertility; crash ao carregar save antigo com esses campos.

---

## Testes de regressão (rodar em todo PR do Epic PR)

Execute esta lista compacta antes de qualquer merge — deve completar em < 10 minutos em modo criativo:

- [ ] **R-1: Genoma persiste no save/load** — coloque uma abelha com genoma conhecido; salve e feche o mundo; reabra; inspecione com `inspect_bee` — genoma idêntico, active/inactive sem re-roll.
- [ ] **R-2: Breeding gera filhote com genoma** — cruce duas abelhas com espécies diferentes; filhote tem genoma derivado dos pais (herança mendeliana); não é determinístico (`A+B != sempre C`).
- [ ] **R-3: Sem crash com abelha sem genoma** — spawne uma abelha sem genoma (via debug ou mundo legado); espécie fallback = Common; WARNING no log; sem crash.
- [ ] **R-4: Advanced Beehive abre GUI** — coloque Advanced Beehive; botão direito abre menu sem crash.
- [ ] **R-5: Centrifuge processa comb** — insira um comb no slot de entrada; aguarde processamento; item de output aparece no slot de saída.
- [ ] **R-6: Hopper extrai outputs da Centrifuge** — hopper abaixo da Centrifuge extrai itens dos output slots; não insere nada nos output slots.
- [ ] **R-7: Bee Jar captura e libera abelha** — ciclo completo captura → release sem perda de genoma; Bee Jar consumido.
- [ ] **R-8: Bee Transporter reutilizável** — captura → release → item volta vazio; segundo ciclo de captura funciona.
- [ ] **R-9: Tab criativa sem nomes legados** — aba do mod não exibe "Apiary", "Genetic Apiary", "Advanced Apiary".
- [ ] **R-10: Nenhum ID bruto em tooltip** — nenhum tooltip de item ou slot de GUI exibe `curiousbees:species/*` ou similar como texto literal.
- [ ] **R-11: Vanilla bee spawn egg → Common** — `minecraft:bee_spawn_egg` sempre produz espécie Common.
- [ ] **R-12: Spawn egg do mod → espécie correta** — `curiousbees:meadow_bee_spawn_egg` produz espécie Meadow; sem override de bioma.
- [ ] **R-13: Mutation feedback visual** — quando mutação ocorre no breeding, partícula e/ou som são disparados no server (`!level.isClientSide()`).
- [ ] **R-14: Lifespan e Fertility ausentes** — nenhum UI de jogador exibe Lifespan ou Fertility.
- [ ] **R-15: `/reload` não corrompe estado** — execute `/reload` com hive em funcionamento; abelhas e outputs intactos; sem crash.
- [ ] **R-16: Upgrade slots rejeitam itens não-tagados** — com Expansion Box instalado, tente inserir pedra/madeira nos 3 slots de upgrade do Expanded GUI; nenhum item deve ser aceito.
- [ ] **R-17: Expanded GUI com 7 abelhas** — instale Expansion Box; insira 7 abelhas via Bee Jar/Transporter; todos os 7 slots visuais devem exibir espécie e pureza sem sobreposição ou crash.
- [ ] **R-18: releaseExcessOccupants preserva abelhas** — insira 5 abelhas com Expansion Box; quebre a box; verifique que exatamente 2 abelhas aparecem no mundo e 3 permanecem no hive ao reabrir o GUI.

---

## Comandos debug úteis

Todos os comandos requerem nível de permissão 2 (cheats on em mundo solo).

| Comando | O que faz |
|---------|-----------|
| `/curiousbees habitat here` | Exibe o predicate de habitat que faria match no bioma atual — útil para debugar PR-T04 e PR-T05. |
| `/curiousbees debug inspect_bee` | Exibe espécie, genoma completo e status de análise da abelha apontada pelo cursor. Útil para todos os cenários de captura e breeding. |
| `/curiousbees debug inspect_apiary` | Exibe estado interno do apiary/beehive apontado (ocupantes, honey level, frames). |
| `/curiousbees debug apiary_metrics` | Métricas de tick do apiary apontado — útil para verificar produção e frame modifiers. |
| `/curiousbees debug set_bee_genome <species>` | Força uma espécie específica em uma abelha apontada — útil para preparar cenários de teste sem depender de breeding natural. Sugestões de espécie disponíveis via tab. |
| `/curiousbees debug roll_production` | Executa um roll de produção simulado no apiary apontado e exibe os outputs resultantes no chat — útil para testar frame modifiers sem esperar o tick completo. |

---

_Última atualização: 2026-05-07. Baseado em Epic PR tasks PR-T01 a PR-T10 (todos `todo`) e ADRs 0014–0019._

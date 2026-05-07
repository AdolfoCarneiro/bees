# Curious Bees — Productization Reset Spec v2

## 0. Diagnóstico direto

O roadmap atual foi marcado como “feito”, mas o comportamento real ainda não bate com o produto esperado.

Problemas críticos encontrados:

```
1. Bee Jar e Bee Transporter capturam a abelha do mundo,
   mas o item fica Empty ou não consegue liberar a abelha.
   Resultado: a abelha vai para o limbo.

2. Existem conceitos demais: apiary, hive, advanced apiary/hive.
   Produto final deve ter uma experiência principal:
   Advanced Beehive.

3. A espécie aparentemente está sendo definida pelo bioma no spawn da entidade.
   Isso está errado para spawn egg, vanilla bee e fluxo de descoberta.

4. A geração de espécies precisa acontecer por:
   - spawn egg específico;
   - wild nest gerado no mundo com abelhas dentro;
   - breeding + herança + mutação;
   - fallback seguro para legacy/missing data.

5. O jogador não deve inserir abelha manualmente na hive como regra base.
   A GUI deve mostrar as abelhas que entraram / habitam a hive.
   Inserção manual só existe via Bee Jar / Bee Transporter e somente no Advanced Beehive.

6. As GUIs atuais estão muito distantes da referência esperada.
   O alvo é uma UX parecida com Productive Bees:
   clara, visual, com slots óbvios, sem parecer tela técnica genérica.
```

---

# 1. Decisão de produto: uma hive principal

## 1.1 Bloco principal

O mod deve ter **um bloco principal de produção**:

```
Advanced Beehive
```

Nome interno recomendado:

```
curiousbees:advanced_beehive
```

Classes recomendadas:

```
AdvancedBeehiveBlock
AdvancedBeehiveBlockEntity
AdvancedBeehiveMenu
AdvancedBeehiveScreen
```

## 1.2 O que remover ou migrar

Não deve existir no produto final uma confusão de:

```
Genetic Apiary
Advanced Apiary
Apiary
Hive
Advanced Hive
```

A experiência player-facing deve ser:

```
Hive Vanilla
Advanced Beehive
Beehive Expansion Box
Centrifuge
Bee Analyzer
Bee Jar / Bee Transporter
```

## 1.3 Regra de linguagem

Usar sempre:

```
Advanced Beehive
Beehive Expansion Box
Centrifuge
```

Evitar:

```
Apiary
Genetic Apiary
Advanced Apiary
```

O termo “apiary” só pode ficar em código antigo enquanto estiver sendo migrado.

---

# 2. Espécies, spawn e vanilla bee

## 2.1 Regra principal

A espécie **não pode** ser sobrescrita automaticamente só porque a abelha nasceu em determinado bioma.

Errado:

```
Qualquer Bee que aparece no deserto vira Arid.
Qualquer Bee que aparece na floresta vira Forest.
Qualquer Bee vanilla passa a ser uma espécie Curious Bees sem escolha.
```

Correto:

```
A origem da abelha define a espécie inicial.
O bioma pode influenciar wild nests e mutações,
mas não deve reescrever toda Bee que spawna.
```

---

## 2.2 Vanilla Bee deve existir como estado válido

O jogador precisa conseguir ter uma abelha vanilla.

Recomendação técnica:

```
Criar uma espécie base:
curiousbees:common
Display: Common Bee
```

Essa espécie representa uma abelha vanilla dentro do sistema genético, sem virar Meadow/Forest/Arid automaticamente.

Isso evita dois problemas:

```
1. Toda abelha continua podendo ter genome, como os docs exigem.
2. O jogador consegue spawnar / manter uma Vanilla Bee que é a espécie common.
```

A lista MVP atual dos docs fixa Meadow, Forest, Arid, Cultivated e Hardy; incluir Common como espécie base exige atualização de docs/decisions, porque hoje o contrato de conteúdo fala em cinco espécies MVP.

---

## 2.3 Fonte da espécie por tipo de spawn

### Spawn egg vanilla

```
Item:
minecraft:bee_spawn_egg

Resultado:
Bee com espécie curiousbees:common.
```

Não deve virar Meadow/Forest/Arid por bioma.

### Spawn egg de espécie Curious Bees

Se o mod tiver ovos próprios:

```
curiousbees:meadow_bee_spawn_egg    -> Meadow Bee
curiousbees:forest_bee_spawn_egg    -> Forest Bee
curiousbees:arid_bee_spawn_egg      -> Arid Bee
curiousbees:cultivated_bee_spawn_egg -> Cultivated Bee
curiousbees:hardy_bee_spawn_egg     -> Hardy Bee
```

O ovo deve sempre vencer o bioma.

### Wild nest gerado no mundo

O wild nest define quais abelhas vêm dentro dele.

Exemplo:

```
Meadow Bee Nest gerado em plains:
- pode nascer com 1 a 3 Meadow Bees dentro.

Forest Bee Nest gerado em forest:
- pode nascer com 1 a 3 Forest Bees dentro.

Arid Bee Nest gerado em desert/savanna/badlands:
- pode nascer com 1 a 3 Arid Bees dentro.
```

O bioma influencia **qual nest pode gerar**, não reescreve abelhas avulsas.

### Breeding

Filhote recebe genome por herança mendeliana dos pais, com mutação probabilística depois da herança. Isso continua alinhado com a arquitetura atual do projeto.

### Fallback / legacy bee

Se uma abelha existe sem genome por bug, mundo antigo ou outro mod:

```
Default seguro:
atribuir Common Bee, logar WARNING, não crashar.
```

Não usar bioma para transformar essa abelha em espécie de bioma, exceto se existir uma regra explícita e data-driven.

---

# 3. Wild nests com abelhas dentro

## 3.1 Objetivo

Exploração deve funcionar assim:

```
Jogador explora o mundo.
Encontra um nest específico.
O nest pode já ter abelhas daquela espécie dentro.
Essas abelhas saem/entram como vanilla bees.
O jogador pode capturar, analisar, breedar ou levar para o Advanced Beehive.
```

## 3.2 O que não fazer

Não fazer:

```
Biome spawn handler transforma toda bee em espécie do bioma.
```

Não fazer:

```
Todo ninho vazio, obrigando o jogador a depender de spawn solto aleatório.
```

Não fazer:

```
Hardcode: if desert then arid, if forest then forest, if plains then meadow.
```

## 3.3 Data-driven nest definition

Cada nest deve ser definido por dados, não por if espalhado.

Modelo conceitual:

```
{
  "id":"curiousbees:arid_bee_nest",
  "block":"curiousbees:arid_bee_nest",
  "biome_tags": [
"minecraft:is_desert",
"minecraft:is_savanna",
"minecraft:is_badlands"
  ],
  "generation_weight":8,
  "occupant_count": {
    "min":1,
    "max":3
  },
  "occupant_species_pool": [
    {
      "species":"curiousbees:arid",
      "weight":100
    }
  ],
  "initial_honey_level": {
    "min":0,
    "max":2
  }
}
```

Para espécies futuras, basta adicionar novo JSON.

## 3.4 Regra agnóstica

O sistema deve perguntar:

```
Quais nest definitions são válidas para este bioma?
Escolhe uma por peso.
Gera o bloco.
Rola quantidade de ocupantes.
Para cada ocupante, escolhe espécie pelo pool do nest.
Cria os dados internos da abelha.
```

Nada de:

```
if biome == DESERT -> ARID
```

---

# 4. Mutação Vanilla + Vanilla por bioma

## 4.1 Objetivo

Permitir descoberta inicial sem destruir a Vanilla/Common bee.

Regra desejada:

```
Duas Vanilla Bees breedadas em um bioma compatível têm pequena chance
de gerar uma espécie daquele contexto.
```

Chance inicial:

```
3%
```

## 4.2 Regra exata

Quando duas bees breedam:

```
Se parentA active species == curiousbees:common
E parentB active species == curiousbees:common
Então rolar 3% de habitat mutation.
```

Se passar:

```
Escolher resultado a partir do pool de espécies habilitadas para aquele bioma.
```

Exemplo:

```
Vanilla + Vanilla em plains:
3% de chance de vir Meadow.

Vanilla + Vanilla em forest:
3% de chance de vir Forest.

Vanilla + Vanilla em desert/savanna/badlands:
3% de chance de vir Arid.
```

Mas isso deve ser data-driven:

```
Não hardcodar Meadow/Forest/Arid.
```

## 4.3 Modelo data-driven recomendado

```
{
  "id":"curiousbees:habitat_discovery_from_vanilla",
  "type":"curiousbees:habitat_discovery",
  "parents": [
"curiousbees:common",
"curiousbees:common"
  ],
  "base_chance":0.03,
  "result_source":"habitat_species_pool",
  "result_modes": {
    "partial_chance":0.95,
    "full_chance":0.05
  }
}
```

O `habitat_species_pool` vem das species/nest/habitat definitions carregadas.

## 4.4 O que significa partial/full

Partial:

```
Common / Common -> Meadow / Vanilla
```

Full:

```
Common / Common -> Meadow / Meadow
```

Isso mantém o espírito Forestry: surpresa, grind, linhagem e purificação.

---

# 5. Advanced Beehive — sem Expansion Box

## 5.1 Função

O Advanced Beehive é o bloco principal de produção do mod.

Ele deve:

```
- aceitar bees vivas que entram naturalmente;
- mostrar quais bees estão habitando;
- produzir combs conforme species/genetics;
- aceitar frames;
- armazenar outputs;
- ser automável;
- parecer visualmente uma evolução da beehive vanilla.
```

## 5.2 Textura / aparência do bloco

Visual esperado:

```
Textura muito próxima da vanilla beehive.
```

Direção:

```
Não parecer máquina de metal.
Não parecer apiary do Forestry.
Não parecer bloco tech genérico.
Não copiar textura do Productive Bees.
```

A linguagem visual deve ser:

```
Vanilla beehive melhorada.
Madeira + honeycomb + tesoura + campfire.
Levemente mais trabalhada, mas ainda Minecraft.
```

## 5.3 Capacidade sem Expansion Box

```
Bee slots:    3
Frame slots:  3
Output slots: 9
Upgrade slots: 0
```

Importante:

```
Os 3 bee slots não são slots de item comuns.
Eles são visualizações das abelhas habitando a hive.
```

## 5.4 Bee slots

Cada bee slot deve mostrar:

```
- ícone/render da abelha;
- espécie e dados geneticos
```

## 5.5 Inserção de abelha

Permitido:

```
1. Abelha entra naturalmente pela AI vanilla
```

Não permitido:

```
Arrastar uma Bee item genérica.
Inserir abelha por hopper.
Inserir abelha em qualquer hive/nest.
```

## 5.6 Frames

Slots:

```
3 frame slots
```

Aceitam apenas itens com tag:

```
curiousbees:frames
```

Frames afetam produção via resolver.

Frames têm durabilidade.

Quando um frame quebra:

```
- slot fica vazio;
- som/partícula opcional;
- log FINE;
- GUI atualiza.
```

## 5.7 Outputs

Slots:

```
9 output slots
```

Devem receber:

```
- combs;
```

## 5.8 Honey level

A Advanced Beehive não deve manter comportamento visual/vanilla de honey level, o produto do Curious Bees para processamento é:

```
Bee -> Beehive -> Comb
Comb -> Centrifuge -> Honey/Wax/By-products
```

O ADR atual de honey já separa isso: honey utilizável vem da centrifuge, não da hive.

## 5.9 Automação

Contrato:

```
Top / sides:
- inserir frames, se slot válido;
- extrair outputs, se pipe permitir.

Bottom:
- extrair outputs;
- não inserir frames;
- não inserir bees.

Bee slots:
- nunca expostos por IItemHandler.

Upgrade slots:
- não existem sem Expansion Box.
```

---

# 6. Advanced Beehive — com Expansion Box

## 6.1 Função da Expansion Box

A Expansion Box não é uma hive separada.

Ela é:

```
um anexo do Advanced Beehive que expande a capacidade da mesma hive.
```

Nome recomendado:

```
Beehive Expansion Box
curiousbees:beehive_expansion_box
```

## 6.2 Mudança necessária nos docs atuais

O ADR atual diz que a extension é principalmente um proxy de automação acima/abaixo e não uma expansão real de capacidade. Isso conflita com o produto esperado agora. Ela também deve funcionar pra automação, é literalmente expansão, tanto em capacidade interna quando de espaço fisico que ocupa, alem de aumentar a capacidade interna, ela é literalmente uma extensão, eu clico abre a interface, coloco um cabo ele funciona igual na hive principal etc

Nova decisão:

```
A Expansion Box deve expandir a capacidade da Advanced Beehive.
```

## 6.3 Capacidade com Expansion Box

```
Bee slots:     7
Frame slots:   3
Output slots:  9
Upgrade slots: 3
```

Comparação:

```
Sem expansion:
3 bees, 3 frames, 9 outputs, 0 upgrades.

Com expansion:
7 bees, 3 frames, 9 outputs, 3 upgrades.
```

## 6.4 Posicionamento no mundo

Regra recomendada:

```
A Expansion Box deve ser colocada diretamente abaixo da Advanced Beehive.
```

Motivo:

```
É o comportamento mais legível visualmente e parecido com a referência.
```

Aceitar acima também é possível, mas menos intuitivo. Para reduzir bug, escolher uma regra:

```
MUST: Expansion Box abaixo da Advanced Beehive.
MUST NOT: Expansion Box lateral.
MUST NOT: múltiplas Expansion Boxes empilhadas.
```

Se o usuário quebrar a Advanced Beehive:

```
- Expansion Box para de funcionar;
- não crasha;
- pode dropar.
```

Se o usuário quebrar a Expansion Box:

```
- capacidade volta para 3 bees;
- se havia mais de 3 bees dentro, as excedentes devem ser liberadas no mundo com segurança;
- nunca apagar abelhas.
```

Recomendação mais segura:

```
Ao remover a Expansion Box, liberar imediatamente as bees excedentes no mundo.
```

Não pode:

```
sumir com bees excedentes.
```

## 6.5 Upgrade slots

Com Expansion Box:

```
3 upgrade slots
```

Sem Expansion Box:

```
0 upgrade slots
```

Upgrade slots aceitam apenas:

```
curiousbees:beehive_upgrades
```

O efeito dos upgrades pode começar simples, mas os slots precisam existir e sincronizar.

Exemplos futuros:

```
- productivity upgrade;
- simulation upgrade;
- range upgrade;
- automation upgrade;
```

Não implementar efeitos complexos agora se não estiverem definidos. Pode aceitar placeholder funcional seguro:

```
Upgrade slot existe.
Aceita item válido.
Item ainda não altera produção, mas isso deve estar marcado como DEV-PLACEHOLDER funcional.
```

---

# 7. GUI — Advanced Beehive sem Expansion Box

## 7.1 Referência visual

A referência é Productive Bees no sentido de UX:

```
- bee area visual;
- slots bem claros;
- layout sem parecer menu debug;
- hive como painel de produção.
```

Não copiar textura, asset ou código.

## 7.2 Tamanho da tela

Recomendação:

```
Background: 176x166 ou 194x166
```

Se precisar manter mais próximo da referência:

```
194x166
```

Mas o mais importante é o layout lógico.

## 7.3 Layout obrigatório

Sem Expansion Box:

```
Título:
Advanced Beehive

Área esquerda:
3 bee slots visuais em formato honeycomb.

Área centro-esquerda:
3 frame slots em coluna vertical.

Área centro-direita:
9 output slots em grid 3x3.

Parte inferior:
Player inventory padrão.
```

Representação em texto:

```
Advanced Beehive

[ Bee 1 ]          [Frame]     [Out][Out][Out]
      [ Bee 2 ]    [Frame]     [Out][Out][Out]
[ Bee 3 ]          [Frame]     [Out][Out][Out]

Inventory
[ player inventory... ]
[ hotbar... ]
```

## 7.4 Estados visuais dos bee slots

Slot vazio:

```
honeycomb outline vazio
```

Slot ocupado por bee :

```
ícone/render da bee + nome da espécie em tooltip e dados geneticos
```

Tooltip mínimo:

```
Meadow Bee
Status: Working / Resting / Has Nectar
Dados geneticos
```

---

# 8. GUI — Advanced Beehive com Expansion Box

## 8.1 Layout obrigatório

Com Expansion Box:

```
Título:
Advanced Beehive

Área esquerda:
7 bee slots visuais em formato honeycomb.

Área centro-esquerda:
3 frame slots em coluna vertical.

Área centro:
9 output slots em grid 3x3.

Área direita:
3 upgrade slots em coluna vertical.

Parte inferior:
Player inventory padrão.
```

Representação em texto:

```
Advanced Beehive

      [Bee]
[Bee] [Bee] [Bee]       [Frame]     [Out][Out][Out]     [Upgrade]
[Bee] [Bee] [Bee]       [Frame]     [Out][Out][Out]     [Upgrade]
      [Bee]             [Frame]     [Out][Out][Out]     [Upgrade]

Inventory
[ player inventory... ]
[ hotbar... ]
```

## 8.2 Regra de consistência

A GUI deve ser a mesma tela, alternando layout conforme:

```
hasExpansionBox == false -> 3 bee slots, 0 upgrade slots
hasExpansionBox == true  -> 7 bee slots, 3 upgrade slots
```

Não criar duas telas completamente diferentes.

## 8.3 Sync obrigatório

Servidor manda para o cliente:

```
- hasExpansionBox;
- beeCount;
- dados resumidos de cada bee occupant;
- frame inventory;
- output inventory;
- upgrade inventory;
- status/error enum;
```

Cliente não deve inventar capacidade.

---

# 9. Bee Jar e Bee Transporter

## 9.1 Estado atual

Bug crítico:

```
Captura remove a bee do mundo,
mas o item fica Empty ou não consegue liberar.
```

Isso é P0/blocker.

## 9.2 Regra absoluta

Nunca remover a bee do mundo antes de garantir que os dados foram gravados no item.

Fluxo correto:

```
1. Player right-click Bee com Bee Jar/Transporter vazio.
2. Server valida que o item está vazio.
3. Server serializa dados da bee.
4. Server grava dados no ItemStack.
5. Server confirma que o ItemStack agora está carregado.
6. Só então remove a bee do mundo.
```

Se qualquer etapa falhar:

```
- bee continua viva no mundo;
- item continua vazio;
- log WARNING;
- mensagem localizada para o player.
```

## 9.3 Dados mínimos armazenados

O item carregado deve armazenar:

```
- entity type: minecraft:bee;
- genome data;
- custom name, se houver;
- age/baby/adult, se relevante;
- health;
- has nectar, se relevante;
- anger state, se for seguro;
- species display cache opcional;
- full entity NBT somente se seguro e compatível.
```

Recomendação:

```
StoredBeeData
```

Com codec/data component.

## 9.4 Tooltip

Bee Jar vazio:

```
Empty Bee Jar
```

Bee Jar carregado com bee:

```
Bee Jar
Contains: Meadow Bee
Dados genéticos
```

Bee Transporter segue a mesma lógica.

## 9.5 Release no mundo

Right-click com item carregado:

```
1. Validar posição.
2. Criar Bee entity.
3. Restaurar dados.
4. Spawnar no mundo.
5. Confirmar spawn.
6. Limpar/consumir item.
```

Bee Jar:

```
single-use;
ao liberar, item some
```

Bee Transporter:

```
reusable;
ao liberar, item volta vazio.
```

Se não conseguir spawnar:

```
- não limpar item;
- não consumir item;
- mostrar erro;
- log WARNING.
```

## 9.6 Inserção na Advanced Beehive

Com GUI aberta:

```
Player clica bee slot vazio segurando item carregado.
```

Fluxo:

```
1. Server valida hive.
2. Server valida slot vazio.
3. Server valida item carregado.
4. Server transfere StoredBeeData para occupant interno da hive.
5. Server confirma occupant gravado.
6. Server limpa/consome item.
7. Server synca menu.
```

Se falhar:

```
- item continua carregado;
- hive não muda parcialmente;
- bee não desaparece.
```

## 9.7 Não permitido

```
Bee Jar em nest block não deve inserir bee.
Bee Transporter em nest block não deve inserir bee.
Hopper não interage com bee item slot.
Bee item não pode guardar mais de uma bee.
```

O ADR atual já aceitou Bee Jar/Transporter como escopo restrito para Advanced Hive, mas a implementação precisa ser corrigida para nunca perder dados.

---

# 10. Centrifuge

## 10.1 Função

A Centrifuge processa combs.

Fluxo:

```
Comb -> Centrifuge -> Wax + by-products + honey portions
Honey portions + empty bottle -> honey bottle
```

Sem energia.

Processamento lento estilo fornalha.

## 10.2 Slots obrigatórios

```
Input:
1 slot de comb.

Outputs:
9 slots de itens resultantes.

Bottle input:
1 slot para glass bottle.

Honey bottle output:
1 slot dedicado para honey bottle.

Honey indicator:
barra/coluna com até 5 porções de mel.
```

## 10.3 Layout obrigatório

Referência visual:

```
Centrifuge

[Comb Input] -> [Out][Out][Out]       [Bottle In]       [Honey Bar]
                [Out][Out][Out]           ↓
                [Out][Out][Out]     [process visual]
													                ↓
                                   [Honey Bottle Out]

Inventory
[player inventory]
[hotbar]
```

## 10.4 Honey counter

A Centrifuge tem contador interno:

```
honeyCounter: 0..5
```

Cada ciclo de processamento pode adicionar porções de mel conforme receita.

Se `honeyCounter > 0` e houver glass bottle:

```
- consumir 1 glass bottle;
- reduzir honeyCounter em 1;
- gerar 1 honey bottle no slot dedicado.
```

Se slot de honey bottle estiver cheio:

```
- não consumir bottle;
- não reduzir honeyCounter.
```

Se `honeyCounter == 0`:

```
- não produzir honey bottle.
```

## 10.5 Overflow

Aqui há uma decisão a confirmar, porque o ADR atual diz que overflow pode ser descartado e logado em FINE.

Minha recomendação para gameplay:

```
Se honeyCounter está em 5, a Centrifuge continua processando,
mas novas porções de honey são perdidas.
```

Motivo:

```
Evita travar a máquina inteira por falta de garrafa.
```

Mas a GUI precisa deixar claro:

```
Honey buffer full.
Insert bottles to collect honey.
```

## 10.6 Processing

Sem energia:

```
Não requer FE.
Não requer combustível.
Não requer redstone.
```

Velocidade:

```
Lenta como fornalha.
Base sugerida: 200 ticks por comb.
```

Upgrades futuros podem alterar isso, mas o comportamento base não deve depender de upgrade.

## 10.7 Recipes

Recipe JSON conceitual:

```
{
  "type":"curiousbees:centrifuge",
  "ingredient": {
    "item":"curiousbees:meadow_comb"
  },
  "input_count":1,
  "processing_time":200,
  "honey_portions":1,
  "outputs": [
    {
      "item":"curiousbees:wax",
      "count":1,
      "chance":1.0
    },
    {
      "item":"minecraft:yellow_dye",
      "count":1,
      "chance":0.5
    }
  ]
}
```

## 10.8 Automação

Contrato:

```
Top:
- inserir comb no input.

Sides:
- inserir glass bottle no bottle input;
- extrair outputs se pipe permitir.

Bottom:
- extrair outputs;
- extrair honey bottle;
- não inserir comb;
- não inserir bottle;
```

Outputs são extract-only.

---

# 11. UX geral das telas

## 11.1 O que precisa melhorar

As telas não podem parecer:

```
painel cinza genérico;
debug menu;
slot grid sem intenção;
```

Elas precisam parecer:

```
Minecraft GUI;
beehive/honeycomb;
legível;
produtiva;
parecida em clareza com Productive Bees.
```

## 11.2 Direção visual

Tema:

```
wood + honey + honeycomb + vanilla UI
```

Paleta:

```
cinza vanilla para slots;
marrom madeira;
amarelo mel;
bordas escuras Minecraft;
sem gradiente moderno.
```

## 11.3 Regra de asset

Se ainda não tiver asset final:

```
usar placeholder marcado DEV-PLACEHOLDER.
```

Não pode commitar asset “bonitinho” fingindo que é final sem revisão. Isso já é regra nos docs atuais de asset.

---

# 12. Persistência e sync

## 12.1 Nunca perder bee

Qualquer sistema que armazena bee deve seguir regra transacional:

```
Só remover da origem depois que o destino confirmou persistência.
```

Origem pode ser:

```
- mundo;
- Bee Jar;
- Bee Transporter;
- Advanced Beehive;
```

Destino pode ser:

```
- mundo;
- Bee Jar;
- Bee Transporter;
- Advanced Beehive;
```

Se falhar:

```
estado anterior permanece.
```

## 12.2 Dados da Advanced Beehive

Persistir:

```
- occupant bees;
- frame inventory;
- output inventory;
- upgrade inventory;
- progress/status se existir;
- link com Expansion Box;
```

## 12.3 Dados da Centrifuge

Persistir:

```
- comb input;
- output slots;
- bottle input;
- honey bottle output;
- upgrade slots;
- progress;
- max progress;
- honeyCounter;
```

## 12.4 Sync de GUI

Toda informação exibida na GUI precisa vir do servidor.

Não aceitar:

```
cliente recalculando occupant count;
cliente inferindo hasExpansionBox sozinho;
cliente mostrando bee que o servidor não tem.
```

---

# 13. Ordem de implementação recomendada

## Sprint/fase 1 — Correção de bugs bloqueantes

```
1. Corrigir Bee Jar / Bee Transporter.
2. Garantir capture/release sem perda de bee.
3. Criar teste/manual checklist para:
   - capture;
   - tooltip;
   - release;
   - save/load.
```

Definition of done:

```
Nenhuma ação de transporte pode apagar bee.
```

---

## Sprint/fase 2 — Reset de conceito da hive

```
1. Renomear/migrar conceito para Advanced Beehive.
2. Remover player-facing Genetic Apiary / Apiary.
3. Garantir creative tab, lang, recipes e docs usando Advanced Beehive.
4. Definir compat/migration para blocos antigos.
```

Definition of done:

```
Jogador só vê Advanced Beehive como hive principal do mod.
```

---

## Sprint/fase 3 — Spawn/species correto

```
1. Adicionar Vanilla/Common Bee como espécie base ou definir representação equivalente.
2. Corrigir spawn egg vanilla para não virar species de bioma, e sim a common bee que é a vanilla
3. Corrigir spawn eggs de espécies para respeitar espécie do ovo.
4. Remover handler que reescreve espécie por bioma.
5. Implementar wild nests com occupants.
6. Implementar habitat discovery mutation Vanilla + Vanilla com 3%.
```

Definition of done:

```
Spawn egg vanilla gera Common Bee/vanilla bee.
Wild nest gera bee da definição do nest.
Breeding vanilla em bioma pode descobrir espécie por chance.
Nada é hardcoded por if de bioma.
```

---

## Sprint/fase 4 — Advanced Beehive GUI sem expansion

```
1. Layout com 3 bee slots visuais.
2. 3 frame slots.
3. 9 output slots.
4. Sem upgrade slots.
5. Bee preview/tooltip funcionando.
6. Inserção manual só via Bee Jar/Transporter.
```

Definition of done:

```
A tela parece uma hive produtiva, não um debug inventory.
```

---

## Sprint/fase 5 — Expansion Box

```
1. Expansion Box abaixo da Advanced Beehive.
2. Capacidade muda para 7 bees.
3. Adiciona 3 upgrade slots.
4. GUI muda dinamicamente.
5. Quebra/remoção nunca apaga bees.
```

Definition of done:

```
Com expansion: 7 bees, 3 frames, 9 outputs, 3 upgrades.
Sem expansion: 3 bees, 3 frames, 9 outputs, 0 upgrades.
```

---

## Sprint/fase 6 — Centrifuge UX e lógica final

```
1. Slot comb input.
2. 9 output slots.
3. Bottle input.
4. Honey bottle output dedicado.
5. Honey counter 0..5.
6. Barra visual de honey.
7. 3 upgrade slots.
8. Processamento lento sem energia.
```

Definition of done:

```
Comb processa.
Itens saem.
Honey counter sobe.
Bottle vira honey bottle.
Máquina salva/recarrega estado.
```

---

# 14. Prompt para mandar para agente de código

```
You are working on Curious Bees, a NeoForge 1.21.1 Minecraft mod.

The current implementation finished a roadmap on paper, but product review rejected several behaviors. Treat this as a productization reset, not species expansion.

Core rules:
- Living vanilla Bee entities remain the default gameplay representation.
- Do not turn the whole mod into item-only bees.
- Genetics remains Mendelian and probabilistic.
- Do not reveal full genetics before analysis.
- The player-facing hive concept must be Advanced Beehive, not Apiary/Genetic Apiary.
- Productive Bees is a UX/layout reference only. Do not copy assets or code.

Implement in small commits.

Priority 1:
Fix Bee Jar and Bee Transporter.
They currently remove bees from the world but leave the item empty or unable to release the bee.
Capture must be transactional:
1. serialize bee data into item;
2. verify item is loaded;
3. only then remove bee from world.
If anything fails, keep the bee alive and leave the item unchanged.
Release must restore the bee and only clear/consume the item after successful spawn.
Insertion into Advanced Beehive bee slots must also be transactional.

Priority 2:
Reset species assignment.
Do not assign species only from biome on generic bee spawn.
Vanilla Bee Spawn Egg must create a Vanilla Bee genome/species.
Species spawn eggs must create their exact species.
Wild nests define which species they contain.
Biome only influences which wild nest can generate and habitat-discovery mutation.

Priority 3:
Implement wild nests with occupants.
Nest generation must be data-driven.
A generated nest can start with 1–3 bees inside.
The nest’s occupant_species_pool defines what species those bees are.
No hardcoded if biome == desert -> arid style logic.

Priority 4:
Implement habitat discovery mutation:
Vanilla + Vanilla breeding in a biome with habitat species candidates has 3% chance to mutate into one species from that habitat pool.
This must be data-driven and species-agnostic.

Priority 5:
Advanced Beehive UI.
Without Expansion Box:
- 3 visual bee slots;
- 3 frame slots;
- 9 output slots;
- 0 upgrade slots.

With Beehive Expansion Box:
- 7 visual bee slots;
- 3 frame slots;
- 9 output slots;
- 3 upgrade slots.

Bee slots are not normal item slots and are not exposed to automation.
They show bees inhabiting the hive.
Manual insertion is allowed only through loaded Bee Jar/Bee Transporter.

Priority 6:
Centrifuge UI and logic.
Slots:
- 1 comb input;
- 9 result output slots;
- 1 glass bottle input;
- 1 honey bottle output;
- 3 upgrade slots;
- honey counter 0..5 shown in GUI.

No energy.
Processing should be slow like a furnace.
Honey bottle output consumes one honey portion and one glass bottle only when output can accept the honey bottle.

Do not add new resource bees.
Do not add large species trees.
Do not implement temperature/humidity simulation.
Do not implement JEI/REI unless separately scoped.
```

---

# 15. Critério final de aceite

O reset só pode ser considerado pronto quando:

```
1. Bee Jar não perde bee.
2. Bee Transporter não perde bee.
3. Spawn egg vanilla gera Vanilla Bee.
4. Spawn egg de espécie gera a espécie correta.
5. Bioma não sobrescreve espécie de qualquer bee genérica.
6. Wild nest pode nascer com bees dentro.
7. Vanilla + Vanilla tem mutação habitat-discovery de 3%, data-driven.
8. Só existe Advanced Beehive como hive principal player-facing.
9. Advanced Beehive sem expansion tem 3 bees, 3 frames, 9 outputs.
10. Advanced Beehive com expansion tem 7 bees, 3 frames, 9 outputs, 3 upgrades.
11. Bee slots mostram occupants, não itens fake.
12. Centrifuge tem comb input, 9 outputs, bottle input, honey bottle output, honey counter 0..5 e 3 upgrades.
13. Nenhum fluxo apaga bee silenciosamente.
14. Save/load preserva bees em item, hive e mundo.
15. GUI é visualmente próxima da referência de UX, não painel cinza genérico.
```

# Addendum A — Remover análise como gate de gameplay

## A.1 Decisão nova

Remover a necessidade de “analisar” a abelha para liberar informação genética.

A regra antiga:

```
Bee precisa ser analisada antes de revelar genética.
```

Deve ser substituída por:

```
Dados genéticos são visíveis sempre que o jogador estiver em uma interface controlada do mod:
- Advanced Beehive;
- Bee Jar / Bee Transporter no inventário;
- tooltip/inspeção de item capturado;
- telas internas do mod.
```

Isso muda uma regra forte dos docs atuais, porque hoje o projeto diz que a genética não deve ser revelada antes da análise. Essa regra precisa ser atualizada em `requirements.md`, `architecture.md`, `TASKS.md` e possivelmente `decisions.md`.

---

## A.2 Motivo

O sistema atual não é Forestry.

No Forestry, a análise faz sentido porque:

```
bee = item;
bee circula em máquinas;
automação trabalha com princess/drone/queen;
analyzer é parte natural da progressão.
```

No Curious Bees:

```
bee = entidade viva vanilla;
produção acontece com abelhas habitando hive;
transporte é exceção controlada;
automação principal é hive + frames + outputs + centrifuge.
```

Então exigir análise como etapa obrigatória cria atrito sem entregar uma automação interessante.

---

## A.3 Novo papel do Bee Analyzer

Decisão recomendada:

```
Bee Analyzer deixa de ser requisito do loop principal.
```

 Remover Bee Analyzer do gameplay

```
- Remover receita.
- Remover item da creative tab principal.
- Remover necessidade de analyzed flag.
- Remover tela/bloco de analyzer se existir.
- Manter apenas código interno de “genetic report” para renderizar dados em outras UIs.
```

A regra importante é:

```
Analyzer não deve mais ser gate.
```

---

## A.4 Remover analyzed flag como mecânica obrigatória

O campo:

```
analyzed: true/false
```

não deve mais controlar se a genética aparece.

Se for mantido temporariamente por compatibilidade, ele deve ser tratado como deprecated:

```
- pode existir em saves antigos;
- pode existir no StoredBeeData antigo;
- não deve esconder dados;
- não deve ser usado para bloquear UI;
- pode ser removido numa migração futura.
```

---

# Addendum B — Mostrar genética direto no Advanced Beehive

## B.1 Nova regra

O Advanced Beehive deve mostrar os dados genéticos das abelhas ocupantes diretamente.

Não apenas:

```
Meadow Bee
Working
```

Mas sim uma leitura genética útil:

```
Species: Meadow / Forest
Purity: Hybrid
Productivity: Fast / Normal
Fertility: Two / Three
Flower Type: Flowers / Leaves
```

Sem `Lifespan`.

---

## B.2 Layout recomendado

A tela principal ainda mostra os bee slots visuais.

Ao passar o mouse em uma bee slot:

```
Tooltip resumido com genética.
```

Ao clicar em uma bee slot:

```
Painel lateral ou popup interno com relatório completo.
```

Exemplo de tooltip resumido:

```
Meadow Bee
Species: Meadow / Forest
Purity: Hybrid
Productivity: Fast / Normal
Fertility: Two / Three
Flower Type: Flowers / Leaves
```

Exemplo de painel completo:

```
Genetic Report

Species
Active: Meadow
Inactive: Forest
Purity: Hybrid

Productivity
Active: Fast
Inactive: Normal

Fertility
Active: Two
Inactive: Three

Flower Type
Active: Flowers
Inactive: Leaves
```

---

## B.3 Sem raw IDs

Mesmo mostrando genética direto, a UI não deve mostrar IDs internos.

Não mostrar:

```
curiousbees:species/meadow
curiousbees:productivity/fast
```

Mostrar:

```
Meadow
Fast
Flowers
```

IDs continuam só para debug/log.

---

## B.4 Sem dependência de análise

A hive não deve fazer:

```
if bee.isAnalyzed() show genes else hide genes
```

A hive deve fazer:

```
if bee has valid genome:
    show genetic report
else:
    show "Invalid or missing genome" and recover safely
```

Se a bee não tiver genome por bug/save antigo:

```
- aplicar fallback seguro;
- logar WARNING;
- atualizar a UI depois da correção;
- nunca crashar.
```

---

# Addendum C — Mostrar genética no inventário quando a bee está capturada

## C.1 Nova regra

Bee Jar e Bee Transporter carregados devem mostrar genética direto no tooltip do item.

Não deve depender de análise.

---

## C.2 Tooltip padrão

Tooltip sem Shift:

```
Bee Transporter
Contains: Meadow Bee
Species: Meadow / Forest
Productivity: Fast / Normal
Fertility: Two / Three
Flower Type: Flowers / Leaves
```

Para Bee Jar:

```
Bee Jar
Contains: Arid Bee
Species: Arid / Vanilla
Productivity: Slow / Normal
Fertility: One / Two
Flower Type: Cactus / Flowers
```

---

## C.3 Tooltip detalhado com Shift

Se o tooltip ficar grande demais, usar padrão Minecraft:

```
Hold Shift for genetic details
```

Sem Shift:

```
Bee Transporter
Contains: Meadow Bee
Purity: Hybrid
Productivity: Fast
```

Com Shift:

```
Genetic Details

Species
Active: Meadow
Inactive: Forest

Productivity
Active: Fast
Inactive: Normal

Fertility
Active: Two
Inactive: Three

Flower Type
Active: Flowers
Inactive: Leaves
```

---

## C.4 Dados usados pelo tooltip

O tooltip deve ler do `StoredBeeData` salvo no item.

Se o item está carregado mas não tem genome válido:

```
Bee Transporter
Contains: Bee
Genome: Missing / Invalid
```

E o código deve:

```
- logar WARNING;
- não apagar o item;
- não apagar a bee armazenada;
- permitir release seguro se houver entity data suficiente.
```

---

# Addendum D — Remover Lifespan do sistema

## D.1 Decisão nova

Remover `LIFESPAN` como trait/chromosome relevante do Curious Bees.

Motivo:

```
Abelha vanilla não tem ciclo de vida tipo Forestry.
Não existe queen morrendo.
Não existe princess/drone substituindo queen.
Não existe larvae/death lifecycle por padrão.
```

Os próprios docs atuais já dizem que lifecycle/death/larvae não devem ser mecânicas padrão, então manter Lifespan como gene principal passa a mensagem errada.

---

## D.2 Chromosomes válidos após reset

MVP deve ficar com:

```
SPECIES
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

Remover da UI e do conteúdo:

```
LIFESPAN
```

---

## D.3 O que remover do jogador

Não mostrar mais:

```
Lifespan: Short / Normal / Long
```

Não usar lifespan para:

```
- produção;
- morte;
- tempo dentro da hive;
- breeding;
- tooltip;
- analyzer;
- hive report;
- captured bee tooltip.
```

---

## D.4 Migração técnica segura

Não precisa necessariamente apagar o enum/classe em um commit gigante.

Fazer em duas fases:

### Fase 1 — Desativar gameplay/display

```
- Remover Lifespan das species definitions atuais.
- Remover Lifespan dos reports.
- Remover Lifespan da GUI.
- Remover Lifespan dos tooltips.
- Remover Lifespan dos testes de display.
- Serializer deve tolerar genomes antigos que ainda tenham LIFESPAN.
```

### Fase 2 — Limpeza estrutural

```
- Remover ChromosomeType.LIFESPAN se for seguro.
- Remover trait definitions antigas.
- Remover lang keys.
- Remover JSONs antigos.
- Atualizar testes de inheritance/content.
```

Se remover de uma vez quebrar save antigo ou muito teste, manter como deprecated internamente:

```
LIFESPAN exists only for legacy compatibility.
It is ignored by gameplay and hidden from UI.
```

---

## D.5 Conteúdo built-in atualizado

As espécies não devem mais definir:

```
lifespan: normal
lifespan: long
lifespan: short
```

Exemplo conceitual novo:

```
{
  "id":"curiousbees:meadow",
  "traits": {
    "productivity":"normal",
    "fertility":"two",
    "flower_type":"flowers"
  }
}
```

---

# Addendum E — Atualizar o conceito de genetic report

## E.1 Novo report padrão

O genetic report deve conter:

```
Species
Productivity
Fertility
Flower Type
Purity / Hybrid status
```

Não conter:

```
Lifespan
Analyzed / Unanalyzed gate
```

---

## E.2 Modelo conceitual

```
BeeGeneticReport
- speciesGene
- productivityGene
- fertilityGene
- flowerTypeGene
- isSpeciesPurebred
- isOverallPurebred
```

Cada gene report:

```
GeneReport
- activeDisplayName
- inactiveDisplayName
- activeDominance
- inactiveDominance
- isPurebred
```

Opcional:

```
- activeId
- inactiveId
```

Mas IDs só para lógica/debug, não para UI player-facing.

---

# Addendum F — Atualizações obrigatórias nos docs

## F.1 Requirements

Remover/sobrescrever a regra:

```
Genetics may not be revealed in full before analysis.
```

Trocar por:

```
Genetic data may be shown directly in Curious Bees controlled interfaces,
including Advanced Beehive, captured bee item tooltips, and optional inspector tools.
Internal raw IDs must not be shown in normal player-facing UI.
```

---

## F.2 Architecture

Atualizar a seção de Analyzer.

Antes:

```
Analyzer gates genetic visibility.
```

Depois:

```
GeneticReport is a reusable UI/report service.
Analyzer, if kept, is only one consumer.
Advanced Beehive and captured bee items can also render the same report.
```

Remover `LIFESPAN` da lista de chromosomes MVP.

---

## F.3 Decisions

Criar nova ADR:

```
ADR-00XX — Remove analysis gating and expose genetics in controlled UIs
```

Criar nova ADR ou mesma ADR:

```
ADR-00XY — Remove Lifespan as MVP chromosome
```

Essas decisões supersedem partes antigas do design.

---

## F.4 Tasks

Adicionar tasks novas:

```
P0/P1 — Remove analysis gate from UI/report paths.
P0/P1 — Show genetic report in Advanced Beehive bee slots.
P0/P1 — Show genetic report in Bee Jar/Bee Transporter tooltip.
P0/P1 — Deprecate/remove Lifespan from reports/content/UI.
P0/P1 — Update tests for no analyzed gate and no lifespan.
```

---

# Addendum G — Critérios de aceite adicionais

```
1. Nenhuma UI principal exige analyzed=true para mostrar genética.
2. Advanced Beehive mostra genética das bees ocupantes.
3. Bee Jar carregado mostra genética no tooltip.
4. Bee Transporter carregado mostra genética no tooltip.
5. Bee Analyzer, se existir, é ferramenta opcional, não gate.
6. Lifespan não aparece em nenhuma UI player-facing.
7. Lifespan não afeta produção, breeding, hive ou centrifuge.
8. Saves/genomes antigos com Lifespan não crasham.
9. Raw allele IDs não aparecem para o jogador em UI normal.
10. Genetic report é reutilizado por hive, item tooltip e analyzer/inspector opcional.
```

---

# Addendum H — Prompt curto para encaixar no reset

```
Additional product reset changes:

Remove analysis gating.
The player should not need to analyze a bee to see its genetics in Curious Bees controlled interfaces.
Advanced Beehive bee slots, captured Bee Jar/BeeTransporter tooltips, and optional inspector/analyzer tools should show genetic data directly.
The Analyzer must not be a progression gate anymore. If kept, it is only an optional inspection tool.

Remove the analyzed/unanalyzed visibility split from core gameplay UI.
Do not hide species/traits in the Advanced Beehive.
Do not hide species/traits in captured bee item tooltips.
Still do not show raw internal allele IDs in player-facing UI.

Remove Lifespan as an MVP chromosome/trait.
Vanilla bees do not have Forestry-style queen lifespan/death lifecycle, so Lifespan does not fit the current design.
The active MVP chromosomes should be:
- SPECIES
- PRODUCTIVITY
- FERTILITY
- FLOWER_TYPE

Lifespan must not appear in:
- Advanced Beehive UI
- Bee Jar tooltip
- BeeTransporter tooltip
- Analyzer/inspector report
- production logic
- breeding gameplay
- hive timing

For migration safety, old genomes containing Lifespan must not crash.
Either ignore Lifespan as deprecated legacy data or remove it in a dedicated cleanup after compatibility is handled.
```
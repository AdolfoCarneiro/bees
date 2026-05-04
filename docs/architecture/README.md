# Technical architecture specs (02–05)

Canonical definitions for Curious Bees **platform boundaries**, **genetics model**, **breeding and mutation**, and **content data**. These documents are the source of truth for rules agents and implementers must follow; shipped behavior is also reflected in code.

| Doc | Topic |
|-----|--------|
| [02-technical-architecture.md](02-technical-architecture.md) | Module split, `common/genetics` purity, docs layout |
| [03-genetics-system-spec.md](03-genetics-system-spec.md) | Chromosomes, alleles, dominance, genome |
| [04-breeding-and-mutation-spec.md](04-breeding-and-mutation-spec.md) | Breeding flow, inheritance, mutations |
| [05-content-design-spec.md](05-content-design-spec.md) | Species, traits, products as data |

**Product intent** (goals, UX parity, hybrid bee model): [`../post-mvp/gameplay-direction.md`](../post-mvp/gameplay-direction.md)  
**Priorities:** [`../ROADMAP.md`](../ROADMAP.md)

The former `docs/mvp/` path was renamed to `docs/architecture/` to reflect that these files are **living technical specs**, not an MVP roadmap.

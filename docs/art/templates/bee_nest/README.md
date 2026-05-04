# Bee nest block UV reference

Species bee nests use the same block geometry as the vanilla **bee nest**: parent model
`minecraft:block/orientable_with_bottom`. Each face is a separate **16×16** PNG.

## Vanilla layout reference (Minecraft 1.21.1)

Use Mojang’s bee nest textures as a **layout and proportion guide** (do not copy pixels into shipping assets):

- [bee_nest_bottom.png](https://github.com/InventivetalentDev/minecraft-assets/blob/1.21.1/assets/minecraft/textures/block/bee_nest_bottom.png)
- [bee_nest_top.png](https://github.com/InventivetalentDev/minecraft-assets/blob/1.21.1/assets/minecraft/textures/block/bee_nest_top.png)
- [bee_nest_side.png](https://github.com/InventivetalentDev/minecraft-assets/blob/1.21.1/assets/minecraft/textures/block/bee_nest_side.png)
- [bee_nest_front.png](https://github.com/InventivetalentDev/minecraft-assets/blob/1.21.1/assets/minecraft/textures/block/bee_nest_front.png)
- [bee_nest_front_honey.png](https://github.com/InventivetalentDev/minecraft-assets/blob/1.21.1/assets/minecraft/textures/block/bee_nest_front_honey.png)

The block model assigns `particle` to the same texture as `side`.

## Curious Bees files per species

Replace `<species>` with `meadow`, `forest`, or `arid`:

```text
textures/block/<species>_bee_nest_bottom.png
textures/block/<species>_bee_nest_top.png
textures/block/<species>_bee_nest_side.png
textures/block/<species>_bee_nest_front.png
textures/block/<species>_bee_nest_front_honey.png
```

Face roles match vanilla: **front** carries the entrance hole; **front_honey** is the dripping-honey variant for full honey level.

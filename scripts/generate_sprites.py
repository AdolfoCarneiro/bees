#!/usr/bin/env python3
"""
Generates Curious Bees 16×16 item and block-face sprites (PNG).
Uses only Python stdlib — no external dependencies.

Run from the repo root:
  python scripts/generate_sprites.py
"""

import struct
import zlib
from pathlib import Path
from copy import deepcopy

# ---------------------------------------------------------------------------
# PNG encoder
# ---------------------------------------------------------------------------

def _encode_png(pixels: list) -> bytes:
    h = len(pixels)
    w = len(pixels[0])

    def chunk(tag: bytes, data: bytes) -> bytes:
        crc = zlib.crc32(tag + data) & 0xFFFFFFFF
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", crc)

    ihdr = struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0)  # RGBA

    raw = bytearray()
    for row in pixels:
        raw.append(0)
        for (r, g, b, a) in row:
            raw.extend((r, g, b, a))

    return (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", ihdr)
        + chunk(b"IDAT", zlib.compress(bytes(raw), 9))
        + chunk(b"IEND", b"")
    )


def _write(path: Path, pixels: list) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(_encode_png(pixels))
    rel = "/".join(path.parts[-4:])
    print(f"  {rel}")


# ---------------------------------------------------------------------------
# Canvas helpers
# ---------------------------------------------------------------------------

TRANS = (0, 0, 0, 0)

def canvas(w=16, h=16, bg=TRANS):
    return [[bg] * w for _ in range(h)]

def fill(cv, x1, y1, x2, y2, color):
    """Fill rectangle [x1,x2) × [y1,y2) with color."""
    for y in range(max(0, y1), min(len(cv), y2)):
        for x in range(max(0, x1), min(len(cv[0]), x2)):
            cv[y][x] = color

def hline(cv, y, x1, x2, color):
    fill(cv, x1, y, x2, y + 1, color)

def vline(cv, x, y1, y2, color):
    fill(cv, x, y1, x + 1, y2, color)


# ---------------------------------------------------------------------------
# Palette
# ---------------------------------------------------------------------------

T   = (  0,   0,   0,   0)   # transparent
DBR = ( 62,  37,  10, 255)   # dark wood border
MBR = (100,  60,  18, 255)   # medium brown
WOO = (160, 100,  38, 255)   # wood mid
LWO = (210, 155,  70, 255)   # light wood / amber
AMB = (200, 148,  58, 255)   # honey amber
GAM = (240, 185,  50, 255)   # bright amber / gold
HON = (248, 220,  80, 255)   # pale honey yellow
DGR = ( 32, 110,  50, 255)   # dark green
LGR = ( 64, 192,  80, 255)   # bright green
DPU = (100,  40, 120, 255)   # dark purple
LPU = (170,  80, 200, 255)   # bright purple
GLS = (136, 176, 210, 255)   # glass blue
DGL = ( 88, 130, 170, 255)   # darker glass
STO = (110, 110, 110, 255)   # stone
LST = (160, 160, 160, 255)   # light stone
HOT = (220,  90,  30, 255)   # orange heat
DRK = ( 30,  18,   6, 255)   # near-black hollow
BLK = ( 20,  20,  20, 255)   # almost black


# ---------------------------------------------------------------------------
# Frame items (16×16) — border + inner window + tint dot
# ---------------------------------------------------------------------------

def make_frame(dot_color=None):
    cv = canvas()
    # Outer border 2px
    fill(cv, 0, 0, 16, 16, DBR)
    # Frame body 3px band
    fill(cv, 2, 2, 14, 14, LWO)
    # Inner hollow
    fill(cv, 3, 3, 13, 13, DRK)
    # Highlight edges
    hline(cv, 2, 2, 14, GAM)
    vline(cv, 2, 2, 14, GAM)
    # Shadow edges
    hline(cv, 13, 2, 14, DBR)
    vline(cv, 13, 2, 14, DBR)
    # Center dot if tinted
    if dot_color:
        fill(cv, 6, 6, 10, 10, dot_color)
        # bright center
        fill(cv, 7, 7, 9, 9, tuple(min(255, c + 60) for c in dot_color[:3]) + (255,))
    return cv


# ---------------------------------------------------------------------------
# Bee Jar (16×16)
# ---------------------------------------------------------------------------

def make_bee_jar():
    cv = canvas()
    # Cap
    fill(cv, 5, 0, 11, 2, DBR)
    fill(cv, 6, 0, 10, 2, AMB)
    # Jar body — oval approximation with fills
    # Row 2: narrow
    fill(cv, 4, 2, 12, 3, DGL)
    # Rows 3-13: main jar
    for y in range(3, 13):
        fill(cv, 3, y, 13, y + 1, DGL)
    # Row 13: narrow
    fill(cv, 4, 13, 12, 14, DGL)
    fill(cv, 5, 14, 11, 15, DBR)
    # Honey fill inside
    fill(cv, 4, 4, 12, 12, GAM)
    fill(cv, 5, 5, 11, 11, HON)
    # Glass highlight
    vline(cv, 3, 3, 13, GLS)
    hline(cv, 3, 4, 8, GLS)
    # Bee silhouette (tiny dot)
    fill(cv, 7, 7, 9, 9, DBR)
    fill(cv, 7, 7, 8, 8, AMB)
    return cv


# ---------------------------------------------------------------------------
# Bee Transporter (16×16)
# ---------------------------------------------------------------------------

def make_bee_transporter():
    cv = canvas()
    # Body
    fill(cv, 1, 1, 15, 15, STO)
    fill(cv, 0, 0, 16, 16, BLK)
    fill(cv, 1, 1, 15, 15, STO)
    fill(cv, 2, 2, 14, 14, LST)
    # Amber window
    fill(cv, 4, 3, 12, 11, DRK)
    fill(cv, 5, 4, 11, 10, DGL)
    fill(cv, 6, 5, 10, 9, GAM)
    fill(cv, 7, 6, 9, 8, HON)
    # Grip / side details
    vline(cv, 2, 4, 12, STO)
    vline(cv, 13, 4, 12, STO)
    # Button
    fill(cv, 6, 11, 10, 13, AMB)
    fill(cv, 7, 11, 9, 12, GAM)
    return cv


# ---------------------------------------------------------------------------
# Block face helpers
# ---------------------------------------------------------------------------

def _block_border(cv):
    """1px dark border around a 16×16 block face."""
    fill(cv, 0, 0, 16, 1, DBR)
    fill(cv, 0, 15, 16, 16, DBR)
    fill(cv, 0, 0, 1, 16, DBR)
    fill(cv, 15, 0, 16, 16, DBR)


def _wood_planks(cv, light=LWO, dark=WOO):
    """Horizontal plank bands."""
    for y in range(1, 15):
        c = light if (y // 2) % 2 == 0 else dark
        fill(cv, 1, y, 15, y + 1, c)


def _wood_grain(cv, light=LWO, dark=AMB):
    """Vertical grain lines."""
    for x in range(1, 15):
        c = light if (x // 2) % 2 == 0 else dark
        fill(cv, x, 1, x + 1, 15, c)


def _slot_recess(cv, cx, cy, w=5, h=5):
    """Sunken slot rectangle centered at (cx,cy)."""
    x1 = cx - w // 2
    y1 = cy - h // 2
    fill(cv, x1, y1, x1 + w, y1 + h, DRK)
    fill(cv, x1 + 1, y1 + 1, x1 + w - 1, y1 + h - 1, GAM)
    fill(cv, x1 + 2, y1 + 2, x1 + w - 2, y1 + h - 2, HON)
    # shadow
    hline(cv, y1,         x1,     x1 + w, DBR)
    vline(cv, x1,         y1,     y1 + h, DBR)
    hline(cv, y1 + h - 1, x1,     x1 + w, LWO)
    vline(cv, x1 + w - 1, y1,     y1 + h, LWO)


# ── Genetic Apiary ──────────────────────────────────────────────────────────

def make_genetic_apiary_end():
    cv = canvas()
    _wood_planks(cv)
    _block_border(cv)
    # Honey dot in center
    fill(cv, 6, 6, 10, 10, AMB)
    fill(cv, 7, 7, 9, 9, GAM)
    return cv


def make_genetic_apiary_side():
    cv = canvas()
    _wood_grain(cv)
    _block_border(cv)
    return cv


def make_genetic_apiary_front():
    cv = canvas()
    _wood_grain(cv)
    _block_border(cv)
    # Single hex opening
    _slot_recess(cv, 8, 8, 7, 7)
    return cv


# ── Advanced Apiary ─────────────────────────────────────────────────────────

def make_advanced_apiary_end():
    cv = canvas()
    _wood_planks(cv, light=AMB, dark=MBR)
    _block_border(cv)
    # Two dots
    fill(cv, 4, 7, 7, 9, GAM)
    fill(cv, 5, 7, 6, 8, HON)
    fill(cv, 9, 7, 12, 9, GAM)
    fill(cv, 10, 7, 11, 8, HON)
    return cv


def make_advanced_apiary_side():
    cv = canvas()
    _wood_grain(cv, light=AMB, dark=MBR)
    _block_border(cv)
    return cv


def make_advanced_apiary_front():
    cv = canvas()
    _wood_grain(cv, light=AMB, dark=MBR)
    _block_border(cv)
    # Two smaller openings
    _slot_recess(cv, 5, 8, 5, 5)
    _slot_recess(cv, 11, 8, 5, 5)
    return cv


# ── Apiary Extension ────────────────────────────────────────────────────────

def make_apiary_extension_all():
    cv = canvas()
    _wood_planks(cv, light=WOO, dark=MBR)
    _block_border(cv)
    # Subtle amber stripe
    fill(cv, 4, 6, 12, 10, AMB)
    fill(cv, 5, 7, 11, 9, LWO)
    return cv


# ── Centrifuge ──────────────────────────────────────────────────────────────

def make_centrifuge_all():
    cv = canvas()
    fill(cv, 0, 0, 16, 16, STO)
    fill(cv, 1, 1, 15, 15, LST)
    _block_border(cv)
    # Amber accent ring
    fill(cv, 3, 3, 13, 13, AMB)
    fill(cv, 4, 4, 12, 12, STO)
    fill(cv, 5, 5, 11, 11, HOT)
    fill(cv, 6, 6, 10, 10, GAM)
    fill(cv, 7, 7, 9, 9, HON)
    return cv


def make_centrifuge_front():
    cv = canvas()
    fill(cv, 0, 0, 16, 16, STO)
    fill(cv, 1, 1, 15, 15, LST)
    _block_border(cv)
    # Round dial opening
    fill(cv, 4, 4, 12, 12, DRK)
    fill(cv, 5, 5, 11, 11, HOT)
    fill(cv, 6, 6, 10, 10, GAM)
    fill(cv, 7, 7, 9, 9, HON)
    # Cross spokes
    hline(cv, 7, 5, 11, DBR)
    hline(cv, 8, 5, 11, DBR)
    vline(cv, 7, 5, 11, DBR)
    vline(cv, 8, 5, 11, DBR)
    return cv


def make_centrifuge_side():
    cv = canvas()
    fill(cv, 0, 0, 16, 16, STO)
    fill(cv, 1, 1, 15, 15, LST)
    _block_border(cv)
    # Exhaust pipe / vent slots
    for i in range(3):
        y = 5 + i * 3
        fill(cv, 4, y, 12, y + 2, AMB)
        fill(cv, 5, y, 11, y + 1, DRK)
    return cv


def make_centrifuge_top():
    cv = canvas()
    fill(cv, 0, 0, 16, 16, STO)
    fill(cv, 1, 1, 15, 15, LST)
    _block_border(cv)
    # Feed opening
    fill(cv, 5, 5, 11, 11, DRK)
    fill(cv, 6, 6, 10, 10, AMB)
    fill(cv, 7, 7, 9, 9, GAM)
    return cv


# ── Forest Bee Log Nest ─────────────────────────────────────────────────────

def make_log_nest_top():
    cv = canvas()
    # Log end grain rings
    fill(cv, 0, 0, 16, 16, MBR)
    fill(cv, 2, 2, 14, 14, WOO)
    fill(cv, 4, 4, 12, 12, MBR)
    fill(cv, 6, 6, 10, 10, WOO)
    fill(cv, 7, 7, 9, 9, AMB)
    _block_border(cv)
    return cv


def make_log_nest_side():
    cv = canvas()
    # Bark — vertical dark/light stripes
    for x in range(16):
        c = MBR if x % 3 == 0 else WOO
        fill(cv, x, 0, x + 1, 16, c)
    _block_border(cv)
    return cv


def make_log_nest_front():
    cv = canvas()
    for x in range(16):
        c = MBR if x % 3 == 0 else WOO
        fill(cv, x, 0, x + 1, 16, c)
    _block_border(cv)
    # Single hole opening
    _slot_recess(cv, 8, 8, 7, 7)
    return cv


def make_log_nest_front_honey():
    cv = make_log_nest_front()
    # Honey drip around opening
    fill(cv, 6, 11, 10, 13, GAM)
    return cv


def make_log_nest_bottom():
    cv = make_log_nest_top()  # same as top
    return cv


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

if __name__ == "__main__":
    base = Path(__file__).parent.parent
    item_dir  = base / "neoforge/src/main/resources/assets/curiousbees/textures/item"
    block_dir = base / "neoforge/src/main/resources/assets/curiousbees/textures/block"

    print("Generating item sprites…")
    _write(item_dir / "basic_frame.png",        make_frame())
    _write(item_dir / "mutation_frame.png",      make_frame(dot_color=DPU))
    _write(item_dir / "productivity_frame.png",  make_frame(dot_color=DGR))
    _write(item_dir / "bee_jar.png",             make_bee_jar())
    _write(item_dir / "bee_transporter.png",     make_bee_transporter())

    print("Generating block face textures…")
    _write(block_dir / "genetic_apiary_end.png",           make_genetic_apiary_end())
    _write(block_dir / "genetic_apiary_side.png",          make_genetic_apiary_side())
    _write(block_dir / "genetic_apiary_front.png",         make_genetic_apiary_front())
    _write(block_dir / "advanced_apiary_end.png",          make_advanced_apiary_end())
    _write(block_dir / "advanced_apiary_side.png",         make_advanced_apiary_side())
    _write(block_dir / "advanced_apiary_front.png",        make_advanced_apiary_front())
    _write(block_dir / "apiary_extension_all.png",         make_apiary_extension_all())
    _write(block_dir / "centrifuge_all.png",               make_centrifuge_all())
    _write(block_dir / "centrifuge_front.png",             make_centrifuge_front())
    _write(block_dir / "centrifuge_side.png",              make_centrifuge_side())
    _write(block_dir / "centrifuge_top.png",               make_centrifuge_top())
    _write(block_dir / "forest_bee_log_nest_top.png",      make_log_nest_top())
    _write(block_dir / "forest_bee_log_nest_side.png",     make_log_nest_side())
    _write(block_dir / "forest_bee_log_nest_front.png",    make_log_nest_front())
    _write(block_dir / "forest_bee_log_nest_front_honey.png", make_log_nest_front_honey())
    _write(block_dir / "forest_bee_log_nest_bottom.png",   make_log_nest_bottom())
    print("Done.")

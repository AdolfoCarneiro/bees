#!/usr/bin/env python3
"""
Generates Curious Bees GUI background textures (256x256 RGBA PNG).
Uses only Python stdlib — no external dependencies.

Outputs:
  neoforge/src/main/resources/assets/curiousbees/textures/gui/genetic_apiary.png
  neoforge/src/main/resources/assets/curiousbees/textures/gui/centrifuge.png

Run from the repo root:
  python scripts/generate_gui_textures.py
"""

import struct
import zlib
import os
from pathlib import Path

# ---------------------------------------------------------------------------
# Palette — honey / warm-wood theme
# ---------------------------------------------------------------------------
TRANSPARENT      = (0,   0,   0,   0)
BORDER_DARK      = (62,  37,  10, 255)   # dark wood
BORDER_LIGHT     = (220, 180,  90, 255)  # lit highlight edge
TITLE_BAND       = (210, 165,  78, 255)  # warm gold title bar
CONTENT_BG       = (188, 143,  60, 255)  # honey amber content area
SEPARATOR        = (100,  70,  25, 255)  # divider between content and inv
INV_BG           = (168, 124,  48, 255)  # player inventory area (slightly darker)
SLOT_SHADOW      = (100,  68,  20, 255)  # slot inner shadow (dark edge)
SLOT_HIGHLIGHT   = (228, 190, 105, 255)  # slot inner highlight (light edge)
SLOT_BG          = (144, 104,  36, 255)  # slot fill

# GUI active region
GUI_W = 176
GUI_H = 166
IMG_SIZE = 256


def make_pixel(x: int, y: int, slot_rects: list) -> tuple:
    """Return the RGBA tuple for pixel (x, y) given a list of slot rectangles."""

    # Outside active GUI area → transparent
    if x >= GUI_W or y >= GUI_H:
        return TRANSPARENT

    # Outer border (1px)
    if x == 0 or y == 0 or x == GUI_W - 1 or y == GUI_H - 1:
        return BORDER_DARK

    # Inner highlight edge (1px)
    if x == 1 or y == 1:
        return BORDER_LIGHT

    # Inner dark edge on right/bottom
    if x == GUI_W - 2 or y == GUI_H - 2:
        return BORDER_DARK

    # Title band (y 2–16)
    if y < 18:
        return TITLE_BAND

    # Separator strip between content area and player inventory
    if y == 77:
        return BORDER_DARK
    if y == 78:
        return BORDER_LIGHT
    if y in (79, 80):
        return SEPARATOR
    if y == 81:
        return BORDER_LIGHT
    if y == 82:
        return BORDER_DARK

    # Player inventory area
    if y >= 83:
        return INV_BG

    # Content area — check slot decorations
    for sx, sy, sw, sh in slot_rects:
        # Slot outer shadow frame
        if sx <= x < sx + sw and sy <= y < sy + sh:
            if x == sx or y == sy:
                return SLOT_SHADOW
            if x == sx + sw - 1 or y == sy + sh - 1:
                return SLOT_HIGHLIGHT
            return SLOT_BG

    return CONTENT_BG


def build_pixels(slot_rects: list) -> list:
    rows = []
    for y in range(IMG_SIZE):
        row = []
        for x in range(IMG_SIZE):
            row.append(make_pixel(x, y, slot_rects))
        rows.append(row)
    return rows


def encode_png(rows: list) -> bytes:
    """Encode pixel data as a valid PNG file."""

    def chunk(tag: bytes, data: bytes) -> bytes:
        crc = zlib.crc32(tag + data) & 0xFFFFFFFF
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", crc)

    # IHDR: width, height, bit-depth=8, color-type=6 (RGBA), compress=0, filter=0, interlace=0
    ihdr = struct.pack(">IIBBBBB", IMG_SIZE, IMG_SIZE, 8, 6, 0, 0, 0)

    # Scanlines: filter byte (0 = None) + RGBA per pixel
    raw = bytearray()
    for row in rows:
        raw.append(0)
        for (r, g, b, a) in row:
            raw.extend((r, g, b, a))

    return (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", ihdr)
        + chunk(b"IDAT", zlib.compress(bytes(raw), 9))
        + chunk(b"IEND", b"")
    )


def slot_rect(menu_x: int, menu_y: int) -> tuple:
    """Convert a menu slot top-left (inside the GUI coord system) to a slot rect.
    Minecraft slots are 18x18 including the 1px border.
    menu_x/y are the leftPos-relative positions used in SlotItemHandler(inv, 0, x, y).
    The visual slot occupies (x-1, y-1) to (x+17, y+17) i.e. 18x18 at (x-1, y-1).
    """
    return (menu_x - 1, menu_y - 1, 18, 18)


# ---------------------------------------------------------------------------
# Slot layouts (from the Menu classes)
# ---------------------------------------------------------------------------

# CentrifugeMenu slot positions:
#   Input (comb):  56, 35
#   Bottle:       107, 17
#   Output 2×2:   116, 35 / 134, 35 / 116, 53 / 134, 53
CENTRIFUGE_SLOTS = [
    slot_rect(56,  35),   # comb input
    slot_rect(107, 17),   # bottle
    slot_rect(116, 35),   # output 0,0
    slot_rect(134, 35),   # output 0,1
    slot_rect(116, 53),   # output 1,0
    slot_rect(134, 53),   # output 1,1
]

# GeneticApiaryMenu slot positions:
#   Outputs 3×2 starting at (62,17) with 18px step
#   Frames 3×1 at (122, 17/35/53)
APIARY_SLOTS = [
    slot_rect(62,  17), slot_rect(80, 17),  slot_rect(98, 17),
    slot_rect(62,  35), slot_rect(80, 35),  slot_rect(98, 35),
    slot_rect(122, 17), slot_rect(122, 35), slot_rect(122, 53),
]


def write_texture(path: Path, slot_rects: list) -> None:
    pixels = build_pixels(slot_rects)
    data = encode_png(pixels)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(data)
    kb = len(data) / 1024
    print(f"  Written {path}  ({kb:.1f} KB)")


if __name__ == "__main__":
    base = Path(__file__).parent.parent
    gui_dir = base / "neoforge/src/main/resources/assets/curiousbees/textures/gui"

    print("Generating Curious Bees GUI textures…")
    write_texture(gui_dir / "centrifuge.png",    CENTRIFUGE_SLOTS)
    write_texture(gui_dir / "genetic_apiary.png", APIARY_SLOTS)
    print("Done.")

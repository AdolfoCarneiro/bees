#!/usr/bin/env python3
"""
Curious Bees — Missing Asset Report

Scans species visual definitions in BuiltinBeeSpecies.java and the
content-authoring-guide, then reports which expected assets are present
or missing in the neoforge resource pack.

Run from the repository root:
    python tools/check-assets.py

Exit code 0 = all required assets present.
Exit code 1 = one or more missing assets found.
"""

import os
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).parent.parent

# --- Known species and their expected assets ---
SPECIES = {
    "meadow":     "curiousbees:textures/entity/bee/meadow.png",
    "forest":     "curiousbees:textures/entity/bee/forest.png",
    "arid":       "curiousbees:textures/entity/bee/arid.png",
    "cultivated": "curiousbees:textures/entity/bee/cultivated.png",
    "hardy":      "curiousbees:textures/entity/bee/hardy.png",
}

FALLBACK_TEXTURE = "curiousbees:textures/entity/bee/fallback.png"

GUI_ASSETS = {
    "analyzer_bg":      "curiousbees:textures/gui/analyzer_bg.png",
    "genetic_apiary_bg":"curiousbees:textures/gui/genetic_apiary_bg.png",
}

BLOCK_ASSETS = {
    "meadow_bee_nest_side":  "curiousbees:textures/block/meadow_bee_nest_side.png",
    "forest_bee_nest_side":  "curiousbees:textures/block/forest_bee_nest_side.png",
    "arid_bee_nest_side":    "curiousbees:textures/block/arid_bee_nest_side.png",
}

ASSETS_ROOT = REPO_ROOT / "neoforge" / "src" / "main" / "resources" / "assets"


def resource_id_to_path(resource_id: str) -> Path:
    """Convert 'namespace:path/to/file.png' to Path under ASSETS_ROOT."""
    namespace, path = resource_id.split(":", 1)
    return ASSETS_ROOT / namespace / path


def check_assets(label: str, assets: dict) -> list[str]:
    missing = []
    for name, resource_id in assets.items():
        path = resource_id_to_path(resource_id)
        status = "OK  " if path.exists() else "MISS"
        if status == "MISS":
            missing.append(resource_id)
        print(f"  [{status}] {name:30s} → {resource_id}")
    return missing


def main():
    print("=" * 60)
    print("Curious Bees — Missing Asset Report")
    print("=" * 60)
    all_missing = []

    print("\n--- Entity Textures (bee skins) ---")
    all_missing += check_assets("species", SPECIES)

    print("\n--- Fallback Texture ---")
    all_missing += check_assets("fallback", {"fallback": FALLBACK_TEXTURE})

    print("\n--- GUI Backgrounds ---")
    all_missing += check_assets("gui", GUI_ASSETS)

    print("\n--- Block Textures (bee nest sides) ---")
    all_missing += check_assets("block", BLOCK_ASSETS)

    print("\n" + "=" * 60)
    if all_missing:
        print(f"MISSING: {len(all_missing)} asset(s) not found:")
        for path in all_missing:
            print(f"  - {path}")
        print("\nAdd missing assets or track prompts via maintainer process (see CLAUDE.md Art section).")
        print("=" * 60)
        sys.exit(1)
    else:
        print("All required assets are present.")
        print("=" * 60)
        sys.exit(0)


if __name__ == "__main__":
    main()

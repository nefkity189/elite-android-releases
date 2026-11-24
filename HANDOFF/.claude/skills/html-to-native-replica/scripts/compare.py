#!/usr/bin/env python3
"""Compare the prototype screenshot (reference) with a screenshot of the native app.
Outputs a side-by-side 'ref | app | diff' PNG and a list of the regions that differ most, in dp,
so you know exactly WHERE to look and fix.

  python compare.py ref.png app.png --out diff.png [--dpr 2] [--threshold 24]
If sizes differ the app image is resized to the reference (a warning is printed: fix the size mismatch first, it usually means wrong insets/status bar).
"""
import argparse, numpy as np
from PIL import Image, ImageDraw

def main():
    p = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    p.add_argument("ref"); p.add_argument("app"); p.add_argument("--out", default="diff.png")
    p.add_argument("--dpr", type=float, default=2.0, help="pixels per dp of the REFERENCE image")
    p.add_argument("--threshold", type=int, default=24, help="per-channel difference (0-255) that counts as different")
    p.add_argument("--grid", default="6x12", help="COLSxROWS grid used to rank regions")
    a = p.parse_args()
    ref = Image.open(a.ref).convert("RGB"); app = Image.open(a.app).convert("RGB")
    if ref.size != app.size:
        ra, aa = ref.width / ref.height, app.width / app.height
        print(f"WARNING size differs ref={ref.size} app={app.size} (aspect {ra:.3f} vs {aa:.3f}). Resizing app to ref; fix insets/window size first if aspect differs a lot.")
        app = app.resize(ref.size, Image.LANCZOS)
    A, B = np.asarray(ref).astype(int), np.asarray(app).astype(int)
    d = np.abs(A - B).max(axis=2); mask = d > a.threshold
    pct = mask.mean() * 100
    gx, gy = map(int, a.grid.split("x")); H, W = mask.shape; cells = []
    for j in range(gy):
        for i in range(gx):
            sl = mask[j*H//gy:(j+1)*H//gy, i*W//gx:(i+1)*W//gx]
            cells.append((sl.mean()*100, i, j))
    cells.sort(reverse=True)
    heat = np.zeros((H, W, 3), np.uint8); heat[mask] = (255, 60, 60); heat[~mask] = (A[~mask] * 0.25).astype(np.uint8)
    diff = Image.fromarray(heat); dr = ImageDraw.Draw(diff)
    for pc, i, j in cells[:5]:
        if pc > 1: dr.rectangle([i*W//gx, j*H//gy, (i+1)*W//gx-1, (j+1)*H//gy-1], outline=(255, 220, 0), width=3)
    sheet = Image.new("RGB", (W*3 + 16, H), "#555"); sheet.paste(ref, (0, 0)); sheet.paste(app, (W+8, 0)); sheet.paste(diff, (2*W+16, 0)); sheet.save(a.out)
    print(f"different pixels: {pct:.2f}%  (rule of thumb: <1% = match, 1-4% = small drift, >4% = something structural is off)")
    print("worst regions (dp, x0,y0 -> x1,y1):")
    for pc, i, j in cells[:6]:
        if pc < 0.5: break
        print(f"  {pc:5.1f}% differ  ({i*W/gx/a.dpr:.0f},{j*H/gy/a.dpr:.0f}) -> ({(i+1)*W/gx/a.dpr:.0f},{(j+1)*H/gy/a.dpr:.0f})")
    print(f"saved {a.out}  (left=reference, middle=app, right=diff)  -> open it with the Read tool")

if __name__ == "__main__":
    main()

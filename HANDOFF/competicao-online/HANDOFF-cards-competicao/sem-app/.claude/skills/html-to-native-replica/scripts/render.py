#!/usr/bin/env python3
"""Render an HTML prototype to PNG so you can LOOK at it (use the Read tool on the PNG).

Examples
  python render.py proto.html --out shots/today.png
  python render.py proto.html --out shots/menu-open.png --step click:#fab
  python render.py proto.html --out shots/orb.png --filmstrip 8 --interval 150 --step click:#coach   # animation frames
  python render.py proto.html --out shots/rm.png --reduced-motion
The screenshot is cropped to the detected "screen" root, so pixel (x,y)/dpr == dp coordinates inside the app screen.
"""
import argparse, pathlib, sys
from playwright.sync_api import sync_playwright
from PIL import Image
sys.path.insert(0, str(pathlib.Path(__file__).parent))
from _common import add_common_args, open_page, find_root, run_steps


def main():
    p = add_common_args(argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter))
    p.add_argument("--out", required=True)
    p.add_argument("--filmstrip", type=int, default=0, help="capture N frames of the live animation (animations NOT frozen) and tile them")
    p.add_argument("--interval", type=int, default=150, help="ms between filmstrip frames")
    a = p.parse_args()
    out = pathlib.Path(a.out); out.parent.mkdir(parents=True, exist_ok=True)
    with sync_playwright() as pw:
        if a.filmstrip:
            a.no_freeze = True
            browser, page, msgs = open_page(pw, a, freeze=False)
        else:
            browser, page, msgs = open_page(pw, a)
        sel, box = find_root(page, a.root)
        clip = {"x": box["x"], "y": box["y"], "width": box["width"], "height": box["height"]}
        print(f"root={sel} size={box['width']:.0f}x{box['height']:.0f}dp at ({box['x']:.0f},{box['y']:.0f}) viewport={a.width}x{a.height} dpr={a.dpr}")
        if a.filmstrip:
            frames = []
            for i in range(a.filmstrip):
                f = out.with_name(f"{out.stem}_f{i}.png"); page.screenshot(path=str(f), clip=clip); frames.append(f)
                page.wait_for_timeout(a.interval)
            ims = [Image.open(f) for f in frames]
            sheet = Image.new("RGB", (sum(i.width for i in ims) + 8 * (len(ims) - 1), ims[0].height), "#444")
            x = 0
            for im in ims: sheet.paste(im, (x, 0)); x += im.width + 8
            sheet.save(out); [f.unlink() for f in frames]
            print(f"filmstrip: {a.filmstrip} frames every {a.interval}ms -> {out}")
        else:
            page.screenshot(path=str(out), clip=clip)
            print(f"saved {out}")
        for m in msgs: print("page-warning:", m)
        browser.close()

if __name__ == "__main__":
    main()

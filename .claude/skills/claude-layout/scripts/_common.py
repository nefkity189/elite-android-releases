"""Shared helpers: open an HTML prototype in headless Chromium the way a phone would."""
import argparse, json, pathlib, sys
from playwright.sync_api import sync_playwright

FREEZE_CSS = "*,*::before,*::after{animation-play-state:paused!important;caret-color:transparent!important}"
AUTO_ROOTS = [".phone", "[data-screen]", "main.phone", "#app", "main"]


def add_common_args(p):
    p.add_argument("html", help="path to the .html prototype")
    p.add_argument("--width", type=int, default=393, help="viewport width in CSS px (= dp). Default 393 (Poco X7 / modern Android)")
    p.add_argument("--height", type=int, default=851, help="viewport height in CSS px (= dp)")
    p.add_argument("--dpr", type=float, default=2, help="device pixel ratio for screenshots")
    p.add_argument("--root", default=None, help="CSS selector of the 'screen' element. Default: auto-detect (.phone, [data-screen], main...). Use 'body' to force whole page")
    p.add_argument("--step", action="append", default=[], metavar="ACTION",
                   help="interaction before capture, repeatable: click:SEL | hover:SEL | wait:MS | press:SEL (mouse down and hold) | eval:JS")
    p.add_argument("--reduced-motion", action="store_true", help="emulate prefers-reduced-motion: reduce")
    p.add_argument("--no-freeze", action="store_true", help="do not pause CSS animations (default: paused at t=0 for a stable image)")
    return p


def open_page(pw, a, freeze=True):
    browser = pw.chromium.launch()
    ctx = browser.new_context(
        viewport={"width": a.width, "height": a.height},
        device_scale_factor=a.dpr,
        reduced_motion="reduce" if a.reduced_motion else "no-preference",
        has_touch=True, is_mobile=False,
    )
    page = ctx.new_page()
    msgs = []
    page.on("console", lambda m: msgs.append(m.text) if m.type == "error" else None)
    page.on("requestfailed", lambda r: msgs.append("request failed: " + r.url))
    page.goto(pathlib.Path(a.html).resolve().as_uri(), wait_until="load")
    try:
        page.wait_for_function("document.fonts.status==='loaded'", timeout=4000)
    except Exception:
        pass
    if freeze and not a.no_freeze:
        page.add_style_tag(content=FREEZE_CSS)
    run_steps(page, a.step)
    page.wait_for_timeout(350)  # let transitions settle
    return browser, page, msgs


def run_steps(page, steps):
    for s in steps:
        kind, _, arg = s.partition(":")
        if kind == "click": page.click(arg)
        elif kind == "hover": page.hover(arg)
        elif kind == "wait": page.wait_for_timeout(int(arg))
        elif kind == "press":
            page.hover(arg); page.mouse.down()
        elif kind == "eval": page.evaluate(arg)
        else: raise SystemExit(f"unknown step '{s}'")
        page.wait_for_timeout(120)


def find_root(page, selector):
    cands = [selector] if selector else AUTO_ROOTS
    for c in cands:
        el = page.query_selector(c)
        if el:
            box = el.bounding_box()
            if box and box["width"] > 100:
                return c, box
    return "body", page.query_selector("body").bounding_box()

#!/usr/bin/env python3
"""Self-check for an HTML prototype BEFORE handing it over. Catches the bugs that make a native copy impossible:
text hidden under another layer, content outside the screen, scrolling when it shouldn't, touch targets < 44dp,
buttons without accessible name. Exit code 1 if any ERROR.

  python lint.py proto.html [--step click:#fab] [--allow-scroll]
"""
import argparse, pathlib, sys
from playwright.sync_api import sync_playwright
sys.path.insert(0, str(pathlib.Path(__file__).parent))
from _common import add_common_args, open_page, find_root

JS = r"""(rootSel)=>{
 const root=document.querySelector(rootSel)||document.body, R=root.getBoundingClientRect(), res=[];
 const name=e=>{let s=e.tagName.toLowerCase(); if(e.id)s+='#'+e.id; const c=(e.getAttribute('class')||'').trim().split(/\s+/).filter(Boolean); if(c.length)s+='.'+c.join('.'); return s;};
 const vis=e=>{const cs=getComputedStyle(e); return cs.display!=='none'&&cs.visibility!=='hidden'&&parseFloat(cs.opacity)>0.05;};
 if(root.scrollHeight>root.clientHeight+1) res.push(['WARN','root scrolls: scrollHeight '+root.scrollHeight+' > '+root.clientHeight+' (is scrolling intended?)']);
 root.querySelectorAll('*').forEach(e=>{
  if(!vis(e)||(e.closest('svg')&&e.tagName!=='svg')) return; const b=e.getBoundingClientRect(); if(b.width===0||b.height===0) return;
  const own=[...e.childNodes].some(c=>c.nodeType===3&&c.textContent.trim());
  if(own){ // text visibility: topmost element at the text center must be the element, a descendant or an ancestor
    const cx=b.left+b.width/2, cy=b.top+Math.min(b.height/2, 10);
    if(cx>=0&&cy>=0&&cx<innerWidth&&cy<innerHeight){ const t=document.elementFromPoint(cx,cy);
      if(t && !(e.contains(t)||t.contains(e))) res.push(['ERROR','text "'+e.textContent.trim().slice(0,30)+'" in '+name(e)+' is covered by '+name(t)+' (needs position:relative; z-index above the layer)']); }
    const cs=getComputedStyle(e); if(cs.color && /rgba\(.*,\s*0\)/.test(cs.color)) res.push(['WARN','text '+name(e)+' has transparent color (ok only for gradient text)']);
  }
  if(b.right>R.right+1||b.left<R.left-1||b.bottom>R.bottom+1||b.top<R.top-1){ res.push(['WARN',name(e)+' extends outside the screen: '+[b.left-R.left,b.top-R.top,b.right-R.left,b.bottom-R.top].map(n=>Math.round(n)).join(',')+' (screen '+Math.round(R.width)+'x'+Math.round(R.height)+')']); }
  if(['BUTTON','A'].includes(e.tagName)||e.getAttribute('role')==='button'){
    if(Math.min(b.width,b.height)<44 && !(e.className||'').toString().match(/\bx\b/)) res.push(['WARN','touch target '+name(e)+' is '+Math.round(b.width)+'x'+Math.round(b.height)+' (<44dp)']);
    if(!e.textContent.trim() && !e.getAttribute('aria-label')) res.push(['ERROR','icon-only '+name(e)+' has no aria-label']); }
 });
 return res; }"""

def main():
    p = add_common_args(argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter))
    a = p.parse_args()
    with sync_playwright() as pw:
        browser, page, msgs = open_page(pw, a, freeze=False)
        sel, box = find_root(page, a.root)
        res = page.evaluate(JS, sel); browser.close()
    seen = set(); errs = 0
    for lvl, m in res:
        if m in seen: continue
        seen.add(m); print(f"{lvl}: {m}"); errs += lvl == "ERROR"
    print(f"root={sel} {box['width']:.0f}x{box['height']:.0f}dp  errors={errs} warnings={len(seen)-errs}")
    sys.exit(1 if errs else 0)

if __name__ == "__main__":
    main()

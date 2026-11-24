#!/usr/bin/env python3
"""Extract the EXACT numbers from an HTML prototype: every element's box (dp, relative to the screen),
colors, fonts, radius, padding, gaps, gradients, shadows, z-order, transitions and keyframe animations.
This is the 'hands' data: never eyeball a CSS file, read this instead.

  python inspect_layout.py proto.html --out spec/today          # writes spec/today.json and spec/today.md
  python inspect_layout.py proto.html --out spec/menu --step click:#fab   # state after an interaction

Units: CSS px == dp (Android) == logical points (Flutter/iOS). Coordinates are relative to the screen root.
"""
import argparse, json, pathlib, sys
from playwright.sync_api import sync_playwright
sys.path.insert(0, str(pathlib.Path(__file__).parent))
from _common import add_common_args, open_page, find_root

JS = r"""
(rootSel) => {
 const root = document.querySelector(rootSel) || document.body;
 const R = root.getBoundingClientRect();
 const KEYS = ['display','position','flex-direction','justify-content','align-items','flex-grow','flex-shrink','gap',
  'padding','margin','color','background-color','background-image','border','border-radius','box-shadow','backdrop-filter','filter',
  'opacity','font-family','font-size','font-weight','line-height','letter-spacing','text-align','font-variant-numeric','text-transform',
  'transform','z-index','overflow','mix-blend-mode','transition','animation','grid-template-columns'];
 const INHERIT = new Set(['color','font-family','font-size','font-weight','line-height','letter-spacing','text-align','font-variant-numeric','text-transform']);
 const DEF = new Set(['none','normal','auto','0px','static','visible','rgba(0, 0, 0, 0)','start','baseline','row','stretch','block','left','0px 0px 0px 0px','0px 0px']);
 function styles(cs, pcs){ const o={};
   for(const k of KEYS){ let v=cs.getPropertyValue(k).trim(); if(!v) continue;
     if(k==='flex-grow'){ if(v!=='0') o[k]=v; continue; }
     if(k==='flex-shrink'){ if(v!=='1' && cs.display.includes('flex')===false) {} if(v!=='1') o[k]=v; continue; }
     if(k==='opacity'){ if(v!=='1') o[k]=v; continue; }
     if(k==='border'){ if(!/^0px/.test(v)) o[k]=v; continue; }
     if(k==='transition'){ if(cs.getPropertyValue('transition-duration')!=='0s') o[k]=v; continue; }
     if(k==='animation'){ if(cs.getPropertyValue('animation-name')!=='none') o[k]=v; continue; }
     if(k==='display' && v==='inline') continue;
     if(DEF.has(v)) continue;
     if(pcs && INHERIT.has(k) && pcs.getPropertyValue(k).trim()===v) continue;
     o[k]=v; }
   return o; }
 function r(b){ return {x:+(b.left-R.left).toFixed(1), y:+(b.top-R.top).toFixed(1), w:+b.width.toFixed(1), h:+b.height.toFixed(1)}; }
 function sel(e){ let s=e.tagName.toLowerCase(); if(e.id) s+='#'+e.id; const c=(e.getAttribute('class')||'').trim().split(/\s+/).filter(Boolean); if(c.length) s+='.'+c.join('.'); return s; }
 const out=[]; let n=0; const FAMS=new Set();
 (function walk(e,depth){
   if(['SCRIPT','STYLE','LINK','META','TITLE','HEAD'].includes(e.tagName)) return;
   const cs=getComputedStyle(e); if(cs.display==='none'){ out.push({depth,sel:sel(e),hidden:true}); return; }
   const b=e.getBoundingClientRect(); FAMS.add(cs.fontFamily.split(',')[0].replace(/["']/g,'').trim());
   const own=[...e.childNodes].filter(c=>c.nodeType===3).map(c=>c.textContent.trim()).filter(Boolean).join(' ');
   const item={id:n++,depth,sel:sel(e),text:own||undefined,rect:r(b),style:styles(cs, e.parentElement?getComputedStyle(e.parentElement):null)};
   const aria=e.getAttribute('aria-label'); if(aria) item.aria=aria;
   const dc=[...e.attributes].filter(a=>a.name.startsWith('data-')).map(a=>a.name+'='+a.value); if(dc.length) item.data=dc;
   if(e.tagName==='svg'||e.closest('svg')){ item.svg = e.tagName==='svg' ? e.outerHTML.slice(0,1500) : undefined; }
   out.push(item);
   for(const ps of ['::before','::after']){ const pc=getComputedStyle(e,ps); if(pc.content && pc.content!=='none' && pc.content!=='normal'){ out.push({depth:depth+1,sel:sel(e)+ps,pseudo:true,content:pc.content,rect:{w:pc.width,h:pc.height,top:pc.top,left:pc.left,inset:pc.inset},style:styles(pc, getComputedStyle(e))}); } }
   if(e.tagName!=='svg') for(const c of e.children) walk(c,depth+1);
 })(root,0);
 const anims=document.getAnimations().map(a=>{ const ef=a.effect; const t=ef&&ef.getTiming?ef.getTiming():{}; const tg=ef&&ef.target;
   return {name:a.animationName||a.transitionProperty||'(transition)', type:a.constructor.name, target:tg?sel(tg)+(ef.pseudoElement||''):null,
     duration_ms:t.duration, delay_ms:t.delay, iterations:t.iterations===Infinity?'infinite':t.iterations, easing:t.easing, direction:t.direction, fill:t.fill,
     keyframes: ef&&ef.getKeyframes? ef.getKeyframes().map(k=>{const o={}; for(const p in k){ if(!['composite','computedOffset'].includes(p)) o[p]=k[p]; } return o;}):[]};});
 const fams=FAMS;
 const cv=document.createElement('canvas').getContext('2d'); const T='Hamburgefonstiv 0123456789 mmmm';
 function w(font){ cv.font='40px '+font; return cv.measureText(T).width; }
 const fonts=[...fams].map(f=>{ const q='"'+f+'"'; const loaded = w(q+',monospace')!==w('monospace') || w(q+',serif')!==w('serif'); return {family:f, loaded}; });
 const vars={}; const rs=getComputedStyle(document.documentElement); for(const sh of document.styleSheets){ try{ for(const rule of sh.cssRules){ if(rule.selectorText===':root'){ for(const p of rule.style){ if(p.startsWith('--')) vars[p]=rs.getPropertyValue(p).trim(); } } } }catch(e){} }
 const mq=[]; for(const sh of document.styleSheets){ try{ for(const rule of sh.cssRules){ if(rule.type===4) mq.push(rule.conditionText); } }catch(e){} }
 return {screen:{w:+R.width.toFixed(1),h:+R.height.toFixed(1)}, tokens:vars, fonts, media_queries:mq, animations:anims, elements:out};
}
"""

def fmt_rect(r):
    if not r: return ""
    if isinstance(r.get("w"), str): return f"size {r['w']}x{r['h']} (pseudo; inset {r.get('inset')})"
    return f"@({r['x']:g},{r['y']:g}) {r['w']:g}x{r['h']:g}"

def to_md(d):
    L = [f"# Layout spec (dp = CSS px)\n", f"Screen: {d['screen']['w']:g} x {d['screen']['h']:g} dp\n"]
    if d["tokens"]:
        L.append("## Design tokens (:root)"); L += [f"- `{k}`: {v}" for k, v in d["tokens"].items()]; L.append("")
    L.append("## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)")
    L += [f"- {f['family']}  loaded={f['loaded']}" for f in d["fonts"]]; L.append("")
    if d["media_queries"]:
        L.append("## Media queries (responsive rules to port)"); L += [f"- `@media {m}`" for m in d["media_queries"]]; L.append("")
    L.append("## Animations / transitions running or defined")
    if not d["animations"]: L.append("- none active in this state")
    for a in d["animations"]:
        L.append(f"- **{a['name']}** on `{a['target']}`: {a['duration_ms']}ms, delay {a['delay_ms']}ms, iterations {a['iterations']}, easing `{a['easing']}`, direction {a['direction']}")
        for k in a["keyframes"]: L.append(f"    - keyframe {json.dumps(k, ensure_ascii=False)}")
    L += ["", "## Element tree (indent = nesting; x,y relative to screen)"]
    for e in d["elements"]:
        ind = "  " * e["depth"]
        if e.get("hidden"): L.append(f"{ind}- {e['sel']}  [display:none]"); continue
        t = f' "{e["text"]}"' if e.get("text") else ""
        t += f' aria="{e["aria"]}"' if e.get("aria") else ""
        st = "; ".join(f"{k}:{v}" for k, v in e["style"].items())
        L.append(f"{ind}- {e['sel']}{t}  {fmt_rect(e['rect'])}" + (f"\n{ind}    {st}" if st else ""))
        if e.get("content"): L.append(f"{ind}    content:{e['content']}")
        if e.get("svg"): L.append(f"{ind}    svg: {e['svg']}")
    return "\n".join(L) + "\n"

def main():
    p = add_common_args(argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter))
    p.add_argument("--out", required=True, help="output prefix, writes <out>.json and <out>.md")
    a = p.parse_args()
    out = pathlib.Path(a.out); out.parent.mkdir(parents=True, exist_ok=True)
    with sync_playwright() as pw:
        browser, page, msgs = open_page(pw, a, freeze=False)  # keep animations live so getAnimations() sees them
        sel, box = find_root(page, a.root)
        data = page.evaluate(JS, sel)
        data["root_selector"] = sel
        browser.close()
    out.with_suffix(".json").write_text(json.dumps(data, ensure_ascii=False, indent=1))
    out.with_suffix(".md").write_text(to_md(data))
    print(f"root={sel} screen={data['screen']['w']:g}x{data['screen']['h']:g}dp elements={len(data['elements'])} animations={len(data['animations'])}")
    bad = [f['family'] for f in data['fonts'] if not f['loaded']]
    if bad: print("WARNING fonts not loaded in this render (offline or fallback):", ", ".join(bad), "- values still come from the CSS, but pixel widths of text may differ")
    print(f"wrote {out.with_suffix('.md')} and {out.with_suffix('.json')}")

if __name__ == "__main__":
    main()

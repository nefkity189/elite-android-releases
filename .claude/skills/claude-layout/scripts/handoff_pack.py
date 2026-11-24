#!/usr/bin/env python3
"""Build the HANDOFF folder that Claude Code needs to replicate a prototype on the first try.

  python handoff_pack.py proto.html --out HANDOFF/today --name today \
      --state "menu-open=click:#fab" --state "orb-tap=click:#coach" --stack compose

Creates   <out>/
            proto.html              the prototype (source of truth for visuals)
            screens/<name>.png      rendered default state + one PNG per --state + reduced-motion
            spec/<name>.md|json     measured dp numbers, tokens, fonts, animations (default + each state)
            lint.txt                result of the self-check
            README_FOR_CLAUDE_CODE.md   the prompt to paste (stack-specific, filled with facts)
            .claude/skills/replica-claude/   the replication skill, so Claude Code auto-loads it
Then YOU still have to write SPEC.md (see assets/SPEC_template.md) for intent that numbers can't express.
"""
import argparse, pathlib, shutil, subprocess, sys, json
HERE = pathlib.Path(__file__).resolve().parent
SK = HERE.parent
PY = sys.executable

def run(*args):
    r = subprocess.run([PY, *map(str, args)], capture_output=True, text=True)
    return r.returncode, (r.stdout + r.stderr).strip()

def main():
    p = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    p.add_argument("html"); p.add_argument("--out", required=True); p.add_argument("--name", required=True)
    p.add_argument("--state", action="append", default=[], help="NAME=step[;step...] e.g. menu-open=click:#fab")
    p.add_argument("--stack", default="compose", choices=["compose", "flutter", "react-native", "swiftui", "unknown"])
    p.add_argument("--width", default="393"); p.add_argument("--height", default="851"); p.add_argument("--root", default=None)
    a = p.parse_args()
    out = pathlib.Path(a.out); (out / "screens").mkdir(parents=True, exist_ok=True); (out / "spec").mkdir(exist_ok=True)
    html = out / "proto.html"; shutil.copy(a.html, html)
    base = ["--width", a.width, "--height", a.height] + (["--root", a.root] if a.root else [])
    log = []
    states = [("default", [])] + [(s.split("=", 1)[0], s.split("=", 1)[1].split(";")) for s in a.state]
    for name, steps in states:
        st = sum([["--step", x] for x in steps], [])
        n = a.name if name == "default" else f"{a.name}--{name}"
        rc, o = run(HERE / "render.py", html, "--out", out / "screens" / f"{n}.png", *base, *st); log.append(o.splitlines()[0] if o else "")
        rc, o = run(HERE / "inspect_layout.py", html, "--out", out / "spec" / n, *base, *st); log += [l for l in o.splitlines() if l.startswith("WARNING")]
    run(HERE / "render.py", html, "--out", out / "screens" / f"{a.name}--reduced-motion.png", *base, "--reduced-motion")
    rc, lint = run(HERE / "lint.py", html, *base); (out / "lint.txt").write_text(lint + "\n")
    # ship the replication skill so Claude Code finds it automatically in .claude/skills
    dst = out / ".claude" / "skills" / "replica-claude"
    src = SK / "assets" / "replica-bundle"
    if dst.exists(): shutil.rmtree(dst)
    shutil.copytree(src, dst, ignore=shutil.ignore_patterns("__pycache__"))
    (dst / "SKILL.md.src").rename(dst / "SKILL.md")  # stored as .src so this package has a single SKILL.md
    tpl = (SK / "assets" / "claude_code_prompt_template.md").read_text()
    stack = {"compose": "Jetpack Compose (Kotlin)", "flutter": "Flutter (Dart)", "react-native": "React Native", "swiftui": "SwiftUI", "unknown": "a stack you detect from the repo"}[a.stack]
    shots = "\n".join(f"- screens/{pathlib.Path(f).name}" for f in sorted(map(str, (out / 'screens').glob('*.png'))))
    (out / "README_FOR_CLAUDE_CODE.md").write_text(tpl.replace("{{STACK}}", stack).replace("{{NAME}}", a.name).replace("{{SCREENSHOTS}}", shots))
    print("\n".join(l for l in log if l)); print("lint:", lint.splitlines()[-1] if lint else "?")
    if rc: print("!! lint found ERRORS - fix the prototype before delivering (see lint.txt)")
    print(f"HANDOFF ready: {out}")

if __name__ == "__main__":
    main()

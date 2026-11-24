import subprocess
import threading
import time
from http.server import HTTPServer, BaseHTTPRequestHandler
import os

SCREEN_DIR = os.path.join(os.path.dirname(__file__), "screen_cache")
os.makedirs(SCREEN_DIR, exist_ok=True)

current_frame = None
frame_lock = threading.Lock()

def capture_loop():
    global current_frame
    while True:
        try:
            result = subprocess.run(
                ["adb", "shell", "screencap", "-p", "/sdcard/mirror.png"],
                capture_output=True, text=True, timeout=5
            )
            if result.returncode == 0:
                subprocess.run(
                    ["adb", "pull", "/sdcard/mirror.png", os.path.join(SCREEN_DIR, "latest.png")],
                    capture_output=True, timeout=5
                )
                with frame_lock:
                    current_frame = os.path.join(SCREEN_DIR, "latest.png")
        except Exception as e:
            pass
        time.sleep(0.3)  # ~3 fps

class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == "/" or self.path == "/index.html":
            html = '''<!DOCTYPE html>
<html>
<head><title>Android Mirror</title>
<style>body{margin:0;background:#111;display:flex;flex-direction:column;align-items:center;justify-content:center;height:100vh;font-family:sans-serif;}
img{max-width:90vw;max-height:80vh;border-radius:8px;box-shadow:0 4px 20px rgba(0,0,0,.5);}
h3{color:#aaa;margin-bottom:10px;}</style>
</head>
<body>
<h3>📱 Android Emulator — Live Mirror</h3>
<img id="screen" src="/screen?t=' + str(int(time.time()*1000)) + '" />
<script>setInterval(()=>document.getElementById('screen').src='/screen?t='+Date.now(),330)</script>
</body></html>'''
            self.send_response(200)
            self.send_header("Content-Type", "text/html")
            self.end_headers()
            self.wfile.write(html.encode())
        elif self.path.startswith("/screen"):
            with frame_lock:
                path = current_frame
            if path and os.path.exists(path):
                with open(path, "rb") as f:
                    self.send_response(200)
                    self.send_header("Content-Type", "image/png")
                    self.send_header("Cache-Control", "no-store")
                    self.end_headers()
                    self.wfile.write(f.read())
            else:
                self.send_response(404)
                self.end_headers()
        else:
            self.send_response(404)
            self.end_headers()
    
    def log_message(self, format, *args):
        pass  # suppress logs

if __name__ == "__main__":
    t = threading.Thread(target=capture_loop, daemon=True)
    t.start()
    print("Starting mirror server on http://localhost:8765")
    HTTPServer(("0.0.0.0", 8765), Handler).serve_forever()

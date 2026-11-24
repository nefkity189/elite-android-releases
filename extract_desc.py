with open('ui_full.xml', 'rb') as f:
    content = f.read()
    content_str = content.decode('utf-8', errors='ignore')
    import re
    texts = re.findall(r'content-desc="([^"]*)"', content_str)
    for t in texts:
        if t.strip():
            print(t)
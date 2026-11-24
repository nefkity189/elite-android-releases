with open('ui.xml', 'rb') as f:
    content = f.read()
    content_str = content.decode('utf-8', errors='ignore')
    import re
    # Find Descartar button bounds
    pattern = r'<node[^>]*text="Descartar"[^>]*bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"'
    match = re.search(pattern, content_str)
    if match:
        x1, y1, x2, y2 = map(int, match.groups())
        cx = (x1 + x2) // 2
        cy = (y1 + y2) // 2
        print(f"Descartar bounds: [{x1},{y1}][{x2},{y2}]")
        print(f"Center: ({cx}, {cy})")
    else:
        print("Descartar button not found")
    
    # Find Retomar button bounds
    pattern = r'<node[^>]*text="Retomar corrida"[^>]*bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"'
    match = re.search(pattern, content_str)
    if match:
        x1, y1, x2, y2 = map(int, match.groups())
        cx = (x1 + x2) // 2
        cy = (y1 + y2) // 2
        print(f"Retomar bounds: [{x1},{y1}][{x2},{y2}]")
        print(f"Center: ({cx}, {cy})")
    else:
        print("Retomar button not found")
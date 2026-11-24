import xml.etree.ElementTree as ET

tree = ET.parse('D:/Aplicativos_RODANDO/AISLE_Proj/cur.xml')
root = tree.getroot()
for node in root.iter():
    text = node.get('text', '')
    if text:
        bounds = node.get('bounds', '')
        cls = node.get('class', '')
        res = node.get('resource-id', '')
        print(f'{bounds} | {cls} | {res} | {text}')
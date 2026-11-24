import xml.etree.ElementTree as ET
tree = ET.parse('D:/Aplicativos_RODANDO/AISLE_Proj/uidump7.xml')
root = tree.getroot()

def find_element(node, path=''):
    for i, child in enumerate(node):
        new_path = f'{path}/node[{i}]'
        if 'btn_free_run_descanso' in child.attrib.get('resource-id', ''):
            print(f'Found at {new_path}')
            print(f'  bounds: {child.attrib.get("bounds")}')
            print(f'  clickable: {child.attrib.get("clickable")}')
            print(f'  class: {child.attrib.get("class")}')
            print(f'  text: {child.attrib.get("text")}')
            return
        find_element(child, new_path)

find_element(root)
#!/usr/bin/env python3
"""Batch migrate old Elite/Runna theme to Elite theme in Kotlin files."""
import re
import sys
from pathlib import Path

# Import removals
IMPORT_REPLACEMENTS = {
    'import com.elite.sch.app.ui.theme.EliteBackground\n': '',
    'import com.elite.sch.app.ui.theme.EliteCard\n': '',
    'import com.elite.sch.app.ui.theme.EliteBorder\n': '',
    'import com.elite.sch.app.ui.theme.ElitePurple\n': '',
    'import com.elite.sch.app.ui.theme.EliteTextPrimary\n': '',
    'import com.elite.sch.app.ui.theme.EliteTextSecondary\n': '',
    'import com.elite.sch.app.ui.theme.EliteTextTertiary\n': '',
    'import com.elite.sch.app.ui.theme.EliteTurquoise\n': '',
    'import com.elite.sch.app.ui.theme.EliteAlert\n': '',
    'import com.elite.sch.app.ui.theme.EliteActionOrange\n': '',
    'import com.elite.sch.app.ui.theme.EliteSuccess\n': '',
    'import com.elite.sch.app.ui.theme.EliteWarning\n': '',
    'import com.elite.sch.app.ui.theme.EliteError\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnPrimary\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnSecondary\n': '',
    'import com.elite.sch.app.ui.theme.EliteSurfaceVariant\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnSurfaceVariant\n': '',
    'import com.elite.sch.app.ui.theme.EliteSurface\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnSurface\n': '',
    'import com.elite.sch.app.ui.theme.ElitePrimary\n': '',
    'import com.elite.sch.app.ui.theme.EliteSecondary\n': '',
    'import com.elite.sch.app.ui.theme.EliteTertiary\n': '',
    'import com.elite.sch.app.ui.theme.ElitePrimaryContainer\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnPrimaryContainer\n': '',
    'import com.elite.sch.app.ui.theme.EliteSecondaryContainer\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnSecondaryContainer\n': '',
    'import com.elite.sch.app.ui.theme.EliteTertiaryContainer\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnTertiaryContainer\n': '',
    'import com.elite.sch.app.ui.theme.EliteSpacing\n': '',
    'import com.elite.sch.app.ui.theme.EliteShape\n': '',
    'import com.elite.sch.app.ui.theme.EliteCardTop\n': '',
    'import com.elite.sch.app.ui.theme.EliteCardBottom\n': '',
    'import com.elite.sch.app.ui.theme.EliteTrack\n': '',
    'import com.elite.sch.app.ui.theme.EliteTextAccent2\n': '',
    'import com.elite.sch.app.ui.theme.EliteTealDark\n': '',
    'import com.elite.sch.app.ui.theme.EliteFieldBorder\n': '',
    'import com.elite.sch.app.ui.theme.EliteOnAction\n': '',
    'import com.elite.sch.app.ui.theme.EliteShadowOverlay\n': '',
    'import com.elite.sch.app.ui.theme.EliteDangerContainer\n': '',
    'import com.elite.sch.app.ui.theme.EliteBottomBarSurface\n': '',
    'import com.elite.sch.app.ui.theme.RunnaBackground\n': '',
    'import com.elite.sch.app.ui.theme.RunnaGlass\n': '',
    'import com.elite.sch.app.ui.theme.RunnaGlassElevated\n': '',
    'import com.elite.sch.app.ui.theme.RunnaGlassBorder\n': '',
    'import com.elite.sch.app.ui.theme.RunnaTextPrimary\n': '',
    'import com.elite.sch.app.ui.theme.RunnaTextSecondary\n': '',
    'import com.elite.sch.app.ui.theme.RunnaTeal\n': '',
    'import com.elite.sch.app.ui.theme.RunnaTealOn\n': '',
    'import com.elite.sch.app.ui.theme.RunnaOrange\n': '',
    'import com.elite.sch.app.ui.theme.RunnaOrangeOn\n': '',
    'import com.elite.sch.app.ui.theme.RunnaRedDot\n': '',
    'import com.elite.sch.app.ui.theme.RunnaNumberStyles\n': '',
    'import com.elite.sch.app.ui.theme.RunnaTextStyles\n': '',
    'import com.elite.sch.app.ui.theme.BrandGradient\n': '',
    'import com.elite.sch.app.ui.theme.BrandButtonGradient\n': '',
    'import com.elite.sch.app.ui.theme.ActionGradient\n': '',
    'import com.elite.sch.app.ui.theme.StatusCompleted\n': '',
    'import com.elite.sch.app.ui.theme.StatusSkipped\n': '',
    'import com.elite.sch.app.ui.theme.StatusPlanned\n': '',
    'import com.elite.sch.app.ui.theme.TealSoft\n': '',
    'import com.elite.sch.app.ui.theme.RedSoft\n': '',
    'import com.elite.sch.app.ui.theme.RedLine\n': '',
    'import com.elite.sch.app.ui.theme.TrackOffBorder\n': '',
    'import com.elite.sch.app.ui.theme.Field\n': '',
    'import com.elite.sch.app.ui.theme.PauseBannerGradientEnd\n': '',
    'import com.elite.sch.app.ui.theme.PrimaryDark\n': '',
    'import com.elite.sch.app.ui.theme.OnPrimaryDark\n': '',
    'import com.elite.sch.app.ui.theme.PrimaryContainerDark\n': '',
    'import com.elite.sch.app.ui.theme.OnPrimaryContainerDark\n': '',
    'import com.elite.sch.app.ui.theme.SecondaryDark\n': '',
    'import com.elite.sch.app.ui.theme.OnSecondaryDark\n': '',
    'import com.elite.sch.app.ui.theme.SecondaryContainerDark\n': '',
    'import com.elite.sch.app.ui.theme.OnSecondaryContainerDark\n': '',
    'import com.elite.sch.app.ui.theme.TertiaryDark\n': '',
    'import com.elite.sch.app.ui.theme.OnTertiaryDark\n': '',
    'import com.elite.sch.app.ui.theme.TertiaryContainerDark\n': '',
    'import com.elite.sch.app.ui.theme.OnTertiaryContainerDark\n': '',
    'import com.elite.sch.app.ui.theme.SurfaceVariantDark\n': '',
    'import com.elite.sch.app.ui.theme.SurfaceContainerDark\n': '',
    'import com.elite.sch.app.ui.theme.SurfaceContainerHighDark\n': '',
    'import com.elite.sch.app.ui.theme.OutlineVariantDark\n': '',
    'import com.elite.sch.app.ui.theme.ScrimDark\n': '',
    'import com.elite.sch.app.ui.theme.InverseSurfaceDark\n': '',
    'import com.elite.sch.app.ui.theme.InverseOnSurfaceDark\n': '',
    'import com.elite.sch.app.ui.theme.RunnaTheme\n': '',
    'import com.elite.sch.app.ui.theme.RunnaNumberStyles\n': '',
    'import com.elite.sch.app.ui.theme.RunnaTextFont\n': '',
}

USAGE_REPLACEMENTS = [
    (r'EliteBackground', 'Background'),
    (r'RunnaBackground', 'Background'),
    (r'EliteCard(?![A-Z])', 'Card'),
    (r'EliteBorder', 'LineStrong'),
    (r'EliteTextPrimary\b', 'Text'),
    (r'EliteTextSecondary\b', 'TextMuted'),
    (r'EliteTextTertiary\b', 'TextDim'),
    (r'EliteTextAccent2\b', 'Brass'),
    (r'EliteTurquoise\b', 'Olive'),
    (r'RunnaTeal\b', 'Olive'),
    (r'RunnaTealOn\b', 'OnOlive'),
    (r'ElitePurple\b', 'Brass'),
    (r'EliteTextAccent2\b', 'Brass'),
    (r'EliteActionOrange\b', 'Red'),
    (r'RunnaOrange\b', 'Red'),
    (r'RunnaOrangeOn\b', 'OnRed'),
    (r'EliteAlert\b', 'Red'),
    (r'EliteError\b', 'Red'),
    (r'EliteSuccess\b', 'Ok'),
    (r'EliteWarning\b', 'Amber'),
    (r'BrandGradient\b', 'SolidColor(Olive)'),
    (r'BrandButtonGradient\b', 'SolidColor(Olive)'),
    (r'ActionGradient\b', 'SolidColor(Red)'),
    (r'BrandGradientVertical\b', 'SolidColor(Olive)'),
    (r'ActionGradientVertical\b', 'SolidColor(Red)'),
    (r'StatusCompleted\b', 'Olive'),
    (r'StatusSkipped\b', 'TextMuted'),
    (r'StatusPlanned\b', 'Brass'),
    (r'TealSoft\b', 'OliveSoft'),
    (r'RedSoft\b', 'RedSoft'),
    (r'RedLine\b', 'Red.copy(alpha = 0.35f)'),
    (r'TrackOffBorder\b', 'LineStrong'),
    (r'Field\b', 'Field'),
    (r'RunnaRedDot\b', 'Red'),
    (r'RunnaTextPrimary\b', 'Text'),
    (r'RunnaTextSecondary\b', 'TextMuted'),
    (r'EliteOnPrimary\b', 'OnOlive'),
    (r'EliteOnSecondary\b', 'OnOlive'),
    (r'EliteOnAction\b', 'OnRed'),
    (r'EliteDangerContainer\b', 'RedSoft'),
]

NEW_IMPORTS = '''import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.TextDim
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.OnRed
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Amber
import com.elite.sch.app.ui.theme.Ok
import com.elite.sch.app.ui.theme.Field
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.EliteLogoFont
import com.elite.sch.app.ui.theme.EliteNumberStyles
import com.elite.sch.app.ui.theme.EliteTextStyles
'''

def migrate_file(filepath):
    content = filepath.read_text(encoding='utf-8')
    original = content

    for pattern, replacement in IMPORT_REPLACEMENTS.items():
        content = content.replace(pattern, replacement)

    for pattern, replacement in USAGE_REPLACEMENTS:
        content = re.sub(pattern, replacement, content)

    if content != original:
        lines = content.split('\n')
        last_import_idx = -1
        for i, line in enumerate(lines):
            if line.strip().startswith('import '):
                last_import_idx = i

        if last_import_idx >= 0:
            if 'Background' not in content or 'Card' not in content:
                lines.insert(last_import_idx + 1, NEW_IMPORTS)
                content = '\n'.join(lines)
        filepath.write_text(content, encoding='utf-8')
        return True
    return False

def main():
    screens_dir = Path('app/src/main/java/com/example/ui/screens')
    count = 0
    for f in screens_dir.glob('*.kt'):
        if migrate_file(f):
            print(f'Migrated: {f}')
            count += 1
        else:
            print(f'No changes: {f}')
    print(f'\nTotal migrated: {count}')

if __name__ == '__main__':
    main()
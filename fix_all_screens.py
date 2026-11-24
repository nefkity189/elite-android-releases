#!/usr/bin/env python3
"""Fix all remaining screen imports and usage for Elite theme."""
import re
from pathlib import Path

SCREENS_DIR = Path("app/src/main/java/com/example/ui/screens")

SCREENS_TO_FIX = [
    "DaysSelectionScreen.kt",
    "DurationScreen.kt",
    "FullPlanScreen.kt",
    "GoalScreen.kt",
    "GoalSelectionScreen.kt",
    "PlanAdjustmentsScreen.kt",
    "PlanPreviewScreen.kt",
    "PostWorkoutFeedback.kt",
    "PreWorkoutCheckinBottomSheet.kt",
    "ProfileScreen.kt",
    "ProgressScreen.kt",
    "QuizScreens.kt",
    "TodayScreen.kt",
    "WorkoutSummaryScreen.kt",
]

COMMON_IMPORTS = """import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.EliteShape
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.LineStrong
import androidx.compose.foundation.shape.RoundedCornerShape"""

USAGE_REPLACEMENTS = [
    (r"EliteBackground", "Background"),
    (r"RunnaBackground", "Background"),
    (r"EliteCard(?![A-Z])", "Card"),
    (r"EliteBorder", "LineStrong"),
    (r"EliteTextPrimary\b", "Text"),
    (r"EliteTextSecondary\b", "TextMuted"),
    (r"EliteTextTertiary\b", "TextDim"),
    (r"EliteTextAccent2\b", "Brass"),
    (r"EliteTurquoise\b", "Olive"),
    (r"RunnaTeal\b", "Olive"),
    (r"RunnaTealOn\b", "OnOlive"),
    (r"ElitePurple\b", "Brass"),
    (r"EliteTextAccent2\b", "Brass"),
    (r"EliteActionOrange\b", "Red"),
    (r"RunnaOrange\b", "Red"),
    (r"RunnaOrangeOn\b", "OnRed"),
    (r"EliteAlert\b", "Red"),
    (r"EliteError\b", "Red"),
    (r"EliteSuccess\b", "Ok"),
    (r"EliteWarning\b", "Amber"),
    (r"BrandGradient\b", "Brush.solidColor(Olive)"),
    (r"BrandButtonGradient\b", "Brush.solidColor(Olive)"),
    (r"ActionGradient\b", "Brush.solidColor(Red)"),
    (r"BrandGradientVertical\b", "Brush.solidColor(Olive)"),
    (r"ActionGradientVertical\b", "Brush.solidColor(Red)"),
    (r"StatusCompleted\b", "Olive"),
    (r"StatusSkipped\b", "TextMuted"),
    (r"StatusPlanned\b", "Brass"),
    (r"TealSoft\b", "OliveSoft"),
    (r"RedSoft\b", "RedSoft"),
    (r"RedLine\b", "Red.copy(alpha = 0.35f)"),
    (r"TrackOffBorder\b", "LineStrong"),
    (r"Field\b", "Field"),
    (r"RunnaRedDot\b", "Red"),
    (r"RunnaTextPrimary\b", "Text"),
    (r"RunnaTextSecondary\b", "TextMuted"),
    (r"EliteOnPrimary\b", "OnOlive"),
    (r"EliteOnSecondary\b", "OnOlive"),
    (r"EliteOnAction\b", "OnRed"),
    (r"EliteDangerContainer\b", "RedSoft"),
    (r"EliteCardBottom\b", "CardElevated"),
    (r"EliteCardTop\b", "Card"),
    (r"EliteTrack\b", "CardElevated"),
    (r"EliteCardGradient\b", "Brush.verticalGradient(listOf(Card, CardElevated))"),
    (r"BrandGradient\b", "Brush.solidColor(Olive)"),
    (r"EliteShape\b", "RoundedCornerShape"),
    (r"EliteCardGradient\b", "Brush.verticalGradient(listOf(CardColor, CardElevated))"),
]

OLD_IMPORTS = [
    "import com.elite.sch.app.ui.theme.EliteBackground",
    "import com.elite.sch.app.ui.theme.EliteCard",
    "import com.elite.sch.app.ui.theme.EliteBorder",
    "import com.elite.sch.app.ui.theme.ElitePurple",
    "import com.elite.sch.app.ui.theme.EliteTextPrimary",
    "import com.elite.sch.app.ui.theme.EliteTextSecondary",
    "import com.elite.sch.app.ui.theme.EliteTextTertiary",
    "import com.elite.sch.app.ui.theme.EliteTurquoise",
    "import com.elite.sch.app.ui.theme.EliteAlert",
    "import com.elite.sch.app.ui.theme.EliteActionOrange",
    "import com.elite.sch.app.ui.theme.EliteSuccess",
    "import com.elite.sch.app.ui.theme.EliteWarning",
    "import com.elite.sch.app.ui.theme.EliteError",
    "import com.elite.sch.app.ui.theme.EliteOnPrimary",
    "import com.elite.sch.app.ui.theme.EliteOnSecondary",
    "import com.elite.sch.app.ui.theme.EliteSurfaceVariant",
    "import com.elite.sch.app.ui.theme.EliteOnSurfaceVariant",
    "import com.elite.sch.app.ui.theme.EliteSurface",
    "import com.elite.sch.app.ui.theme.EliteOnSurface",
    "import com.elite.sch.app.ui.theme.ElitePrimary",
    "import com.elite.sch.app.ui.theme.EliteSecondary",
    "import com.elite.sch.app.ui.theme.EliteTertiary",
    "import com.elite.sch.app.ui.theme.ElitePrimaryContainer",
    "import com.elite.sch.app.ui.theme.EliteOnPrimaryContainer",
    "import com.elite.sch.app.ui.theme.EliteSecondaryContainer",
    "import com.elite.sch.app.ui.theme.EliteOnSecondaryContainer",
    "import com.elite.sch.app.ui.theme.EliteTertiaryContainer",
    "import com.elite.sch.app.ui.theme.EliteOnTertiaryContainer",
    "import com.elite.sch.app.ui.theme.EliteSpacing",
    "import com.elite.sch.app.ui.theme.EliteShape",
    "import com.elite.sch.app.ui.theme.EliteCardTop",
    "import com.elite.sch.app.ui.theme.EliteCardBottom",
    "import com.elite.sch.app.ui.theme.EliteTrack",
    "import com.elite.sch.app.ui.theme.EliteTextAccent2",
    "import com.elite.sch.app.ui.theme.EliteTealDark",
    "import com.elite.sch.app.ui.theme.EliteFieldBorder",
    "import com.elite.sch.app.ui.theme.EliteOnAction",
    "import com.elite.sch.app.ui.theme.EliteShadowOverlay",
    "import com.elite.sch.app.ui.theme.EliteDangerContainer",
    "import com.elite.sch.app.ui.theme.EliteBottomBarSurface",
    "import com.elite.sch.app.ui.theme.RunnaBackground",
    "import com.elite.sch.app.ui.theme.RunnaGlass",
    "import com.elite.sch.app.ui.theme.RunnaGlassElevated",
    "import com.elite.sch.app.ui.theme.RunnaGlassBorder",
    "import com.elite.sch.app.ui.theme.RunnaTextPrimary",
    "import com.elite.sch.app.ui.theme.RunnaTextSecondary",
    "import com.elite.sch.app.ui.theme.RunnaTeal",
    "import com.elite.sch.app.ui.theme.RunnaTealOn",
    "import com.elite.sch.app.ui.theme.RunnaOrange",
    "import com.elite.sch.app.ui.theme.RunnaOrangeOn",
    "import com.elite.sch.app.ui.theme.RunnaRedDot",
    "import com.elite.sch.app.ui.theme.RunnaNumberStyles",
    "import com.elite.sch.app.ui.theme.RunnaTextStyles",
    "import com.elite.sch.app.ui.theme.BrandGradient",
    "import com.elite.sch.app.ui.theme.BrandButtonGradient",
    "import com.elite.sch.app.ui.theme.ActionGradient",
    "import com.elite.sch.app.ui.theme.StatusCompleted",
    "import com.elite.sch.app.ui.theme.StatusSkipped",
    "import com.elite.sch.app.ui.theme.StatusPlanned",
    "import com.elite.sch.app.ui.theme.TealSoft",
    "import com.elite.sch.app.ui.theme.RedSoft",
    "import com.elite.sch.app.ui.theme.RedLine",
    "import com.elite.sch.app.ui.theme.TrackOffBorder",
    "import com.elite.sch.app.ui.theme.Field",
    "import com.elite.sch.app.ui.theme.PauseBannerGradientEnd",
    "import com.elite.sch.app.ui.theme.PrimaryDark",
    "import com.elite.sch.app.ui.theme.OnPrimaryDark",
    "import com.elite.sch.app.ui.theme.PrimaryContainerDark",
    "import com.elite.sch.app.ui.theme.OnPrimaryContainerDark",
    "import com.elite.sch.app.ui.theme.SecondaryDark",
    "import com.elite.sch.app.ui.theme.OnSecondaryDark",
    "import com.elite.sch.app.ui.theme.SecondaryContainerDark",
    "import com.elite.sch.app.ui.theme.OnSecondaryContainerDark",
    "import com.elite.sch.app.ui.theme.TertiaryDark",
    "import com.elite.sch.app.ui.theme.OnTertiaryDark",
    "import com.elite.sch.app.ui.theme.TertiaryContainerDark",
    "import com.elite.sch.app.ui.theme.OnTertiaryContainerDark",
    "import com.elite.sch.app.ui.theme.SurfaceVariantDark",
    "import com.elite.sch.app.ui.theme.SurfaceContainerDark",
    "import com.elite.sch.app.ui.theme.SurfaceContainerHighDark",
    "import com.elite.sch.app.ui.theme.OutlineVariantDark",
    "import com.elite.sch.app.ui.theme.ScrimDark",
    "import com.elite.sch.app.ui.theme.InverseSurfaceDark",
    "import com.elite.sch.app.ui.theme.InverseOnSurfaceDark",
    "import com.elite.sch.app.ui.theme.RunnaTheme",
    "import com.elite.sch.app.ui.theme.RunnaNumberStyles",
    "import com.elite.sch.app.ui.theme.RunnaTextStyles",
    "import com.elite.sch.app.ui.theme.EliteCard",
    "import com.elite.sch.app.ui.theme.EliteCardGradient",
    "import com.elite.sch.app.ui.theme.EliteShape",
]

SCREENS_TO_FIX = [
    "DaysSelectionScreen.kt",
    "DurationScreen.kt",
    "FullPlanScreen.kt",
    "GoalScreen.kt",
    "GoalSelectionScreen.kt",
    "PlanAdjustmentsScreen.kt",
    "PlanPreviewScreen.kt",
    "PostWorkoutFeedback.kt",
    "PreWorkoutCheckinBottomSheet.kt",
    "ProfileScreen.kt",
    "ProgressScreen.kt",
    "QuizScreens.kt",
    "TodayScreen.kt",
    "WorkoutSummaryScreen.kt",
]

COMMON_IMPORTS = """import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.EliteShape
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.LineStrong
import androidx.compose.foundation.shape.RoundedCornerShape"""

def fix_file(filepath):
    content = filepath.read_text(encoding="utf-8")
    original = content

    for old_import in OLD_IMPORTS:
        content = content.replace(old_import + "\n", "")

    for pattern, replacement in USAGE_REPLACEMENTS:
        content = re.sub(pattern, replacement, content)

    if content != original:
        lines = content.split("\n")
        last_import_idx = -1
        for i, line in enumerate(lines):
            if line.strip().startswith("import "):
                last_import_idx = i

        if last_import_idx >= 0:
            if "Background" not in content or "Card as CardColor" not in content:
                lines.insert(last_import_idx + 1, COMMON_IMPORTS)
                content = "\n".join(lines)
        filepath.write_text(content, encoding="utf-8")
        return True
    return False

def main():
    count = 0
    for f in SCREENS_TO_FIX:
        filepath = SCREENS_DIR / f
        if filepath.exists():
            if fix_file(filepath):
                print(f"Fixed: {f}")
                count += 1
            else:
                print(f"No changes: {f}")
        else:
            print(f"Not found: {f}")
    print(f"\nTotal fixed: {count}")

if __name__ == "__main__":
    main()
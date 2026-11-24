package com.elite.sch.app.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import com.elite.sch.app.ui.competition.CompetitionDistance
import kotlin.math.roundToInt

data class SharePodiumEntry(val name: String, val time: String)
data class ShareRankingEntry(val position: Int, val name: String, val elo: Int)

/** Six static 4:5 cards. All athlete and race details come from the caller. */
object CompetitionShareCardGenerator {
    const val WIDTH = 1080
    const val HEIGHT = 1350
    private val ink = Color.rgb(10, 11, 9)
    private val bone = Color.rgb(236, 232, 218)
    private val muted = Color.rgb(143, 147, 132)
    private val olive = Color.rgb(163, 181, 101)
    private val red = Color.rgb(209, 43, 43)
    private val brass = Color.rgb(201, 162, 75)
    private val elev = Color.rgb(29, 33, 22)
    private const val SKEW = -0.21256f

    fun render(
        context: Context, kind: Int, distance: CompetitionDistance = CompetitionDistance.K5,
        missionTime: String? = null, participants: Int? = null, runnerName: String? = null,
        resultTime: String? = null, placement: Int? = null, elo: Int? = null,
        podium: List<SharePodiumEntry> = emptyList(), rankPosition: Int? = null,
        rankState: String? = null, neighbors: List<ShareRankingEntry> = emptyList(),
        achievementTitle: String? = null, achievementDescription: String? = null
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        val f = ShareCardFonts.load(context)
        c.drawColor(ink)
        mark(c, if (kind == 5) 610f else 560f, 300f, 700f, Color.argb(14, 163, 181, 101), true)
        mark(c, 72f, 72f, 64f, bone, false)
        text(c, "ELITE", 160f, 120f, 44f, bone, f.logo)
        val label = when (kind) {
            0 -> "Corrida ${distance.label}"; 1 -> "Prova ${distance.label}"; 2 -> "Conquista"
            3 -> if (rankState.isNullOrBlank()) "Ranking global" else "Ranking $rankState"
            4 -> "Convite"; else -> "Prova ${distance.label}"
        }
        text(c, label, 1008f, 108f, 26f, muted, f.bodyBold, Paint.Align.RIGHT)
        when (kind.coerceIn(0, 5)) {
            0 -> participation(c, f, distance, runnerName, resultTime, placement, participants)
            1 -> podium(c, f, podium, placement)
            2 -> achievement(c, f, achievementTitle, achievementDescription)
            3 -> ranking(c, f, runnerName, elo, rankPosition, neighbors)
            4 -> invite(c, f, distance, missionTime, participants)
            else -> noApp(c, f, distance, missionTime)
        }
        if (kind != 1) skewRect(c, 72f, 1232f, 64f, 10f, red)
        text(c, "Missão dada é missão cumprida", 72f, 1286f, 24f, muted, f.bodyBold)
        text(c, "Competição online", 1008f, 1286f, 24f, bone, f.bodyBold, Paint.Align.RIGHT)
        return bitmap
    }

    private fun participation(c: Canvas, f: ShareCardFonts, distance: CompetitionDistance, name: String?, time: String?, place: Int?, count: Int?) {
        text(c, if (time == null) "Participação" else "Missão cumprida", 72f, 230f, 32f, olive, f.bodyBold)
        name?.takeIf(String::isNotBlank)?.let { text(c, it, 72f, 295f, 52f, bone, f.display, max = 900f) }
        if (time == null) {
            text(c, "Resultado ainda não confirmado", 72f, 500f, 40f, muted, f.bodyBold)
            return
        }
        italic(c, time, 72f, 650f, 320f, bone, f.display, 950f)
        skewRect(c, 72f, 700f, 240f, 12f, red)
        text(c, "tempo registrado na prova ${distance.label}", 72f, 754f, 26f, muted, f.body)
        c.drawRect(72f, 1008f, 1008f, 1012f, Paint().apply { color = olive })
        runner(c, 730f, 772f, 2f)
        c.drawCircle(1000f, 1010f, 14f, Paint(3).apply { color = bone })
        val parts = time.split(':').mapNotNull { it.toIntOrNull() }
        val seconds = if (parts.size == 2) parts[0] * 60 + parts[1] else if (parts.size == 3) parts[0] * 3600 + parts[1] * 60 + parts[2] else null
        val pace = seconds?.let { (it / (distance.meters / 1000.0)).roundToInt() }
        val values = listOf(place?.let { "${it}º" } ?: "—", count?.toString() ?: "—", pace?.let { "%d:%02d".format(it / 60, it % 60) } ?: "—")
        listOf("colocação", "corredores", "ritmo por km").forEachIndexed { i, title ->
            val x = 72f + 312f * i
            text(c, values[i], x, 1130f, 84f, bone, f.display)
            text(c, title, x, 1172f, 26f, muted, f.body)
        }
    }

    private fun podium(c: Canvas, f: ShareCardFonts, entries: List<SharePodiumEntry>, place: Int?) {
        italic(c, "NO PÓDIO", 72f, 350f, 170f, bone, f.display, 930f)
        text(c, "Os primeiros colocados da missão", 72f, 430f, 32f, muted, f.body)
        if (entries.isEmpty() || place == null || place !in 1..3) {
            text(c, "Resultado de pódio indisponível", 72f, 590f, 34f, muted, f.body)
            return
        }
        val xs = listOf(386f, 72f, 700f); val heights = listOf(450f, 300f, 220f)
        val colors = listOf(brass, Color.rgb(184, 188, 176), Color.rgb(168, 112, 62))
        entries.take(3).forEachIndexed { i, entry ->
            val top = 1150f - heights[i]; val x = xs[i]
            skewRect(c, x, top, 272f, heights[i], colors[i])
            if (place == i + 1) skewRect(c, x + 60f, top - 120f, 80f, 12f, red)
            text(c, entry.name, x + 65f, top - 58f, 36f, bone, f.display, max = 220f)
            text(c, entry.time, x + 65f, top - 10f, 48f, if (place == i + 1) bone else muted, f.display)
            text(c, "${i + 1}", x + 165f, top + 175f, 190f, ink, f.display, Paint.Align.CENTER)
        }
    }

    private fun achievement(c: Canvas, f: ShareCardFonts, title: String?, description: String?) {
        if (title.isNullOrBlank()) {
            text(c, "Nenhuma conquista confirmada", 72f, 450f, 40f, muted, f.bodyBold)
            return
        }
        val path = Path().apply { moveTo(307f, 190f); lineTo(495f, 256f); lineTo(486f, 445f); cubicTo(467f, 552f, 378f, 613f, 307f, 641f); cubicTo(232f, 613f, 143f, 552f, 128f, 445f); lineTo(119f, 256f); close() }
        c.drawPath(path, Paint(3).apply { color = brass }); mark(c, 205f, 280f, 200f, ink, false)
        text(c, "Conquista desbloqueada", 72f, 755f, 32f, brass, f.bodyBold)
        italic(c, title.uppercase(), 72f, 930f, 120f, bone, f.display, 930f)
        description?.let { text(c, it, 72f, 1000f, 30f, muted, f.body, max = 930f) }
    }

    private fun ranking(c: Canvas, f: ShareCardFonts, name: String?, elo: Int?, position: Int?, neighbors: List<ShareRankingEntry>) {
        text(c, "Sua posição na temporada", 72f, 230f, 32f, olive, f.bodyBold)
        if (position == null || elo == null || name.isNullOrBlank()) {
            text(c, "Classificação ainda não disponível", 72f, 450f, 40f, muted, f.bodyBold)
            return
        }
        italic(c, "#$position", 72f, 600f, 350f, olive, f.display, 720f)
        text(c, "$elo pontos Elo", 760f, 600f, 55f, bone, f.display)
        text(c, name, 72f, 700f, 44f, muted, f.body, max = 900f)
        neighbors.take(3).forEachIndexed { i, row ->
            val y = 790f + i * 115f; val current = row.position == position
            skewRect(c, 72f, y, 900f, 96f, if (current) olive else elev)
            val color = if (current) ink else bone
            text(c, row.position.toString(), 110f, y + 65f, 44f, color, f.display)
            text(c, row.name, 215f, y + 63f, 36f, color, f.bodyBold, max = 490f)
            text(c, row.elo.toString(), 930f, y + 65f, 48f, color, f.display, Paint.Align.RIGHT)
        }
    }

    private fun invite(c: Canvas, f: ShareCardFonts, distance: CompetitionDistance, time: String?, count: Int?) {
        text(c, "Corra a próxima prova com a gente", 72f, 220f, 32f, olive, f.bodyBold)
        italic(c, distance.label, 72f, 795f, if (distance == CompetitionDistance.K25) 500f else 650f, olive, f.display, 1100f)
        skewRect(c, 72f, 846f, 300f, 12f, red)
        time?.takeIf { it.isNotBlank() && it != "--:--" }?.let { italic(c, "LARGADA $it", 72f, 980f, 116f, bone, f.display, 900f) }
        repeat(10) { i -> skewRect(c, 72f + i * 92f, 1010f, 64f, 26f, olive, outlined = i >= (count ?: 0)) }
        val countText = if (count == null || count <= 0) "Primeira vaga disponível" else "$count ${if (count == 1) "corredor inscrito" else "corredores inscritos"}"
        text(c, countText, 72f, 1080f, 30f, bone, f.body)
        skewRect(c, 72f, 1100f, 900f, 96f, olive)
        text(c, "Entre na sala da prova", 522f, 1160f, 38f, ink, f.bodyBold, Paint.Align.CENTER)
    }

    private fun noApp(c: Canvas, f: ShareCardFonts, distance: CompetitionDistance, time: String?) {
        text(c, "Você foi convidado", 72f, 220f, 32f, olive, f.bodyBold)
        italic(c, "VEM PARA", 72f, 400f, 180f, bone, f.display, 970f)
        italic(c, "O ELITE", 72f, 565f, 180f, olive, f.display, 970f)
        val suffix = time?.takeIf { it.isNotBlank() && it != "--:--" }?.let { " das $it" }.orEmpty()
        text(c, "Corra a prova ${distance.label}$suffix com a gente", 72f, 650f, 32f, muted, f.body, max = 900f)
        listOf("Baixe o app Elite", "Crie seu perfil de corredor", "Entre na sala da prova ${distance.label}").forEachIndexed { i, item ->
            val y = 740f + i * 110f
            c.drawRect(72f, y, 632f, y + 2f, Paint().apply { color = elev })
            text(c, "${i + 1}", 72f, y + 85f, 76f, olive, f.display)
            text(c, item, 150f, y + 66f, 36f, bone, f.bodyBold, max = 490f)
        }
        runner(c, 650f, 660f, 2.85f)
        skewRect(c, 72f, 1090f, 900f, 96f, bone, outlined = true)
        text(c, "Baixe o Elite", 522f, 1152f, 38f, bone, f.bodyBold, Paint.Align.CENTER)
    }

    private fun text(c: Canvas, value: String, x: Float, baseline: Float, size: Float, color: Int, font: Typeface, align: Paint.Align = Paint.Align.LEFT, max: Float = 1000f) {
        val p = Paint(3).apply { this.color = color; textSize = size; typeface = font; textAlign = align }
        while (p.measureText(value) > max && p.textSize > 18f) p.textSize -= 1f
        c.drawText(value, x, baseline, p)
    }
    private fun italic(c: Canvas, value: String, x: Float, baseline: Float, size: Float, color: Int, font: Typeface, max: Float) {
        c.save(); c.translate(x, baseline); c.skew(SKEW, 0f)
        text(c, value, 0f, 0f, size, color, font, max = max)
        c.restore()
    }
    private fun skewRect(c: Canvas, x: Float, y: Float, w: Float, h: Float, color: Int, outlined: Boolean = false) {
        c.save(); c.translate(x, y + h); c.skew(SKEW, 0f)
        c.drawRect(0f, -h, w, 0f, Paint(3).apply { this.color = color; if (outlined) { style = Paint.Style.STROKE; strokeWidth = 3f } })
        c.restore()
    }
    private fun mark(c: Canvas, x: Float, y: Float, size: Float, color: Int, monochrome: Boolean) {
        c.save(); c.translate(x, y); c.scale(size / 64f, size / 64f)
        val a = Path().apply { moveTo(19.1f,8f); lineTo(55.1f,8f); lineTo(53.2f,17f); lineTo(27.2f,17f); lineTo(20.8f,47f); lineTo(46.8f,47f); lineTo(44.9f,56f); lineTo(8.9f,56f); close() }
        val b = Path().apply { moveTo(25.1f,27f); lineTo(45.1f,27f); lineTo(43.1f,36f); lineTo(23.1f,36f); close() }
        c.drawPath(a, Paint(3).apply { this.color = color }); c.drawPath(b, Paint(3).apply { this.color = if (monochrome) color else red }); c.restore()
    }
    private fun runner(c: Canvas, x: Float, y: Float, scale: Float) {
        c.save(); c.translate(x - 30f * scale, y - 5f * scale); c.scale(scale, scale)
        fun segment(x1: Float, y1: Float, x2: Float, y2: Float, width: Float, color: Int) { c.drawLine(x1,y1,x2,y2,Paint(3).apply { this.color=color; strokeWidth=width; strokeCap=Paint.Cap.ROUND }) }
        val far = Color.rgb(111,115,94)
        segment(112f,38f,127.6f,53.6f,9f,far); segment(127.6f,53.6f,142.2f,39.9f,7.5f,far)
        segment(94f,72f,77f,99.1f,13f,far); segment(77f,99.1f,47.1f,100.7f,9.5f,far); segment(47.1f,100.7f,45.7f,111.8f,7f,Color.rgb(142,31,31))
        segment(94f,72f,103f,55f,14f,bone); segment(103f,55f,112f,38f,18f,bone); c.drawCircle(121f,23f,10f,Paint(3).apply { color=bone })
        segment(94f,72f,117.8f,93.4f,14.5f,bone); segment(117.8f,93.4f,130.9f,120.4f,10.5f,bone); segment(130.9f,120.4f,141.7f,117.4f,8f,red)
        segment(112f,38f,97.9f,54.9f,10f,bone); segment(97.9f,54.9f,105.4f,73.4f,8.5f,bone); c.restore()
    }
}

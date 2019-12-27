import java.util.*

class Generator internal constructor(seed: Int) {
    private val random: Random = Random(seed.toLong())
    private val probability: Int
        get() = random.nextInt(100)

    val expression: String
        get() = T + eP

    private val T: String
        get() = S + TP

    private val S: String
        get() = F + SP

    private val F: String
        get() {
            val pr = probability
            return when {
                pr < 10 -> {
                    "($expression)"
                }
                pr < 50 -> {
                    "!$F"
                }
                else -> {
                    letter
                }
            }
        }

    private val eP: String
        get() = if (probability < 33) {
            "|$T$eP"
        } else ""

    private val TP: String
        get() {
            return if (probability < 33) {
                "^$S$TP"
            } else ""
        }

    private val SP: String
        get() {
            return if (probability < 33) {
                "&$F$TP"
            } else ""
        }

    private val letter: String
        get() = ('a' + random.nextInt('z' - 'a')).toString()

}
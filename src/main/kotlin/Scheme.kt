import java.lang.StringBuilder

class Scheme(val formulas: List<Formula>) {
    fun getFirstApplicable(word: Word) = formulas.firstOrNull { it.isApplicable(word) }
    fun applyOnce(word: Word) = getFirstApplicable(word)?.apply(word)

    fun applyAll(word: Word, maxOps: Int = 1000, maxLength: Int = 1000): Word {
        var current = word
        var ops = 0
        while (true) {
            ops++
            val formula = getFirstApplicable(current) ?: return current
            current = formula.apply(current)
            if (formula.isFinal)
                return current
            if (current.length() > maxLength)
                throw ExecutionLimitException("Word length limit exceeded")
            if (ops >= maxOps)
                throw ExecutionLimitException("Operations limit exceeded")
        }
    }
    fun applyAllVerbose(word: Word, maxOps: Int = 1000, maxLength: Int = 1000): Pair<Word, String> {
        var current = word
        var ops = 0
        val log = StringBuilder()
        while (true) {
            ops++
            val formula = getFirstApplicable(current) ?: return current to log.toString()
            log.append(formula.viewApplication(current))
            log.append("\n")
            current = formula.apply(current)
            if (formula.isFinal)
                return current to log.toString()
            if (current.length() > maxLength)
                throw ExecutionLimitException("Word length limit exceeded")
            if (ops >= maxOps)
                throw ExecutionLimitException("Operations limit exceeded")
        }
    }

    fun applyAllOrError(word: Word, verbose: Boolean, maxOps: Int = 1000, maxLength: Int = 1000, emptyWord: String = ""): String {
        return try {
            if (verbose) {
                val result = applyAllVerbose(word, maxOps, maxLength)
                result.second + result.first.view(emptyWord)
            }
            else {
                applyAll(word, maxOps, maxLength).view(emptyWord)
            }
        } catch (e: ExecutionLimitException) {
            e.message ?: "Unknown error"
        }
    }
}
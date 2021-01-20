import java.lang.StringBuilder

// Represents a Markov algorithm as a sequence of formulas
class Scheme(val formulas: List<Formula>) {
    // Finds the first formula of the scheme which can be applied to    a word
    fun getFirstApplicable(word: Word) = formulas.firstOrNull { it.isApplicable(word) }

    // Applies the first of the formulas which can be applied to a word
    // Returns null if there aren't any
    fun applyOnce(word: Word) = getFirstApplicable(word)?.apply(word)

    // Executes the Markov algorithm until there aren't any applicable substitutions
    // formulas or a final formula is applied. Throws ExecutionLimitException if the
    // number of operations or the length of the resulting word becomes too big
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

    // Executes the Markov algorithm until there aren't any applicable substitutions
    // formulas or a final formula is applied. Returns the result of the algorithm and
    // the list of formula applications separated by newlines
    private fun applyAllVerbose(word: Word, maxOps: Int = 1000, maxLength: Int = 1000): Pair<Word, String> {
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
    // Returns the result of the algorithm as it should be viewed to the user
    fun applyAllOrError(
        word: Word,
        verbose: Boolean,
        maxOps: Int = 1000,
        maxLength: Int = 1000,
        emptyWord: String = ""
    ): String {
        return try {
            if (verbose) {
                val result = applyAllVerbose(word, maxOps, maxLength)
                result.second + result.first.view(emptyWord)
            } else {
                applyAll(word, maxOps, maxLength).view(emptyWord)
            }
        } catch (e: ExecutionLimitException) {
            e.message ?: "Unknown error"
        }
    }
}
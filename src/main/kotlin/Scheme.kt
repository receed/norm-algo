class Scheme(val formulas: List<Formula>) {
    fun getFirstApplicable(word: Word) = formulas.firstOrNull { it.isApplicable(word) }
    fun applyOnce(word: Word) = getFirstApplicable(word)?.apply(word)
    fun applyAll(word: Word, maxOps: Int, maxLength: Int): Word {
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
    fun applyAllOrError(word: Word, maxOps: Int, maxLength: Int, emptyWord: String = ""): String {
        try {
            val result = applyAll(word, maxOps, maxLength).getSymbols()
            return if (result.isEmpty()) emptyWord else result
        } catch(e: ExecutionLimitException) {
            return e.message ?: "Unknown error"
        }
    }
}
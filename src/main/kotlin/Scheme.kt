import java.lang.Exception

class Scheme(val formulas: List<Formula>) {
    fun getFirstApplicable(word: Word) = formulas.firstOrNull { it.isApplicable(word) }
    fun applyOnce(word: Word) = getFirstApplicable(word)?.apply(word)
    fun applyAll(word: Word, maxOps: Int = 1000, maxLength: Int = 10000): Word {
        var current = word
        var ops = 0
        while (true) {
            val formula = getFirstApplicable(current) ?: return current
            current = formula.apply(current)
            ops++
            if (current.length() > maxLength)
                throw Exception("Word length limit exceeded")
            if (ops >= maxOps)
                throw Exception("Operations limit exceeded")
        }
    }
}
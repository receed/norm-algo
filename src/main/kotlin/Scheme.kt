import java.lang.Exception
import kotlin.math.max

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
    fun applyAllOrError(word: Word, maxOps: Int, maxLength: Int): String {
        return try {
            applyAll(word, maxOps, maxLength).getSymbols()
        } catch(e: ExecutionLimitException) {
            e.message ?: "Unknown error"
        }
    }
}
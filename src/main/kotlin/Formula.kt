// Represents a substitution formula left->right
class Formula(private val left: Word, private val right: Word, val isFinal: Boolean = false) {
    // Checks if the formula can be applied to a word
    fun isApplicable(word: Word): Boolean = word.firstMatch(left) != -1
    // Returns the result of applying the formula to a word or null if it is not applicable
//    fun tryApply(word: Word): Word? {
//        val pos = word.firstMatch(left)
//        return if (pos == -1) null else word.replace(pos, pos + left.length(), right)
//    }
    // Applies the formula to a word assuming this is possible
    fun apply(word: Word): Word {
        val pos = word.firstMatch(left)
        return word.replace(pos, pos + left.length(), right)
    }
    // Returns a readable representation of the way the formula can be applied to a word
    fun viewApplication(word: Word): String {
        val pos = word.firstMatch(left)
        val viewLeft = word.replace(pos, pos + left.length(), Word("(${left.getSymbols()})")).getSymbols()
        val viewRight = word.replace(pos, pos + left.length(), Word("(${right.getSymbols()})")).getSymbols()
        return "$viewLeft =>${if (isFinal) "." else ""} $viewRight"
    }
}
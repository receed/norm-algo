class Formula(val left: Word, val right: Word, val isFinal: Boolean = false) {
    fun isApplicable(word: Word): Boolean = word.firstMatch(left) != -1
    fun tryApply(word: Word): Word? {
        val pos = word.firstMatch(left)
        return if (pos == -1) null else word.replace(pos, pos + left.length(), right)
    }
    fun apply(word: Word): Word {
        val pos = word.firstMatch(left)
        return word.replace(pos, pos + left.length(), right)
    }
}
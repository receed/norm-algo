class Word(private val symbols: String) {
    fun isEmpty(): Boolean = symbols.isEmpty()
    fun length(): Int = symbols.length
    fun firstMatch(sub: Word) = symbols.indexOf(sub.symbols)
    fun replace(startIndex: Int, endIndex: Int, replacement: Word) =
        Word(symbols.substring(0, startIndex) + replacement.symbols + symbols.substring(endIndex))
    override fun toString(): String = "\"$symbols\""
    override fun equals(other: Any?): Boolean =
        if (other is Word)
            symbols == other.symbols
        else
            false
}
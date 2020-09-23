// Represents a single word
// All indices are zero-based
class Word(private val symbols: String) {
    companion object {
        // Creates new word from readable representation
        fun create(symbols: String, emptyWord: String) = Word(if (symbols == emptyWord) "" else symbols)
    }
    fun isEmpty(): Boolean = symbols.isEmpty()
    fun length(): Int = symbols.length
    // Finds first occurrence of sub as a substring
    fun firstMatch(sub: Word) = symbols.indexOf(sub.symbols)
    // Replaces symbols from startIndex (inclusive) to endIndex (exclusive) with replacement
    fun replace(startIndex: Int, endIndex: Int, replacement: Word) =
        Word(symbols.substring(0, startIndex) + replacement.symbols + symbols.substring(endIndex))
    fun getSymbols() = symbols
    // Returns quoted symbols
    override fun toString(): String = "\"$symbols\""
    // Checks if symbols of two words are equal
    override fun equals(other: Any?): Boolean =
        if (other is Word)
            symbols == other.symbols
        else
            false
    // Substitutes empty word with readable string
    fun view(emptyWord: String) = if (symbols.isEmpty()) emptyWord else symbols
    override fun hashCode(): Int {
        return symbols.hashCode()
    }
}
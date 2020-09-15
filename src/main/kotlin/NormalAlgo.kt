import java.io.File

enum class Option(val command: String, val takesValue: Boolean, val defaultValue: String? = null) {
    LOG_LEVEL("l", true),
    OUTPUT_FILE("o", true),
    EMPTY_WORD("e", true, "\\"),
    MAX_OPERATIONS("mo", true, "1000"),
    MAX_LENGTH("ml", true, "1000")
}

class InvalidInputException(message:String): Exception(message)
class ExecutionLimitException(message:String): Exception(message)

fun readSchemeAndWords(fileName: String, emptyWord: String): Pair<Scheme, List<Word>> {
    val file = File(fileName)
    if (!file.exists()) {
        throw InvalidInputException("$fileName: no such file")
    }
    val content = file.readLines().map { it.split(' ') }.filter { it.isNotEmpty() }
    val scheme = Scheme(content.takeWhile {it.size == 2}.map {Formula(Word.create(it[0], emptyWord), Word.create(it[1], emptyWord))})
    val words = content.dropWhile { it.size == 2 }
    val invalidWord = words.firstOrNull { it.size != 1}
    if (invalidWord != null)
        throw InvalidInputException("Invalid word: $invalidWord")
    val wordList = words.map { Word.create(it[0], emptyWord) }
    return scheme to wordList
}
fun main(args: Array<String>) {
    try {
        var inputFile: String? = null
        var lastOption: Option? = null
        val nameToOption = Option.values().map { it.command to it }.toMap()
        val enabledOptions = mutableSetOf<Option>()
        val optionValues = mutableMapOf<Option, String>()
        for (arg in args) {
            if (arg.startsWith("-")) {
                if (lastOption != null) {
                    throw InvalidInputException("No value for ${lastOption.name}")
                }
                val option = nameToOption[arg.substring(1)] ?: throw InvalidInputException("Unknown option: $arg");
                if (option.takesValue) {
                    lastOption = option
                } else {
                    enabledOptions.add(option)
                }
            } else {
                if (lastOption != null) {
                    if (optionValues.containsKey(lastOption)) {
                        throw InvalidInputException("More than one value for option ${lastOption.command}")
                    }
                    optionValues[lastOption] = arg
                    lastOption = null
                } else {
                    if (inputFile == null)
                        inputFile = arg
                    else {
                        throw InvalidInputException("More than one input file")
                    }
                }
            }
        }
        if (lastOption != null) {
            throw InvalidInputException("No value for ${lastOption.name}")
        }
        if (inputFile == null) {
            throw InvalidInputException("No input file")
        }
        for (option in Option.values())
            if (!optionValues.containsKey(option) && option.defaultValue != null)
                optionValues[option] = option.defaultValue
        val (scheme, words) = readSchemeAndWords(inputFile, optionValues[Option.EMPTY_WORD]!!)
        val outputFile = optionValues[Option.OUTPUT_FILE]
        val maxOperations: Int
        try {
            maxOperations = optionValues[Option.MAX_OPERATIONS]!!.toInt()
        }
        catch (exception: Exception) {
            throw InvalidInputException("Operations limit isn't a number")
        }
        val maxLength: Int
        try {
            maxLength = optionValues[Option.MAX_LENGTH]!!.toInt()
        }
        catch (e: Exception) {
            throw InvalidInputException("Length limit isn't a number")
        }
        if (outputFile == null) {
            for (word in words)
                println(scheme.applyAllOrError(word, maxOperations, maxLength))
        } else {
            File(outputFile).writeText(words.map { scheme.applyAllOrError(it, maxOperations, maxLength) }.joinToString("\n"))
        }
    } catch (e: InvalidInputException) {
        println(e.message)
    }
}
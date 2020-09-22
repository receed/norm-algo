import java.io.File

// Contains names and defaults for command-line options
enum class Option(val command: String, val takesValue: Boolean, val defaultValue: String? = null) {
    INPUT_FILE("", true),
    OUTPUT_FILE("o", true),
    EMPTY_WORD("e", true, "\\"),
    MAX_OPERATIONS("mo", true, "1000"),
    MAX_LENGTH("ml", true, "1000"),
    BATCH("b", false),
    VERBOSE("v", false)
}

// Contains options in fields of proper types
class OptionValues(val inputFile: String, val outputFile: String?, val emptyWord: String, val maxOps: Int, val maxLength: Int, val batch: Boolean, val verbose: Boolean)

class InvalidInputException(message: String) : Exception(message)
class ExecutionLimitException(message: String) : Exception(message)

// Reads algorithm scheme and input words from file
fun readSchemeAndWords(fileName: String, emptyWord: String): Pair<Scheme, List<Word>> {
    val file = File(fileName)
    if (!file.exists()) {
        throw InvalidInputException("$fileName: no such file")
    }
    val content = file.readLines().map { it.split(' ') }.filter { it.isNotEmpty() }
    val scheme = Scheme(content.takeWhile { it.size == 2 || it.size == 3 && it[2] == "." }
        .map { Formula(Word.create(it[0], emptyWord), Word.create(it[1], emptyWord), it.size == 3) })
    val words = content.drop(scheme.formulas.size)
    val invalidWord = words.firstOrNull { it.size != 1 }
    if (invalidWord != null)
        throw InvalidInputException("Invalid word: $invalidWord")
    val wordList = words.map { Word.create(it[0], emptyWord) }
    return scheme to wordList
}

// Associates string values of options with their names
fun parseOptions(args: Array<String>): Map<Option, String> {
    var lastOption: Option? = null
    val nameToOption = Option.values().map { it.command to it }.toMap()
    val optionStrings = mutableMapOf<Option, String>()
    for (arg in args) {
        if (arg.startsWith("-")) {
            if (lastOption != null) {
                throw InvalidInputException("No value for ${lastOption.name}")
            }
            val option = nameToOption[arg.substring(1)] ?: throw InvalidInputException("Unknown option: $arg");
            if (option.takesValue) {
                lastOption = option
            } else {
                optionStrings[option] = "1"
            }
        } else {
            if (lastOption != null) {
                if (optionStrings.containsKey(lastOption)) {
                    throw InvalidInputException("More than one value for option ${lastOption.command}")
                }
                optionStrings[lastOption] = arg
                lastOption = null
            } else {
                if (!optionStrings.containsKey(Option.INPUT_FILE))
                    optionStrings[Option.INPUT_FILE] = arg
                else {
                    throw InvalidInputException("More than one input file")
                }
            }
        }
    }
    if (lastOption != null) {
        throw InvalidInputException("No value for ${lastOption.name}")
    }
    for (option in Option.values())
        if (!optionStrings.containsKey(option) && option.defaultValue != null)
            optionStrings[option] = option.defaultValue
    return optionStrings
}

// Converts string values of the options to required types
fun convertOptions(optionStrings: Map<Option, String>): OptionValues {
    return OptionValues(
        optionStrings[Option.INPUT_FILE] ?: throw InvalidInputException("No input file"),
        optionStrings[Option.OUTPUT_FILE],
        optionStrings[Option.EMPTY_WORD] ?: error("No empty word"),
        optionStrings[Option.MAX_OPERATIONS]?.toIntOrNull() ?: throw InvalidInputException("Operations limit isn't a number"),
        optionStrings[Option.MAX_LENGTH]?.toIntOrNull() ?: throw InvalidInputException("Length limit isn't a number"),
        optionStrings.containsKey(Option.BATCH),
        optionStrings.containsKey(Option.VERBOSE),
    )
}

// Runs a Markov algorithm and writes the result into a file
fun runSchemeToFile(outputFile: String, scheme: Scheme, words: List<Word>, optionValues: OptionValues) {
    File(outputFile).writeText(words.joinToString("\n") { word ->
        scheme.applyAllOrError(
            word,
            optionValues.verbose,
            optionValues.maxOps,
            optionValues.maxLength,
            optionValues.emptyWord
        )
    })
}

// Runs a Markov algorithm and writes the result into console
fun runSchemeToStdout(scheme: Scheme, words: List<Word>, optionValues: OptionValues) {
    for (word in words)
        println(
            scheme.applyAllOrError(
                word,
                optionValues.verbose,
                optionValues.maxOps,
                optionValues.maxLength,
                optionValues.emptyWord
            )
        )
}

// Entry point
fun main(args: Array<String>) {
    try {
        val optionStrings = parseOptions(args)
        val optionValues = convertOptions(optionStrings)
        if (optionValues.batch) {
            File(optionValues.inputFile).forEachLine {
                val (scheme, words) = readSchemeAndWords(it, optionValues.emptyWord)
                runSchemeToFile("$it.out", scheme, words, optionValues)
            }
        } else {
            val (scheme, words) = readSchemeAndWords(optionValues.inputFile, optionValues.emptyWord)
            val outputFile = optionValues.outputFile
            if (outputFile == null) {
                runSchemeToStdout(scheme, words, optionValues)
            } else {
                runSchemeToFile(outputFile, scheme, words, optionValues)
            }
        }
    } catch (e: InvalidInputException) {
        println(e.message)
    }
}
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
class OptionValues(
    val inputFile: String,
    val outputFile: String?,
    val emptyWord: String,
    val maxOps: Int,
    val maxLength: Int,
    val batch: Boolean,
    val verbose: Boolean
)

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

// Processes an argument which is an option name (starts with "-")
// Takes last option which should be assigned a value and returns new value of such option
fun processOptionName(
    arg: String,
    lastOption: Option?,
    commandToOption: Map<String, Option>,
    optionStrings: MutableMap<Option, String>
): Option? {
    if (lastOption != null) {
        throw InvalidInputException("No value for -${lastOption.command}")
    }
    val option = commandToOption[arg.substring(1)] ?: throw InvalidInputException("Unknown option: $arg");
    return if (option.takesValue) { // Value for the option should be the next argument
        option
    } else { // A boolean option is enabled
        optionStrings[option] = "1"
        null
    }
}

// Processes an argument which is an option value (doesn't start with "-")
// Takes last option which should be assigned a value
fun processOptionValue(arg: String, lastOption: Option?, optionStrings: MutableMap<Option, String>) {
    if (lastOption != null) {
        if (optionStrings.containsKey(lastOption)) {
            throw InvalidInputException("More than one value for option ${lastOption.command}")
        }
        optionStrings[lastOption] = arg
    } else {
        if (!optionStrings.containsKey(Option.INPUT_FILE))
            optionStrings[Option.INPUT_FILE] = arg
        else {
            throw InvalidInputException("More than one input file")
        }
    }
}

// Assigns default values to options without a value
fun fillDefaultValues(optionStrings: MutableMap<Option, String>) {
    for (option in Option.values())
        if (!optionStrings.containsKey(option) && option.defaultValue != null)
            optionStrings[option] = option.defaultValue
}

// Associates string values of options with their names
fun parseOptions(args: Array<String>): Map<Option, String> {
    var lastOption: Option? = null // last option without a value
    val commandToOption = Option.values().map { it.command to it }.toMap()
    val optionStrings = mutableMapOf<Option, String>()
    for (arg in args) {
        lastOption = if (arg.startsWith("-")) {
            processOptionName(arg, lastOption, commandToOption, optionStrings)
        } else {
            processOptionValue(arg, lastOption, optionStrings)
            null
        }
    }
    if (lastOption != null) {
        throw InvalidInputException("No value for -${lastOption.command}")
    }
    fillDefaultValues(optionStrings)
    return optionStrings
}

// Converts string values of the options to required types
fun convertOptions(optionStrings: Map<Option, String>): OptionValues {
    return OptionValues(
        optionStrings[Option.INPUT_FILE] ?: throw InvalidInputException("No input file"),
        optionStrings[Option.OUTPUT_FILE],
        optionStrings[Option.EMPTY_WORD] ?: error("No empty word"),
        optionStrings[Option.MAX_OPERATIONS]?.toIntOrNull()
            ?: throw InvalidInputException("Operations limit isn't a number"),
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

// Writes the result for the input file to the output file or to the console
fun processSingle(optionValues: OptionValues) {
    val (scheme, words) = readSchemeAndWords(optionValues.inputFile, optionValues.emptyWord)
    val outputFile = optionValues.outputFile
    if (outputFile == null) {
        runSchemeToStdout(scheme, words, optionValues)
    } else {
        runSchemeToFile(outputFile, scheme, words, optionValues)
    }
}

// Uses input file as a list of files to process
fun processBatch(optionValues: OptionValues) {
    File(optionValues.inputFile).forEachLine {
        val (scheme, words) = readSchemeAndWords(it, optionValues.emptyWord)
        runSchemeToFile("$it.out", scheme, words, optionValues)
    }
}

// Entry point
fun main(args: Array<String>) {
    try {
        val optionStrings = parseOptions(args)
        val optionValues = convertOptions(optionStrings)
        if (optionValues.batch) {
            processBatch(optionValues)
        } else {
            processSingle(optionValues)
        }
    } catch (e: InvalidInputException) {
        println(e.message)
    }
}
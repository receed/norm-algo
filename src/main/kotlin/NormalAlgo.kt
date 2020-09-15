import java.io.File

enum class Option(val command: String, val takesArgument: Boolean) {
    LOG_LEVEL("l", true),
    OUTPUT_FILE("o", true)
}

class InvalidInputException(message:String): Exception(message)

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
                val option = nameToOption[arg.substring(1)];
                if (option == null) {
                    throw InvalidInputException("Unknown option: $arg")
                    return
                }
                if (option.takesArgument) {
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
    }
    catch (exception: InvalidInputException) {
        println(exception.message)
    }
}

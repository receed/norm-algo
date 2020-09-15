enum class Option(val command: String, val takesArgument: Boolean) {
    LOG_LEVEL("l", true),
    OUTPUT_FILE("o", true)
}

fun main(args: Array<String>) {
    var inputFile: String? = null
    var lastOption: Option? = null
    val nameToOption = Option.values().map {it.command to it}.toMap()
    val enabledOptions = mutableSetOf<Option>()
    val optionValues = mutableMapOf<Option, String>()
    for (arg in args) {
        if (arg.startsWith("-")) {
            if (lastOption != null) {
                println("No value for ${lastOption.name}")
                return
            }
            val option = nameToOption[arg.substring(1)];
            if (option == null) {
                println("Unknown option: $arg")
                return
            }
            if (option.takesArgument) {
                lastOption = option
            }
            else {
                enabledOptions.add(option)
            }
        }
        else {
            if (lastOption != null) {
                if (optionValues.containsKey(lastOption)) {
                    println("More than one value for option ${lastOption.command}")
                    return
                }
                optionValues[lastOption] = arg
                lastOption = null
            }
            else {
                if (inputFile == null)
                    inputFile = arg
                else {
                    println("More than one input file")
                    return
                }
            }
        }
    }
    if (lastOption != null) {
        println("No value for ${lastOption.name}")
        return
    }
    if (inputFile == null) {
        println("No input file")
        return
    }
}

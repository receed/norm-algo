import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

// Tests everything from reading file and options to writing file
internal class NormalAlgoTest {
    // Tests batch mode. Input files are read from file "test"
    // Compares the output for the file "testN" with "testN.a"
    @Test
    fun batch() {
        val inputFile = "data/test"
        processAll(arrayOf(inputFile, "-b"))
        File(inputFile).forEachLine {
            val result = File("$it.out").readLines()
            val expected = File("$it.a").readLines()
            assertEquals(expected, result)
        }
    }
    // Tests single-file mode. Input files are test1, ..., test5
    // Compares the output for the file "testN" with "testN.a"
    @Test
    fun single() {
        val inputFiles = (1..5).map{"data/test$it"}
        for (inputFile in inputFiles) {
            processAll(arrayOf(inputFile, "-o", "output"))
            val result = File("output").readLines()
            val expected = File("$inputFile.a").readLines()
            assertEquals(expected, result)
        }
    }
    // Tests verbose mode (when every formula application is printed). Input files are test1, ..., test5
    // Compares the output for the file "testN" with "testN.v.a"
    @Test
    fun verbose() {
        val inputFiles = (1..5).map{"data/test$it"}
        for (inputFile in inputFiles) {
            processAll(arrayOf(inputFile, "-o", "output", "-v"))
            val result = File("output").readLines()
            val expected = File("$inputFile.v.a").readLines()
            assertEquals(expected, result)
        }
    }
    @Test
    fun invalidInput() {
        assertEquals("No input file", processAll(arrayOf("-v", "-ml", "3000")))
        assertEquals("No value for -o", processAll(arrayOf("data/test1", "-o", "-mo", "3000")))
        assertEquals("No value for -o", processAll(arrayOf("data/test1", "-mo", "3000", "-o")))
        assertEquals("FILE: no such file", processAll(arrayOf("FILE")))
        assertEquals("More than one value for option -o", processAll(arrayOf("-o", "out2", "data/test1", "-o", "out1")))
        assertEquals("Operations limit isn't a number", processAll(arrayOf("data/test1", "-o", "out1", "-mo", "inf")))
        assertEquals("Length limit isn't a number", processAll(arrayOf("data/test1", "-o", "out1", "-ml", "3.14")))
        assertEquals("More than one input file", processAll(arrayOf("data/test1", "data/test2")))
        assertEquals("More than one input file", processAll(arrayOf("data/test1", "data/test2")))
        assertEquals("Unknown option: -x", processAll(arrayOf("x", "-x")))
    }
}
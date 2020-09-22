import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class NormalAlgoTest {
    @Test
    fun batch() {
        val inputFile = "data/test"
        main(arrayOf(inputFile, "-b"))
        File(inputFile).forEachLine {
            val result = File("$it.out").readLines()
            val expected = File("$it.a").readLines()
            assertEquals(expected, result)
        }
    }
    @Test
    fun single() {
        val inputFiles = (1..5).map{"data/test$it"}
        for (inputFile in inputFiles) {
            main(arrayOf(inputFile, "-o", "output"))
            val result = File("output").readLines()
            val expected = File("$inputFile.a").readLines()
            assertEquals(expected, result)
        }
    }
}
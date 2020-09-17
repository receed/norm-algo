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
}
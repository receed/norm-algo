import org.junit.jupiter.api.Test
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class WordTest {
    private val empty = Word("")
    private val a = Word("a")
    private val b = Word("b")
    private val abacaba = Word("abacaba")
    private val aaaa = Word("aaaa")
    private val caba = Word("caba")
    private val acaba = Word("acaba")

    @Test
    fun isEmpty() {
        assertTrue(empty.isEmpty())
        assertFalse(a.isEmpty())
    }


    @TestFactory
    fun length() = listOf(
        empty to 0,
        abacaba to 7,
        aaaa to 4)
    .map {(word, len) -> DynamicTest.dynamicTest("length of $word is $len") { assertEquals(len, word.length())}}

    @TestFactory
    fun firstMatch() = listOf(
        Triple(empty, empty, 0),
        Triple(a, empty, 0),
        Triple(b, b, 0),
        Triple(abacaba, caba, 3),
        Triple(aaaa, b, -1),
        Triple(empty, a, -1)
    )
        .map {(word, sub, pos) -> DynamicTest.dynamicTest("$word matches $sub from position $pos")
            { assertEquals(pos, word.firstMatch(sub))}}

    @TestFactory
    fun replace() = listOf(
        ReplaceTest(empty, 0, 0, empty, empty),
        ReplaceTest(empty, 0, 0, aaaa, aaaa),
        ReplaceTest(abacaba, 1, 3, empty, acaba),
        ReplaceTest(aaaa, 0, 4, b, b)
    )
    .map {DynamicTest.dynamicTest(
        "replacing [${it.startIndex}, ${it.endIndex}) in ${it.word} with ${it.sub} gives ${it.result}")
        { assertEquals(it.result, it.word.replace(it.startIndex, it.endIndex, it.sub))}}
    companion object {
        data class ReplaceTest(val word: Word, val startIndex: Int, val endIndex: Int, val sub: Word, val result: Word)
    }
}
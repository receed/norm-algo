import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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

    @Test
    fun length() {
        assertEquals(empty.length(), 0)
        assertEquals(abacaba.length(), 7)
        assertEquals(aaaa.length(), 4)
    }

    @Test
    fun firstMatch() {
        assertEquals(empty.firstMatch(empty), 0)
        assertEquals(aaaa.firstMatch(empty), 0)
        assertEquals(b.firstMatch(b), 0)
        assertEquals(abacaba.firstMatch(caba), 3)
        assertEquals(aaaa.firstMatch(b), -1)
        assertEquals(empty.firstMatch(a), -1)
    }

    @Test
    fun replace() {
        assertEquals(empty.replace(0, 0, empty), empty)
        assertEquals(empty.replace(0, 0, aaaa), aaaa)
        assertEquals(abacaba.replace(1, 3, empty), acaba)
        assertEquals(aaaa.replace(0, 4, b), b)
    }
}
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class WordTest {
    val empty = Word("")
    val a = Word("a")
    val b = Word("b")
    val abacaba = Word("abacaba")
    val aaaa = Word("aaaa")
    val caba = Word("caba")
    val acaba = Word("acaba")

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
        assertEquals(empty.length(), 0)
        assertEquals(abacaba.length(), 7)
        assertEquals(aaaa.length(), 4)
    }

    @Test
    fun replace() {
        assertEquals(empty.replace(0, 0, empty), empty)
        assertEquals(empty.replace(0, 0, aaaa), aaaa)
        assertEquals(abacaba.replace(1, 3, empty), acaba)
        assertEquals(aaaa.replace(0, 4, b), b)
    }
}
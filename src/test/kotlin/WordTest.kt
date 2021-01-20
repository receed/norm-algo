import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

// Test the internal methods of the word class
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
    }

    @Test
    fun isNotEmpty() {
        assertFalse(a.isEmpty())
    }

    @Test
    fun `length if empty is 0`() {
        assertEquals(0, empty.length())
    }

    @Test
    fun length() {
        assertEquals(7, abacaba.length())
    }

    @Test
    fun `empty matches empty`() {
        assertEquals(0, empty.firstMatch(empty))
    }

    @Test
    fun `empty matches start of any word`() {
        assertEquals(0, a.firstMatch(empty))
    }

    @Test
    fun `word matches itself`() {
        assertEquals(0, b.firstMatch(b))
    }

    @Test
    fun `word does not match empty`() {
        assertEquals(-1, empty.firstMatch(a))
    }

    @Test
    fun `first match`() {
        assertEquals(3, abacaba.firstMatch(caba))
    }

    @Test
    fun `no match`() {
        assertEquals(-1, aaaa.firstMatch(b))
    }

    @Test
    fun `replace empty by empty`() {
        assertEquals(empty, empty.replace(0, 0, empty))
    }

    @Test
    fun `replace empty by non-empty`() {
        assertEquals(aaaa, empty.replace(0, 0, aaaa))
    }

    @Test
    fun `replace all word`() {
        assertEquals(b, aaaa.replace(0, 4, b))
    }

    @Test
    fun `replace something by empty`() {
        assertEquals(acaba, abacaba.replace(1, 3, empty))
    }

    @Test
    fun replace() {
        assertEquals(abacaba, aaaa.replace(1, 3, Word("bacab")))
    }
}
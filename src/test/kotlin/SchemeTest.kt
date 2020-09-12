import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SchemeTest {
    val scheme1 = Scheme(listOf(Formula(Word("ab"), Word("")),
        Formula(Word("a"), Word("b"))))
    val scheme2 = Scheme(listOf(Formula(Word("a"), Word("b"))))
    val sceme3 = Scheme(listOf(Formula(Word("b"), Word("a"), true),
        Formula(Word("a"), Word("b"))))

    @Test
    fun getFirstApplicable() {
        assertEquals(scheme1.formulas[0], scheme1.getFirstApplicable(Word("aababab")))
        assertEquals(null, scheme2.getFirstApplicable(Word("bbb")))
        assertEquals(null, scheme2.getFirstApplicable(Word("")))
        assertEquals(scheme2.formulas[0], scheme2.getFirstApplicable(Word("a")))
    }

    @Test
    fun applyOnce() {
        assertEquals(Word("aabab"), scheme1.applyOnce(Word("aababab")))
    }

    @Test
    fun applyAll() {
        assertEquals(Word("b"), scheme1.applyAll(Word("aababab")))
        assertEquals(Word("bb"), scheme2.applyAll(Word("aa")))
        assertEquals(Word("aaa"), sceme3.applyAll(Word("aaa")))
    }
}
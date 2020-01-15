import Graphviz.visualizeTree
import org.junit.Assert
import org.junit.Test
import java.io.IOException
import java.text.ParseException

class ParserTest {
    private fun deleteSpaces(s: String): String {
        val res = StringBuilder()
        for (i in s.indices) {
            if (!Character.isSpaceChar(s[i])) {
                res.append(s[i])
            }
        }
        return res.toString()
    }

    private fun test(expression: String, shouldFail: Boolean) {
        val result = try {
            Parser().parse(expression).toString()
        } catch (e: ParseException) {
            Assert.assertTrue(shouldFail)
            return
        }
        Assert.assertFalse(shouldFail)
        Assert.assertEquals(deleteSpaces(expression), result)
    }

    @Test
    fun sampleTestWithVisualization() {
        val parser = Parser()
        val tree: Node
        try {
            tree = parser.parse("!(!a)")
            visualizeTree(tree, "test")
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun testVariable() {
        test("a", false)
        test("(d)", false)
        test("(((x)))", false)
    }

    @Test
    fun testNegation() {
        test("!a", false)
        test("!(!a)", false)
    }

    @Test
    fun testConjunction() {
        test("a&a", false)
        test("a&b&c", false)
        test("(a&b)&(c&(d&e))", false)
    }

    @Test
    fun testXor() {
        test("a^a", false)
        test("!a^b^c", false)
        test("!a^b&c^d&e", false)
        test("a^b&(!c^d)&e", false)
    }

    @Test
    fun testDisjunction() {
        test("a|a", false)
        test("a|b|c|d", false)
        test("a|b&c|d&e", false)
        test("a|b&(c|d)^e|f^a", false)
    }

    @Test
    fun testWithSpaces() {
        test("a | b", false)
        test("    a", false)
        test("b   ", false)
        test("   a   ^  !  b   ", false)
    }

    @Test
    fun testIncorrectInput() {
        test("b)", true)
        test("!!x & a | b)))", true)
        test("a !& b", true)
        test("a ! b", true)
        test("x () c", true)
        test("x &", true)
        test("x |", true)
        test("x ^", true)
        test("& x", true)
        test("| x", true)
        test("^ x", true)
    }

    @Test
    fun testIllegalCharacter() {
        test("a+b", true)
        test("x|y'", true)
        test("[c&d]", true)
        test("X&X", true)

    }

    @Test
    fun testEmptyInput() {
        test("", true)
        test("()()", true)

    }

    @Test
    fun testNotNot() {
        test("!!a", true)
    }

    @Test
    fun randomTests() {
        /*for (i in 0..999) {
            test(Generator(239 * i).expression, false)
        }*/
    }
}
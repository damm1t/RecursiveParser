import org.junit.Assert
import org.junit.Test
import java.text.ParseException

class LexerTest {
    @Test
    fun testCorrectInput() {
        val testString = StringBuilder("|^&!()")
        var c = 'a'
        while (c <= 'z') {
            testString.append(c)
            c++
        }
        val tokenizer: Tokenizer
        try {
            tokenizer = Tokenizer(testString.toString())
            while (tokenizer.token !== Token.EOF) {
                tokenizer.nextToken()
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Assert.fail("true tokens here")
        }
    }

    @Test
    fun testWrong1Input() {
        val testString = StringBuilder("|^&!(+-")
        val tokenizer: Tokenizer
        var catched = false
        try {
            tokenizer = Tokenizer(testString.toString())
            while (tokenizer.token !== Token.EOF) {
                tokenizer.nextToken()
            }
        } catch (e: ParseException) {
            catched = true
        } finally {
            if (!catched) {
                Assert.fail("wrong tokens here")
            }
        }
    }

    @Test
    fun testWrong2Input() {
        val testString = StringBuilder("|^&!(XAX")
        val tokenizer: Tokenizer
        var catched = false
        try {
            tokenizer = Tokenizer(testString.toString())
            while (tokenizer.token !== Token.EOF) {
                tokenizer.nextToken()
            }
        } catch (e: ParseException) {
            catched = true
        } finally {
            if (!catched) {
                Assert.fail("wrong tokens here")
            }
        }
    }
}
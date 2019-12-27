import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.text.ParseException

class Tokenizer(private val inputStream: InputStream) {
    var position = 0
    private var curChar = 0
    var token: Token = Token.MISTAKE
    private val toToken = HashMap<Char, Token>()

    constructor(inputString: String) : this(
        ByteArrayInputStream(
            inputString.toByteArray(Charset.forName("UTF-8"))
        )
    ) {
    }

    init {
        toToken['('] = Token.OPEN_BRACE
        toToken[')'] = Token.CLOSE_BRACE
        toToken['&'] = Token.AND
        toToken['|'] = Token.OR
        toToken['^'] = Token.XOR
        toToken['!'] = Token.NOT
        nextToken()
    }

    @Throws(ParseException::class)
    private fun nextChar() {
        position++
        curChar = try {
            inputStream.read()
        } catch (e: IOException) {
            throw ParseException(e.message, position)
        }
        while (Character.isSpaceChar(curChar)) {
            nextChar()
        }
    }

    @Throws(ParseException::class)
    fun nextToken() {
        nextChar()
        token = when {
            toToken.containsKey(curChar.toChar()) -> {
                toToken[curChar.toChar()]!!
            }
            Character.isLowerCase(curChar) -> {
                Token.VARIABLE
            }
            curChar == -1 -> {
                Token.EOF
            }
            else -> { // Token.MISTAKE
                throw ParseException("Illegal character : " + curChar.toChar(), position)
            }
        }
    }

    fun getChar(): Char {
        return curChar.toChar()
    }
}
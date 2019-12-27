import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.text.ParseException

class Parser {
    private var tokenizer: Tokenizer? = null
    @Throws(ParseException::class)
    fun parse(expression: String): Node {
        return parse(ByteArrayInputStream(expression.toByteArray()))
    }

    @Throws(ParseException::class)
    fun parse(inputStream: InputStream?): Node {
        tokenizer = Tokenizer(inputStream!!)
        val res = E()
        if (tokenizer!!.token !== Token.EOF) {
            throw ParseException("Incorrect input at index : ", tokenizer!!.position)
        }
        return res
    }

    @Throws(ParseException::class)
    private fun E(): Node {
        val res = Node("E")
        when (tokenizer!!.token) {
            Token.NOT, Token.VARIABLE, Token.OPEN_BRACE -> {
                res.children.add(T())
                res.children.add(EPrime())
            }
            else -> throw ParseException(
                "unexpected char : " + tokenizer!!.getChar(),
                tokenizer!!.position
            )
        }
        return res
    }

    @Throws(ParseException::class)
    private fun T(): Node {
        val res = Node("T")
        when (tokenizer!!.token) {
            Token.NOT, Token.VARIABLE, Token.OPEN_BRACE -> {
                res.children.add(S())
                res.children.add(TPrime())
            }
            else -> throw ParseException(
                "unexpected char : " + tokenizer!!.getChar(),
                tokenizer!!.position
            )
        }
        return res
    }

    @Throws(ParseException::class)
    private fun S(): Node {
        val res = Node("S")
        when (tokenizer!!.token) {
            Token.NOT, Token.VARIABLE, Token.OPEN_BRACE -> {
                res.children.add(F())
                res.children.add(SPrime())
            }
            else -> throw ParseException(
                "unexpected char : " + tokenizer!!.getChar(),
                tokenizer!!.position
            )
        }
        return res
    }

    @Throws(ParseException::class)
    private fun F(): Node {
        val res = Node("F")
        when (tokenizer!!.token) {
            Token.NOT -> {
                res.children.add(Node("!"))
                tokenizer!!.nextToken()
                res.children.add(F())
            }
            Token.OPEN_BRACE -> {
                res.children.add(Node("("))
                tokenizer!!.nextToken()
                res.children.add(E())
                if (tokenizer!!.token !== Token.CLOSE_BRACE) {
                    throw ParseException(
                        "unexpected char : " + tokenizer!!.getChar() + ", expected : )",
                        tokenizer!!.position
                    )
                }
                res.children.add(Node(")"))
                tokenizer!!.nextToken()
            }
            Token.VARIABLE -> {
                res.children.add(Node(Character.toString(tokenizer!!.getChar())))
                tokenizer!!.nextToken()
            }
            else -> throw ParseException(
                "unexpected char : " + tokenizer!!.getChar(),
                tokenizer!!.position
            )
        }
        return res
    }

    @Throws(ParseException::class)
    private fun EPrime(): Node {
        val res = Node("E'")
        when (tokenizer!!.token) {
            Token.OR -> {
                res.children.add(Node("|"))
                tokenizer!!.nextToken()
                res.children.add(T())
                res.children.add(EPrime())
            }
            Token.CLOSE_BRACE, Token.EOF -> {
            }
            else -> throw ParseException(
                "unexpected char : " + tokenizer!!.getChar(),
                tokenizer!!.position
            )
        }
        return res
    }

    @Throws(ParseException::class)
    private fun TPrime(): Node {
        val res = Node("T'")
        when (tokenizer!!.token) {
            Token.XOR -> {
                res.children.add(Node("^"))
                tokenizer!!.nextToken()
                res.children.add(S())
                res.children.add(TPrime())
            }
            Token.CLOSE_BRACE, Token.OR, Token.EOF -> {
            }
            else -> throw ParseException(
                "unexpected char : " + tokenizer!!.getChar(),
                tokenizer!!.position
            )
        }
        return res
    }

    @Throws(ParseException::class)
    private fun SPrime(): Node {
        val res = Node("S'")
        when (tokenizer!!.token) {
            Token.AND -> {
                res.children.add(Node("&"))
                tokenizer!!.nextToken()
                res.children.add(F())
                res.children.add(SPrime())
            }
            Token.CLOSE_BRACE, Token.OR, Token.XOR, Token.EOF -> {
            }
            else -> throw ParseException(
                "unexpected char : " + tokenizer!!.getChar(),
                tokenizer!!.position
            )
        }
        return res
    }
}
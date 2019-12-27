class Node(val type: String) {
    val children: MutableList<Node> = mutableListOf()

    override fun toString(): String {
        return if (isTerminal) {
            type
        } else {
            val res = StringBuilder()
            for (child in children) {
                res.append(child.toString())
            }
            res.toString()
        }
    }

    val arraySymb = arrayOf("^", "|", "&", "!", "(", ")")
    val isTerminal: Boolean
        get() = type in arraySymb || Character.isLowerCase(type[0])
}
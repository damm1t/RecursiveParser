import java.io.*

object GraphvizUtils {
    @Throws(IOException::class, InterruptedException::class)
    fun visualizeTree(tree: Node, name: String) {
        BufferedWriter(OutputStreamWriter(FileOutputStream("$name.dot"))).use { writer ->
            writer.write("digraph $name {\n")
            printGraph(tree, 0, writer)
            writer.write("}\n")
        }
        makePng(name)
    }

    @Throws(IOException::class)
    private fun printNode(tree: Node, id: Int, writer: BufferedWriter): String {
        val vertexName = "A$id"
        writer.write(vertexName + "[label=\"" + tree.type + "\"")
        if (tree.isTerminal) {
            writer.write(",color=blue")
        }
        writer.write("]\n")
        return vertexName
    }

    @Throws(IOException::class)
    private fun printGraph(tree: Node, id: Int, writer: BufferedWriter): Int {
        val vertexName = printNode(tree, id, writer)
        var nextId = id
        for (child in tree.children) {
            nextId++
            val nextVertexName = "A$nextId"
            writer.write("$vertexName->$nextVertexName")
            writer.newLine()
            nextId = printGraph(child, nextId, writer)
        }
        return nextId
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun makePng(name: String) {
        ProcessBuilder("dot", "-Tpng", "$name.dot")
            .redirectOutput(File("$name.png"))
            .start()
            .waitFor()
    }
}
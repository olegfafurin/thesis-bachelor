import kotlin.math.*

/**
 * created by imd on 03.05.2021
 */

class FRTTree(val n: Int, val depth: Int, val beta: Double, val pi: List<Int>) {

    val root = Node(null, 0)
    val corr: MutableMap<Int, Node> = mutableMapOf()
    val asc: MutableMap<Node, MutableList<Node>> = mutableMapOf()

    init {
        val ascRoot = mutableListOf<Node>()
        for (i in 0..ceil(log2(depth.toDouble())).toInt())
            ascRoot.add(root)
        asc[root] = ascRoot
    }

    inner class Node(val parent: Node?, val center: Int) {

        val level: Int = parent?.level?.plus(1) ?: 0
        val children: MutableMap<Int, Node> = mutableMapOf()

        init {
            if (parent != null) {
                parent.children[center] = this
            }
        }

    }


    fun addCenterSequence(nodeIndex: Int, seq: List<Int>) {
        var vertex = root
        for (center in seq.drop(1)) {
            if (center in vertex.children) {
                vertex = vertex.children[center]!!
            } else {
                vertex = Node(vertex, center)
                val vertexAsc = mutableListOf<Node>()
                vertexAsc.add(vertex.parent ?: root)
                for (i in 1..ceil(log2(depth.toDouble())).toInt())
                    vertexAsc.add(asc[vertexAsc[i - 1]]!![i - 1])
                asc[vertex] = vertexAsc
            }
        }
        corr[nodeIndex] = vertex
        val endpoint = vertex
        assert(endpoint.level == depth) { "Wrong leaf depth" }
    }

    fun dist(lhsIndex: Int, rhsIndex: Int): Int {
        if (lhsIndex < 0 || lhsIndex >= n || rhsIndex < 0 || rhsIndex >= n)
            throw IndexOutOfBoundsException("Incorrect vertex indices $lhsIndex, $rhsIndex")
        var lhs = corr.getValue(lhsIndex)
        var rhs = corr.getValue(rhsIndex)
        for (i in ceil(log2(depth.toDouble())).toInt() downTo 0) {
            if (asc[lhs]!![i] != asc[rhs]!![i]) {
                lhs = asc.getValue(lhs)[i]
                rhs = asc.getValue(rhs)[i]
            }
        }
        assert(lhs != rhs) { "LCA error for $lhsIndex and $rhsIndex" }
        lhs = lhs.parent!!
        rhs = rhs.parent!!
        assert(lhs == rhs) { "LCA error for $lhsIndex and $rhsIndex" }
        return 2.0.pow((depth - lhs.level + 2).toDouble()).toInt() - 4
    }

    fun calcSum(): Int {
        var acc = 0

        fun visit(v: Node): Int {
            var leaves = 0
            if (v.level == depth)
                return 1
            for (child in v.children.values) {
                val childLeaves = visit(child)
                leaves += childLeaves
                acc += childLeaves * (n - childLeaves) * 2.0.pow(depth - v.level).toInt()
            }
            return leaves
        }

        visit(root)
        return acc
    }

}
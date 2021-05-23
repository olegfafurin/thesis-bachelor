package ru.itmo.ctd.fafurin.spanner

/**
 * created by imd on 22.05.2021
 */


/**
 * Constructs a greedy spanner of a graph using a short cycles removal technique
 */
class GreedySpanner(val n: Int, val edges: Sequence<Pair<Int, Int>>) {

    /**
     * @param stretch upper bound on an edge stretch
     * @return adjacency list of a spanner graph
     */
    fun compute(stretch: Int): List<List<Int>> {
        val insertedEdges = MutableList(n) { mutableListOf<Int>() }
        for (edgeToInsert in edges) {
            val u = edgeToInsert.first
            val v = edgeToInsert.second
            val pathLength = bfs(n, u, insertedEdges)[v]
            if (pathLength > stretch) {
                insertedEdges[u].add(v)
                insertedEdges[v].add(u)
            }
        }
        return insertedEdges
    }
}
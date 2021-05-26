package ru.itmo.ctd.fafurin.spanner

/**
 * created by imd on 22.05.2021
 */


/**
 * Constructs a greedy spanner of a graph using a short cycles removal technique
 */
class GreedySpanner(val graph: Graph) {

    /**
     * @param stretch upper bound on an edge stretch
     * @return adjacency list of a spanner graph
     */
    fun compute(stretch: Int): Graph {
        val currentGraph = EdgeSetGraph(graph.n)
        for (edgeToInsert in graph.edgeList()) {
            val u = edgeToInsert.first
            val v = edgeToInsert.second
            val pathLength = bfs(u, currentGraph)[v]
            if (pathLength > stretch) {
                currentGraph.addEdge(Pair(u, v))
            }
        }
        return currentGraph
    }
}
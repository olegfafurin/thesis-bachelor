package ru.itmo.ctd.fafurin.spanner

/**
 * created by imd on 26.05.2021
 */

class AdjListGraph(override val n: Int) : Graph {

    private val adjList: MutableList<MutableList<Int>> = MutableList(n) { mutableListOf() }

    constructor(n: Int, adjList: List<List<Int>>) : this(n) {
        for (u in adjList.indices) {
            for (v in adjList[u]) {
                this.adjList[u].add(v)
            }
        }
    }

    override fun edgeList(): List<Pair<Int, Int>> {
        val edges = mutableListOf<Pair<Int, Int>>()
        for (u in adjList.indices) {
            for (v in adjList[u]) {
                if (u < v) {
                    edges.add(Pair(u, v))
                }
            }
        }
        edges.shuffle()
        return edges
    }

    override fun adjList(): List<List<Int>> = adjList

    override fun adjMatrix(): List<List<Int>> {
        val conn = MutableList(n) { MutableList(n) { 0 } }
        for (u in adjList.indices) {
            for (v in adjList[u]) {
                conn[u][v] = 1
            }
        }
        return conn
    }

    fun toEdgeSetGraph(): EdgeSetGraph = EdgeSetGraph(n, edgeList().toSet())
}
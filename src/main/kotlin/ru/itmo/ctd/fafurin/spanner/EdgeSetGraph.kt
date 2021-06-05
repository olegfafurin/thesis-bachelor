package ru.itmo.ctd.fafurin.spanner

import java.lang.Integer.max
import java.lang.Integer.min

/**
 * created by imd on 26.05.2021
 */

class EdgeSetGraph(override val n: Int) : Graph {

    private val edgeSet = mutableSetOf<Pair<Int, Int>>()

    constructor(n: Int, edgeSet: Set<Pair<Int, Int>>) : this(n) {
        for ((u, v) in edgeSet) {
            this.edgeSet.add(Pair(min(u, v), max(u, v)))
        }
    }

    override fun edgeList(): List<Pair<Int, Int>> = edgeSet.toList().shuffled()

    override fun adjList(): List<List<Int>> {
        val adj = MutableList(n) { mutableListOf<Int>() }
        for ((u, v) in edgeSet) {
            adj[u].add(v)
            adj[v].add(u)
        }
        return adj
    }

    override fun adjMatrix(): List<List<Int>> {
        val conn = MutableList(n) { MutableList(n) { 0 } }
        for ((u, v) in edgeSet) {
            conn[u][v] = 1
            conn[v][u] = 1
        }
        return conn
    }

    fun addEdge(e: Pair<Int, Int>) {
        val (u, v) = e
        if (Pair(u,v) !in edgeSet && Pair(v, u) !in edgeSet)
            edgeSet.add(e)
    }

    fun toAdjListGraph(): AdjListGraph = AdjListGraph(n, adjList())
}
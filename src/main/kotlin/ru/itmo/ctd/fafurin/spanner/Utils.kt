package ru.itmo.ctd.fafurin.spanner

import java.lang.Integer.min
import java.util.*
import kotlin.random.Random

/**
 * created by imd on 04.05.2021
 */


/**
 * Performs a binary search for a value on a sorted non-decreasing list
 *
 * @param threshold threshold for a search
 * @return index of a last element less than a threshold
 */
fun List<Int>.lowerBound(threshold: Double): Int {
    var l = 0
    var r = size - 1
    when {
        this[l] > threshold -> return -1
        this[r] <= threshold -> return r
    }
    while (l + 1 < r) {
        val m = (l + r) / 2
        if (this[m] < threshold)
            l = m
        else
            r = m
    }
    return l
}

/**
 * Produces a random graph in Erdos-Renyi model
 *
 * @param n number of vertices
 * @param deg average degree of a vertex
 * @return adjacency list
 */
fun makeErdosRenyiGraph(n: Int, deg: Int): Graph {
    val rand = Random(System.nanoTime())
    val p = deg.toDouble() / (n - 1)
    val adj = MutableList(n) { mutableListOf<Int>() }
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            if (rand.nextDouble() < p) {
                adj[i].add(j)
                adj[j].add(i)
            }
        }
    }
    return AdjListGraph(n, adj)
}


/**
 * @param graph adjacency list
 * @return distances matrix
 */
fun calcDistFloyd(graph: Graph): List<List<Int>> {
    val n = graph.n
    val neighbours = graph.adjList()
    val dist =
        MutableList(n) { i -> MutableList(n) { j -> if (i == j) 0 else if (j in neighbours[i]) 1 else Integer.MAX_VALUE / 2 } }
    val m = neighbours.sumOf { it.size } / 2
    for (k in 0 until n) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
            }
        }
    }
    for (i in 0 until n) {
        for (j in 0 until n) {
            assert(dist[i][j] < Integer.MAX_VALUE / 2) {
                "Graph is not connected: $j is not reachable from $i"
            }
        }
    }
    return dist
}


/**
 * @param graph adjacency list
 * @return distances matrix
 */
fun calcDistBFS(graph: Graph): List<List<Int>> {
    val dist = mutableListOf<List<Int>>()
    repeat(graph.n) {
        dist.add(bfs(it, graph))
    }
    return dist
}


/**
 * @return distances list
 */
fun bfs(v: Int, graph: Graph): List<Int> {
    val neighbours = graph.adjList()
    val d = MutableList(graph.n) { Integer.MAX_VALUE }
    val used = MutableList(graph.n) { false }
    d[v] = 0
    used[v] = true
    val queue = ArrayDeque<Int>()
    queue.add(v)
    while (queue.isNotEmpty()) {
        val u = queue.pop()
        for (vertex in neighbours[u]) {
            if (!used[vertex]) {
                used[vertex] = true
                d[vertex] = d[u] + 1
                queue.add(vertex)
            }
        }
    }
    return d
}


fun Graph.degrees(): List<Int> = adjList().map { it.size }


fun Graph.getComponents(): List<Graph> {

    val corr = MutableList(n) { -1 }
    val component = MutableList(n) { -1 }
    var currentComponent = 0
    var enumCounter = 0

    fun dfs(g: Graph, u: Int) {
        corr[u] = enumCounter++
        component[u] = currentComponent
        for (v in g.adjList()[u]) {
            if (component[v] == -1)
                dfs(g, v)
        }
    }

    for (i in 0 until n) {
        if (component[i] == -1) {
            dfs(this, i)
            currentComponent++
            enumCounter = 0
        }
    }

    val componentCount = MutableList(currentComponent) { 0 }
    for (i in 0 until n) {
        componentCount[component[i]]++
    }
    val components = MutableList(currentComponent) {
        MutableList(componentCount[it]) {
            mutableListOf<Int>()
        }
    }
    val adjList = adjList()
    for (u in 0 until n) {
        val comp = component[u]
        for (v in adjList[u]) {
            components[comp][corr[u]].add(corr[v])
        }
    }

    return components.mapIndexed { i, comp ->
        AdjListGraph(componentCount[i], comp)
    }
}
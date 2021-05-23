package ru.itmo.ctd.fafurin.spanner

import kotlin.math.log2
import kotlin.math.max

/**
 * created by imd on 22.05.2021
 */

fun main() {
    erdosRenyiExample(1000)
}

fun erdosRenyiExample(n: Int) {
    val graph = makeErdosRenyiGraph(n, n / 10)
    val approxDistTree = algo(n, graph)
    for(i in 0 until n) {
        for (j in 0 until n) {
            println("d'(i,j) = ${approxDistTree.dist(i, j)}")
        }
    }

}

fun algo(n: Int, e: List<List<Int>>): FRTTree {
    val edges = mutableListOf<Pair<Int, Int>>()
    for (u in e.indices) {
        for (v in e[u]) {
            if (u < v) edges.add(Pair(u, v))
        }
    }
    edges.shuffle()
    val stretch = max(2,(log2(n.toDouble())/ log2(log2(n.toDouble()))).toInt())
    val greedySpanner = GreedySpanner(n, edges.asSequence()).compute(stretch)
    val spannerDist = calcDistBFS(greedySpanner)
    return FRT(n, spannerDist).calc()
}
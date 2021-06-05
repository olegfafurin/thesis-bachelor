package ru.itmo.ctd.fafurin.spanner

import java.lang.Integer.min
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow
import kotlin.random.Random

/**
 * created by imd on 03.05.2021
 */

class FRT(val n: Int, val d: List<List<Int>>) {

    val rand = Random(System.nanoTime())

    /**
     * Random value taken from the distribution with a density function p = 1/(x * ln 2)
     */
    val beta: Double =
        2.0.pow(rand.nextDouble())

    /**
     * Random permutation of vertices
     */
    val pi = MutableList(n) { it }.apply {
        shuffle()
    }

    val piReversed = MutableList(n) { 0 }.apply {
        for (i in pi.indices)
            this[pi[i]] = i
    }

    /**
     * Diameter of the graph.
     * Requires the graph to be connected
     */
    val diameter = d.maxOf { it.maxOf { it } }

    /**
     * FRT decomposition tree depth
     */
    val depth = ceil(log2(diameter.toDouble())).toInt() + 1


    /**
     * Computes the cluster chain for a given vertex
     *
     * @param center vertex to process
     */
    fun makeChain(center: Int): List<Int> {
        val chain = mutableListOf<Int>()
        val distOrder =
            List(n) { d[center][it] }
                .mapIndexed { i, d -> Pair(d, piReversed[i]) }
                .sortedBy { it.first }
        val dist = distOrder.map { it.first }
        val minOnPrefix = MutableList(n) { n - 1 }
        minOnPrefix[0] = distOrder[0].second
        for (i in 1 until n) {
            minOnPrefix[i] = min(minOnPrefix[i - 1], distOrder[i].second)
        }
        for (deg in depth - 1 downTo -1) {
            val ind = dist.lowerBound(beta * 2.0.pow(deg))
            chain.add(pi[minOnPrefix[ind]])
        }
        assert(chain.last() == center)
        return chain
    }


    /**
     * Computes the hierarchical decomposition tree
     */
    fun calc(): FRTTree {
        return FRTTree(n, depth, beta, pi).apply {
            repeat(n) {
                addClusterCenterChain(it, makeChain(it))
            }
        }
    }
}
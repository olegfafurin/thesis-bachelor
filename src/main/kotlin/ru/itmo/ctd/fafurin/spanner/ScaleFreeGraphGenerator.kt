package ru.itmo.ctd.fafurin.spanner

import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.log
import kotlin.random.Random

/**
 * created by imd on 26.05.2021
 */

object ScaleFreeGraphGenerator {

    val rand = Random(System.nanoTime())

    /**
     * Generates n samples from the exponential distribution
     */
    fun randPowerLaw(n: Int, d: Double): List<Double> = MutableList(n) {
        (-log(1.0 - rand.nextDouble(), d)) % n
    }

    /**
     * Generates a random scale-free graph
     *
     * @param n number of vertices
     * @param d exponential decay base
     *
     * @return random graph with degrees distributed by power-law
     */
    fun makeGraph(n: Int, d: Double): Graph {
        val potential = randPowerLaw(n, d)
        val adj = MutableList(n) { mutableListOf<Int>() }
        val potSum = potential.sum()
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                if (rand.nextDouble() < potential[i] * potential[j] / potSum) {
                    adj[i].add(j)
                    adj[j].add(i)
                }
            }
        }
        return AdjListGraph(n, adj)
    }

}
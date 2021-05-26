package ru.itmo.ctd.fafurin.spanner

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import org.junit.Test
import java.io.File

/**
 * created by imd on 23.05.2021
 */

class GreedySpannerTest {

    val n = 100
    val randomGraph = makeErdosRenyiGraph(n, n / 10)
    val d = calcDistBFS(randomGraph)
    val edges = randomGraph.edgeList()
    val spanner = GreedySpanner(randomGraph)


    @Test
    fun greedySpannerDominatesTest() {
        val stretch = 5
        val approxDist = calcDistBFS(spanner.compute(stretch))
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                assert(approxDist[i][j] >= d[i][j])
            }
        }
    }

    @Test
    fun stretchUpperBoundTest() {
        val n = 100
        val randomGraph = makeErdosRenyiGraph(n, n / 10)
        val d = calcDistBFS(randomGraph)
        val spanner = GreedySpanner(randomGraph)
        val stretch = 4
        val approxDist = calcDistBFS(spanner.compute(stretch))
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                assert(approxDist[i][j] <= stretch * d[i][j])
            }
        }
    }
}


package ru.itmo.ctd.fafurin.spanner.evaluation

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import rand
import ru.itmo.ctd.fafurin.spanner.*
import java.io.File

/**
 * created by imd on 28.05.2021
 */

fun main() {
//    val n = 9000

//    for (d in listOf(2.1, 2.4, 2.7)) {
//        val outputFile = File("res/frt/neighbours_scale-free_${d.toString().replace('.', 'p')}.csv")
//        outputFile.writeText("n, orig, approx, max, diam, iter\n")
//        for (verticesCount in List(10) { ((it + 1) * 1000 * d).toInt() }) {
//            val graph = ScaleFreeGraphGenerator.makeGraph(verticesCount, d).getComponents().maxByOrNull { it.n }!!
//            val diam = calcDistBFS(graph).maxOf { it.maxOf { it } }
//            repeat(5) {
//                val iter = 20
//                val stats = evalNeighbours(graph, iter)
//                outputFile.appendText("${graph.n}, 1, ${stats.mean}, ${stats.max}, $diam, $iter\n")
//            }
//        }
//    }

    for (d in listOf(2.1, 2.4, 2.7)) {
        val verticesCount = (1000 * d * 8).toInt()
        val graph = ScaleFreeGraphGenerator.makeGraph(verticesCount, d).getComponents().maxByOrNull { it.n }!!
        val outputFile = File("res/frt/fixed_scale-free_${graph.n}_${d.toString().replace('.', 'p')}.csv")
        val dist = calcDistBFS(graph)

        outputFile.writeText("n, orig, approx_avg, distortion_avg, iter\n")
        repeat(5) {

            var u = rand.nextInt(graph.n)
            var v = rand.nextInt(graph.n)
            while (v == u || dist[u][v] != 4) {
                v = rand.nextInt(graph.n)
                u = rand.nextInt(graph.n)
            }
            val iter = 20
            val stats = evalFixed(graph, dist, u, v, iter)
            outputFile.appendText("${graph.n}, ${dist[u][v]}, ${stats.mean}, ${stats.mean / dist[u][v]} $iter\n")
        }
    }
}

fun evalNeighbours(graph: Graph, iterations: Int): DescriptiveStatistics {
    val (u, v) = graph.edgeList().random(rand)
    val stats = DescriptiveStatistics()

    val dist = calcDistBFS(graph)
    repeat(iterations) {
        stats.addValue(FRT(graph.n, dist).calc().dist(u, v).toDouble())
    }
    return stats
}

fun evalFixed(graph: Graph, dist: List<List<Int>>, u: Int, v: Int, iterations: Int): DescriptiveStatistics {
    val stats = DescriptiveStatistics()

    repeat(iterations) {
        stats.addValue(FRT(graph.n, dist).calc().dist(u, v).toDouble())
    }
    return stats
}
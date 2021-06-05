package ru.itmo.ctd.fafurin.spanner.evaluation

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import rand
import ru.itmo.ctd.fafurin.spanner.*
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * created by imd on 23.05.2021
 */

fun main(args: Array<String>) {
    val facebookVerticeCount = 4039
    val edges = mutableSetOf<Pair<Int, Int>>()
    for (line in File("data/facebook_combined.txt").readLines()) {
        val edge = line.split(" ").map { it.toInt() }
        edges.add(Pair(min(edge[0], edge[1]), max(edge[0], edge[1])))
    }
//    val randGraph = EdgeSetGraph(facebookVerticeCount, edges).toAdjListGraph().getComponents().maxByOrNull {
//            it.n
//        }!!
//    runNeighbours(facebookGraph, 1.0,"facebook")

//    val avg_deg = 25
//    val randGraph = makeErdosRenyiGraph(5000, avg_deg).getComponents().maxByOrNull {
//        it.n
//    }!!

//    evalAverageDistortion(randGraph, calcDistBFS(randGraph), "res/greedy/avg_facebook.csv")

    val n = 9000
    for (d in listOf(2.5)) {
        val randGraph = ScaleFreeGraphGenerator.makeGraph(12000, d).getComponents().maxByOrNull {
            it.n
        }!!

    evalAverageDistortion(randGraph,
        calcDistBFS(randGraph),
        "res/avg_greedy_scale-free_${randGraph.n}_${d.toString().replace('.', 'p')}.csv")


//        runFixed(randGraph, n, d, "greedy_scale-free")
//        runNeighbours(randGraph, d, "greedy_scale-free")
    }

}

fun runNeighbours(graph: Graph, d: Double, desc: String) {
    val neighboursFile = File("res/greedy/neighbours_${desc}_${graph.n}_${
        d.toString().replace('.', 'p')
    }.csv").apply {
        writeText("distortion_upper_bound, orig, approx, iter\n")
    }

    for (stretchBound in 2..4) {
        repeat(5) {
            evalNeighbours(graph, stretchBound, neighboursFile, 5)
        }
    }
}

fun runFixed(graph: Graph, d: Double, desc: String) {
    val fixedEdgeDistortionFile =
        File("res/fixed_${desc}_${graph.n}_${
            d.toString().replace('.', 'p')
        }.csv").apply {
            writeText("distortion_upper_bound, orig, approx, iter\n")
        }
    for (stretchBound in 2..20) {
        repeat(10) {
            val u = rand.nextInt(graph.n)
            var v = rand.nextInt(graph.n)
            while (v == u)
                v = rand.nextInt(graph.n)
            evalFixedEdgeDistortion(graph, stretchBound, u, v, bfs(u, graph)[v], fixedEdgeDistortionFile, 20)
        }
    }
}

fun evalAverageDistortion(graph: Graph, originalDistance: List<List<Int>>, filename: String) {
    val greedySpanner = GreedySpanner(graph)
    val diam = originalDistance.maxOf { it.maxOf { it } }
    val outputFile = File(filename)
    outputFile.writeText("stretch_upper_bound, avg_stretch, max_stretch\n")
    for (i in 2..20) {
        val approxDist = calcDistBFS(greedySpanner.compute(i))
        val stats = DescriptiveStatistics()
        for (u in 0 until graph.n) {
            for (v in u + 1 until graph.n) {
                stats.addValue(approxDist[u][v].toDouble() / originalDistance[u][v])
            }
        }
        outputFile.appendText("$i, ${stats.mean}, ${stats.max}\n")
    }
}

fun evalNeighbours(graph: Graph, stretchUpperBound: Int, outputFile: File, iterations: Int) {
    val greedySpanner = GreedySpanner(graph)
    val stats = DescriptiveStatistics()

    val (u, v) = graph.edgeList().random(rand)

    repeat(iterations) {
        val approxDist = calcDistBFS(greedySpanner.compute(stretchUpperBound))
        stats.addValue(approxDist[u][v].toDouble())
    }
    outputFile.appendText("$stretchUpperBound, 1, ${stats.mean}, $iterations\n")
}

fun evalFixedEdgeDistortion(
    graph: Graph,
    stretchUpperBound: Int,
    u: Int,
    v: Int,
    originalDistance: Int,
    outputFile: File,
    iterations: Int,
) {
    val greedySpanner = GreedySpanner(graph)
    val stats = DescriptiveStatistics()

    repeat(iterations) {
        val approxDist = calcDistBFS(greedySpanner.compute(stretchUpperBound))
        stats.addValue(approxDist[u][v].toDouble() / originalDistance)
    }

    outputFile.appendText("$stretchUpperBound, $originalDistance, ${stats.mean}, $iterations\n")
}
package ru.itmo.ctd.fafurin.spanner.evaluation

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import ru.itmo.ctd.fafurin.spanner.*
import java.io.File

/**
 * created by imd on 23.05.2021
 */

fun main(args: Array<String>) {
//    val n = 4039
//    val adjFacebook = MutableList(n) { mutableListOf<Int>() }
//    val edges = mutableListOf<Pair<Int, Int>>()
//    for (line in File("data/facebook_combined.txt").readLines()) {
//        val edge = line.split(" ").map { it.toInt() }
//        adjFacebook[edge[0]].add(edge[1])
//        adjFacebook[edge[1]].add(edge[0])
//        edges.add(Pair(edge[0], edge[1]))
//    }

//    eval(n, edges, calcDistBFS(adjFacebook),"res/avg_greedy_facebook.csv")
//    val avg_deg = 30
//    val randGraph = makeErdosRenyiGraph(10000, avg_deg)
    val randGraph = getComponents(ScaleFreeGraphGenerator.makeGraph(5000, 2.5)).sortedByDescending {
        it.n
    }.first()
    eval(randGraph, calcDistBFS(randGraph), "res/avg_greedy_scale-free_5000_2p5.csv")
//    eval(10000, randGraph.edgeList(), calcDistBFS(randGraph), "res/avg_greedy_erdos-renyi_1000_$avg_deg.csv")

}

fun eval(graph: Graph, originalDistance: List<List<Int>>, filename: String) {

    val greedySpanner = GreedySpanner(graph)
    val diam = originalDistance.maxOf { it.maxOf { it } }
    val outputFile = File(filename)
    outputFile.writeText("stretch_upper_bound, avg_stretch, max_stretch\n")
    for (i in 2..diam * 2) {
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
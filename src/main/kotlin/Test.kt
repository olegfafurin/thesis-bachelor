import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import ru.itmo.ctd.fafurin.spanner.*
import java.io.File
import kotlin.math.abs
import kotlin.math.pow
import kotlin.random.Random

/**
 * created by imd on 04.05.2021
 */

val rand = Random(System.nanoTime())

fun main() {
//    testDistMethods(ru.itmo.ctd.fafurin.spanner.makeErdos(100,8))
    testFacebook()
}

fun FRTTree.calcSum(): Long {
    var acc = 0L

    fun visit(v: FRTTree.Node): Long {
        var leaves = 0L
        if (v.level == depth)
            return 1
        for (child in v.children.values) {
            val childLeaves = visit(child)
            leaves += childLeaves
            acc += childLeaves * (n - childLeaves) * 2.0.pow(depth - v.level).toInt()
        }
        return leaves
    }

    visit(root)
    return acc
}


fun testFacebook() {
    val n = 4039
    val e = MutableList(n) { mutableListOf<Int>() }
    for (line in File("data/facebook_combined.txt").readLines()) {
        val edge = line.split(" ").map { it.toInt() }
        e[edge[0]].add(edge[1])
        e[edge[1]].add(edge[0])
    }
    val d = calcDistBFS(e)
    for (i in 0 until 100) {
        val u = rand.nextInt(n)
        var v = rand.nextInt(n)
        while (v == u)
            v = rand.nextInt(n)
        test(d, u, v, "facebook", 20)
    }
}

fun testErdos() {
    for (size in 12..1000) {
        val graph = calcDistFloyd(makeErdosRenyiGraph(size, 10))
        testSum(graph, "erdos: n=$size")
    }
}

fun testFloyd(e: List<List<Int>>, lhsIndex: Int, rhsIndex: Int, graphDesc: String) {
    val d = calcDistFloyd(e)
    test(d, lhsIndex, rhsIndex, "Floyd on $graphDesc")
}

fun testCycleManual(n: Int, lhsIndex: Int, rhsIndex: Int) {
    val d = MutableList(n) { i -> MutableList(n) { j -> abs(j - i) % n } }
    test(d, lhsIndex, rhsIndex, "Manually built cycle: n=$n")
}

fun testChainManual(n: Int, lhsIndex: Int, rhsIndex: Int) {
    val d = MutableList(n) { i -> MutableList(n) { j -> abs(j - i) } }
    test(d, lhsIndex, rhsIndex, "Manually built chain: n=$n")
}

fun test(d: List<List<Int>>, lhsIndex: Int, rhsIndex: Int, desc: String, times: Int = 1000) {
    val stats = DescriptiveStatistics()
    println(desc)
    repeat(times) {
        val tree = FRT(d.size, d).calc()
        val dist = tree.dist(lhsIndex, rhsIndex)
        stats.addValue(dist.toDouble())
    }
    println("________")
    println("initial: d($lhsIndex, $rhsIndex) = ${d[lhsIndex][rhsIndex]}, diameter = ${d.maxOf { it.maxOf { it } }}")
    println("avg: ${stats.mean}, std: ${stats.standardDeviation}, min: ${stats.min}, max: ${stats.max}\n")
}

fun testSum(d: List<List<Int>>, desc: String) {
    val stats = DescriptiveStatistics()
    repeat(20) {
        val tree = FRT(d.size, d).calc()
        val sumDist = d.sumOf { it.sumOf { it } } / 2
        val sumDistApproximate = tree.calcSum()
        stats.addValue(sumDistApproximate.toDouble() / sumDist)
        println("n: ${d.size}, real sum: $sumDist, approximate sum: $sumDistApproximate, ratio=${sumDistApproximate.toDouble() / sumDist}")
    }
    File("sum_erdos").appendText("${d.size} ${"%.2f".format(stats.mean)} ${"%.2f".format(stats.standardDeviation)}\n")
}
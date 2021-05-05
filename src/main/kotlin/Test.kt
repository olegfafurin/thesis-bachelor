import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import java.io.File
import java.lang.Integer.min
import kotlin.math.abs

/**
 * created by imd on 04.05.2021
 */

fun main() {
//    testCycleManual(100, 4, 11)
//    testChainManual(10, 3, 9)
//    testFloyd(makeErdos(100, 10), 0, 1, "erdos with n=100, deg=10")
//    testFloyd(makeErdos(100, 10), 0, 10, "erdos with n=100, deg=10")
//    testFloyd(makeErdos(100, 10), 7, 84, "erdos with n=100, deg=10")
}

fun testErdos() {
    for (size in 12..1000) {
        val graph = calcDist(makeErdos(size, 10))
        testSum(graph, "erdos: n=$size")
    }
}

fun testFloyd(e: List<List<Int>>, lhsIndex: Int, rhsIndex: Int, graphDesc: String) {
    val d = calcDist(e)
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
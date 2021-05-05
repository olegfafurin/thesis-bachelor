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
    val beta: Double =
        2.0.pow(rand.nextDouble()) // generate beta from the distribution with density function p = 1/(x * ln 2)
    val pi = MutableList(n) { it }.apply {
        shuffle()
    }

    //    val pi = mutableListOf(0,1,3,2)
    val piReversed = MutableList(n) { 0 }.apply {
        for (i in pi.indices)
            this[pi[i]] = i
    }
    val diameter = d.maxOf { it.maxOf { it } } // requires a graph to be connected
    val depth = ceil(log2(diameter.toDouble())).toInt() + 1

    fun calcChain(center: Int): List<Int> {
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

    fun calc(): FRTTree {
        val tree = FRTTree(n, depth, beta, pi)
        repeat(n) {
            tree.addCenterSequence(it, calcChain(it))
        }
        return tree
    }
}
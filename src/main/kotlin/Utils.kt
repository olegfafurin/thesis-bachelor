import java.lang.Integer.min
import kotlin.random.Random

/**
 * created by imd on 04.05.2021
 */

fun List<Int>.lowerBound(threshold: Double): Int {
    var l = 0
    var r = size - 1
    when {
        this[l] > threshold -> return -1
        this[r] <= threshold -> return r
    }
    while (l + 1 < r) {
        val m = (l + r) / 2
        if (this[m] < threshold)
            l = m
        else
            r = m
    }
    return l
}

/**
 * @param n number of vertices
 * @param deg average degree
 * @return adjacency list
 */
fun makeErdos(n: Int, deg: Int): List<List<Int>> {
    val rand = Random(System.nanoTime())
    val p = deg.toDouble() / (n - 1)
    val e = MutableList(n) { mutableListOf<Int>() }
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            if (rand.nextDouble() < p) {
                e[i].add(j)
                e[j].add(i)
            }
        }
    }
    return e
}

//fun diskstra(v: Int, e: List<List<Int>>): List<Int> {
//
//}


/**
 * @param e adjacency list
 * @return distances matrix
 */
fun calcDist(e: List<List<Int>>): List<List<Int>> {
    val n = e.size
    val dist =
        MutableList(n) { i -> MutableList(n) { j -> if (i == j) 0 else if (j in e[i]) 1 else Integer.MAX_VALUE / 2 } }
    val m = e.sumOf { it.size } / 2
    for (k in 0 until n) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
            }
        }
    }
    for (i in 0 until n) {
        for (j in 0 until n) {
            assert(dist[i][j] < Integer.MAX_VALUE / 2) {
                "Graph is not connected: $j is not reachable from $i"
            }
        }
    }
    return dist
}
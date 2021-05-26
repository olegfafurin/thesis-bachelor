package ru.itmo.ctd.fafurin.spanner

import org.junit.Test
import ru.itmo.ctd.fafurin.spanner.calcDistBFS
import ru.itmo.ctd.fafurin.spanner.calcDistFloyd
import kotlin.math.exp
import kotlin.test.assertEquals

/**
 * created by imd on 21.05.2021
 */

class DistanceTest {

    @Test
    fun bfsTest() {
        val adj = listOf(
            listOf(1, 3, 4),
            listOf(0, 5, 6),
            listOf(6),
            listOf(0, 5),
            listOf(0),
            listOf(1, 3),
            listOf(1, 2)
        )
        val d: List<List<Int>> = calcDistBFS(AdjListGraph(7, adj))
        val expectedDist = listOf(
            listOf(0, 1, 3, 1, 1, 2, 2),
            listOf(1, 0, 2, 2, 2, 1, 1),
            listOf(3, 2, 0, 4, 4, 3, 1),
            listOf(1, 2, 4, 0, 2, 1, 3),
            listOf(1, 2, 4, 2, 0, 3, 3),
            listOf(2, 1, 3, 1, 3, 0, 2),
            listOf(2, 1, 1, 3, 3, 2, 0)
        )
        assertEquals(d, expectedDist)
    }

    @Test
    fun floydTest() {
        val adj = listOf(
            listOf(1, 2, 3),
            listOf(0),
            listOf(0),
            listOf(0, 4),
            listOf(3)
        )
        val d: List<List<Int>> = calcDistFloyd(AdjListGraph(5, adj))
        val expectedDist = listOf(
            listOf(0, 1, 1, 1, 2),
            listOf(1, 0, 2, 2, 3),
            listOf(1, 2, 0, 2, 3),
            listOf(1, 2, 2, 0, 1),
            listOf(2, 3, 3, 1, 0)
        )
        assertEquals(d, expectedDist)
    }
}
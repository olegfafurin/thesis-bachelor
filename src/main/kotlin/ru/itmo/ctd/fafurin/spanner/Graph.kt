package ru.itmo.ctd.fafurin.spanner

/**
 * created by imd on 26.05.2021
 */

interface Graph {

    val n: Int

    fun edgeList(): List<Pair<Int, Int>>

    fun adjList(): List<List<Int>>

    fun adjMatrix(): List<List<Int>>

}
package com.ba.ex.mvvmsample.extensions

/**
 * 示例扩展函数
 */
fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // “this”对应该列表
    this[index1] = this[index2]
    this[index2] = tmp
}
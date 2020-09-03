package com.ba.ex.mvvmsample.repository.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 水果类
 */


@Entity(tableName = "fruit")
data class Fruit(
    @PrimaryKey val id: String,
    val name: String, // 名字
    val description: String, //描述
    val imageUrl: String = "" //封面地址
)
package com.ba.ex.mvvmsample.models

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.ba.ex.mvvmsample.models.data.Fruit
import com.ba.ex.mvvmsample.models.database.AppDatabase
import com.ba.ex.mvvmsample.models.database.FruitDao
import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class LikeFruitsRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var fruitDao: FruitDao
    private lateinit var likeFruitsRepository: LikeFruitsRepository
    private val fruitA = Fruit("1", "A", "fruit A", "")
    private val fruitB = Fruit("2", "B", "fruit B", "")
    private val fruitC = Fruit("3", "C", "fruit C", "")

    @Before
    fun createDb() = runBlocking {
        //获取数据库
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        fruitDao = database.fruitDao()

        fruitDao.insert(listOf(fruitA, fruitB, fruitC))
        likeFruitsRepository = LikeFruitsRepository.getInstance(fruitDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun load() {
        val list = mutableListOf<Fruit>()
        list.add(likeFruitsRepository.getFruit(fruitA.id))
        list.add(likeFruitsRepository.getFruit(fruitB.id))
        list.add(likeFruitsRepository.getFruit(fruitC.id))

        Logger.d("LikeFruitsRepositoryTest : load >>> fruits = $list")

        assertEquals(list[0] , fruitA)
        assertEquals(list[1] , fruitB)
        assertEquals(list[2] , fruitC)
    }
}
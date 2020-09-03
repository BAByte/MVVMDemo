package com.ba.ex.mvvmsample

import android.view.KeyEvent.KEYCODE_BACK
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.ba.ex.mvvmsample.adapters.FruitListAdapter
import com.ba.ex.mvvmsample.adapters.LikeFruitsAdapter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test


class MainActivityTest {
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun selectFruit() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // When the "Add Plant" button is clicked
        val completableDeferred = CompletableDeferred<Unit>()
        delay(2000)
        select(2)

        openBigPic()
        like()

        select(4)
        openBigPic()
        like()

        select(6)
        openBigPic()
        like()


        select(8)
        openBigPic()
        like()

        delay(1000)
        //打开menu
        openContextualActionModeOverflowMenu()
        //模拟点击菜单收藏
        onView(withText(context.getString(R.string.menu_like)))
            .perform(click())

        delay(1000)
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<LikeFruitsAdapter.ViewHolder>(
                    0,
                    click()
                )
            )

        openBigPic()

        //返回
        delay(2000)
        InstrumentationRegistry.getInstrumentation().sendKeyDownUpSync(KEYCODE_BACK)

        delay(1000)
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<LikeFruitsAdapter.ViewHolder>(
                    1,
                    longClick()
                )
            )

        delay(1000)
        onView(withText(context.getString(R.string.dialog_ok)))
            .perform(
                click()
            )

        //返回
        delay(2000)
        InstrumentationRegistry.getInstrumentation().sendKeyDownUpSync(KEYCODE_BACK)

        completableDeferred.await()
    }

    private suspend fun select(position: Int) {
        delay(1000)
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<FruitListAdapter.ViewHolder>(
                    position,
                    click()
                )
            )
    }

    private suspend fun openBigPic() {
        delay(1000)
        onView(withId(R.id.fruit_pic)).perform(click())
        //返回
        delay(1000)
        InstrumentationRegistry.getInstrumentation().sendKeyDownUpSync(KEYCODE_BACK)
    }

    private suspend fun like() {
        delay(1000)
        onView(withId(R.id.fab_like))
            .perform(click())
    }
}
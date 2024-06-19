package com.example.mymultiplayer

import com.example.mymultiplayer.view.PlayerFragment
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlayerFragmentTest {
    @Test
    fun calculateNextIndex() {
        val playerFragment = PlayerFragment()
        //Case 1: index is not higher than size.
        var expected = 4
        var index = 4
        var size = 5
        assertEquals(expected, playerFragment.calculateNextIndex(index, size))

        //Case 2: index is higher than size.
        expected = 4
        index = 6
        size = 5
        assertEquals(expected, playerFragment.calculateNextIndex(index, size))

        //Case 3: index is lower than -1.
        expected = 4
        index = -2
        size = 5
        assertEquals(expected, playerFragment.calculateNextIndex(index, size))
    }
}
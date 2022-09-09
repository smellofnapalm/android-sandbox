package com.example.criminalintent

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.Companion.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckedBoxTest {
    private lateinit var scenario: FragmentScenario<CrimeDetailFragment>

    @Before
    fun setUp() {
        scenario = launch(CrimeDetailFragment::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun checkBoxTest() {
        onView(withId(R.id.crime_solved)).perform(click())
        onView(withId(R.id.crime_solved)).check(matches())
    }
}
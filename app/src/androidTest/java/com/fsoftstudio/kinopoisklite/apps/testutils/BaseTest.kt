package com.fsoftstudio.kinopoisklite.apps.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import com.fsoftstudio.kinopoisklite.apps.testutils.rules.FakeImageLoaderRule
import com.fsoftstudio.kinopoisklite.data.repository.DataRepositoryImp
import com.fsoftstudio.kinopoisklite.apps.testutils.rules.TestViewModelScopeRule
import javax.inject.Inject

/**
 * Base class for all UI tests.
 */
open class BaseTest {

    @get:Rule
    val testViewModelScopeRule = TestViewModelScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val fakeImageLoaderRule = FakeImageLoaderRule()

    @Inject
    lateinit var dataRepositoryImp: DataRepositoryImp


    @Before
    open fun setUp() {
        hiltRule.inject()
    }

}
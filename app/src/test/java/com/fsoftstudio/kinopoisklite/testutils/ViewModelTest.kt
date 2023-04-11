package com.fsoftstudio.kinopoisklite.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fsoftstudio.kinopoisklite.data.repository.DataRepositoryImp
import com.fsoftstudio.kinopoisklite.testutils.rules.TestViewModelScopeRule
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.junit.Rule

open class ViewModelTest {

    @get:Rule
    val testViewModelScopeRule = TestViewModelScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var dataRepositoryImp: DataRepositoryImp

}
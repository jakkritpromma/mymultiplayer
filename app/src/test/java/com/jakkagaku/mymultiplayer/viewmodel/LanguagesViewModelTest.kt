package com.jakkagaku.mymultiplayer.viewmodel

import com.google.gson.JsonObject
import com.jakkagaku.mymultiplayer.repository.LanguageRepository
import com.jakkagaku.mymultiplayer.retrofit.ApiService
import com.jakkagaku.mymultiplayer.retrofit.LanguageApiResult
import com.jakkagaku.mymultiplayer.model.LanguageModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*

import org.junit.jupiter.api.*
import org.mockito.Mockito.*
import retrofit2.Response
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LanguagesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: LanguageRepository
    private lateinit var apiService: ApiService

    @BeforeEach
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        apiService = mock(ApiService::class.java)

        val flagsJson = JsonObject().apply { addProperty("svg", "https://flagcdn.com/gb.svg") }
        val nameJson = JsonObject().apply { addProperty("common", "English") }
        val languagesJson = JsonObject().apply { addProperty("eng", "English") }

        val mockLanguages = listOf(
            LanguageModel(flags = flagsJson, name = nameJson, languages = languagesJson)
        )
        
        `when`(apiService.getLanguages()).thenReturn(Response.success(mockLanguages))

        repository = LanguageRepository(apiService)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_fetchLanguages_emitsSuccess() = runTest(testDispatcher) {
        val viewModel = LanguagesViewModel(repository)

        viewModel.fetchLanguages()
        advanceUntilIdle() // Let all coroutines complete

        val result = viewModel.languageState.first { it is LanguageApiResult.Success }

        assertTrue(result is LanguageApiResult.Success)
    }
}

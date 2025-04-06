package com.jakkagaku.mymultiplayer.repository

import com.google.gson.JsonObject
import com.jakkagaku.mymultiplayer.model.LanguageModel
import com.jakkagaku.mymultiplayer.retrofit.ApiService
import com.jakkagaku.mymultiplayer.retrofit.LanguageApiResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.test.Test

@ExperimentalCoroutinesApi
class LanguageRepositoryTest {

    private lateinit var repository: LanguageRepository
    private val apiService: ApiService = mock()

    @BeforeEach
    fun setup() {
        repository = LanguageRepository(apiService)
    }

    private fun createMockLanguageModel(): LanguageModel {
        val flagsJson = JsonObject().apply { addProperty("svg", "https://flagcdn.com/gb.svg") }
        val nameJson = JsonObject().apply { addProperty("common", "English") }
        val languagesJson = JsonObject().apply { addProperty("eng", "English") }

        return LanguageModel(flags = flagsJson, name = nameJson, languages = languagesJson)
    }

    @Test
    fun `getLanguages returns Success when response is successful`() = runTest {
        val mockLanguages = listOf(createMockLanguageModel())
        val response = Response.success(mockLanguages)

        whenever(apiService.getLanguages()).thenReturn(response)

        val result = repository.getLanguages()

        assertTrue(result is LanguageApiResult.Success)
        assertEquals(mockLanguages, (result as LanguageApiResult.Success).data)
    }

    @Test
    fun `getLanguages returns Error when response is unsuccessful`() = runTest {
        val errorResponse = Response.error<List<LanguageModel>>(400, "Bad Request".toResponseBody())

        whenever(apiService.getLanguages()).thenReturn(errorResponse)

        val result = repository.getLanguages()

        assertTrue(result is LanguageApiResult.Error)
        assertEquals("Bad Request", (result as LanguageApiResult.Error).message)
    }

    @Test
    fun `getLanguages returns Error when exception is thrown`() = runTest {
        whenever(apiService.getLanguages()).thenThrow(RuntimeException("Network error"))

        val result = repository.getLanguages()

        assertTrue(result is LanguageApiResult.Error)
        assertEquals("Network error", (result as LanguageApiResult.Error).message)
    }
}


package io.xorum.codeforceswatcher.network

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.xorum.codeforceswatcher.network.responses.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json.Companion.nonstrict

private const val CODEFORCES_API_LINK = "www.codeforces.com/api"

internal class CodeforcesRepository {

    private val codeforcesApiClient = makeCodeforcesApiClient()

    suspend fun getUsers(handles: String, lang: String) = try {
        codeforcesApiClient.get<UsersResponse>(path = "user.info") {
            parameter("handles", handles)
            parameter("lang", lang)
        }
    } catch (t: Throwable) {
        null
    }

    suspend fun getRating(handle: String, lang: String) = try {
        codeforcesApiClient.get<RatingChangeResponse>(path = "user.rating") {
            parameter("handle", handle)
            parameter("lang", lang)
        }
    } catch (t: Throwable) {
        null
    }

    suspend fun getActions(maxCount: Int = 100, lang: String) = try {
        codeforcesApiClient.get<ActionsResponse>(path = "recentActions") {
            parameter("maxCount", maxCount)
            parameter("lang", lang)
        }
    } catch (t: Throwable) {
        null
    }

    suspend fun getCodeforcesContests() = try {
        codeforcesApiClient.get<CodeforcesContestsResponse>(path = "contest.list")
    } catch (t: Throwable) {
        null
    }

    suspend fun getProblems(lang: String) = try {
        codeforcesApiClient.get<ProblemsResponse>(path = "problemset.problems") {
            parameter("lang", lang)
        }
    } catch (t: Throwable) {
        null
    }

    @UseExperimental(UnstableDefault::class)
    private fun makeCodeforcesApiClient(): HttpClient = HttpClient {
        expectSuccess = false
        defaultRequest {
            url {
                host = CODEFORCES_API_LINK
                protocol = URLProtocol.HTTPS
            }
        }
        Json {
            serializer = KotlinxSerializer(json = nonstrict)
        }
        Logging {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
}

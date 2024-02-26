package com.prosecshane.quoteapp.data.remote

import com.prosecshane.quoteapp.domain.model.RequestBody
import com.prosecshane.quoteapp.domain.model.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * This interface allows to make an API call.
 * Instance of this class is created automatically by Retrofit2.
 */
interface QuoteApi {
    /**
     * Makes a POST API call to the "completions" end-point. Uses the constants and requires a body.
     *
     * @param requestBody The body of the request, containing all the information for a
     * POST call.
     * @return Response of the call in form of a Response body data class instance.
     */
    @Headers(QuoteApiConstants.AcceptHeader, QuoteApiConstants.ContentTypeHeader)
    @POST("completions")
    suspend fun generateQuote(
        @Body requestBody: RequestBody
    ): Response<ResponseBody>
}

package com.vaibhav.sociofy.data.remote

import com.vaibhav.sociofy.data.models.remote.PushNotification
import com.vaibhav.sociofy.util.FcmUtils.API_KEY
import com.vaibhav.sociofy.util.FcmUtils.CONTENT_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {

    @Headers("Authorization: key=$API_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}
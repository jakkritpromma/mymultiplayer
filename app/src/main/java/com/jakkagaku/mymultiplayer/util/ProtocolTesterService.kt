package com.jakkagaku.mymultiplayer.util

import retrofit2.Response
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProtocolTesterService {

    @PUT("blade/bind/{phoneaddr}")
    suspend fun bindPhone(@Path("phoneaddr") phoneAddr: String):Response<Unit>

    @PUT("blade/unbind/{phoneaddr}")
    suspend fun unbindPhone(@Path("phoneaddr") phoneAddr: String):Response<Unit>

}
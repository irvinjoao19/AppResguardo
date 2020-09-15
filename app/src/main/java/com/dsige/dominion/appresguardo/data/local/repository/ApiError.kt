package com.dsige.dominion.appresguardo.data.local.repository

import com.itfperu.appitf.helper.MessageError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

class ApiError(retrofit: Retrofit) {
    val errorConverter: Converter<ResponseBody, MessageError> =
        retrofit.responseBodyConverter(MessageError::class.java, arrayOfNulls<Annotation>(0))
}

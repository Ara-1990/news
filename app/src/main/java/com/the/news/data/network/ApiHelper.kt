package com.the.news.data.network

import com.the.news.data.network.mappers.ResponseMapper
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber


fun <RESPONSE, OUTPUT> getMappedResponse(
    response: Response<RESPONSE>,
    responseMapper: ResponseMapper<RESPONSE, OUTPUT?>
): Request<OUTPUT> {
    if (response.isSuccessful) {
        val output = response.body()?.let { responseMapper.modelFromResponse(it) }
        return if (output != null) {
            Request.Success.Network(output)
        }
        else {
            Request.Error.EmptyResponse
        }
    } else {
        val code = response.code()
        if (code in 400..500) {
            getErrorObj(response)?.let { obj ->
                val message = getErrorMessage(obj)
                message?.let { return Request.Error.Other(it, code) }
            }
        }
        return Request.Error.ResponseNotSucceed
    }
}

fun getErrorMessage(jsonObject: JSONObject): String? {
    return try {
        jsonObject.getString("detail")
    } catch (ex: Exception) {
        Timber.d("request error: %s", ex.message)
        null
    }
}

fun getErrorObj(response: Response<*>): JSONObject? {
    return try {
        response.errorBody()?.string()?.let { JSONObject(it) }
    } catch (ex: Exception) {
        Timber.d("request error: %s", ex.message)
        null
    }
}
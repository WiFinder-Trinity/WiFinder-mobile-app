package fr.wifinder.wifinderandroid.infrastructure

import android.widget.Toast
import fr.wifinder.wifinderandroid.activities.HotspotsListActivity
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.logging.Logger


open class ApiClient(val baseUrl: String, val activity: HotspotsListActivity) {
    companion object {
        protected val ContentType = "Content-Type"
        protected val Accept = "Accept"
        protected val JsonMediaType = "application/json"
        protected val FormDataMediaType = "multipart/form-data"
        protected val XmlMediaType = "application/xml"

        protected val logger = Logger.getLogger(ApiClient::class.java.name)

        @JvmStatic
        val client: OkHttpClient = OkHttpClient()

        @JvmStatic
        var defaultHeaders: Map<String, String> by ApplicationDelegates.setOnce(mapOf(ContentType to JsonMediaType, Accept to "*/*"))

        @JvmStatic
        val jsonHeaders: Map<String, String> = mapOf(ContentType to JsonMediaType, Accept to JsonMediaType)
    }

    protected inline fun <reified T> requestBody(content: T, mediaType: String = JsonMediaType): RequestBody {
        when {
            content is File -> return RequestBody.create(
                    MediaType.parse(mediaType), content
            )
            mediaType == FormDataMediaType -> {
                var builder = FormBody.Builder()
                // content's type *must* be Map<String, Any>
                @Suppress("UNCHECKED_CAST")
                (content as Map<String, String>).forEach { key, value ->
                    builder = builder.add(key, value)
                }
                return builder.build()
            }
            mediaType == JsonMediaType -> return RequestBody.create(
                    MediaType.parse(mediaType), Serializer.moshi.adapter(T::class.java).toJson(content)
            )
            mediaType == XmlMediaType -> TODO("xml not currently supported.")
        }

        // TODO: this should be extended with other serializers

        // TODO: this should be extended with other serializers
        TODO("requestBody currently only supports JSON body and File body.")
    }

    protected inline fun <reified T : Any?> responseBody(body: ResponseBody?, mediaType: String = JsonMediaType): T? {
        if (body == null) return null
        return when (mediaType) {
            JsonMediaType -> Serializer.moshi.adapter(T::class.java).fromJson(body.source())
            else -> null
        }
    }

    protected inline fun <reified T : Any?> request(requestConfig: RequestConfig, body: Any? = null): ApiInfrastructureResponse<T?>? {
        val httpUrl = HttpUrl.parse(baseUrl) ?: throw IllegalStateException("baseUrl is invalid.")

        var urlBuilder = httpUrl.newBuilder()
                .addPathSegments(requestConfig.path.trimStart('/'))

        requestConfig.query.forEach { query ->
            query.value.forEach { queryValue ->
                urlBuilder = urlBuilder.addQueryParameter(query.key, queryValue)
            }
        }

        val url = urlBuilder.build()
        val headers = requestConfig.headers + defaultHeaders

        if (headers[ContentType] ?: "" == "") {
            throw kotlin.IllegalStateException("Missing Content-Type header. This is required.")
        }

        if (headers[Accept] ?: "" == "") {
            throw kotlin.IllegalStateException("Missing Accept header. This is required.")
        }

        // TODO: support multiple contentType,accept options here.
        val contentType = (headers[ContentType] as String).substringBefore(";").toLowerCase()
        val accept = (headers[Accept] as String).substringBefore(";").toLowerCase()

        var request: Request.Builder = when (requestConfig.method) {
            RequestMethod.DELETE -> Request.Builder().url(url).delete()
            RequestMethod.GET -> Request.Builder().url(url)
            RequestMethod.HEAD -> Request.Builder().url(url).head()
            RequestMethod.PATCH -> Request.Builder().url(url).patch(requestBody(body!!, contentType))
            RequestMethod.PUT -> Request.Builder().url(url).put(requestBody(body!!, contentType))
            RequestMethod.POST -> Request.Builder().url(url).post(requestBody(body!!, contentType))
            RequestMethod.OPTIONS -> Request.Builder().url(url).method("OPTIONS", null)
        }

        headers.forEach { header -> request = request.addHeader(header.key, header.value) }

        val realRequest = request.build()

        logger.info(realRequest.toString())

        client.newCall(realRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    logger.info("Request is successful")
                    val bodyString = response.body()!!.string()
                    logger.info(bodyString)
                    notifyResponse(bodyString)
                } else
                    logger.info("request failed :(")
            }
        })

        return null

//        val response = client.newCall(realRequest).execute()
//
//        // TODO: handle specific mapping types. e.g. Map<int, Class<?>>
//        when {
//            response.isRedirect -> return Redirection(
//                    response.code(),
//                    response.headers().toMultimap()
//            )
//            response.isInformational -> return Informational(
//                    response.message(),
//                    response.code(),
//                    response.headers().toMultimap()
//            )
//            response.isSuccessful -> return Success(
//                    responseBody(response.body(), accept),
//                    response.code(),
//                    response.headers().toMultimap()
//            )
//            response.isClientError -> return ClientError(
//                    response.body()?.string(),
//                    response.code(),
//                    response.headers().toMultimap()
//            )
//            else -> return ServerError(
//                    null,
//                    response.body()?.string(),
//                    response.code(),
//                    response.headers().toMultimap()
//            )
//        }
    }

    protected fun notifyResponse(response: String?) {
        activity.runOnUiThread { Toast.makeText(activity.baseContext, "SUCCESS ! $response", Toast.LENGTH_LONG).show() }
    }
}
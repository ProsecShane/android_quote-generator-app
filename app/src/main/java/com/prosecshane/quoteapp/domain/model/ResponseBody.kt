package com.prosecshane.quoteapp.domain.model

/**
 * Response for API calls. Contains different versions of outputs.
 * By default the app only requests one version.
 *
 * @param choices A list of objects that represent the different versions of outputs.
 */
data class ResponseBody(
    val choices: List<ResponseChoice>,
)

package com.foryouandme.core.data.api.common.response

import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource


@JsonApi(type = "default")
class UnknownResourceResponse : Resource() { /* nothing...*/ }
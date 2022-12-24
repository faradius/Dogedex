package com.alex.dogedex.api.responses

import com.alex.dogedex.api.dto.DogDTO
import com.squareup.moshi.Json

class DogApiResponse(
    val message: String,
    @field:Json(name = "is_success") val isSuccess: Boolean,
    val data: DogResponse
)
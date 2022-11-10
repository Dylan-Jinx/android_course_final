package com.example.final_535_app.common

data class ApiResponse<T>( val code: Int? = null,
                        val data: T? = null,
                        val dateTime: String? = null,
                        val errorMsg: String? = null ) {

}

data class ApiResponseList<T>( val code: Int? = null,
                           val data: List<T>? = null,
                           val dateTime: String? = null,
                           val errorMsg: String? = null ) {

}

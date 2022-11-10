package com.example.final_535_app.model

data class PageDataModel<T>(
    val countId: Any? = null,
    val current: Int? = null,
    val maxLimit: Any? = null,
    val optimizeCountSql: Boolean? = null,
    val orders: List<Any?>? = null,
    val pages: Int? = null,
    val records: MutableList<T>? = null,
    val searchCount: Boolean? = null,
    val size: Int? = null,
    val total: Int? = null
)

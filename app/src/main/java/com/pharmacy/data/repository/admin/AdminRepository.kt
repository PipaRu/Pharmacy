package com.pharmacy.data.repository.admin

import kotlinx.coroutines.flow.Flow

interface AdminRepository {

    fun fetchReportByUsers(): Flow<Unit>

    fun fetchReportByProduct(productId: Int): Flow<Unit>

    fun fetchReportByCategory(categoryId: Int): Flow<Unit>

}
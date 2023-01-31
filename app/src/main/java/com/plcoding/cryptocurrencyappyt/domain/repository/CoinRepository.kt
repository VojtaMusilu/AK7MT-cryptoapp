package com.plcoding.cryptocurrencyappyt.domain.repository

import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.data.remote.dto.CoinDetailDto
import com.plcoding.cryptocurrencyappyt.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    //suspend fun getCoins(): Flow<Resource<List<Coin>>>
    suspend fun getCoins(query: String): Flow<Resource<List<Coin>>>
    suspend fun getCoinById(coinId: String): CoinDetailDto
}
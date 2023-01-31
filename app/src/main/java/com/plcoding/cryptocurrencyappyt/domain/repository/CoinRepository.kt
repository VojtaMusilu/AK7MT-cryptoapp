package com.plcoding.cryptocurrencyappyt.domain.repository

import com.plcoding.cryptocurrencyappyt.data.remote.dto.CoinDetailDto
import com.plcoding.cryptocurrencyappyt.data.remote.dto.CoinDto

interface CoinRepository {
    //suspend fun getCoins(): Flow<Resource<List<Coin>>>
    suspend fun getCoins(query: String): List<CoinDto>
    suspend fun getCoinById(coinId: String): CoinDetailDto
}
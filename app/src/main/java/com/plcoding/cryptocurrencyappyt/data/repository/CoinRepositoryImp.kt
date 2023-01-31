package com.plcoding.cryptocurrencyappyt.data.repository

import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.data.remote.CoinPaprikaApi
import com.plcoding.cryptocurrencyappyt.data.remote.dto.CoinDetailDto
import com.plcoding.cryptocurrencyappyt.data.remote.dto.toCoin
import com.plcoding.cryptocurrencyappyt.domain.model.Coin
import com.plcoding.cryptocurrencyappyt.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImp @Inject constructor(
    private val api: CoinPaprikaApi
) : CoinRepository {

    override suspend fun getCoins(query: String)
    : Flow<Resource<List<Coin>>> {
        //return api.getCoins()
        return flow {
            emit(Resource.Loading(true))

            val remote = try{
                if(query.isBlank()){
                    val response = api.getCoins()
                    emit(Resource.Success(
                        data = response.map { it.toCoin() }
                    ))
                }else{
                    val response = api.getCoins(query)
                    emit(Resource.Success(
                        data = response.map { it.toCoin() }
                    ))
                }


            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldnt load data"))
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldnt load data"))
            }
        } as Flow<Resource<List<Coin>>>
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}
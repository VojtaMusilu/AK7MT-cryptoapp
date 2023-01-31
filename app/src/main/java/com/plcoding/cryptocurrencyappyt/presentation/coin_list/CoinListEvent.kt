package com.plcoding.cryptocurrencyappyt.presentation.coin_list

sealed class CoinListEvent{
    object Refresh: CoinListEvent()
    data class OnSearchQueryChange(val query:String): CoinListEvent()
}

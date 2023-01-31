package com.plcoding.cryptocurrencyappyt.presentation.coin_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.domain.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import com.plcoding.cryptocurrencyappyt.domain.use_case.get_coins.GetCoinsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class CoinListViewModel @Inject constructor(private val repository: CoinRepository, private val getCoinsUseCase: GetCoinsUseCase)
    : ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    private var searchJob: Job? = null

    var state by mutableStateOf(CoinListState())
    fun onEvent(event: CoinListEvent){
        when(event){
            is CoinListEvent.Refresh->{
                getCoins()
            }
            is CoinListEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCoins(state.searchQuery)
                }
            }
        }
    }

    init {
        getCoins()
    }

    private fun getCoins(
        query: String = state.searchQuery.lowercase()
    ) {

        getCoinsUseCase(query).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = CoinListState(coins = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = CoinListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = CoinListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    }
}
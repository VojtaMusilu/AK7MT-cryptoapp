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
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collect


@HiltViewModel
class CoinListViewModel @Inject constructor(private val repository: CoinRepository)
    : ViewModel() {

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
        viewModelScope.launch {
            repository.getCoins(query)
                .collect {result ->
                    when(result){
                        is Resource.Success -> {
                            result.data?.let { list ->
                                state = state.copy(
                                    coins = list
                                )
                            }
                        }
                        is Resource.Error -> Unit
                    }
                }
        }
    }
}
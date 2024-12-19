package br.edu.ifsp.dmo.sitesfavoritos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo.sitesfavoritos.model.Site

class MainViewModel : ViewModel(){

    private val _sites = MutableLiveData<MutableList<Site>>(mutableListOf())
    val sites: LiveData<MutableList<Site>> = _sites

    fun addSite(site: Site){
        _sites.value?.add(site)
        _sites.value = _sites.value
    }

    fun removerSite(position: Int){
        _sites.value?.removeAt(position)
        _sites.value = _sites.value
    }

    fun favoritarOuDesfavoritar(position: Int){
        _sites.value?.get(position)?.let {
            it.favorito = !it.favorito
        }

        _sites.value = _sites.value
    }
}
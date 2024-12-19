package br.edu.ifsp.dmo.sitesfavoritos.view

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.sitesfavoritos.R
import br.edu.ifsp.dmo.sitesfavoritos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.sitesfavoritos.databinding.AddsitesLayoutBinding
import br.edu.ifsp.dmo.sitesfavoritos.model.Site
import br.edu.ifsp.dmo.sitesfavoritos.view.adapters.SiteAdapter
import br.edu.ifsp.dmo.sitesfavoritos.view.listeners.SiteItemClickListener
import br.edu.ifsp.dmo.sitesfavoritos.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), SiteItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: SiteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configListeners()
        configRecyclerView()
        congigObservers()
    }

    override fun clickSiteItem(position: Int) {
        val site = adapter.dataset[position]
        val mIntent  = Intent(Intent.ACTION_VIEW)

        mIntent.setData(Uri.parse("http://" + site.url))
        startActivity(mIntent)
    }

    override fun clickHeartSiteItem(position: Int) {
        viewModel.favoritarOuDesfavoritar(position)
    }

    override fun clickExcluirSiteItem(position: Int) {
        viewModel.removerSite(position)
    }

    private fun configListeners(){
        binding.buttonAdd.setOnClickListener{handleAddSite()}
    }

    private fun configRecyclerView(){
        adapter = SiteAdapter(this, ArrayList(), this)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)

        binding.recyclerviewSites.layoutManager = layoutManager
        binding.recyclerviewSites.adapter = adapter
    }

    private fun congigObservers(){
        viewModel.sites.observe(this, { sites ->
            adapter.dataset.clear()
            sites?.let { adapter.dataset.addAll(it) }
            adapter.notifyDataSetChanged()
        })
    }

    private fun handleAddSite(){
        val tela = layoutInflater.inflate(R.layout.addsites_layout, null)
        val bindingDialog: AddsitesLayoutBinding = AddsitesLayoutBinding.bind(tela)

        val builder = AlertDialog.Builder(this)
            .setView(tela)
            .setTitle(R.string.novo_site)
            .setPositiveButton(R.string.salvar,
                DialogInterface.OnClickListener{dialog, which ->
                    val novoSite = Site(
                            bindingDialog.edittextApelido.text.toString(),
                            bindingDialog.edittextUrl.text.toString()
                    )
                    viewModel.addSite(novoSite)
                    dialog.dismiss()
                })
            .setNegativeButton(R.string.cancelar,
                DialogInterface.OnClickListener{dialog, which ->
                    dialog.dismiss()
                })

        val dialog = builder.create()
        dialog.show()
    }
}
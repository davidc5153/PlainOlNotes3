package com.example.plainolnotes3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plainolnotes3.database.NoteEntity
import com.example.plainolnotes3.databinding.ActivityMainBinding
import com.example.plainolnotes3.databinding.ContentMainBinding
import com.example.plainolnotes3.ui.NotesAdapter
import com.example.plainolnotes3.viewmodel.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    // private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var contentMainBinding: ContentMainBinding
    private var notesData = ArrayList<NoteEntity>()
    private lateinit var mAdapter: NotesAdapter
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentMainBinding = ContentMainBinding.bind(findViewById(R.id.content_main))

        initViewModel()

        /*
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        */

        binding.fab.setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java))
        }

        /*
        Log.i("PlainOlNotes", "Getting notes data from MainModelView")
        notesData.addAll(mViewModel.mNotes)
        for (note in notesData) {
            Log.i("PlainOlNotes", note.toString())
        }
        */
        initRecyclerView()

    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mAdapter = NotesAdapter(notesData, this)
        contentMainBinding.recyclerView.adapter = mAdapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                (mViewModel.mNotes as Flow<List<NoteEntity>>).collect {
                    notesData.clear()
                    notesData.addAll(it)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun initRecyclerView() {
        val mRecyclerView = contentMainBinding.recyclerView
        mRecyclerView.setHasFixedSize(true) // All elements in the list will be the same size
        val linearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = linearLayoutManager

        val divider = DividerItemDecoration(mRecyclerView.context, linearLayoutManager.orientation)
        mRecyclerView.addItemDecoration(divider)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_sample_data -> {
                addSampleData()
                true
            }
            R.id.action_delete_all -> {
                deleteAllNotes()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addSampleData() {
        mViewModel.addSampleData()
    }


    private fun deleteAllNotes() {
        mViewModel.deleteAllNotes()
    }

    /*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
     */
}
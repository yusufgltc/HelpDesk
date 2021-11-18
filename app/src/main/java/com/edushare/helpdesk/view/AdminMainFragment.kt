package com.edushare.helpdesk.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edushare.helpdesk.R
import com.edushare.helpdesk.adapter.RecyclerViewAdapterAdmin
import com.edushare.helpdesk.data.Requests
import com.edushare.helpdesk.databinding.FragmentAdminMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminMainFragment : Fragment(R.layout.fragment_admin_main) {

    private var _binding: FragmentAdminMainBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val notebookRef = db.collection("requests")
    private var adapter: RecyclerViewAdapterAdmin? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logout.setOnClickListener {
            val action = AdminMainFragmentDirections.actionAdminMainFragmentToIntroFragment()
            findNavController().navigate(action)
        }
        retrieveData()
    }

    private fun retrieveData() {
        val query: Query = notebookRef

        val options = FirestoreRecyclerOptions.Builder<Requests>()
            .setQuery(query, Requests::class.java)
            .build()

        adapter = RecyclerViewAdapterAdmin(options)

        binding.recyclerViewAdmin.setHasFixedSize(true)
        binding.recyclerViewAdmin.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewAdmin.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter!!.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recyclerViewAdmin)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
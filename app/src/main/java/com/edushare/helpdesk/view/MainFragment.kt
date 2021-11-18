package com.edushare.helpdesk.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.edushare.helpdesk.R
import com.edushare.helpdesk.adapter.RecyclerViewAdapterAdmin
import com.edushare.helpdesk.data.Requests
import com.edushare.helpdesk.databinding.FragmentMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.ItemTouchHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath

class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth
    private val requestRef = db.collection("requests")
    private var adapter: RecyclerViewAdapterAdmin? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance();

        binding.fab.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToRequestFragment()
            findNavController().navigate(action)
        }

        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            val action = MainFragmentDirections.actionMainFragmentToIntroFragment()
            findNavController().navigate(action)
        }

        retrieveData()
    }

    private fun retrieveData() {

        val firebaseUser = firebaseAuth.currentUser
        val userEmail = firebaseUser!!.email

        val query: Query = requestRef.whereEqualTo("email", userEmail)
        val options = FirestoreRecyclerOptions.Builder<Requests>()
            .setQuery(query, Requests::class.java)
            .build()

        adapter = RecyclerViewAdapterAdmin(options)

        binding.recyclerViewHome.setHasFixedSize(true)
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewHome.adapter = adapter

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
        }).attachToRecyclerView(binding.recyclerViewHome)

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
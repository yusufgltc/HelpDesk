package com.edushare.helpdesk.view

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edushare.helpdesk.R
import com.edushare.helpdesk.databinding.FragmentUpdateRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateRequestFragment:Fragment(R.layout.fragment_update_request) {
    private var _binding: FragmentUpdateRequestBinding? = null
    private val binding get() = _binding!!
    private val args : UpdateRequestFragmentArgs by navArgs()
    private var db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance();

        binding.textInputRequestTitleUpdate.setText(args.title)
        binding.textInputSubjectUpdate.setText(args.subject)

        binding.imageBack.setOnClickListener {
            val actions = UpdateRequestFragmentDirections.actionUpdateRequestFragmentToMainFragment()
            findNavController().navigate(actions)
        }

        binding.requestUpdate.setOnClickListener {
            updateRequest()
        }

    }

    private fun updateRequest() {
        val requestTitleRequest = binding.textInputRequestTitleUpdate.text.toString().trim()
        val requestSubjectRequest = binding.textInputSubjectUpdate.text.toString().trim()

        val firebaseUser = firebaseAuth.currentUser
        val userEmail = firebaseUser!!.email

        if(TextUtils.isEmpty(requestTitleRequest)){
            binding.textInputRequestTitleUpdate.error = "Title is required"
            return
        }

        if(TextUtils.isEmpty(requestSubjectRequest)){
            binding.textInputSubjectUpdate.error = "Subject is required"
            return
        }

        val request = hashMapOf(
            "title" to requestTitleRequest,
            "subject" to requestSubjectRequest,
            "email" to userEmail,
            "id" to args.id
        )

        db.collection("requests").document(args.id)
            .set(request)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: $it")
            }.addOnFailureListener {
                Log.w(ContentValues.TAG, "Error adding document", it)
            }

        val actions = UpdateRequestFragmentDirections.actionUpdateRequestFragmentToMainFragment()
        findNavController().navigate(actions)

    }
}
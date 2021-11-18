package com.edushare.helpdesk.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.edushare.helpdesk.R
import com.edushare.helpdesk.databinding.FragmentRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RequestFragment : Fragment(R.layout.fragment_request) {
    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private var db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()

        binding.imageBack.setOnClickListener {
            val action = RequestFragmentDirections.actionRequestFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.postRequest.setOnClickListener {
            postRequest()
        }
    }

    private fun postRequest(){
        val requestTitle = binding.textInputRequestTitle.text.toString().trim()
        val requestSubject = binding.textInputSubject.text.toString().trim()

        if(TextUtils.isEmpty(requestTitle)){
            binding.textInputRequestTitle.error = "Title is required"
            return
        }

        if(TextUtils.isEmpty(requestSubject)){
            binding.textInputSubject.error = "Subject is required"
            return
        }

        val firebaseUser = firebaseAuth.currentUser
        val userEmail = firebaseUser!!.email
        val uuid = UUID.randomUUID()
        val randomUUIDString = uuid.toString()


        val request = hashMapOf(
            "title" to requestTitle,
            "subject" to requestSubject,
            "email" to userEmail,
            "id" to randomUUIDString
        )

        db.collection("requests").document(randomUUIDString)
            .set(request)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: $it")
        }.addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
        }

        val action = RequestFragmentDirections.actionRequestFragmentToMainFragment()
        findNavController().navigate(action)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


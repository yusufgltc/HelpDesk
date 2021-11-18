package com.edushare.helpdesk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.edushare.helpdesk.R
import com.edushare.helpdesk.data.Requests
import com.edushare.helpdesk.view.MainFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RecyclerViewAdapterAdmin(options: FirestoreRecyclerOptions<Requests?>) :
    FirestoreRecyclerAdapter<Requests, RecyclerViewAdapterAdmin.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Requests) {
        holder.textViewTitle.text = model.title
        holder.textViewSubject.text = model.subject
        holder.textViewId.text = model.id

        val title = holder.textViewTitle.text.toString()
        val subject = holder.textViewSubject.text.toString()
        val id = holder.textViewId.text.toString()
        holder.textViewTitle.setOnClickListener {

            val action = MainFragmentDirections.actionMainFragmentToUpdateRequestFragment(title,subject,id)
            it.findNavController().navigate(action)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        var textViewSubject: TextView = itemView.findViewById(R.id.text_view_subject)
        var textViewId: TextView = itemView.findViewById(R.id.text_view_id)
    }

}
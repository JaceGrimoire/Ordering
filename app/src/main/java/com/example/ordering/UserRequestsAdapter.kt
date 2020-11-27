package com.example.ordering

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserRequestsAdapter(private val dataSet: Array<UserRequest>) :
        RecyclerView.Adapter<UserRequestsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUsername: TextView = view.findViewById(R.id.txt_username)
        val txtOrigin: TextView = view.findViewById(R.id.txt_request_origin)
        val txtDestination: TextView = view.findViewById(R.id.txt_request_destination)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.stub_user_requests, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtUsername.text = dataSet[position].username
        viewHolder.txtOrigin.text = dataSet[position].originAddress
        viewHolder.txtDestination.text = dataSet[position].destAddress
    }

    override fun getItemCount() = dataSet.size

}
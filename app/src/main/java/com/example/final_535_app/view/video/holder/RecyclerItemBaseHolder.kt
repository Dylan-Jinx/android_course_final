package com.example.final_535_app.view.video.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class RecyclerItemBaseHolder(itemView: View?) : RecyclerView.ViewHolder(
    itemView!!
) {
    var recyclerBaseAdapter: RecyclerView.Adapter<*>? = null
}
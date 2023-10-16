package com.webnexahindicartoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.webnexahindicartoon.R
import com.webnexahindicartoon.modal.CategoryModal
import com.bumptech.glide.Glide
import com.webnexahindicartoon.MainActivity

class categoryAdapter(
    private val context: Context,
    private val categoryList: ArrayList<CategoryModal>
) : RecyclerView.Adapter<categoryAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image1: ImageView = itemView.findViewById(R.id.c1)

        init {

            itemView.setOnClickListener {

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedCategory = categoryList[position]
                    val url =
                        "https://vizzorewards.com/getaia/hindicartoon/increase_category_clicks.php?id=${clickedCategory.id}"

                    val requestQueue = Volley.newRequestQueue(context)


                    val stringRequest = StringRequest(
                        Request.Method.GET, url,
                        { response ->

                        },
                        { error ->

                        }
                    )
                    requestQueue.add(stringRequest)
                    (context as MainActivity).filterVideosByCategoryId(clickedCategory.id)

                }
            }
        }
    }

    fun updateCategory(updateTrans: List<CategoryModal>) {
        categoryList.clear()
        categoryList.addAll(updateTrans)

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCategory = categoryList[position]
        Glide.with(context).load(currentCategory.image)
            .into(holder.image1)

    }

}



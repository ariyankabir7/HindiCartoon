package com.webnexahindicartoon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.webnexahindicartoon.MainActivity
import com.webnexahindicartoon.PlayerActivity
import com.webnexahindicartoon.R
import com.webnexahindicartoon.modal.videosModal

class videoAdapter(
    private val context: Context,
    private val videosList: ArrayList<videosModal>
) : RecyclerView.Adapter<videoAdapter.ViewHolder>() {


    @SuppressLint("SuspiciousIndentation")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoInfoTab: LinearLayout = itemView.findViewById(R.id.videoInfoTab)
        val video_thumbnail: ImageView = itemView.findViewById(R.id.video_thumbnail)
        val category_thumbnail: ImageView = itemView.findViewById(R.id.channel_logo)
        val title: TextView = itemView.findViewById(R.id.video_title)
        val video_subtitle: TextView = itemView.findViewById(R.id.video_subtitle)
        val categoryName: TextView = itemView.findViewById(R.id.channel_name)
        val deviceId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        init {


            fun openVideo() {
                val position = adapterPosition
                val clickedVideo = videosList[position]
                if (position != RecyclerView.NO_POSITION) {

                    val url =
                        "https://vizzorewards.com/getaia/hindicartoon/Increase_viewsNew.php?id=${clickedVideo.id}&cat=${clickedVideo.c_id}&device=$deviceId"

                    val requestQueue = Volley.newRequestQueue(context)


                    val stringRequest = StringRequest(
                        Request.Method.GET, url,
                        { response ->

                        },
                        { error ->

                        }
                    )
                    requestQueue.add(stringRequest)
                    val intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra("videoId", clickedVideo.id)
                    intent.putExtra("videoLink", clickedVideo.video_link)
                    intent.putExtra("videoTitle", clickedVideo.title)
                    intent.putExtra("videoCateImage", clickedVideo.c_image)
                    intent.putExtra("videoViews", clickedVideo.views)
                    intent.putExtra("videoCate", clickedVideo.category_name)
                    var tempVideoList = ArrayList<videosModal>()
                    tempVideoList.clear()
                    tempVideoList.addAll(videosList)

                    if (tempVideoList.size > 15) {
                        tempVideoList.subList(15, tempVideoList.size).clear()
                    }
                    tempVideoList =
                        tempVideoList.filter { it.id != clickedVideo.id } as ArrayList<videosModal>
                    intent.putExtra("videosList", tempVideoList)
                    context.startActivity(intent)

                }
            }
            video_thumbnail.setOnClickListener {
                openVideo()
            }
            videoInfoTab.setOnClickListener {
                openVideo()
            }
            category_thumbnail.setOnClickListener {
                val position = adapterPosition
                val clickedVideo = videosList[position]
                if (position != RecyclerView.NO_POSITION) {
                (context as MainActivity).filterVideosByCategoryId(clickedVideo.category_id)
            }
            }
        }

    }

    fun updateVideo(updateTrans: List<videosModal>) {
        videosList.clear()
        videosList.addAll(updateTrans)

        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videosList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentVideo = videosList[position]
        Glide.with(context).load(currentVideo.image)
            .into(holder.video_thumbnail)
        Glide.with(context).load(currentVideo.c_image)
            .into(holder.category_thumbnail)
        holder.title.text = currentVideo.title
        holder.video_subtitle.text = "${currentVideo.views} views"
        holder.categoryName.text = currentVideo.category_name
    }


}


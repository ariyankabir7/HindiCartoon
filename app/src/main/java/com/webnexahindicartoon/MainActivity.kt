package com.webnexahindicartoon

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.webnexahindicartoon.adapter.categoryAdapter
import com.webnexahindicartoon.adapter.videoAdapter
import com.webnexahindicartoon.databinding.ActivityMainBinding
import com.webnexahindicartoon.modal.CategoryModal
import com.webnexahindicartoon.modal.videosModal
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var binding: ActivityMainBinding
    private lateinit var videoAdapter: videoAdapter
    var tempVideoList = ArrayList<videosModal>()
    val AllVideoList = ArrayList<videosModal>()
    val AllTrendingVideoList = ArrayList<videosModal>()
    var normalHomeOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestQueue = Volley.newRequestQueue(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContentView(binding.root)
        binding.shimmerView.duration = 900
        binding.shimmerView.startShimmerAnimation()


        binding.categoryRecycleview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val CategoryAdapter = categoryAdapter(this, ArrayList())
        binding.categoryRecycleview.adapter = CategoryAdapter


        getCategories(
            onSuccess = { categoryList ->
                CategoryAdapter.updateCategory(categoryList)
            },
            onError = { errorMessage ->
                println("Error: $errorMessage")
            }
        )


        binding.videoRecycleView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        videoAdapter = videoAdapter(this, ArrayList())
        binding.videoRecycleView.adapter = videoAdapter

        getVideos(onSuccess = { videosList ->
            AllVideoList.clear()
            AllVideoList.addAll(videosList)
            videoAdapter.updateVideo(videosList)
            binding.shimmerView
                .visibility = View.GONE
            binding.swipeRefreshLayout.visibility = View.VISIBLE
            binding.shimmerView.stopShimmerAnimation()
        },
            onError = { errorMessage ->
                // Handle the error here
                println("Error: $errorMessage")
            })

        getPopularVideos(onSuccess = { videosList ->
            AllTrendingVideoList.clear()
            AllTrendingVideoList.addAll(videosList)

        },
            onError = { errorMessage ->
                // Handle the error here
                println("Error: $errorMessage")
            })

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.shimmerView
                .visibility = View.VISIBLE
            binding.swipeRefreshLayout.visibility = View.GONE
            binding.shimmerView.startShimmerAnimation()
            getVideos(onSuccess = { videosList ->
                AllVideoList.clear()
                AllVideoList.addAll(videosList)
                videoAdapter.updateVideo(videosList)
                binding.swipeRefreshLayout.isRefreshing = false
                binding.shimmerView
                    .visibility = View.GONE
                binding.swipeRefreshLayout.visibility = View.VISIBLE
                binding.shimmerView.stopShimmerAnimation()
            },
                onError = { errorMessage ->
                    // Handle the error here
                    println("Error: $errorMessage")
                })
        }

        binding.bottomMenu.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.home_btn -> {
                    normalHomeOpened = true
                    videoAdapter.updateVideo(AllVideoList) // Convert filteredList to a mutable list before updating
                }

                R.id.trending_btn -> {
                    normalHomeOpened = false
                    videoAdapter.updateVideo(AllTrendingVideoList) // Convert filteredList to a mutable list before updating
                }

                R.id.more_btn -> {
                    normalHomeOpened = false
                    val bottomSheetFragment = BottomSheetFragment()
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }
            }
            true
        }


    }

    private fun getVideos(onSuccess: (ArrayList<videosModal>) -> Unit, onError: (String) -> Unit) {
        val deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        val url = "https://vizzorewards.com/getaia/hindicartoon/getvideos_home.php?device=$deviceId"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val popularList = ArrayList<videosModal>()

                for (i in 0 until response.length()) {
                    val jsonObject: JSONObject = response.getJSONObject(i)
                    val videosModal = videosModal(
                        jsonObject.getString("c_id"),
                        jsonObject.getString("c_image"),
                        jsonObject.getString("category_id"),
                        jsonObject.getString("category_name"),
                        jsonObject.getString("id"),
                        jsonObject.getString("image"),
                        jsonObject.getString("lan"),
                        jsonObject.getString("status"),
                        jsonObject.getString("tags"),
                        jsonObject.getString("title"),
                        jsonObject.getString("type"),
                        jsonObject.getString("upload_by"),
                        jsonObject.getString("video_link"),
                        jsonObject.getString("views")
                    )
                    popularList.add(videosModal)
                }
                onSuccess(popularList)
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun getPopularVideos(
        onSuccess: (List<videosModal>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "https://vizzorewards.com/getaia/hindicartoon/getvideos_popular.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val popularList = ArrayList<videosModal>()

                for (i in 0 until response.length()) {
                    val jsonObject: JSONObject = response.getJSONObject(i)
                    val videosModal = videosModal(
                        jsonObject.getString("c_id"),
                        jsonObject.getString("c_image"),
                        jsonObject.getString("category_id"),
                        jsonObject.getString("category_name"),
                        jsonObject.getString("id"),
                        jsonObject.getString("image"),
                        jsonObject.getString("lan"),
                        jsonObject.getString("status"),
                        jsonObject.getString("tags"),
                        jsonObject.getString("title"),
                        jsonObject.getString("type"),
                        jsonObject.getString("upload_by"),
                        jsonObject.getString("video_link"),
                        jsonObject.getString("views")
                    )
                    popularList.add(videosModal)
                }
                onSuccess(popularList)
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun getCategories(onSuccess: (List<CategoryModal>) -> Unit, onError: (String) -> Unit) {
        val url = "https://vizzorewards.com/getaia/hindicartoon/getcategory.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val categoryList = ArrayList<CategoryModal>()

                for (i in 0 until response.length()) {
                    val jsonObject: JSONObject = response.getJSONObject(i)
                    val categoryModal = CategoryModal(
                        jsonObject.getString("category_name"),
                        jsonObject.getString("clicks"),
                        jsonObject.getString("id"),
                        jsonObject.getString("image"),
                        jsonObject.getString("status")
                    )
                    categoryList.add(categoryModal)
                }

                onSuccess(categoryList)
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest)
    }

    fun filterVideosByCategoryId(categoryId: String) {
        normalHomeOpened = false
        tempVideoList.clear()
        tempVideoList.addAll(AllVideoList)
        videoAdapter.updateVideo(tempVideoList.filter { it.category_id == categoryId }) // Convert filteredList to a mutable list before updating
    }

    fun callHomeScreen() {
        normalHomeOpened = true
        binding.bottomMenu.selectedItemId = R.id.home_btn
    }

    override fun onBackPressed() {
        if (normalHomeOpened){
            super.onBackPressed()
        }else{
            callHomeScreen()
        }
    }
}
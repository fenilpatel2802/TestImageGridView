package com.wipro.testimagescrollapp.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wipro.testimagescrollapp.R
import com.wipro.testimagescrollapp.adapter.ImageViewRecyclerAdapter.MyHolder
import com.wipro.testimagescrollapp.databinding.ItemPhotoBinding
import com.wipro.testimagescrollapp.domain.model.ImageListResponse

class ImageViewRecyclerAdapter(
    private var context: Context,
    private var list: MutableList<ImageListResponse?>
) :
    RecyclerView.Adapter<MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.binding) {
            progressBar.visibility = View.VISIBLE
            Glide.with(context)
                .load(list[position]?.urls?.regular)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE // Hide progress bar on failure
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE // Hide progress bar on success
                        return false
                    }
                })
                .placeholder(android.R.color.darker_gray)
                .error(R.color.black)
                .into(photoImageView)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

}

package com.egemen.dinle


import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener

class UniversalImageLoader (val mContext: Context) {

    val config:ImageLoaderConfiguration
        get(){
            val defaultOptions = DisplayImageOptions.Builder()
                    .showImageOnLoading(defaultProfilImage)
                    .showImageForEmptyUri(defaultProfilImage)
                    .showImageOnFail(defaultProfilImage)
                    .cacheOnDisk(true).cacheInMemory(true)
                    .cacheOnDisk(true).resetViewBeforeLoading(true)
                    .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565) //burayi yeni ekledik
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(FadeInBitmapDisplayer(400)).build()
            return  ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(defaultOptions)
                    .diskCacheSize(100*1024*1024).build()
        }
    companion object {
        private var defaultProfilImage = R.drawable.ic_image_black_24dp
        fun setImage(imgURL:String, imageView: ImageView, mPorgressBar:ProgressBar?,ilkKisim:String?){
            val imageLoader= ImageLoader.getInstance()
            imageLoader.displayImage(ilkKisim+imgURL,imageView,object :ImageLoadingListener{
                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                    if(mPorgressBar!=null){
                        mPorgressBar.visibility=View.GONE
                    }
                }

                override fun onLoadingStarted(imageUri: String?, view: View?) {
                    if(mPorgressBar!=null){
                        mPorgressBar.visibility=View.VISIBLE
                    }
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    if(mPorgressBar!=null){
                        mPorgressBar.visibility=View.GONE
                    }
                }

                override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                    defaultProfilImage=R.drawable.ic_broken_image_black_24dp
                    if(mPorgressBar!=null){
                        mPorgressBar.visibility=View.GONE

                    }
                }
            })

        }
    }
}
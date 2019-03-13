package ptsiogas.gr.kotlincoroutineexample

import android.content.Context
import androidx.multidex.MultiDexApplication

class BaseApplication : MultiDexApplication() {
    private var mContext: BaseApplication? = null

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    fun getContext(): Context? {
        return mContext
    }
}
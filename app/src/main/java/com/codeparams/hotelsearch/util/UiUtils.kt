package com.codeparams.hotelsearch.util

import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

object UiUtils {
    fun doPostponedTransition(fragment: Fragment?, binding: ViewDataBinding?) {
        if (fragment != null && binding != null) {
            fragment.postponeEnterTransition()
            val vto = binding.root.viewTreeObserver
            vto.addOnPreDrawListener(object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (vto.isAlive) {
                        vto.removeOnPreDrawListener(this)
                    } else {
                        binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                    }
                    fragment.startPostponedEnterTransition()
                    binding.lifecycleOwner = fragment.viewLifecycleOwner
                    return true
                }
            })
        }
    }
}

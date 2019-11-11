package com.brins.lightmusic.ui.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.lang.Exception
import java.util.*

class BaseDialogFragment : DialogFragment() {

    companion object{
        private val sWaitShowDialogMap = HashMap<Int, LinkedList<BaseDialogFragment>>()
        private val sShowingDialogMap = HashMap<Int, BaseDialogFragment>()
        val DEFAULT_DIM_AMOUNT = 0.6f

    }

    val TAG = this::class.java.simpleName
    private var mMapKey: Int = 0

    fun show(fragment : FragmentManager): BaseDialogFragment{
            return show(fragment ,true)
    }

    private fun show(fragmentManager: FragmentManager, isCheck: Boolean): BaseDialogFragment {
        try {
            mMapKey = fragmentManager.hashCode()
            fragmentManager.executePendingTransactions()
            val fragment = fragmentManager.findFragmentByTag(TAG)
            if (fragment != null && fragment.isAdded) {
                return fragment as BaseDialogFragment
            }
            var isCanShow = true
            if (isCheck){
                val showingDialog = sShowingDialogMap.get(mMapKey)
                if (showingDialog != null && showingDialog.isAdded){
                    isCanShow = false
                    var linkedList = sWaitShowDialogMap[mMapKey]
                    if (linkedList == null){
                        linkedList = LinkedList()
                        sWaitShowDialogMap.put(mMapKey,linkedList)
                    }
                    linkedList.add(this)
                }
            }
            if (isCanShow) {
                val ft = fragmentManager.beginTransaction()
                ft.add(this, TAG)
                ft.commitAllowingStateLoss()
                sShowingDialogMap[mMapKey] = this
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        return this
    }
}
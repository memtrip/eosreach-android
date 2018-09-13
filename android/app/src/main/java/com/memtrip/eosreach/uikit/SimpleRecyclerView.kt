package com.memtrip.eosreach.uikit

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class SimpleRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private lateinit var simpleAdapter: SimpleAdapter<*>

    init {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    atEnd()
                }
            }
        })
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {

        if (adapter !is SimpleAdapter<*>) {
            throw IllegalStateException(
                "SimpleRecyclerView must use a SimpleAdapter as its source")
        }

        super.setAdapter(adapter)

        simpleAdapter = adapter
    }

    private fun atEnd() {
        simpleAdapter.atEnd(id)
    }
}
package com.orcchg.flatradiogroup

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout

class FlatRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {

    private val radioButtons = mutableListOf<CompoundButton>()
    private var skipRecursiveChecking = false
    @IdRes var currentCheckedViewId: Int = NO_ID
        private set
    @IdRes private var checkedViewIdBeforePreLayout: Int = NO_ID

    interface OnCheckedChangeListener {
        fun onCheckedChanged(radioGroup: FlatRadioGroup, @IdRes checkedViewId: Int, isChecked: Boolean)
    }
    var onCheckedChangeListener: OnCheckedChangeListener? = null

    private val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (skipRecursiveChecking) {
            return@OnCheckedChangeListener
        }

        if (currentCheckedViewId != NO_ID) {
            skipRecursiveChecking = true
            val isCheckedReal = if (buttonView.id != currentCheckedViewId) false else isChecked
            radioButtons.find { it.id == currentCheckedViewId }?.isChecked = isCheckedReal
            skipRecursiveChecking = false
        }

        setCurrentCheckedViewId(buttonView.id, isChecked)
    }

    fun clearCheck() {
        if (currentCheckedViewId == NO_ID) {
            return
        }

        skipRecursiveChecking = true
        radioButtons.find { it.id == currentCheckedViewId }?.isChecked = false
        skipRecursiveChecking = false

        setCurrentCheckedViewId(NO_ID, false)
    }

    override fun init(attrs: AttributeSet?) {
        super.init(attrs)
        mUseViewMeasure = false
    }

    override fun updatePreLayout(container: ConstraintLayout) {
        super.updatePreLayout(container)

        for (i in 0 until mCount) {
            val id = mIds[i]
            val childView = container.getViewById(id)
            if (childView is CompoundButton) {
                childView.setOnCheckedChangeListener(listener)
                radioButtons.add(childView)
            }
        }

        checkView(checkedViewIdBeforePreLayout)
    }

    override fun updatePostLayout(container: ConstraintLayout) {
        layoutParams.width = 0
        layoutParams.height = 0
        super.updatePostLayout(container)
    }

    private fun checkView(@IdRes id: Int) {
        if (id == NO_ID) {
            return
        }

        checkedViewIdBeforePreLayout = if (radioButtons.isEmpty()) id else NO_ID

        var found = false
        skipRecursiveChecking = true
        radioButtons.forEach {
            if (it.id == id) {
                found = true
            }
            it.isChecked = it.id == id
        }
        skipRecursiveChecking = false

        if (radioButtons.isNotEmpty()) {
            setCurrentCheckedViewId(if (found) id else NO_ID, found)
        }
    }

    private fun setCurrentCheckedViewId(@IdRes id: Int, isChecked: Boolean) {
        currentCheckedViewId = id
        onCheckedChangeListener?.onCheckedChanged(this, id, isChecked)
    }

    companion object {
        const val NO_ID = -1
    }
}

package nl.dionsegijn.steppertouch

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.View.NO_ID
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * An implementation of DeclaredOnStepCallbackListener that attempts to lazily load a
 * named click handling method from a parent or ancestor mReceiver.
 */
internal class DeclaredOnStepCallbackListener(private val mHostView: View, private val mMethodName: String) : OnStepCallback {

    private var mResolvedMethod: Method? = null
    private var mReceiver: Context = mHostView
            .context

    private fun resolveMethod(context: Context?) {
        var context = context
        while (context != null) {
            try {
                if (!context.isRestricted) {
                    val methods = context
                            .javaClass
                            .methods

                    var method: Method? = null
                    for (method1 in methods) {
                        if (method1.name == mMethodName) {
                            method = method1
                        }
                    }

                    if (method != null) {
                        mResolvedMethod = method
                        this
                                .mReceiver = context
                        return
                    }
                }
            } catch (e: NoSuchMethodException) {
                // Failed to find method, keep searching up the hierarchy.
            }

            context = if (context is ContextWrapper) {
                context
                        .baseContext
            } else {
                // Can't search up the hierarchy, null out and fail.
                null
            }
        }

        val id = mHostView
                .id
        val idText = if (id == NO_ID) ""
        else " with id '" + mHostView.context.resources.getResourceEntryName(id) + "'"
        throw IllegalStateException("Could not find method " + mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + mHostView.javaClass + idText)
    }

    override fun onStep(value: Int, positive: Boolean) {
        if (mResolvedMethod == null) {
            resolveMethod(mHostView.context)
        }

        try {
            mResolvedMethod!!
                    .invoke(mReceiver, value, positive)
        } catch (e: IllegalAccessException) {
            throw IllegalStateException("Could not execute non-public method for android:onClick", e)
        } catch (e: InvocationTargetException) {
            throw IllegalStateException("Could not execute method for android:onClick", e)
        }

    }
}
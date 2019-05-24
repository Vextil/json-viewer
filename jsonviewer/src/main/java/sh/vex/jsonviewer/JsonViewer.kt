package sh.vex.jsonviewer

import android.animation.LayoutTransition
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat

class JsonViewer : LinearLayout {

    private val PADDING = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
    @ColorInt
    private var textColorString: Int = 0
    @ColorInt
    private var textColorBool: Int = 0
    @ColorInt
    private var textColorNull: Int = 0
    @ColorInt
    private var textColorNumber: Int = 0
    @Dimension
    private var textSize: Int = 0

    constructor(context: Context) : super(context) {

        if (isInEditMode)
            initEditMode()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null)
            init(context, attrs)
        if (isInEditMode)
            initEditMode()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null)
            init(context, attrs)
        if (isInEditMode)
            initEditMode()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.JsonViewer, 0, 0)
        try {
            textColorString = attributes.getColor(R.styleable.JsonViewer_textColorString, ContextCompat.getColor(context, R.color.jsonViewer_textColorString))
            textColorNumber = attributes.getColor(R.styleable.JsonViewer_textColorNumber, ContextCompat.getColor(context, R.color.jsonViewer_textColorNumber))
            textColorBool = attributes.getColor(R.styleable.JsonViewer_textColorBool, ContextCompat.getColor(context, R.color.jsonViewer_textColorBool))
            textColorNull = attributes.getColor(R.styleable.JsonViewer_textColorNull, ContextCompat.getColor(context, R.color.jsonViewer_textColorNull))
            textSize = attributes.getDimensionPixelSize(R.styleable.JsonViewer_textSize, resources.getDimensionPixelSize(R.dimen.text_size))
        } finally {
            attributes.recycle()
        }
    }

    private fun initEditMode() {
        val json = "{\"id\":1,\"name\":\"Title\",\"is\":true,\"value\":null,\"array\":[" +
                "{\"item\":1,\"name\":\"One\"},{\"item\":2,\"name\":\"Two\"}],\"object\":" +
                "{\"id\":1,\"name\":\"Title\"},\"simple_array\":[1,2,3]}"
        try {
            setJsonAny(JSONObject(json))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    /**
     * Set JSON for the view. The old one will be erased and the view reset.
     * @param json The JSON to add.
     */
    fun setJson(json: JSONObject) = setJsonAny(json)

    /**
     * Set JSON for the view. The old one will be erased and the view reset.
     * @param json The JSON to add.
     */
    fun setJson(json: JSONArray) = setJsonAny(json)

    /**
     * Set JSON for the view. The old one will be erased and the view reset.
     * @param json The JSON to add.
     */
    fun setJson(json: String) = setJsonAny(JSONObject(json))

    private fun setJsonAny(json: Any) {
        super.setOrientation(VERTICAL)
        this.removeAllViews()
        addJsonNode(this, null, json, false)
    }

    fun setTextColorString(@ColorInt color: Int) {
        textColorString = color
    }

    fun setTextColorNumber(@ColorInt color: Int) {
        textColorNumber = color
    }

    fun setTextColorBool(@ColorInt color: Int) {
        textColorBool = color
    }

    fun setTextColorNull(@ColorInt color: Int) {
        textColorNull = color
    }

    fun setTextSize(@Dimension size: Int) {
        textSize = size
    }

    /**
     * It will collapse all nodes, except the main one.
     */
    fun collapseJson() {
        var i = 0
        while (i < this.childCount) {
            if (this.getChildAt(i) is TextView &&
                    this.getChildAt(i + 1) is ViewGroup &&
                    this.getChildAt(i + 2) is TextView) {
                changeVisibility(this.getChildAt(i + 1) as ViewGroup, View.VISIBLE)
                i += 2
            }
            i++
        }
    }

    /**
     * It will expands all the json nodes.
     */
    fun expandJson() {
        changeVisibility(this, View.GONE)
    }

    /**
     * Switch collapse status of a node content.
     *
     * @param group         The view group to operate on.
     * @param oldVisibility Current visibility.
     */
    private fun changeVisibility(group: ViewGroup, oldVisibility: Int) {
        var i = 0
        while (i < group.childCount) {
            if (group.getChildAt(i) is TextView &&
                    group.getChildAt(i + 1) is ViewGroup &&
                    group.getChildAt(i + 2) is TextView) {
                val groupChild = group.getChildAt(i + 1) as ViewGroup
                groupChild.visibility = oldVisibility
                //                groupChild.setLayoutTransition(null); // remove transition before mass change
                group.getChildAt(i).callOnClick()
                //                groupChild.setLayoutTransition(new LayoutTransition());
                changeVisibility(group.getChildAt(i + 1) as ViewGroup, oldVisibility)
                i += 2
            }
            i++
        }
    }

    /**
     * Add a node to the view with header key and close footer. This method is call for every node in a node.
     *
     * @param content  Current view group.
     * @param nodeKey  key of the current node.
     * @param jsonNode Node to display.
     * @param haveNext If this node is followed by a other one.
     */
    private fun addJsonNode(content: LinearLayout, nodeKey: Any?, jsonNode: Any, haveNext: Boolean) {

        val hasChild = jsonNode is JSONObject && jsonNode.length() != 0 || jsonNode is JSONArray && jsonNode.length() != 0
        val textViewHeader: TextView

        textViewHeader = getHeader(nodeKey, jsonNode, haveNext, true, hasChild)

        content.addView(textViewHeader)

        if (hasChild) {
            val viewGroupChild = getJsonNodeChild(nodeKey, jsonNode)
            val textViewFooter = getFooter(jsonNode, haveNext)

            content.addView(viewGroupChild)
            content.addView(textViewFooter)

            textViewHeader.setOnClickListener {
                val newVisibility: Int
                val showChild: Boolean
                if (viewGroupChild.visibility == View.VISIBLE) {
                    newVisibility = View.GONE
                    showChild = false
                } else {
                    newVisibility = View.VISIBLE
                    showChild = true
                }
                textViewHeader.text = getHeaderText(nodeKey, jsonNode, haveNext, showChild, hasChild)
                viewGroupChild.visibility = newVisibility
                textViewFooter.visibility = newVisibility
            }
        }
    }

    /**
     * Create a view group for a node content and return it.
     *
     * @param nodeKey  Key of the node passed as parameter.
     * @param jsonNode Content of the node use to fill view.
     * @return View group contain all the childs of the node.
     */
    private fun getJsonNodeChild(nodeKey: Any?, jsonNode: Any): ViewGroup {

        val content = LinearLayout(context)

        content.orientation = VERTICAL
        content.setPadding(PADDING.toInt(), 0, 0, 0)
        if (nodeKey != null) {
            content.setBackgroundResource(R.drawable.background)
        }
        content.layoutTransition = LayoutTransition()

        if (jsonNode is JSONObject) {
            // setView key
            val iterator = jsonNode.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                // set view list
                try {
                    addJsonNode(content, key, jsonNode.get(key), iterator.hasNext())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        } else if (jsonNode is JSONArray) {
            // setView key
            for (i in 0 until jsonNode.length()) {
                // set view list
                try {
                    addJsonNode(content, i, jsonNode.get(i), i + 1 < jsonNode.length())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }
        return content
    }

    private fun getHeader(key: Any?, jsonNode: Any?, haveNext: Boolean, childDisplayed: Boolean, haveChild: Boolean): TextView {
        val textView = TextView(context)
        textView.text = getHeaderText(key, jsonNode, haveNext, childDisplayed, haveChild)
        TextViewCompat.setTextAppearance(textView, R.style.JsonViewer_TextAppearance)
        textView.isFocusableInTouchMode = false
        textView.isFocusable = false

        return textView
    }

    private fun getHeaderText(key: Any?, jsonNode: Any?, haveNext: Boolean, childDisplayed: Boolean, hasChild: Boolean): SpannableStringBuilder {

        return SpannableStringBuilder().apply {
            if (key is String) {
                append("\"")
                append(key as String?)
                append("\"")
                append(": ")
            }

            if (!childDisplayed) {
                if (jsonNode is JSONArray) {
                    append("[ ... ]")
                } else if (jsonNode is JSONObject) {
                    append("{ ... }")
                }
                if (haveNext) {
                    append(",")
                }
            } else {
                if (jsonNode is JSONArray) {
                    append("[")
                    if (!hasChild) {
                        append(getFooterText(jsonNode, haveNext))
                    }
                } else if (jsonNode is JSONObject) {
                    append("{")
                    if (!hasChild) {
                        append(getFooterText(jsonNode, haveNext))
                    }
                } else if (jsonNode != null) {
                    if (jsonNode is String) {
                        append("\"" + jsonNode + "\"", ForegroundColorSpan(textColorString), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else if (jsonNode is Int || jsonNode is Float || jsonNode is Double) {
                        append(jsonNode.toString(), ForegroundColorSpan(textColorNumber), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else if (jsonNode is Boolean) {
                        append(jsonNode.toString(), ForegroundColorSpan(textColorBool), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else if (jsonNode === JSONObject.NULL) {
                        append(jsonNode.toString(), ForegroundColorSpan(textColorNull), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else {
                        append(jsonNode.toString())
                    }

                    if (haveNext) {
                        append(",")
                    }

                    val span = LeadingMarginSpan.Standard(0, PADDING.toInt())
                    setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }

    private fun getFooter(jsonNode: Any?, haveNext: Boolean): TextView {
        val textView = TextView(context)
        textView.text = getFooterText(jsonNode, haveNext)
        TextViewCompat.setTextAppearance(textView, R.style.JsonViewer_TextAppearance)
        textView.isFocusableInTouchMode = false
        textView.isFocusable = false

        return textView
    }

    private fun getFooterText(jsonNode: Any?, hasNext: Boolean): StringBuilder? {
        return StringBuilder().apply {
            when (jsonNode) {
                is JSONObject -> append("}")
                is JSONArray -> append("]")
            }
            if (hasNext) append(",")
        }
    }
}

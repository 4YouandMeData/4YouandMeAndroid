package com.foryouandme.core.ext.html

import android.annotation.SuppressLint
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import android.util.Log
import com.foryouandme.core.ext.dpToPx
import org.xml.sax.XMLReader
import java.util.*


/**
 * Implements support for ordered (`<ol>`) and unordered (`<ul>`) lists in to Android TextView.
 *
 *
 * This can be used as follows:<br></br>
 * `textView.setText(Html.fromHtml("<ul><li>item 1</li><li>item 2</li></ul>", null, new HtmlListTagHandler()));`
 *
 *
 * Implementation based on code by Juha Kuitunen (https://bitbucket.org/Kuitsi/android-textview-html-list),
 * released under Apache License v2.0. Refactored & improved by Matthias Stevens (InThePocket.mobi).
 *
 *
 * **Known issues:**
 *  * The indentation on nested `<ul>`s isn't quite right (TODO fix this)
 *  * the `start` attribute of `<ol>` is not supported. Doing so is tricky because
 * [Html.TagHandler.handleTag] does not expose tag attributes.
 * The only way to do it would be to use reflection to access the attribute information kept by the XMLReader
 * (see: http://stackoverflow.com/a/24534689/1084488).
 *
 */
class CustomTagHandler : Html.TagHandler {

    /**
     * Keeps track of lists (ol, ul). On bottom of Stack is the outermost list
     * and on top of Stack is the most nested list
     */
    private val lists = Stack<ListTag>()

    /**
     * @see android.text.Html.TagHandler.handleTag
     */
    @SuppressLint("LogNotTimber")
    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (UL_TAG.equals(tag, ignoreCase = true)) {
            if (opening) {   // handle <ul>
                lists.push(Ul())
            } else {   // handle </ul>
                lists.pop()
            }
        } else if (OL_TAG.equals(tag, ignoreCase = true)) {
            if (opening) {   // handle <ol>
                lists.push(Ol()) // use default start index of 1
            } else {   // handle </ol>
                lists.pop()
            }
        } else if (LI_TAG.equals(tag, ignoreCase = true)) {
            if (opening) {   // handle <li>
                lists.peek().openItem(output)
            } else {   // handle </li>
                lists.peek().closeItem(output, lists.size)
            }
        } else {
            Log.d("TagHandler", "Found an unsupported tag $tag")
        }
    }

    /**
     * Abstract super class for [Ul] and [Ol].
     */
    private abstract class ListTag {
        /**
         * Opens a new list item.
         *
         * @param text
         */
        open fun openItem(text: Editable) {
            if (text.isNotEmpty() && text[text.length - 1] != '\n') {
                text.append("\n")
            }
            val len = text.length
            text.setSpan(this, len, len, Spanned.SPAN_MARK_MARK)
        }

        /**
         * Closes a list item.
         *
         * @param text
         * @param indentation
         */
        fun closeItem(text: Editable, indentation: Int) {
            if (text.isNotEmpty() && text[text.length - 1] != '\n') {
                text.append("\n")
            }
            val replaces = getReplaces(text, indentation)
            val len = text.length
            val listTag = getLast(text)
            val where = text.getSpanStart(listTag)
            text.removeSpan(listTag)
            if (where != len) {
                for (replace in replaces) {
                    text.setSpan(replace, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        protected abstract fun getReplaces(text: Editable, indentation: Int): Array<Any>

        /**
         * Note: This knows that the last returned object from getSpans() will be the most recently added.
         *
         * @see Html
         */
        private fun getLast(text: Spanned): ListTag? {
            val listTags = text.getSpans(0, text.length, ListTag::class.java)
            return if (listTags.isEmpty()) {
                null
            } else listTags[listTags.size - 1]
        }
    }

    /**
     * Class representing the unordered list (`<ul>`) HTML tag.
     */
    private class Ul : ListTag() {

        override fun getReplaces(text: Editable, indentation: Int): Array<Any> {
            // Nested BulletSpans increases distance between BULLET_SPAN and text, so we must prevent it.
            var bulletMargin = getIndentation()
            if (indentation > 1) {
                bulletMargin = getIndentation() - getStandardBullet().getLeadingMargin(true)
                if (indentation > 2) {
                    // This get's more complicated when we add a LeadingMarginSpan into the same line:
                    // we have also counter it's effect to BulletSpan
                    bulletMargin -= (indentation - 2) * getListItemIndent()
                }
            }
            return arrayOf(
                LeadingMarginSpan.Standard(getListItemIndent() * (indentation - 1)),
                ImprovedBulletSpan(3.dpToPx(), bulletMargin)
            )
        }
    }

    /**
     * Class representing the ordered list (`<ol>`) HTML tag.
     */
    private class Ol
    /**
     * Creates a new `<ul>` with given start index.
     *
     * @param startIdx
     */
    @JvmOverloads constructor(private var nextIdx: Int = 1) : ListTag() {

        override fun openItem(text: Editable) {
            super.openItem(text)
            text.append((nextIdx++).toString()).append(" ")
        }

        override fun getReplaces(text: Editable, indentation: Int): Array<Any> {
            var numberMargin = getListItemIndent() * (indentation - 1)
            if (indentation > 2) {
                // Same as in ordered lists: counter the effect of nested Spans
                numberMargin -= (indentation - 2) * getListItemIndent()
            }
            return arrayOf(LeadingMarginSpan.Standard(numberMargin))
        }
    }

    /**
     * Creates a new `<ul>` with start index of 1.
     */// default start index

    companion object {
        const val OL_TAG = "CUSTOM_OL"
        const val UL_TAG = "CUSTOM_UL"
        const val LI_TAG = "CUSTOM_LI"

        /**
         * List indentation in pixels. Nested lists use multiple of this.
         */

        private fun getIndentation() = 8.dpToPx()

        private fun getListItemIndent() = getIndentation() * 2
        private fun getStandardBullet() = ImprovedBulletSpan(gapWidth = getIndentation())
    }
}
package com.example.notificationapp.view.activities

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.notificationapp.R
import com.example.notificationapp.api.API
import com.example.notificationapp.app.Constants
import com.example.notificationapp.app.UserInstance
import com.example.notificationapp.databinding.ActivityCreatePostBinding
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.linkify.LinkifyPlugin

class CreatePostActivity : AppCompatActivity() {

    private lateinit var mClubID: String
    private lateinit var binding: ActivityCreatePostBinding

    private lateinit var mkdEditor: MarkwonEditor
    private lateinit var mkd: Markwon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValues()
        setListener()
    }

    private fun setListener() {
        binding.sendPost.setOnClickListener {
            val message: String = binding.etMessage.text?.toString() ?: ""
            if (message.isEmpty()) {
                Toast.makeText(this, "Can't post empty message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            API.sendPost(UserInstance.getAuthToken(this), mClubID, message, {
                Toast.makeText(this, "Post Sent", Toast.LENGTH_SHORT).show()
                finish()
            }) { Toast.makeText(this, "$it: Unable to post", Toast.LENGTH_SHORT).show() }
        }

        binding.etMessage.addTextChangedListener { MarkwonEditorTextWatcher.withProcess(mkdEditor) }

        binding.previewChip.setOnCheckedChangeListener { _, isChecked ->
            val focus = currentFocus
            if (focus != null) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(focus.windowToken, 0)
            }

            binding.etMessage.isVisible = !isChecked
            binding.mkdTest.isVisible = isChecked
            if (isChecked) {
                mkd.setMarkdown(binding.mkdTest, binding.etMessage.text?.toString() ?: "")
                binding.previewChip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)))
                binding.previewChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_color))
            } else {
                binding.previewChip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)))
                binding.previewChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
            }
        }
    }

    private fun setValues() {
        mkd = Markwon.builder(this)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(TablePlugin.create(this))
            .build()
        mkdEditor = MarkwonEditor.create(mkd)

        val clubID = intent.getStringExtra(Constants.CLUB_ID)
        val clubName = intent.getStringExtra(Constants.CLUB_NAME)
        binding.clubName.text = clubName
        if (clubID == null) {
            Toast.makeText(this, "Error: Club ID NULL", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        mClubID = clubID
    }
}
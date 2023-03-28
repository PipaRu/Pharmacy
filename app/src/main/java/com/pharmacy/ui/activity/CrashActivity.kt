package com.pharmacy.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pharmacy.R
import com.pharmacy.core.logger.Logger
import com.pharmacy.databinding.CrashActivityBinding

class CrashActivity : AppCompatActivity(R.layout.crash_activity) {

    companion object {
        fun createIntent(context: Context, text: String): Intent {
            return Intent(
                context,
                CrashActivity::class.java,
            ).apply {
                putExtra("message", text)
            }
        }
    }

    private val viewBinding by viewBinding(CrashActivityBinding::bind, R.id.content_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = this.intent.extras?.getString("message")?.takeIf { it.isNotEmpty() } ?: "Ooops..."
        viewBinding.textView.text = message
        viewBinding.button.setOnClickListener {
            startActivity(Intent(this@CrashActivity, MainActivity::class.java))
            finish()
        }

    }

}
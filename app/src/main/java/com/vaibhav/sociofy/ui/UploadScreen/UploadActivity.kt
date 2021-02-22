package com.vaibhav.sociofy.ui.UploadScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.ActivityUploadBinding
import com.vaibhav.sociofy.util.Constants.IMAGE_REQUEST_CODE
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.showErrorToast
import com.vaibhav.sociofy.util.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val viewModel: UploadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.uploadButton.setOnClickListener {
            val description = binding.descriptionText.text.toString().trim()
            viewModel.uploadImage(description)
            Timber.d(viewModel.uri.toString())
        }
        binding.imageSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uploadStatus.collect { status ->
                when (status) {
                    is Status.Success -> showViewForSuccess()
                    Status.Loading -> showViewForLoading()
                    is Status.Error -> showViewsForError(status.error)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_enter_anim, R.anim.bottom_exit_anim)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri = data?.data
            viewModel.uri = uri
            binding.imageSelect.setImageURI(uri)
        }
    }


    private fun showViewForSuccess() {
        showSuccessToast(this, this, "Uploaded Successfully")
        binding.loadingAnim.isVisible = false
        finish()
    }

    private fun showViewForLoading() {
        binding.loadingAnim.isVisible = true
    }

    private fun showViewsForError(message: String) {
        binding.loadingAnim.isVisible = false
        showErrorToast(this, this, message)
    }

}
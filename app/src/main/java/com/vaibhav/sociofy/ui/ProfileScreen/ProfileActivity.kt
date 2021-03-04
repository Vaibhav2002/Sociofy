package com.vaibhav.sociofy.ui.ProfileScreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.databinding.ActivityProfileBinding
import com.vaibhav.sociofy.util.Constants.SHOW_POST_DIALOG
import com.vaibhav.sociofy.util.Shared.GridPostsAdapter
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.checkDarkMode
import com.vaibhav.sociofy.util.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var user: User
    val viewModel: ProfileViewModel by viewModels()
    private lateinit var postAdapter: GridPostsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = intent.extras?.getSerializable("user") as User
        Timber.d(user.toString())
        postAdapter = GridPostsAdapter(onImageClick = {
            PostDetailDialog(viewModel.userId, it).show(supportFragmentManager, SHOW_POST_DIALOG)
        })
        viewModel.fetchUserPosts(user.id)
        binding.postRecycler.apply {
            adapter = postAdapter
            setHasFixedSize(false)
        }
        binding.user = user
        Glide.with(this).load(user.profileImg).error(R.drawable.blankuserimg)
            .into(binding.circularImageView)

        viewModel.currentUser.observe(this, Observer {
            if (it.following.containsKey(user.id)) {
                binding.apply {
                    followButton.setBackgroundColor(getColor(R.color.colorPrimary))
                    followButton.text = "Following"
                    followButton.isEnabled = false
                    unfollowButton.isEnabled = true
                }
            } else {
                binding.apply {
                    if (checkDarkMode(this@ProfileActivity))
                        followButton.setBackgroundColor(getColor(R.color.surface_dark))
                    else
                        followButton.setBackgroundColor(getColor(R.color.surface_light))
                    followButton.isEnabled = true
                    unfollowButton.isEnabled = false
                }
            }
        })

        binding.followButton.setOnClickListener {
            viewModel.followUser(user)
        }
        binding.unfollowButton.setOnClickListener {
            viewModel.unFollowUser(user)
        }

        viewModel.userPosts.observe(this, { posts ->
            binding.postsCount.text = posts.size.toString()
            postAdapter.submitList(posts)
        })



        lifecycleScope.launchWhenStarted {
            viewModel.profileStatus.collect { status ->
                when (status) {
                    Status.Loading -> binding.loadingAnim.isVisible =
                        true
                    Status.Success -> binding.loadingAnim.isVisible =
                        false
                    is Status.Error -> showViewsForError(status.error)
                }
            }
        }


    }

    //error
    private fun showViewsForError(message: String) {
        binding.loadingAnim.isVisible = false
        showErrorToast(this, this, message)
    }
}
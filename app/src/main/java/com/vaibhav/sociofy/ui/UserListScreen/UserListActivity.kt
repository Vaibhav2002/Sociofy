package com.vaibhav.sociofy.ui.UserListScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.databinding.ActivityUserListBinding
import com.vaibhav.sociofy.ui.ProfileScreen.ProfileActivity
import com.vaibhav.sociofy.util.Constants
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.Shared.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserListBinding
    private lateinit var userListAdapter: UserListAdapter
    private val viewModel: UserListViewModel by viewModels()
    private lateinit var user: User
    private lateinit var type: Constants.LIST_FOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = intent.extras?.getSerializable("user") as User
        type = intent.extras?.getSerializable("type") as Constants.LIST_FOR
        if (type == Constants.LIST_FOR.Followers)
            viewModel.getUsersList(user.followers)
        else
            viewModel.getUsersList(user.following)

        binding.heading.text = type.name


        userListAdapter = UserListAdapter { user ->
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        binding.userListRecycle.apply {
            adapter = userListAdapter
            setHasFixedSize(false)
        }

        viewModel.userList.observe(this, {
            Timber.d("$it")
            userListAdapter.submitList(it)
        })
        lifecycleScope.launchWhenStarted {
            viewModel.userListStatus.collect {
                when (it) {
                    Status.Loading -> Timber.d("Loading")
                    Status.Success -> Timber.d("Success")
                    is Status.Error -> Timber.d("Error ${it.error}")
                }
            }
        }


    }
}
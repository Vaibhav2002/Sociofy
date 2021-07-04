package com.vaibhav.sociofy.util

import com.vaibhav.sociofy.data.models.Notification
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.data.models.User

object Constants {

    const val loginSuccessMessage = "Successfully logged in"
    const val loginFailureMessage = "Failed to login"
    const val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et"
    const val IMAGE_REQUEST_CODE = 1
    const val SHOW_POST_DIALOG = "show_post_dialog"
    const val DEFAULT_ERROR = "Oops something went wrong"

    enum class LIST_FOR() {
        Followers, Following
    }

    enum class THEME {
        NIGHT, DAY
    }


    val sampleFeedList = listOf(
        Post(
            username = "vaibhav2511",
            timeStamp = System.currentTimeMillis() - 1800000,
            description = "Beautiful Shot 😎",
            likes = 5,
            profileImg = "https://images.unsplash.com/photo-1624558525725-98fced271018?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDQ1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            url = "https://images.unsplash.com/photo-1625125887424-ac3d7b1b96b5?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDR8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            username = "vaibhav2511",
            timeStamp = System.currentTimeMillis() - 1500000,
            description = "Beautiful Shot 😎",
            likes = 5,
            profileImg = "https://images.unsplash.com/photo-1624652782185-90a7237fc37e?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM5fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            url = "https://images.unsplash.com/photo-1558227108-83a15ddbbb15?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDEzfHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        )
    )

    val sampleUserList = listOf(
        User(
            id = "1",
            username = "Jonas",
            profileImg = "https://images.unsplash.com/photo-1624652782185-90a7237fc37e?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM5fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
        ),
        User(
            id = "2",
            username = "Mark",
            profileImg = "https://images.unsplash.com/photo-1624558525725-98fced271018?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDQ1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
        ),
        User(
            id = "3",
            username = "Sasha",
            profileImg = "https://images.unsplash.com/photo-1625125887424-ac3d7b1b96b5?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDR8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        User(
            id = "4",
            username = "Clark",
            profileImg = "https://images.unsplash.com/photo-1624644489153-36d7b5956786?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDI5fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        User(
            id = "5",
            username = "Stephen",
            profileImg = "https://images.unsplash.com/photo-1623970142870-86265570cef1?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        User(
            id = "6",
            username = "Clint",
            profileImg = "https://images.unsplash.com/photo-1625008165193-addddb023516?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
    )

    val sampleAllPostList = listOf(
        Post(
            username = "Sasha",
            timeStamp = System.currentTimeMillis() - 60 * 60 * 1000,
            likes = 3000,
            description = "What a lovely day",
            url = "https://images.unsplash.com/photo-1558227108-83a15ddbbb15?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDEzfHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624352907931-318b3bb70bfc?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDN8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1625125887424-ac3d7b1b96b5?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDR8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624887585535-c51378c4c81e?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624918201580-388eae33e802?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE2fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1625008165193-addddb023516?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624877815516-9cc0ecea541a?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDI2fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624644489153-36d7b5956786?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDI5fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1623970142870-86265570cef1?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1611662781749-2d208fec7e44?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDYyfHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1614880353165-e56fac2ea9a8?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDY3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1558227108-83a15ddbbb15?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDEzfHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624352907931-318b3bb70bfc?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDN8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1625125887424-ac3d7b1b96b5?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDR8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624887585535-c51378c4c81e?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624918201580-388eae33e802?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE2fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1625008165193-addddb023516?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624877815516-9cc0ecea541a?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDI2fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1624644489153-36d7b5956786?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDI5fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1623970142870-86265570cef1?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1611662781749-2d208fec7e44?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDYyfHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Post(
            url = "https://images.unsplash.com/photo-1614880353165-e56fac2ea9a8?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDY3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        )
    )

    val sampleNotificationList = listOf<Notification>(
        Notification(
            uid = System.currentTimeMillis().toString(),
            username = "Laura",
            postImg = "https://images.unsplash.com/photo-1624652782185-90a7237fc37e?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM5fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            timestamp = System.currentTimeMillis() - 5 * 60 * 1000L,
            profilePic = "https://images.unsplash.com/photo-1625125887424-ac3d7b1b96b5?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDR8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Notification(
            uid = System.currentTimeMillis().toString(),
            username = "Mark",
            postImg = "https://images.unsplash.com/photo-1558227108-83a15ddbbb15?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDEzfHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            timestamp = System.currentTimeMillis() - 20 * 60 * 1000L,
            profilePic = "https://images.unsplash.com/photo-1624558525725-98fced271018?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDQ1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
        ),
        Notification(
            uid = System.currentTimeMillis().toString(),
            username = "Clint",
            postImg = "https://images.unsplash.com/photo-1625125887424-ac3d7b1b96b5?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDR8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            timestamp = System.currentTimeMillis() - 40 * 60 * 1000L,
            profilePic = "https://images.unsplash.com/photo-1625125887424-ac3d7b1b96b5?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDR8dG93SlpGc2twR2d8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Notification(
            uid = System.currentTimeMillis().toString(),
            username = "Steve",
            postImg = "https://images.unsplash.com/photo-1614880353165-e56fac2ea9a8?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDY3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            timestamp = System.currentTimeMillis() - 120 * 60 * 1000L,
            profilePic = "https://images.unsplash.com/photo-1623970142870-86265570cef1?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Notification(
            uid = System.currentTimeMillis().toString(),
            username = "Peter",
            postImg = "https://images.unsplash.com/photo-1623970142870-86265570cef1?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            timestamp = System.currentTimeMillis() - 150 * 60 * 1000L,
            profilePic = "https://images.unsplash.com/photo-1624644489153-36d7b5956786?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDI5fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        ),
        Notification(
            uid = System.currentTimeMillis().toString(),
            username = "Anthony",
            postImg = "https://images.unsplash.com/photo-1625008165193-addddb023516?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
            timestamp = System.currentTimeMillis() - 180 * 60 * 1000L,
            profilePic = "https://images.unsplash.com/photo-1625008165193-addddb023516?ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDE3fHRvd0paRnNrcEdnfHxlbnwwfHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"
        )


    )


}
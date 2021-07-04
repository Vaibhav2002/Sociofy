package com.vaibhav.sociofy.data.models

data class PushNotificationItem(
    val title: String = "",
    val description: String = ""
)

data class PushNotification(
    val data: PushNotificationItem,
    val to: String
)

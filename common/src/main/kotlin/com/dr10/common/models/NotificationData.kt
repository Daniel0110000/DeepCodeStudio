package com.dr10.common.models

import com.dr10.common.ui.notification.NotificationType
import com.dr10.common.utilities.ErrorType

data class NotificationData(
    val message: String = "",
    val errorType: ErrorType = ErrorType.CUSTOM,
    val type: NotificationType = NotificationType.INFO,
    val isAutoDismiss: Boolean = false,
    val delay: Int = 5000,
    val xMargin: Int = 0,
    val yMargin: Int = 0
)

package com.dr10.settings.ui.viewModels

import com.dr10.common.models.NotificationData
import com.dr10.common.ui.notification.NotificationType
import com.dr10.common.utilities.ErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class SettingsNotificationsViewModel {

    private val _state = MutableStateFlow(SettingsNotificationsState())
    val state: StateFlow<SettingsNotificationsState> = _state.asStateFlow()

    data class SettingsNotificationsState(
        val data: NotificationData? = null,
        val notificationIdToClose: UUID? = null
    )

    fun showNotification(
        notificationId: UUID = UUID.randomUUID(),
        message: String = "",
        type: NotificationType = NotificationType.INFO,
        errorType: ErrorType = ErrorType.CUSTOM,
        isAutoDismiss: Boolean = true
    ) {
        updateState {
            copy(data = NotificationData(
                notificationId = notificationId,
                message = message,
                type = type,
                isAutoDismiss = isAutoDismiss,
                errorType = errorType
            ))
        }
    }

    fun closeNotificationById(id: UUID) {
        updateState {
            copy(notificationIdToClose = id)
        }
    }

    fun clearNotificationData() {
        updateState { copy(null) }
    }

    fun clearNotificationId() { updateState { copy(notificationIdToClose = null) } }

    private fun updateState(update: SettingsNotificationsState.() -> SettingsNotificationsState) { _state.update(update) }
}
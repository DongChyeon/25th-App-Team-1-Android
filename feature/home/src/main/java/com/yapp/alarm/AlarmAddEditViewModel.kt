package com.yapp.alarm

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.util.ResourceProvider
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.toAlarmDays
import com.yapp.domain.model.toDayOfWeek
import com.yapp.domain.usecase.AlarmUseCase
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.home.R
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmAddEditViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase,
    private val resourceProvider: ResourceProvider,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AlarmAddEditContract.State, AlarmAddEditContract.SideEffect>(
    initialState = AlarmAddEditContract.State(),
) {
    private val alarmId: Long = savedStateHandle.get<Long>("alarmId") ?: -1

    init {
        updateState {
            copy(mode = if (alarmId == -1L) AlarmAddEditContract.EditMode.ADD else AlarmAddEditContract.EditMode.EDIT)
        }

        initializeAlarmWithSounds()
    }

    private fun initializeAlarmWithSounds() = viewModelScope.launch {
        alarmUseCase.getAlarmSounds().onSuccess { sounds ->
            if (alarmId != -1L) {
                alarmUseCase.getAlarm(alarmId).onSuccess { alarm ->
                    val repeatDays = alarm.repeatDays.toAlarmDays()
                    val isAM = alarm.hour < 12
                    val hour = if (isAM) alarm.hour else alarm.hour - 12

                    updateState {
                        copy(
                            initialLoading = false,
                            timeState = timeState.copy(
                                initialAmPm = if (isAM) "오전" else "오후",
                                initialHour = "$hour",
                                initialMinute = alarm.minute.toString().padStart(2, '0'),
                                currentAmPm = if (isAM) "오전" else "오후",
                                currentHour = hour,
                                currentMinute = alarm.minute,
                                alarmMessage = getAlarmMessage(if (isAM) "오전" else "오후", hour, alarm.minute, repeatDays),
                            ),
                            daySelectionState = daySelectionState.copy(
                                selectedDays = repeatDays,
                                isWeekdaysChecked = repeatDays.containsAll(
                                    setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI),
                                ),
                                isWeekendsChecked = repeatDays.containsAll(setOf(AlarmDay.SAT, AlarmDay.SUN)),
                            ),
                            holidayState = holidayState.copy(isDisableHolidayChecked = alarm.isHolidayAlarmOff),
                            snoozeState = snoozeState.copy(
                                isSnoozeEnabled = alarm.isSnoozeEnabled,
                                snoozeIntervalIndex = snoozeState.snoozeIntervals.indexOfFirst { it.startsWith("${alarm.snoozeInterval}") }
                                    .takeIf { it >= 0 } ?: 0, // 값이 없으면 기본값 0 사용
                                snoozeCountIndex = snoozeState.snoozeCounts.indexOfFirst { it.startsWith("${alarm.snoozeCount}") }
                                    .takeIf { it >= 0 } ?: 0,
                            ),
                            soundState = soundState.copy(
                                isVibrationEnabled = alarm.isVibrationEnabled,
                                isSoundEnabled = alarm.isSoundEnabled,
                                soundVolume = alarm.soundVolume,
                                sounds = sounds,
                                soundIndex = sounds.indexOfFirst { sound -> sound.uri.toString() == alarm.soundUri },
                            ),
                        )
                    }
                }
            } else {
                val homecomingIndex = sounds.indexOfFirst { it.title == "Homecoming" }

                updateState {
                    copy(
                        initialLoading = false,
                        soundState = soundState.copy(
                            sounds = sounds,
                            soundIndex = homecomingIndex.takeIf { it >= 0 } ?: 0,
                        ),
                    )
                }
            }
        }.onFailure {
            Log.e("AlarmAddEditViewModel", "Failed to get alarm sounds", it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        alarmUseCase.releaseSoundPlayer()
    }

    fun processAction(action: AlarmAddEditContract.Action) {
        when (action) {
            is AlarmAddEditContract.Action.ClickBack -> navigateBack()
            is AlarmAddEditContract.Action.ClickSave -> saveAlarm()
            is AlarmAddEditContract.Action.UpdateAlarmTime -> updateAlarmTime(action.amPm, action.hour, action.minute)
            is AlarmAddEditContract.Action.ToggleWeekdaysChecked -> toggleWeekdaysChecked()
            is AlarmAddEditContract.Action.ToggleWeekendsChecked -> toggleWeekendsChecked()
            is AlarmAddEditContract.Action.ToggleDaySelection -> toggleDaySelection(action.day)
            is AlarmAddEditContract.Action.ToggleDisableHolidayChecked -> toggleDisableHolidayChecked()
            is AlarmAddEditContract.Action.ToggleSnoozeEnabled -> toggleSnoozeEnabled()
            is AlarmAddEditContract.Action.UpdateSnoozeInterval -> updateSnoozeInterval(action.index)
            is AlarmAddEditContract.Action.UpdateSnoozeCount -> updateSnoozeCount(action.index)
            is AlarmAddEditContract.Action.ToggleVibrationEnabled -> toggleVibrationEnabled()
            is AlarmAddEditContract.Action.ToggleSoundEnabled -> toggleSoundEnabled()
            is AlarmAddEditContract.Action.UpdateSoundVolume -> updateSoundVolume(action.volume)
            is AlarmAddEditContract.Action.UpdateSoundIndex -> updateSoundIndex(action.index)
            is AlarmAddEditContract.Action.ToggleBottomSheetOpen -> toggleBottomSheet(action.sheetType)
        }
    }

    private fun navigateBack() {
        emitSideEffect(AlarmAddEditContract.SideEffect.NavigateBack)
    }

    private fun saveAlarm() {
        val newAlarm = currentState.toAlarm()

        viewModelScope.launch {
            alarmUseCase.getAlarmsByTime(newAlarm.hour, newAlarm.minute, newAlarm.isAm)
                .collect { timeMatchedAlarms ->
                    val exactMatch = timeMatchedAlarms.find { it.copy(id = 0) == newAlarm.copy(id = 0) }
                    if (exactMatch != null) {
                        emitSideEffect(
                            AlarmAddEditContract.SideEffect.ShowSnackBar(
                                message = resourceProvider.getString(R.string.alarm_already_set),
                                iconRes = resourceProvider.getDrawable(core.designsystem.R.drawable.ic_alert),
                                bottomPadding = 78.dp,
                                duration = SnackbarDuration.Short,
                                onDismiss = { },
                                onAction = { },
                            ),
                        )
                        return@collect
                    }

                    val timeMatch = timeMatchedAlarms.firstOrNull()
                    if (timeMatch != null) {
                        val updatedAlarm = timeMatch.copy(
                            repeatDays = newAlarm.repeatDays,
                            isHolidayAlarmOff = newAlarm.isHolidayAlarmOff,
                            isSnoozeEnabled = newAlarm.isSnoozeEnabled,
                            snoozeInterval = newAlarm.snoozeInterval,
                            snoozeCount = newAlarm.snoozeCount,
                            isVibrationEnabled = newAlarm.isVibrationEnabled,
                            isSoundEnabled = newAlarm.isSoundEnabled,
                            soundUri = newAlarm.soundUri,
                            soundVolume = newAlarm.soundVolume,
                            isAlarmActive = newAlarm.isAlarmActive,
                        )

                        alarmUseCase.updateAlarm(updatedAlarm)
                            .onSuccess {
                                emitSideEffect(AlarmAddEditContract.SideEffect.UpdateAlarm(it.id))
                            }
                            .onFailure {
                                Log.e("AlarmAddEditViewModel", "Failed to update alarm", it)
                            }
                    } else {
                        alarmUseCase.insertAlarm(newAlarm)
                            .onSuccess {
                                emitSideEffect(AlarmAddEditContract.SideEffect.SaveAlarm(it.id))
                            }
                            .onFailure {
                                Log.e("AlarmAddEditViewModel", "Failed to insert alarm", it)
                            }
                    }
                }
        }
    }

    private fun updateAlarmTime(amPm: String, hour: Int, minute: Int) {
        val newTimeState = currentState.timeState.copy(
            currentAmPm = amPm,
            currentHour = hour,
            currentMinute = minute,
            alarmMessage = getAlarmMessage(amPm, hour, minute, currentState.daySelectionState.selectedDays),
        )
        updateState {
            copy(timeState = newTimeState)
        }
    }

    private fun toggleWeekdaysChecked() {
        val weekdays = setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI)
        val isChecked = !currentState.daySelectionState.isWeekdaysChecked
        val updatedDays = if (isChecked) {
            currentState.daySelectionState.selectedDays + weekdays
        } else {
            currentState.daySelectionState.selectedDays - weekdays
        }
        val newDayState = currentState.daySelectionState.copy(
            isWeekdaysChecked = isChecked,
            selectedDays = updatedDays,
        )
        updateState {
            copy(
                timeState = timeState.copy(
                    alarmMessage = getAlarmMessage(timeState.currentAmPm, timeState.currentHour, timeState.currentMinute, newDayState.selectedDays),
                ),
                daySelectionState = newDayState,
                holidayState = holidayState.copy(
                    isDisableHolidayEnabled = newDayState.selectedDays.isNotEmpty(),
                    isDisableHolidayChecked = if (newDayState.selectedDays.isEmpty()) false else holidayState.isDisableHolidayChecked,
                ),
            )
        }
    }

    private fun toggleWeekendsChecked() {
        val weekends = setOf(AlarmDay.SAT, AlarmDay.SUN)
        val isChecked = !currentState.daySelectionState.isWeekendsChecked
        val updatedDays = if (isChecked) {
            currentState.daySelectionState.selectedDays + weekends
        } else {
            currentState.daySelectionState.selectedDays - weekends
        }
        val newDayState = currentState.daySelectionState.copy(
            isWeekendsChecked = isChecked,
            selectedDays = updatedDays,
        )
        updateState {
            copy(
                timeState = timeState.copy(
                    alarmMessage = getAlarmMessage(timeState.currentAmPm, timeState.currentHour, timeState.currentMinute, newDayState.selectedDays),
                ),
                daySelectionState = newDayState,
                holidayState = holidayState.copy(
                    isDisableHolidayEnabled = newDayState.selectedDays.isNotEmpty(),
                    isDisableHolidayChecked = if (newDayState.selectedDays.isEmpty()) false else holidayState.isDisableHolidayChecked,
                ),
            )
        }
    }

    private fun toggleDaySelection(day: AlarmDay) {
        val updatedDays = if (day in currentState.daySelectionState.selectedDays) {
            currentState.daySelectionState.selectedDays - day
        } else {
            currentState.daySelectionState.selectedDays + day
        }
        val weekdays = setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI)
        val weekends = setOf(AlarmDay.SAT, AlarmDay.SUN)

        val newDayState = currentState.daySelectionState.copy(
            selectedDays = updatedDays,
            isWeekdaysChecked = updatedDays.containsAll(weekdays),
            isWeekendsChecked = updatedDays.containsAll(weekends),
        )
        updateState {
            copy(
                timeState = timeState.copy(
                    alarmMessage = getAlarmMessage(timeState.currentAmPm, timeState.currentHour, timeState.currentMinute, newDayState.selectedDays),
                ),
                daySelectionState = newDayState,
                holidayState = holidayState.copy(
                    isDisableHolidayEnabled = newDayState.selectedDays.isNotEmpty(),
                    isDisableHolidayChecked = if (newDayState.selectedDays.isEmpty()) false else holidayState.isDisableHolidayChecked,
                ),
            )
        }
    }

    private fun toggleDisableHolidayChecked() {
        val newHolidayState = currentState.holidayState.copy(
            isDisableHolidayChecked = !currentState.holidayState.isDisableHolidayChecked,
        )

        updateState {
            copy(holidayState = newHolidayState)
        }

        if (newHolidayState.isDisableHolidayChecked) {
            emitSideEffect(
                AlarmAddEditContract.SideEffect.ShowSnackBar(
                    message = resourceProvider.getString(R.string.alarm_disabled_warning),
                    label = resourceProvider.getString(R.string.alarm_delete_dialog_btn_cancel),
                    iconRes = resourceProvider.getDrawable(core.designsystem.R.drawable.ic_check_green),
                    bottomPadding = 78.dp,
                    duration = SnackbarDuration.Short,
                    onDismiss = { },
                    onAction = {
                        updateState {
                            copy(holidayState = holidayState.copy(isDisableHolidayChecked = false))
                        }
                    },
                ),
            )
        }
    }

    private fun toggleSnoozeEnabled() {
        val newSnoozeState = currentState.snoozeState.copy(
            isSnoozeEnabled = !currentState.snoozeState.isSnoozeEnabled,
        )
        updateState {
            copy(snoozeState = newSnoozeState)
        }
    }

    private fun updateSnoozeInterval(index: Int) {
        val newSnoozeState = currentState.snoozeState.copy(snoozeIntervalIndex = index)
        updateState {
            copy(snoozeState = newSnoozeState)
        }
    }

    private fun updateSnoozeCount(index: Int) {
        val newSnoozeState = currentState.snoozeState.copy(snoozeCountIndex = index)
        updateState {
            copy(snoozeState = newSnoozeState)
        }
    }

    private fun toggleVibrationEnabled() {
        val newSoundState = currentState.soundState.copy(isVibrationEnabled = !currentState.soundState.isVibrationEnabled)
        updateState {
            copy(soundState = newSoundState)
        }
    }

    private fun toggleSoundEnabled() {
        val newSoundState = currentState.soundState.copy(isSoundEnabled = !currentState.soundState.isSoundEnabled)
        if (newSoundState.isSoundEnabled) {
            alarmUseCase.playAlarmSound(
                alarmSound = currentState.soundState.sounds[currentState.soundState.soundIndex],
                volume = currentState.soundState.soundVolume,
            )
        } else {
            alarmUseCase.stopAlarmSound()
        }
        updateState {
            copy(soundState = newSoundState)
        }
    }

    private fun updateSoundVolume(volume: Int) {
        val newSoundState = currentState.soundState.copy(soundVolume = volume)
        alarmUseCase.updateAlarmVolume(volume)
        updateState {
            copy(soundState = newSoundState)
        }
    }

    private fun updateSoundIndex(index: Int) {
        val newSoundState = currentState.soundState.copy(soundIndex = index)
        updateState {
            copy(soundState = newSoundState)
        }
        alarmUseCase.playAlarmSound(
            currentState.soundState.sounds[index],
            currentState.soundState.soundVolume,
        )
    }

    private fun toggleBottomSheet(sheetType: AlarmAddEditContract.BottomSheetType) {
        val newBottomSheetState = if (currentState.bottomSheetState == sheetType) {
            if (currentState.bottomSheetState == AlarmAddEditContract.BottomSheetType.SoundSetting) {
                alarmUseCase.stopAlarmSound()
            }
            null
        } else {
            sheetType
        }
        updateState {
            copy(bottomSheetState = newBottomSheetState)
        }
    }

    private fun getAlarmMessage(amPm: String, hour: Int, minute: Int, selectedDays: Set<AlarmDay>): String {
        val now = java.time.LocalDateTime.now()
        val alarmHour = convertTo24HourFormat(amPm, hour)
        val alarmTimeToday = now.toLocalDate().atTime(alarmHour, minute)
        val nextAlarmDateTime = calculateNextAlarmDateTime(now, alarmTimeToday, selectedDays)
        val duration = java.time.Duration.between(now, nextAlarmDateTime)
        val totalMinutes = duration.toMinutes()
        val days = totalMinutes / (24 * 60)
        val hours = (totalMinutes % (24 * 60)) / 60
        val minutes = totalMinutes % 60

        return when {
            days > 0 -> "${days}일 ${hours}시간 후에 울려요"
            hours > 0 -> "${hours}시간 ${minutes}분 후에 울려요"
            else -> "${minutes}분 후에 울려요"
        }
    }

    private fun convertTo24HourFormat(amPm: String, hour: Int): Int = when {
        amPm == "오후" && hour != 12 -> hour + 12
        amPm == "오전" && hour == 12 -> 0
        else -> hour
    }

    private fun calculateNextAlarmDateTime(
        now: java.time.LocalDateTime,
        alarmTimeToday: java.time.LocalDateTime,
        selectedDays: Set<AlarmDay>,
    ): java.time.LocalDateTime {
        if (selectedDays.isEmpty()) {
            return if (alarmTimeToday.isBefore(now)) {
                alarmTimeToday.plusDays(1)
            } else {
                alarmTimeToday
            }
        }

        val currentDayOfWeek = now.dayOfWeek.value
        val selectedDaysOfWeek = selectedDays.map { it.toDayOfWeek().value }.sorted()
        val nextDay = selectedDaysOfWeek.firstOrNull { it > currentDayOfWeek }
            ?: selectedDaysOfWeek.first()
        val daysToAdd = if (nextDay > currentDayOfWeek) {
            nextDay - currentDayOfWeek
        } else {
            7 - (currentDayOfWeek - nextDay)
        }
        val nextAlarmDate = now.toLocalDate().plusDays(daysToAdd.toLong())
        return nextAlarmDate.atTime(alarmTimeToday.toLocalTime())
    }
}

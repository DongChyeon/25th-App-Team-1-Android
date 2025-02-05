package com.yapp.domain.usecase

import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmSound
import com.yapp.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend fun getAlarmSounds(): Result<List<AlarmSound>> = alarmRepository.getAlarmSounds()
    fun playAlarmSound(alarmSound: AlarmSound, volume: Int) = alarmRepository.playAlarmSound(alarmSound, volume)
    fun stopAlarmSound() = alarmRepository.stopAlarmSound()
    fun updateAlarmVolume(volume: Int) = alarmRepository.updateAlarmVolume(volume)
    fun releaseSoundPlayer() = alarmRepository.releaseSoundPlayer()
    suspend fun getPagedAlarms(limit: Int, offset: Int): Result<List<Alarm>> = alarmRepository.getPagedAlarms(limit, offset)
    suspend fun getAlarmsByTime(hour: Int, minute: Int): Result<List<Alarm>> = alarmRepository.getAlarmsByTime(hour, minute)
    suspend fun getAlarmCount(): Result<Int> = alarmRepository.getAlarmCount()
    suspend fun insertAlarm(alarm: Alarm): Result<Alarm> = alarmRepository.insertAlarm(alarm)
    suspend fun updateAlarm(alarm: Alarm): Result<Alarm> = alarmRepository.updateAlarm(alarm)
    suspend fun getAlarm(id: Long): Result<Alarm> = alarmRepository.getAlarm(id)
    suspend fun deleteAlarm(id: Long): Result<Unit> = alarmRepository.deleteAlarm(id)
}

package com.foryouandme.researchkit.recorder

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class RecorderServiceConnection(
    val serviceConnected: (RecorderService.RecorderServiceBinder) -> Unit,
    val serviceDisconnected: () -> Unit

) : ServiceConnection {

    override fun onServiceDisconnected(componentName: ComponentName?) {
        serviceDisconnected()
    }

    override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
        (service as? RecorderService.RecorderServiceBinder)?.let { serviceConnected(it) }
    }
}
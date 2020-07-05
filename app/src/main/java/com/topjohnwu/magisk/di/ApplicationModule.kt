package com.topjohnwu.magisk.di

import android.content.Context
import android.os.Build
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.topjohnwu.magisk.core.ResMgr
import com.topjohnwu.magisk.utils.RxBus
import org.koin.core.qualifier.named
import org.koin.dsl.module

val SUTimeout = named("su_timeout")
val Protected = named("protected")

val applicationModule = module {
    single { RxBus() }
    factory { ResMgr.resource }
    factory { get<Context>().packageManager }
    factory(Protected) { createDEContext(get()) }
    single(SUTimeout) { get<Context>(Protected).getSharedPreferences("su_timeout", 0) }
    single { PreferenceManager.getDefaultSharedPreferences(get<Context>(Protected)) }
    single { LocalBroadcastManager.getInstance(get()) }
}

private fun createDEContext(context: Context): Context {
    return if (Build.VERSION.SDK_INT >= 24)
        context.createDeviceProtectedStorageContext()
    else context
}

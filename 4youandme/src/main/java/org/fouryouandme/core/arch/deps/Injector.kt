package org.fouryouandme.core.arch.deps

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.navigation.Navigator

interface Injector {

    /* --- runtime --- */

    val runtimeContext: RuntimeContext

    /* --- navigation --- */

    val navigator: Navigator

    /* --- cache --- */

    val prefs: SharedPreferences

    /* --- json --- */

    val moshi: Moshi
}
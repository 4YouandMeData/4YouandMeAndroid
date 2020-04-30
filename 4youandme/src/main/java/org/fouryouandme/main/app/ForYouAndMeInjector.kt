package org.fouryouandme.main.app

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.deps.Injector
import org.fouryouandme.core.arch.deps.RuntimeContext
import org.fouryouandme.core.arch.navigation.Navigator

class ForYouAndMeInjector(val app: FourYouAndMeApp) : Injector {

    /* --- runtime --- */

    override val runtimeContext: RuntimeContext =
        RuntimeContext(Dispatchers.IO, Dispatchers.Main)

    /* --- navigator --- */

    override val navigator: Navigator =
        Navigator(ForYouAndMeNavigationProvider())

    /* --- cache --- */

    override val prefs: SharedPreferences by lazy {

        EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            app,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /* --- moshi --- */

    override val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}
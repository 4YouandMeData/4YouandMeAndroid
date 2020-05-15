package org.fouryouandme.main.app

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Injector
import org.fouryouandme.core.arch.deps.RuntimeContext
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.data.api.auth.AuthApi
import org.fouryouandme.core.data.api.configuration.ConfigurationApi
import org.fouryouandme.core.data.api.getApiService
import org.fouryouandme.main.app.navigation.ForYouAndMeNavigationProvider

class ForYouAndMeInjector(
    val app: FourYouAndMeApp,
    env: Environment,
    imageConfig: ImageConfiguration
) : Injector {

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

    /* --- environment --- */

    override val environment: Environment = env

    /* --- image configuration --- */

    override val imageConfiguration: ImageConfiguration = imageConfig

    /* --- moshi --- */

    override val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    /* --- api --- */

    override val configurationApi: ConfigurationApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val authApi: AuthApi =
        getApiService(environment.getApiBaseUrl(), moshi)
}
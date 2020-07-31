package org.fouryouandme.core.arch.deps

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import moe.banana.jsonapi2.ResourceAdapterFactory
import org.fouryouandme.core.arch.app.FourYouAndMeApp
import org.fouryouandme.core.arch.navigation.ForYouAndMeNavigationProvider
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.data.api.auth.AuthApi
import org.fouryouandme.core.data.api.common.response.AnswerResponse
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.data.api.common.response.QuestionResponse
import org.fouryouandme.core.data.api.common.response.UnknownResourceResponse
import org.fouryouandme.core.data.api.configuration.ConfigurationApi
import org.fouryouandme.core.data.api.consent.informed.ConsentInfoApi
import org.fouryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import org.fouryouandme.core.data.api.consent.review.ConsentReviewApi
import org.fouryouandme.core.data.api.consent.review.response.ConsentReviewResponse
import org.fouryouandme.core.data.api.consent.user.ConsentUserApi
import org.fouryouandme.core.data.api.consent.user.response.ConsentUserResponse
import org.fouryouandme.core.data.api.getApiService
import org.fouryouandme.core.data.api.integration.IntegrationApi
import org.fouryouandme.core.data.api.integration.response.IntegrationResponse
import org.fouryouandme.core.data.api.optins.OptInsApi
import org.fouryouandme.core.data.api.optins.response.OptInsPermissionResponse
import org.fouryouandme.core.data.api.optins.response.OptInsResponse
import org.fouryouandme.core.data.api.screening.ScreeningApi
import org.fouryouandme.core.data.api.screening.response.ScreeningResponse
import org.fouryouandme.core.data.api.task.TaskApi
import org.fouryouandme.core.data.api.task.response.TaskResponse

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

    override val moshi: Moshi =
        Moshi.Builder()
            .add(
                ResourceAdapterFactory.builder()
                    .add(UnknownResourceResponse::class.java)
                    .add(PageResponse::class.java)
                    .add(QuestionResponse::class.java)
                    .add(AnswerResponse::class.java)
                    .add(ScreeningResponse::class.java)
                    .add(ConsentInfoResponse::class.java)
                    .add(ConsentReviewResponse::class.java)
                    .add(ConsentUserResponse::class.java)
                    .add(OptInsPermissionResponse::class.java)
                    .add(OptInsResponse::class.java)
                    .add(IntegrationResponse::class.java)
                    .add(TaskResponse::class.java)
                    .build()
            )
            .add(KotlinJsonAdapterFactory())
            .build()


    /* --- api --- */

    override val configurationApi: ConfigurationApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val authApi: AuthApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val screeningApi: ScreeningApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val consentInfoApi: ConsentInfoApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val consentReviewApi: ConsentReviewApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val consentUserApi: ConsentUserApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val optInsApi: OptInsApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val integrationApi: IntegrationApi =
        getApiService(environment.getApiBaseUrl(), moshi)

    override val taskApi: TaskApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}
package gohlengann.apps.bitazza_test.di

import android.content.Context
import com.google.gson.Gson
//import com.tinder.scarlet.Scarlet
//import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
//import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
//import com.tinder.scarlet.retry.BackoffStrategy
//import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
//import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
//import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gohlengann.apps.bitazza_test.BuildConfig
import gohlengann.apps.bitazza_test.data.local.AppDatabase
import gohlengann.apps.bitazza_test.data.local.AuthenticatedUserDao
import gohlengann.apps.bitazza_test.data.local.InstrumentDao
import gohlengann.apps.bitazza_test.data.remote.repository.LoginRepository
import gohlengann.apps.bitazza_test.data.remote.repository.MainRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Provides
    internal fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    internal fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(httpClient)
            .build()

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Provides
    fun provideGson() = Gson()

//    @Provides
//    fun provideBackOffStrategy(): BackoffStrategy = ExponentialWithJitterBackoffStrategy(5000, 5000)
//
//    @Provides
//    fun provideWSService(okHttpClient: OkHttpClient, backoffStrategy: BackoffStrategy, application: Application): WSService = Scarlet.Builder()
//        .webSocketFactory(okHttpClient.newWebSocketFactory(BuildConfig.SOCKET_BASE_URL))
//        .addMessageAdapterFactory(GsonMessageAdapter.Factory())
//        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
//        .backoffStrategy(backoffStrategy)
//        .lifecycle(AndroidLifecycle.ofApplicationForeground(application))
//        .build()
//        .create()

    @Provides
    fun provideAuthenticatedUserDao(appDatabase: AppDatabase) = appDatabase.authenticatedUserDao()

    @Provides
    fun provideInstrumentDao(appDatabase: AppDatabase) = appDatabase.instrumentDao()

    @Provides
    fun provideWSRequest() = Request.Builder().url(BuildConfig.SOCKET_BASE_URL).build()

    @Provides
    fun provideMainRepository(
        okHttpClient: OkHttpClient,
        request: Request,
        gson: Gson,
        authenticatedUserDao: AuthenticatedUserDao,
        instrumentDao: InstrumentDao
    ) = MainRepository(okHttpClient, request, gson, authenticatedUserDao, instrumentDao)

    @Provides
    fun provideLoginRepository(
        okHttpClient: OkHttpClient,
        request: Request,
        gson: Gson,
        authenticatedUserDao: AuthenticatedUserDao
    ) = LoginRepository(okHttpClient, request, gson, authenticatedUserDao)
}
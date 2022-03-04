package gohlengann.apps.bitazza_test.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import gohlengann.apps.bitazza_test.BuildConfig
import gohlengann.apps.bitazza_test.data.local.AppDatabase
import gohlengann.apps.bitazza_test.data.local.AuthenticatedUserDao
import gohlengann.apps.bitazza_test.data.local.InstrumentDao
import gohlengann.apps.bitazza_test.data.local.ProductDao
import gohlengann.apps.bitazza_test.data.remote.repository.LoginRepository
import gohlengann.apps.bitazza_test.data.remote.repository.MainRepository
import gohlengann.apps.bitazza_test.data.remote.service.Level1SummaryService
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
    internal fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(httpClient)
            .build()

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    fun provideAuthenticatedUserDao(appDatabase: AppDatabase) = appDatabase.authenticatedUserDao()

    @Provides
    fun provideInstrumentDao(appDatabase: AppDatabase) = appDatabase.instrumentDao()

    @Provides
    fun provideProductDao(appDatabase: AppDatabase) = appDatabase.productDao()

    @Provides
    fun provideWSRequest() = Request.Builder().url(BuildConfig.SOCKET_BASE_URL).build()

    @Provides
    fun provideMainRepository(
        okHttpClient: OkHttpClient,
        request: Request,
        gson: Gson,
        authenticatedUserDao: AuthenticatedUserDao,
        instrumentDao: InstrumentDao,
        productDao: ProductDao,
        level1SummaryService: Level1SummaryService
    ) = MainRepository(okHttpClient, request, gson, authenticatedUserDao, instrumentDao, productDao, level1SummaryService)

    @Provides
    fun provideLoginRepository(
        okHttpClient: OkHttpClient,
        request: Request,
        gson: Gson,
        authenticatedUserDao: AuthenticatedUserDao
    ) = LoginRepository(okHttpClient, request, gson, authenticatedUserDao)

    @Provides
    fun provideLevel1SummaryService(retrofit: Retrofit): Level1SummaryService = retrofit.create(
        Level1SummaryService::class.java
    )
}
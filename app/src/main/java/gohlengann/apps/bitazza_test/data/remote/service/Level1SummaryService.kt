package gohlengann.apps.bitazza_test.data.remote.service

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Level1SummaryService {
    @GET("GetLevel1SummaryMin")
    fun getLevel1Summary(
        @Query("OMSId") oms_id: Long,
        @Query("Quotecurrency") base_currency: String
    ): Single<Response<List<List<String>>>>
}
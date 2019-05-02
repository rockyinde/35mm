package free.rm.skytube.rest.service;

import free.rm.skytube.rest.entity.elastic.ElasticSearchResponse;
import free.rm.skytube.rest.entity.elastic.SearchRequestEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ElasticService {

    @POST("_search")
    Call<ElasticSearchResponse> search (@Body SearchRequestEntity request);
}

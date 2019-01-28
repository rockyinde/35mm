package free.rm.skytube.rest.service;

import java.util.List;

import free.rm.skytube.rest.entity.MMSFetchVideosRequest;
import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.entity.SeventyMMVideo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SeventyMMService {

    @POST("videos")
    Call<MMSFetchVideosResponse> getVideos (@Body MMSFetchVideosRequest request);

    @POST("save")
    Call<SeventyMMVideo> save (@Body SeventyMMVideo video);
}

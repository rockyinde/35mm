package free.rm.skytube.rest.service;

import java.util.List;

import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.entity.SeventyMMVideo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SeventyMMService {

    @GET("videos")
    Call<MMSFetchVideosResponse> fetchVideos (@Query("category") String category);
}

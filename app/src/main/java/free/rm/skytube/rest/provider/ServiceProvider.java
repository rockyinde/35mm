package free.rm.skytube.rest.provider;

import java.io.IOException;
import java.util.List;

import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.entity.SeventyMMVideo;
import free.rm.skytube.rest.service.SeventyMMService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceProvider {

    public static final SeventyMMService SEVENTY_MM_SERVICE =
            new Retrofit.Builder()
            .baseUrl("http://mysfits-nlb-d54bc96e93537702.elb.eu-west-1.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SeventyMMService.class);

    public static SeventyMMService getSeventyMmService() {

        return SEVENTY_MM_SERVICE;
    }

    public static void main (String[] args) throws IOException {

        Call<MMSFetchVideosResponse> call = SEVENTY_MM_SERVICE.fetchVideos("c");

        Response<MMSFetchVideosResponse> response = call.execute();

        MMSFetchVideosResponse fetchVideosResponse = response.body();

        List<SeventyMMVideo> list = fetchVideosResponse.getVideos();

        for (SeventyMMVideo video : list) {

            System.out.println(video.getTitle());
        }
    }
}

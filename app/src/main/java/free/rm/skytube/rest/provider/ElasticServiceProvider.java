package free.rm.skytube.rest.provider;

import android.util.Log;

import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.mms.utility.MMSEntityMapperUtility;
import free.rm.skytube.rest.entity.MMSFetchVideosRequest;
import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.entity.MMSPageToken;
import free.rm.skytube.rest.entity.MMSSearchRequest;
import free.rm.skytube.rest.entity.SeventyMMVideo;
import free.rm.skytube.rest.entity.elastic.ElasticSearchResponse;
import free.rm.skytube.rest.entity.elastic.HitSourceEntity;
import free.rm.skytube.rest.entity.elastic.SearchRequestEntity;
import free.rm.skytube.rest.service.ElasticService;
import free.rm.skytube.rest.service.SeventyMMService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ElasticServiceProvider {

    public static final ElasticService ELASTIC_SERVICE =
            new Retrofit.Builder()
                    .baseUrl("https://search-tmovies-h6qlkaunfxq6rrnd7fqoot7cuu.ap-south-1.es.amazonaws.com/movies/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ElasticService.class);

    public static ElasticService getElasticService() {

        return ELASTIC_SERVICE;
    }

    /**
     * makes a search request to backend
     *
     * @param query
     * @param from
     * @return
     */
    public static ElasticSearchResponse search (String query, int from) {

        SearchRequestEntity request = new SearchRequestEntity(query, from);


        Call<ElasticSearchResponse> call = ELASTIC_SERVICE.search(request);

        try {

            Response<ElasticSearchResponse> response = call.execute();
            Log.i("MMS", "search successful");

            return response.body();
        } catch (IOException e) {

            Log.e("service", "error", e);
            return null;
        }
    }

    private static void testSearch () throws IOException {

        SearchRequestEntity request = new SearchRequestEntity("raja rani", -1);

        Call<ElasticSearchResponse> call = ELASTIC_SERVICE.search(request);
        Response<ElasticSearchResponse> response = call.execute();

        ElasticSearchResponse searchResponse = response.body();

        List<HitSourceEntity> list = Arrays.asList(searchResponse.getHits().getHits());

        for (HitSourceEntity video : list) {

            System.out.println(video.getVideo().getTitle());
        }
    }

    public static void main (String[] args) throws IOException {

//        testSave();
//        testFetch();
        testSearch();
    }
}

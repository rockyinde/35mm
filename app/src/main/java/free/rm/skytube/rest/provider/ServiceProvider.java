package free.rm.skytube.rest.provider;

import android.util.Log;

import java.io.IOException;
import java.time.Clock;
import java.util.List;

import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.mms.utility.MMSEntityMapperUtility;
import free.rm.skytube.rest.entity.MMSFetchVideosRequest;
import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.entity.MMSPageToken;
import free.rm.skytube.rest.entity.MMSSearchRequest;
import free.rm.skytube.rest.entity.SeventyMMVideo;
import free.rm.skytube.rest.service.SeventyMMService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceProvider {

    public static final SeventyMMService SEVENTY_MM_SERVICE =
            new Retrofit.Builder()
            .baseUrl("https://a7fdcsp3ak.execute-api.ap-south-1.amazonaws.com/prod/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SeventyMMService.class);

    public static SeventyMMService getSeventyMmService() {

        return SEVENTY_MM_SERVICE;
    }

    public static void asyncSave (final String id, final String cat, final String body, final String title) {

        new Thread(new Runnable(){
            @Override
            public void run() {
                // Do network action in this function
                save(id, cat, body, title);
            }
        }).start();
    }

    /**
     * makes a search request to backend
     *
     * @param query
     * @param pt
     * @return
     */
    public static MMSFetchVideosResponse search (String query, MMSPageToken pt) {

        MMSSearchRequest request = new MMSSearchRequest();
        request.setQ(query);
        request.setPt(pt);

        Call<MMSFetchVideosResponse> call = SEVENTY_MM_SERVICE.search(request);

        try {

            Response<MMSFetchVideosResponse> response = call.execute();
            Log.i("MMS", "search successful");

            return response.body();
        } catch (IOException e) {

            Log.e("service", "error", e);
            return null;
        }
    }

    public static MMSFetchVideosResponse fetchVideos (String cat, MMSPageToken pt) {

        MMSFetchVideosRequest request = new MMSFetchVideosRequest();
        request.setC(cat);
        request.setPt(pt);

        Call<MMSFetchVideosResponse> call = SEVENTY_MM_SERVICE.getVideos(request);

        try {

            Response<MMSFetchVideosResponse> response = call.execute();
            Log.i("MMS", "fetch successful");

            return response.body();
        } catch (IOException e) {

            Log.e("service", "error", e);
            return null;
        }
    }

    public static SeventyMMVideo save (String id, String cat, String body, String title) {

        SeventyMMVideo video = new SeventyMMVideo();
        video.setId(id);
        video.setCat(cat);
        video.setBody(body);
        video.setTitle(title);

        Call<SeventyMMVideo> call = SEVENTY_MM_SERVICE.save(video);
        try {

            Response<SeventyMMVideo> response = call.execute();
            Log.i("MMS", "save successful");
            return response.body();
        } catch (IOException e) {

            Log.e("service", "error", e);
            return null;
        }
    }

    private static void testSave () throws IOException {


        SeventyMMVideo resVideo = save("test", "test", "test", "test");

        System.out.println(resVideo.getId());
    }

    private static void testFetch () throws IOException {

        MMSFetchVideosRequest request = new MMSFetchVideosRequest();
        request.setPt(null);
        request.setC("c");

        Call<MMSFetchVideosResponse> call = SEVENTY_MM_SERVICE.getVideos(request);
        Response<MMSFetchVideosResponse> response = call.execute();

        MMSFetchVideosResponse fetchVideosResponse = response.body();

        List<SeventyMMVideo> list = fetchVideosResponse.getItems();

        for (SeventyMMVideo video : list) {

            System.out.println(video.getTitle());
        }
    }

    public static void main (String[] args) throws IOException {

//        testSave();
        testFetch();
    }
}

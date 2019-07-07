package free.rm.skytube.mms.fragments;

import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import free.rm.skytube.businessobjects.Logger;
import free.rm.skytube.businessobjects.YouTube.GetYouTubeVideos;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.mms.utility.MMSEntityMapperUtility;
import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.provider.ServiceProvider;

public class MMSCategoryVideoFetcher extends GetYouTubeVideos {

    private String category;

    public MMSCategoryVideoFetcher (String category) {

        this.category = category;
    }

    @Override
    public void init() throws IOException {

    }

    @Override
    public List<YouTubeVideo> getNextVideos() {
        List<YouTubeVideo> searchResultList = null;

        if (!noMoreVideoPages()) {

            // set the page token/id to retrieve
            MMSFetchVideosResponse response = ServiceProvider.fetchVideos(category, mmsPageToken);

            if (response == null) {
                noMoreVideoPages = true;
                return new ArrayList<>();
            }

            // set the next page token
            mmsPageToken = response.getLastEvaluatedKey();

            // if nextPageToken is null, it means that there are no more videos
            if (mmsPageToken == null)
                noMoreVideoPages = true;

            searchResultList = MMSEntityMapperUtility.convert(response.getItems());
//            shuffle(searchResultList);
        } else {

            searchResultList = new ArrayList<>();
        }

        return searchResultList;
    }

    @Override
    public boolean noMoreVideoPages() {
        return noMoreVideoPages;
    }

    /**
     * shuffles the list
     * @param videos
     */
    private void shuffle (List<YouTubeVideo> videos) {

        if (videos == null || videos.isEmpty())
            return;
        else
            Collections.shuffle(videos);
    }
}

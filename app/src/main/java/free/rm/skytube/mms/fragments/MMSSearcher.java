package free.rm.skytube.mms.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import free.rm.skytube.businessobjects.YouTube.GetYouTubeVideos;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.mms.utility.MMSEntityMapperUtility;
import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.provider.ServiceProvider;

public class MMSSearcher extends GetYouTubeVideos {

    private String query;

    @Override
    public void init() throws IOException {

    }

    @Override
    public void setQuery (String query) {

        this.query = query;
    }

    /**
     * TODO update the search to automatically fetch the next page of results if
     *  - empty result set AND
     *  - last evaluated key
     *  are returned
     *
     * @return
     */
    @Override
    public List<YouTubeVideo> getNextVideos() {
        List<YouTubeVideo> searchResultList = null;

        if (!noMoreVideoPages()) {

            // set the page token/id to retrieve
            MMSFetchVideosResponse response = ServiceProvider.search(query, mmsPageToken);

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
}

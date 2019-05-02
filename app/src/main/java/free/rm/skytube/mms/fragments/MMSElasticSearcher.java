package free.rm.skytube.mms.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import free.rm.skytube.businessobjects.YouTube.GetYouTubeVideos;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.mms.utility.MMSEntityMapperUtility;
import free.rm.skytube.rest.entity.MMSFetchVideosResponse;
import free.rm.skytube.rest.entity.elastic.ElasticSearchResponse;
import free.rm.skytube.rest.provider.ElasticServiceProvider;
import free.rm.skytube.rest.provider.ServiceProvider;

public class MMSElasticSearcher extends GetYouTubeVideos {

    private String query;
    private int from;   //pagination attr of elastic
    private int total;

    @Override
    public void init() throws IOException {

        from = -1;
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
            ElasticSearchResponse response = ElasticServiceProvider.search(query, from);

            if (response == null || response.getHits() == null ||
                    response.getHits().getHits() == null) {

                // network error
                noMoreVideoPages = true;
                return new ArrayList<>();
            }

            // first response
            if (from < 0) {

                total = response.getHits().getTotal();
                from = response.getHits().getHits().length + 1;
            } else
                from += response.getHits().getHits().length + 1;

            if (from > total)
                noMoreVideoPages = true;

            searchResultList = MMSEntityMapperUtility.convert(response.getHits().getHits());
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

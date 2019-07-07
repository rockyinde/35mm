package free.rm.skytube.mms.utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import free.rm.skytube.businessobjects.Logger;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeChannel;
import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import free.rm.skytube.rest.entity.SeventyMMVideo;

public class MMSEntityMapperUtility {

    public static List<YouTubeVideo> convert (List<SeventyMMVideo> mmsVideos) {

        List<YouTubeVideo> videos = new ArrayList<>();

        for (SeventyMMVideo video : mmsVideos) {

            // add the video to the list
//            videos.add(convert(video.getBody()));
            videos.add((video.getBody()));
        }

        return videos;
    }
    
    private static YouTubeVideo convert (String json) {

        // convert JSON into YouTubeVideo
        YouTubeVideo video = new Gson().fromJson(json, new TypeToken<YouTubeVideo>(){}.getType());

        // due to upgrade to YouTubeVideo (by changing channel{Id,Name} to YouTubeChannel)
        // from version 2.82 to 2.90
        if (video.getChannel() == null) {
            try {
                JSONObject videoJsonObj = new JSONObject(json);
                final String channelId   = videoJsonObj.get("channelId").toString();
                final String channelName = videoJsonObj.get("channelName").toString();
                video.setChannel(new YouTubeChannel(channelId, channelName));
            } catch (JSONException e) {
                Logger.e("MMS", "Error occurred while extracting channel{Id,Name} from JSON", e);
            }
        }

        // regenerate the video's PublishDatePretty (e.g. 5 hours ago)
        video.forceRefreshPublishDatePretty();

        return video;
    }
}

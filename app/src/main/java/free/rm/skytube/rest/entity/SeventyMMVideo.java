package free.rm.skytube.rest.entity;

import free.rm.skytube.businessobjects.YouTube.POJOs.YouTubeVideo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeventyMMVideo {

    private String id;
    private String cat;
    private YouTubeVideo body;
    private String title;
}

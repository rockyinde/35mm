package free.rm.skytube.rest.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MMSFetchVideosRequest {

    private String c;   // cat
    private MMSPageToken pt;
}
package free.rm.skytube.rest.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MMSSearchRequest {

    private String q;   // query
    private MMSPageToken pt;
}

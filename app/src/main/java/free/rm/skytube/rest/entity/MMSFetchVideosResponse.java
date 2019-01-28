package free.rm.skytube.rest.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MMSFetchVideosResponse {

    private List<SeventyMMVideo> Items;
    private int Count;
    private int ScannedCount;
    private MMSPageToken LastEvaluatedKey;
}

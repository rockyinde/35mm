package free.rm.skytube.rest.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MMSFetchVideosResponse {

    private List<SeventyMMVideo> Items;
    private int Count;
    private int ScannedCount;
    private JsonObject LastEvaluatedKey;
}

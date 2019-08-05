package free.rm.skytube.rest.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MMSFetchVideosRequest {

    private String c;   // cat
    private JsonObject pt;
}

package free.rm.skytube.rest.entity;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MMSSearchRequest {

    private String q;   // query
    private JsonObject pt;
}

package free.rm.skytube.rest.entity.elastic;

import com.google.gson.annotations.SerializedName;

import free.rm.skytube.rest.entity.SeventyMMVideo;
import lombok.Data;

@Data
public class HitSourceEntity {

    @SerializedName("_source")
    private SeventyMMVideo video;
}

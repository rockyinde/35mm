package free.rm.skytube.rest.entity.elastic;

import lombok.Data;

@Data
public class HitsEntity {

    private int total;
    private HitSourceEntity[] hits;
}

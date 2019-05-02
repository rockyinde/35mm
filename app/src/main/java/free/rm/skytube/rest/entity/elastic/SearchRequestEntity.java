package free.rm.skytube.rest.entity.elastic;

import lombok.Data;

@Data
public class SearchRequestEntity {

    public SearchRequestEntity(String query, int from) {

        size = 50;

        if (from > 0)
            this.from = from;

        this.query = new QueryRequestEntity(query);
    }

    private int size;
    private int from;
    private QueryRequestEntity query;
}

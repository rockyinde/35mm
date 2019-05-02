package free.rm.skytube.rest.entity.elastic;

import lombok.Data;

@Data
public class QueryRequestEntity {

    public QueryRequestEntity(String query) {

        match = new MatchRequestEntity(query);
    }

    private MatchRequestEntity match;
}

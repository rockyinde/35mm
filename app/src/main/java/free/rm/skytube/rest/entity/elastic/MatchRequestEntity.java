package free.rm.skytube.rest.entity.elastic;

import lombok.Data;

@Data
public class MatchRequestEntity {

    public MatchRequestEntity(String query) {

        title = new TitleRequestEntity();
        title.setFuzziness("AUTO");
        title.setQuery(query);
    }

    private TitleRequestEntity title;
}

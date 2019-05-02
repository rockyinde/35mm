package free.rm.skytube.rest.entity.elastic;

import lombok.Data;

@Data
public class TitleRequestEntity {

    private String query;
    private String fuzziness;
}

package revend.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateListingResponse {
    private long listingId;
}

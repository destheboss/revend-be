package revend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Listing {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private Long userId;
    private String imageData;
}

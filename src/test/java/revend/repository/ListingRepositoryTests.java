package revend.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import revend.persistence.ListingRepository;
import revend.persistence.entity.ListingEntity;
import revend.persistence.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
class ListingRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ListingRepository listingRepository;

    private ListingEntity listingEntity;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();
        userEntity = entityManager.merge(userEntity);

        listingEntity = ListingEntity.builder()
                .title("Test Listing")
                .description("A test listing description")
                .price(BigDecimal.valueOf(100.00))
                .user(userEntity)
                .build();
        listingEntity = entityManager.merge(listingEntity);
    }

    @Test
    void testFindById() {
        ListingEntity found = listingRepository.findById(listingEntity.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo(listingEntity.getTitle());
    }

    @Test
    void testFindAllListingsByUserId() {
        List<ListingEntity> foundListings = listingRepository.findAllListingsByUserId(userEntity.getId());
        assertThat(foundListings).hasSize(1);
        assertThat(foundListings.get(0).getTitle()).isEqualTo(listingEntity.getTitle());
    }
}

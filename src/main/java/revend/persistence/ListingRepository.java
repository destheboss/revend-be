package revend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import revend.persistence.entity.ListingEntity;

import java.util.List;

public interface ListingRepository extends JpaRepository<ListingEntity, Long> {
    List<ListingEntity> findAllListingsByUserId(Long userId);
}

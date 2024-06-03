package revend.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import revend.business.exception.InvalidUserException;
import revend.business.exception.ListingNotFoundException;
import revend.business.exception.UnauthorizedDataAccessException;
import revend.business.impl.*;
import revend.configuration.security.token.AccessToken;
import revend.domain.CreateListingRequest;
import revend.domain.CreateListingResponse;
import revend.domain.Listing;
import revend.domain.UpdateListingRequest;
import revend.persistence.ListingRepository;
import revend.persistence.UserRepository;
import revend.persistence.entity.ListingEntity;
import revend.persistence.entity.RoleEnum;
import revend.persistence.entity.UserEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingTests {
    @Mock
    private ListingRepository listingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private CreateListingUseCaseImpl createListingUseCase;

    @InjectMocks
    private DeleteListingUseCaseImpl deleteListingUseCase;

    @InjectMocks
    private GetListingsUseCaseImpl getListingsUseCase;

    @InjectMocks
    private GetListingUseCaseImpl getListingUseCase;

    @InjectMocks
    private UpdateListingUseCaseImpl updateListingUseCase;

    @Test
    void testCreateListing() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        CreateListingRequest request = CreateListingRequest.builder()
                .title("Test Listing")
                .description("Test Description")
                .price(BigDecimal.valueOf(100.00))
                .userId(userEntity.getId())
                .build();

        ListingEntity savedListing = new ListingEntity();
        savedListing.setId(1L);
        when(listingRepository.save(any(ListingEntity.class))).thenReturn(savedListing);

        CreateListingResponse response = createListingUseCase.createListing(request);

        assertThat(response.getListingId()).isEqualTo(1L);
    }

    @Test
    void testGetListings() {
        Long userId = 123L;
        List<ListingEntity> listingEntities = new ArrayList<>();
        listingEntities.add(createListingEntity(1L, userId, "Listing 1", "Description 1", BigDecimal.valueOf(100.00)));
        listingEntities.add(createListingEntity(2L, userId, "Listing 2", "Description 2", BigDecimal.valueOf(200.00)));

        when(listingRepository.findAllListingsByUserId(userId)).thenReturn(listingEntities);

        List<Listing> listings = getListingsUseCase.getListings(userId);

        assertThat(listings).hasSize(2);
        assertThat(listings.get(0).getId()).isEqualTo(1L);
        assertThat(listings.get(0).getTitle()).isEqualTo("Listing 1");
        assertThat(listings.get(0).getDescription()).isEqualTo("Description 1");
        assertThat(listings.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(listings.get(1).getId()).isEqualTo(2L);
        assertThat(listings.get(1).getTitle()).isEqualTo("Listing 2");
        assertThat(listings.get(1).getDescription()).isEqualTo("Description 2");
        assertThat(listings.get(1).getPrice()).isEqualTo(BigDecimal.valueOf(200.00));
    }

    private ListingEntity createListingEntity(Long id, Long userId, String title, String description, BigDecimal price) {
        return ListingEntity.builder()
                .id(id)
                .user(UserEntity.builder().id(userId).build())
                .title(title)
                .description(description)
                .price(price)
                .build();
    }

    @Test
    void testCreateListingUserNotFound() {
        CreateListingRequest request = CreateListingRequest.builder()
                .title("Test Listing")
                .description("Test Description")
                .price(BigDecimal.valueOf(100.00))
                .userId(1L)
                .build();

        lenient().when(userRepository.findById(1L)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(InvalidUserException.class, () -> {
            createListingUseCase.createListing(request);
        });
    }

    @Test
    void testDeleteListingAuthorizedAsAdmin() {
        Long userId = 123L;
        Long listingId = 1L;

        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);

        ListingEntity listingEntity = ListingEntity.builder()
                .id(listingId)
                .user(UserEntity.builder().id(userId).build())
                .build();
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listingEntity));

        deleteListingUseCase.deleteListing(listingId);

        verify(listingRepository, times(1)).deleteById(listingId);
    }

    @Test
    void testDeleteListingAuthorizedAsOwner() {
        Long userId = 123L;
        Long listingId = 1L;

        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(userId);

        ListingEntity listingEntity = ListingEntity.builder()
                .id(listingId)
                .user(UserEntity.builder().id(userId).build())
                .build();
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listingEntity));

        deleteListingUseCase.deleteListing(listingId);

        verify(listingRepository, times(1)).deleteById(listingId);
    }

    @Test
    void testDeleteListingUnauthorized() {
        Long userId = 123L;
        Long listingId = 1L;

        ListingEntity listingEntity = ListingEntity.builder()
                .id(listingId)
                .user(UserEntity.builder().id(userId).build())
                .build();
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listingEntity));

        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(456L);

        assertThrows(UnauthorizedDataAccessException.class, () -> deleteListingUseCase.deleteListing(listingId));
    }

    @Test
    void testDeleteListingNotFound() {
        Assertions.assertThrows(ListingNotFoundException.class, () -> {
            deleteListingUseCase.deleteListing(1L);
        });

        verify(listingRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateListingAuthorized() {
        Long listingId = 1L;
        Long userId = 1L;

        ListingEntity listingEntity = ListingEntity.builder()
                .id(listingId)
                .title("Old Title")
                .description("Old Description")
                .price(BigDecimal.valueOf(100.00))
                .user(UserEntity.builder().id(userId).build())
                .build();

        when(listingRepository.findById(1L)).thenReturn(Optional.of(listingEntity));
        when(requestAccessToken.getUserId()).thenReturn(userId);

        UpdateListingRequest request = UpdateListingRequest.builder()
                .id(listingId)
                .title("New Title")
                .description("New Description")
                .price(BigDecimal.valueOf(200.00))
                .build();

        updateListingUseCase.updateListing(request);

        verify(listingRepository, times(1)).save(any(ListingEntity.class));
        assertThat(listingEntity.getTitle()).isEqualTo("New Title");
        assertThat(listingEntity.getDescription()).isEqualTo("New Description");
        assertThat(listingEntity.getPrice()).isEqualTo(BigDecimal.valueOf(200.00));
    }

    @Test
    void testUpdateListingUnauthorized() {
        Long listingId = 1L;
        Long userId = 1L;
        Long unauthorizedUserId = 2L;

        ListingEntity listingEntity = ListingEntity.builder()
                .id(listingId)
                .title("Old Title")
                .description("Old Description")
                .price(BigDecimal.valueOf(100.00))
                .user(UserEntity.builder().id(userId).build())
                .build();

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listingEntity));
        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(unauthorizedUserId);

        UpdateListingRequest request = UpdateListingRequest.builder()
                .id(1L)
                .title("New Title")
                .description("New Description")
                .price(BigDecimal.valueOf(200.00))
                .build();

        assertThrows(UnauthorizedDataAccessException.class, () -> updateListingUseCase.updateListing(request));

        verify(listingRepository, never()).save(any(ListingEntity.class));
    }

    @Test
    void testUpdateListingNotFound() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateListingRequest request = UpdateListingRequest.builder()
                .id(1L)
                .title("New Title")
                .description("New Description")
                .price(BigDecimal.valueOf(200.00))
                .build();

        assertThrows(ListingNotFoundException.class, () -> {
            updateListingUseCase.updateListing(request);
        });

        verify(listingRepository, never()).save(any(ListingEntity.class));
    }

    @Test
    void testGetListingById() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        ListingEntity listingEntity = ListingEntity.builder()
                .id(1L)
                .title("Test Listing")
                .description("Test Description")
                .price(BigDecimal.valueOf(100.00))
                .user(userEntity)
                .build();

        when(listingRepository.findById(1L)).thenReturn(Optional.of(listingEntity));

        Optional<Listing> result = getListingUseCase.getListingById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void testGetListingByIdNotFound() {
        when(listingRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ListingNotFoundException.class, () -> {
            getListingUseCase.getListingById(1L);
        });
    }
}

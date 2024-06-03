package revend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import revend.business.*;
import revend.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListingControllerTests {
    @Mock
    private CreateListingUseCase createListingUseCase;

    @Mock
    private GetListingUseCase getListingUseCase;

    @Mock
    private UpdateListingUseCase updateListingUseCase;

    @Mock
    private DeleteListingUseCase deleteListingUseCase;

    @Mock
    private GetListingsUseCase getListingsUseCase;

    @InjectMocks
    private ListingController listingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateListing() {
        CreateListingRequest request = new CreateListingRequest();
        CreateListingResponse expectedResponse = CreateListingResponse.builder().listingId(123).build();
        when(createListingUseCase.createListing(request)).thenReturn(expectedResponse);

        ResponseEntity<CreateListingResponse> response = listingController.createListing(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetListingById() {
        Listing listing = new Listing();
        when(getListingUseCase.getListingById(1L)).thenReturn(Optional.of(listing));

        ResponseEntity<Listing> response = listingController.getListingById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listing, response.getBody());
    }

    @Test
    void testGetListingByIdNotFound() {
        when(getListingUseCase.getListingById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Listing> response = listingController.getListingById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateListing() {
        UpdateListingRequest request = new UpdateListingRequest();
        doNothing().when(updateListingUseCase).updateListing(request);

        ResponseEntity<Void> response = listingController.updateListing(1L, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteListing() {
        doNothing().when(deleteListingUseCase).deleteListing(1L);

        ResponseEntity<Void> response = listingController.deleteListing(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetUserListings() {
        List<Listing> listings = new ArrayList<>();
        listings.add(new Listing());
        listings.add(new Listing());
        when(getListingsUseCase.getListings(123L)).thenReturn(listings);

        ResponseEntity<List<Listing>> response = listingController.getUserListings(123L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listings, response.getBody());
    }
}

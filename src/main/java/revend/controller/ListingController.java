package revend.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revend.business.*;
import revend.domain.CreateListingRequest;
import revend.domain.CreateListingResponse;
import revend.domain.Listing;
import revend.domain.UpdateListingRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listings")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ListingController {
    private final CreateListingUseCase createListingUseCase;
    private final GetListingUseCase getListingUseCase;
    private final UpdateListingUseCase updateListingUseCase;
    private final DeleteListingUseCase deleteListingUseCase;
    private final GetListingsUseCase getListingsUseCase;
    private final GetAllListingsUseCase getAllListingsUseCase;

    @PostMapping
    public ResponseEntity<CreateListingResponse> createListing(@RequestBody CreateListingRequest request) {
        CreateListingResponse response = createListingUseCase.createListing(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable Long id) {
        Optional<Listing> listing = getListingUseCase.getListingById(id);
        return listing.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateListing(@PathVariable Long id, @RequestBody UpdateListingRequest request) {
        updateListingUseCase.updateListing(request);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"USER", "ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        deleteListingUseCase.deleteListing(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Listing>> getUserListings(@PathVariable Long userId) {
        List<Listing> listings = getListingsUseCase.getListings(userId);
        return ResponseEntity.ok(listings);
    }

    @GetMapping()
    public ResponseEntity<List<Listing>> getAllListings() {
        List<Listing> listings = getAllListingsUseCase.getAllListings();
        return ResponseEntity.ok(listings);
    }
}

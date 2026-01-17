package org.team2.roktokhoj_backend.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.team2.roktokhoj_backend.models.BloodDonor;
import org.team2.roktokhoj_backend.models.FindBloodDonor;
import org.team2.roktokhoj_backend.models.RegisterBloodDonor;
import org.team2.roktokhoj_backend.services.BloodDonorService;
import org.team2.roktokhoj_backend.services.MapsService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class BloodDonorController {
    private final MapsService mapsService;
    private final BloodDonorService service;

    public BloodDonorController(MapsService mapsService, BloodDonorService service) {
        this.mapsService = mapsService;
        this.service = service;
    }

    @PostMapping("/register_donor")
    @Async
    public CompletableFuture<BloodDonor> registerDonor(@Valid @RequestBody RegisterBloodDonor registerBloodDonor) {
        var info = registerBloodDonor.getInfo();
        var phone = info.getPhone().replaceFirst("(^\\+)|(-)", "");
        info.setPhone(phone);
        if (service.findByPhone(info.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with the same phone number already exists");
        }
        registerBloodDonor.setInfo(info);
        var addressSelection = registerBloodDonor.getAddressSelection();
        try {
            return mapsService.reverseSearch(addressSelection.getLat(), addressSelection.getLon()).thenApply(place -> {
                if (!place.getCountryCode().equalsIgnoreCase("bd")
                        && !place.getCountryName().equalsIgnoreCase("bangladesh")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a address within Bangladesh!");
                }

                // TODO: Verify with OTP in Future.
                return service.registerDonor(registerBloodDonor);
            });
        } catch (Exception e) {
            log.error("register_donor:: Reverse geocode search failed: {}", e.getMessage());
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/find_donor")
    public CompletableFuture<List<BloodDonor>> findDonor(@Valid @RequestBody FindBloodDonor findBloodDonor) {
        var addressSelection = findBloodDonor.getAddressSelection();
        try {
            return mapsService.reverseSearch(addressSelection.getLat(), addressSelection.getLon()).thenApply(place -> {
                if (!place.getCountryCode().equalsIgnoreCase("bd")
                        && !place.getCountryName().equalsIgnoreCase("bangladesh")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a address within Bangladesh!");
                }

                return service.findDonor(addressSelection.getLat(), addressSelection.getLon(), addressSelection.getRadius(),
                        findBloodDonor.getBloodGroup());
            });
        } catch (Exception e) {
            log.error("find_donor:: Reverse geocode search failed: {}", e.getMessage());
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package org.team2.roktokhoj_backend.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.team2.roktokhoj_backend.models.*;
import org.team2.roktokhoj_backend.services.BloodDonorService;
import org.team2.roktokhoj_backend.services.MapsService;
import org.team2.roktokhoj_backend.services.W84GeographyService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class BloodDonorController {
    private final MapsService mapsService;
    private final W84GeographyService geographyService;
    private final BloodDonorService service;
    private final PasswordEncoder passwordEncoder;

    public BloodDonorController(
            MapsService mapsService,
            W84GeographyService geographyService,
            BloodDonorService service,
            PasswordEncoder passwordEncoder) {
        this.mapsService = mapsService;
        this.geographyService = geographyService;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register_donor")
    @Async
    public CompletableFuture<ProfileBloodDonor> registerDonor(@Valid @RequestBody NewBloodDonor newBloodDonor) {
        var profile = newBloodDonor.getProfile();
        var info = profile.getInfo();
        var phone = info.getPhone().replaceFirst("(^\\+)|(-)", "");
        info.setPhone(phone);
        if (service.findByPhone(info.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with the same phone number already exists");
        }
        profile.setInfo(info);
        newBloodDonor.setProfile(profile);
        var addressSelection = profile.getAddressSelection();
        try {
            return mapsService.reverseSearch(addressSelection.getLat(), addressSelection.getLon()).thenApply(place -> {
                if (!place.getCountryCode().equalsIgnoreCase("bd")
                        && !place.getCountryName().equalsIgnoreCase("bangladesh")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a address within Bangladesh!");
                }

                // TODO: Verify with OTP in Future.
                return service.registerDonor(newBloodDonor);
            });
        } catch (Exception e) {
            log.error("register_donor:: Reverse geocode search failed: {}", e.getMessage());
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login_donor")
    public ProfileBloodDonor loginDonor(@Valid @RequestBody LoginBloodDonor loginBloodDonor) {
        var phone = loginBloodDonor.getPhone().replaceFirst("(^\\+)|(-)", "");
        loginBloodDonor.setPhone(phone);
        var donor = service.authenticateDonor(loginBloodDonor);
        if (donor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided phone number or password or both is invalid.");
        }
        return donor.get();
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
                        findBloodDonor.getBloodGroup(), Availability.AVAILABLE);
            });
        } catch (Exception e) {
            log.error("find_donor:: Reverse geocode search failed: {}", e.getMessage());
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get_profile")
    public ProfileBloodDonor getDonor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Middleware should've prevented, right?
        assert authentication != null;

        var currentUser = (org.team2.roktokhoj_backend.entities.BloodDonor) authentication.getPrincipal();
        return ProfileBloodDonor.fromEntity(currentUser);
    }

    @PutMapping("/update_profile")
    public ProfileBloodDonor updateProfile(@Valid @RequestBody NewBloodDonor newDonor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Middleware should've prevented, right?
        assert authentication != null;

        var old = (org.team2.roktokhoj_backend.entities.BloodDonor) authentication.getPrincipal();

        assert old != null;

        if (!passwordEncoder.matches(newDonor.getPassword(), old.getPass())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password mismatch!");
        }

        var newProfile = newDonor.getProfile();

        var newPhone = newProfile.getInfo().getPhone();
        var oldPhone = old.getPhone();
        if (!newPhone.substring(newPhone.length() - 10)
                .equals(oldPhone.substring(oldPhone.length() - 10))
            && service.findByPhone(newPhone).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user with the updated phone number already exists!");
        }

        var addrSelection = newProfile.getAddressSelection();
        var location = geographyService.createPoint(addrSelection.getLat(), addrSelection.getLon());
        var entity = org.team2.roktokhoj_backend.entities.BloodDonor.newFromModel(newProfile.getInfo(), old.getPass(),
                location, addrSelection.getRadius());
        entity.setId(old.getId());
        entity.setCreatedAt(old.getCreatedAt());
        entity.setUpdatedAt(new Date());

        return service.updateProfile(entity);
    }
}

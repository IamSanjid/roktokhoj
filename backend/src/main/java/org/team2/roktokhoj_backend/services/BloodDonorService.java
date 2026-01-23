package org.team2.roktokhoj_backend.services;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team2.roktokhoj_backend.models.*;
import org.team2.roktokhoj_backend.repositories.BloodDonorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class BloodDonorService {
    private final BloodDonorRepository repository;
    private final W84GeographyService geographyService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public BloodDonorService(
            BloodDonorRepository repository,
            W84GeographyService geographyService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.repository = repository;
        this.geographyService = geographyService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public ProfileBloodDonor registerDonor(NewBloodDonor newBloodDonor) {
        var profile = newBloodDonor.getProfile();
        var addrSelection = profile.getAddressSelection();
        var location = geographyService.createPoint(addrSelection.getLat(), addrSelection.getLon());
        var pass = passwordEncoder.encode(newBloodDonor.getPassword());
        var entity = org.team2.roktokhoj_backend.entities.BloodDonor.newFromModel(profile.getInfo(), pass,
                location, addrSelection.getRadius());
        return ProfileBloodDonor.fromEntity(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public Optional<ProfileBloodDonor> authenticateDonor(LoginBloodDonor loginBloodDonor) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginBloodDonor.getPhone(),
                        loginBloodDonor.getPassword()
                )
        );

        return repository.findByPhone(loginBloodDonor.getPhone()).map(this::getProfileWithJwtToken);
    }

    @Transactional(readOnly = true)
    public List<BloodDonor> findDonor(double lat, double lon, double radius, BloodGroup bloodGroup, Availability availability) {
        log.debug("Looking for donor around {} {}, radius: {}, blood group: {}", lat, lon, radius, bloodGroup);
        Point p = geographyService.createPoint(lat, lon);

        var stream = StreamSupport.stream(repository.findMatchingDonors(p, radius, bloodGroup, availability).spliterator(), false);
        return stream.map(BloodDonor::fromEntity).toList();
    }

    public Optional<BloodDonor> findByPhone(String phone) {
        return repository.findByPhone(phone).map(BloodDonor::fromEntity);
    }

    @Transactional
    public ProfileBloodDonor updateProfile(org.team2.roktokhoj_backend.entities.BloodDonor newProfile) {
        return getProfileWithJwtToken(repository.save(newProfile));
    }

    private ProfileBloodDonor getProfileWithJwtToken(org.team2.roktokhoj_backend.entities.BloodDonor donor) {
        var token = jwtService.generateToken(donor);
        var mDonor = ProfileBloodDonor.fromEntity(donor);
        mDonor.getInfo().setToken(token);
        mDonor.getInfo().setTokenExpiration(jwtService.getExpirationTime());
        return mDonor;
    }
}

package org.team2.roktokhoj_backend.services;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.team2.roktokhoj_backend.models.BloodDonor;
import org.team2.roktokhoj_backend.models.BloodGroup;
import org.team2.roktokhoj_backend.models.RegisterBloodDonor;
import org.team2.roktokhoj_backend.repositories.BloodDonorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class BloodDonorService {
    private final BloodDonorRepository repository;
    private final GeometryFactory factory;

    public BloodDonorService(BloodDonorRepository repository) {
        this.repository = repository;
        this.factory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    public BloodDonor registerDonor(RegisterBloodDonor registerBloodDonor) {
        var addrSelection = registerBloodDonor.getAddressSelection();
        var location = factory.createPoint(new Coordinate(addrSelection.getLon(), addrSelection.getLat()));
        var entity = org.team2.roktokhoj_backend.entities.BloodDonor.fromModel(registerBloodDonor.getInfo(),
                location, addrSelection.getRadius());
        return BloodDonor.fromEntity(repository.save(entity));
    }

    public List<BloodDonor> findDonor(double lat, double lon, double radius, BloodGroup bloodGroup) {
        log.debug("Looking for donor around {} {}, radius: {}, blood group: {}", lat, lon, radius, bloodGroup);
        Point p = factory.createPoint(new Coordinate(lon, lat));

        var stream = StreamSupport.stream(repository.findMatchingDonors(p, radius, bloodGroup).spliterator(), false);
        return stream.map(BloodDonor::fromEntity).toList();
    }

    public Optional<BloodDonor> findByPhone(String phone) {
        return repository.findByPhone(phone).map(BloodDonor::fromEntity);
    }
}

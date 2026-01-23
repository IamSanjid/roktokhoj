package org.team2.roktokhoj_backend.repositories;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.team2.roktokhoj_backend.entities.BloodDonor;
import org.team2.roktokhoj_backend.models.Availability;
import org.team2.roktokhoj_backend.models.BloodGroup;

import java.util.Optional;

@Repository
public interface BloodDonorRepository extends JpaRepository<BloodDonor, Long>, CrudRepository<BloodDonor, Long> {
    //@Query(value="SELECT * FROM donors WHERE blood_group = :bloodGroup AND ST_DistanceSphere(otherLocation, :p) < radius + :otherRadius", nativeQuery = true)
    @Query(value = "SELECT * FROM donors WHERE availability = ?4 AND blood_group = ?3 AND ST_DWithin(location::geography, CAST(?1 AS geography), radius + ?2)", nativeQuery = true)
    Iterable<BloodDonor> findMatchingDonors(Point otherLocation, double otherRadius, BloodGroup bloodGroup, Availability availability);

    @Query(value = "SELECT * FROM donors WHERE RIGHT(phone, 10) = RIGHT(:phone, 10)", nativeQuery = true)
    Optional<BloodDonor> findByPhone(String phone);
}

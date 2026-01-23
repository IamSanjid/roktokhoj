package org.team2.roktokhoj_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.team2.roktokhoj_backend.models.Availability;
import org.team2.roktokhoj_backend.models.BloodGroup;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity(name="donors")
@Getter
@Setter
@EqualsAndHashCode(exclude="id")
@ToString
public class BloodDonor implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String pass;

    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "blood_group", nullable = false)
    private BloodGroup bloodGroup;

    @Column(columnDefinition = "Geometry(Point,4326)", nullable = false)
    private Point location;

    @Column(name = "radius", nullable = false)
    private double radius;

    @Column(nullable = false)
    private Availability availability;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    @NullMarked
    // Role-based access control...
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return pass;
    }

    @Override
    @NullMarked
    public String getUsername() {
        return phone;
    }

    public static BloodDonor newFromModel(org.team2.roktokhoj_backend.models.BloodDonor info, String pass, Point location, Double radius) {
        var instance = new BloodDonor();
        instance.name = info.getName();
        instance.pass = pass;
        instance.email = info.getEmail();
        instance.phone = info.getPhone();
        instance.bloodGroup = info.getBloodGroup();
        instance.location = location;
        instance.radius = radius;
        instance.availability = info.getAvailability();

        var now = new Date();
        instance.createdAt = now;
        instance.updatedAt = now;

        return instance;
    }
}

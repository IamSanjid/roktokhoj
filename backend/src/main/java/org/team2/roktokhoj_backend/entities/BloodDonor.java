package org.team2.roktokhoj_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.team2.roktokhoj_backend.models.BloodGroup;

@Entity(name="donors")
@Getter
@Setter
@EqualsAndHashCode(exclude="id")
@ToString
public class BloodDonor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="blood_group")
    private BloodGroup bloodGroup;

    @Column(columnDefinition = "Geometry(Point,4326)")
    private Point location;

    @Column(name="radius")
    private double radius;

    public static BloodDonor fromModel(org.team2.roktokhoj_backend.models.BloodDonor info, Point location, Double radius) {
        var instance = new BloodDonor();
        instance.setName(info.getName());
        instance.setEmail(info.getEmail());
        instance.setPhone(info.getPhone());
        instance.setBloodGroup(info.getBloodGroup());
        instance.setLocation(location);
        instance.setRadius(radius);
        return instance;
    }
}

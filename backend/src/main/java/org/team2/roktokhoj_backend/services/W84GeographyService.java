package org.team2.roktokhoj_backend.services;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class W84GeographyService {
    private final GeometryFactory factory;

    public  W84GeographyService() {
        this.factory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    public Point createPoint(double lat, double lon) {
        return factory.createPoint(new Coordinate(lon, lat));
    }
}

CREATE INDEX IF NOT EXISTS donors_location_gist
    ON donors
        USING GIST (location);
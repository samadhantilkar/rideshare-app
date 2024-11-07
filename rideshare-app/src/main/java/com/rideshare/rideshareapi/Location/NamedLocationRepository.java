    package com.rideshare.rideshareapi.Location;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface NamedLocationRepository extends JpaRepository<NamedLocation,Long> {
    }

package org.vaadin.olli.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.olli.domain.Location;
import org.vaadin.olli.domain.User;

import java.util.List;

public interface LocationsRepository extends JpaRepository<Location, Long> {

}

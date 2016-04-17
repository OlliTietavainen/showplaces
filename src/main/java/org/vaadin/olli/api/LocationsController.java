package org.vaadin.olli.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.vaadin.olli.domain.Location;
import org.vaadin.olli.domain.User;
import org.vaadin.olli.repository.LocationsRepository;

@RestController
@RequestMapping("/api/expenses")
public class LocationsController {
    @Autowired
    LocationsRepository locationsRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Location> getExpenses() {
        return locationsRepository.findByUserOrderByDateDesc(getCurrentUser());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createExpense(@RequestBody Location location) {
        location.setUser(getCurrentUser());
        locationsRepository.save(location);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ResponseEntity<Void> updateExpense(@PathVariable Long id, @RequestBody Location newExpense) {
        Location location = locationsRepository.findOne(id);

        if (location.getUser().equals(getCurrentUser())) {
            location.setDate(newExpense.getDate());
            location.setComment(newExpense.getComment());

            locationsRepository.save(location);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        Location location = locationsRepository.findOne(id);

        if (location.getUser().equals(getCurrentUser())) {
            locationsRepository.delete(location);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private User getCurrentUser() {
        return null; //TODO
    }
}

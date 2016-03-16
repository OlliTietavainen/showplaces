package org.vaadin.olli.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.vaadin.olli.auth.UserAuthentication;
import org.vaadin.olli.domain.Location;
import org.vaadin.olli.domain.Status;
import org.vaadin.olli.domain.User;
import org.vaadin.olli.repository.LocationsRepository;

import java.util.List;

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
        location.setStatus(Status.NEW);
        location.setUser(getCurrentUser());
        locationsRepository.save(location);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ResponseEntity<Void> updateExpense(@PathVariable Long id, @RequestBody Location newExpense) {
        Location location = locationsRepository.findOne(id);

        if (location.getUser().equals(getCurrentUser())) {
            location.setDate(newExpense.getDate());
            location.setMerchant(newExpense.getMerchant());
            location.setComment(newExpense.getComment());
            location.setTotal(newExpense.getTotal());

            locationsRepository.save(location);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        Location location = locationsRepository.findOne(id);

        if (location.getUser().equals(getCurrentUser()) && location.getStatus() == Status.NEW) {
            locationsRepository.delete(location);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private User getCurrentUser() {
        return ((UserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getDetails().getUser();
    }
}

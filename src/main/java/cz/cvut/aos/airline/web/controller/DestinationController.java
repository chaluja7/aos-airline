package cz.cvut.aos.airline.web.controller;

import cz.cvut.aos.airline.entity.Destination;
import cz.cvut.aos.airline.service.DestinationService;
import cz.cvut.aos.airline.web.exception.BadRequestException;
import cz.cvut.aos.airline.web.exception.ResourceNotFoundException;
import cz.cvut.aos.airline.web.wrapper.CreateDestinationWrapper;
import cz.cvut.aos.airline.web.wrapper.DestinationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@RestController
@RequestMapping(value = "/destination")
public class DestinationController extends AbstractController {

    private static final String PATH = "/destination";

    @Autowired
    private DestinationService destinationService;

    @RequestMapping(value = "/{destinationId}", method = RequestMethod.GET)
    public DestinationWrapper getDestination(@PathVariable Long destinationId) {
        DestinationWrapper destination = getDestinationWrapper(destinationService.find(destinationId));
        if(destination == null) {
            throw new ResourceNotFoundException();
        }

        return destination;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<DestinationWrapper> getDestinations(@RequestHeader(value = X_ORDER, required = false) String xOrder) {
        //zde je dle zadani v hlavicce X-Order pouze asc nebo desc - vyvozeno z toho, ze se radi automaticky dle jmena
        final boolean desc = xOrder != null && xOrder.equalsIgnoreCase("desc");

        List<DestinationWrapper> list = new ArrayList<>();
        for(Destination destination : destinationService.findAll(desc)) {
            list.add(getDestinationWrapper(destination));
        }

        return list;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createDestination(@RequestBody CreateDestinationWrapper wrapper) {
        if(wrapper == null) {
            throw new BadRequestException();
        }

        Destination destination = getDestinationFromWrapper(wrapper);
        try {
            destinationService.persist(destination);
        } catch (PersistenceException | ConstraintViolationException | DataIntegrityViolationException e) {
            throw new BadRequestException();
        }

        return getResponseCreated(getDestinationWrapper(destinationService.find(destination.getId())), getResourceDestination(destination.getId()));
    }

    @RequestMapping(value = "/{destinationId}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void updateDestination(@PathVariable Long destinationId, @RequestBody CreateDestinationWrapper wrapper) {
        if(destinationService.find(destinationId) == null) {
            throw new ResourceNotFoundException();
        }

        if(wrapper == null) {
            throw new BadRequestException();
        }

        Destination destination = getDestinationFromWrapper(wrapper);
        destination.setId(destinationId);
        try {
            destinationService.merge(destination);
        } catch (PersistenceException | ConstraintViolationException | DataIntegrityViolationException e) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/{destinationId}", method = RequestMethod.DELETE)
    public void deleteDestination(@PathVariable Long destinationId) {
        Destination destination = destinationService.find(destinationId);
        if(destination == null) {
            //OK takova destinace neni v DB
            return;
        }

        try {
            destinationService.delete(destination.getId());
        } catch (DataIntegrityViolationException e) {
            //destinaci nejde smazat, protoze ji jiz vyuziva nejaky flight
            throw new BadRequestException();
        }
    }

    private DestinationWrapper getDestinationWrapper(Destination destination) {
        if(destination == null) return null;

        DestinationWrapper wrapper = new DestinationWrapper();
        wrapper.add(ControllerLinkBuilder.linkTo(DestinationController.class).slash(destination.getId()).withSelfRel());
        wrapper.setEntityId(destination.getId());
        wrapper.setName(destination.getName());
        wrapper.setLat(destination.getLat());
        wrapper.setLon(destination.getLon());

        return wrapper;
    }

    private Destination getDestinationFromWrapper(CreateDestinationWrapper wrapper) {
        if(wrapper == null) return null;

        Destination destination = new Destination();
        destination.setName(wrapper.getName());
        destination.setLat(wrapper.getLat());
        destination.setLon(wrapper.getLon());

        return destination;
    }

    public static String getResourceDestination(Long id) {
        return PATH + "/" + id;
    }
}

package cz.cvut.aos.airline.web.controller;

import cz.cvut.aos.airline.entity.Destination;
import cz.cvut.aos.airline.service.DestinationService;
import cz.cvut.aos.airline.web.exception.BadRequestException;
import cz.cvut.aos.airline.web.exception.ResourceNotFoundException;
import cz.cvut.aos.airline.web.wrapper.DestinationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@RestController
public class DestinationController extends AbstractController {

    private static final String PATH = "/destination";

    @Autowired
    private DestinationService destinationService;

    @RequestMapping(value = PATH + "/{destinationId}", method = RequestMethod.GET)
    public DestinationWrapper getDestination(@PathVariable Long destinationId) {
        DestinationWrapper destination = getDestinationWrapper(destinationService.find(destinationId));
        if(destination == null) {
            throw new ResourceNotFoundException();
        }

        return destination;
    }

    @RequestMapping(value = PATH, method = RequestMethod.GET)
    public List<DestinationWrapper> getDestinations() {
        List<DestinationWrapper> list = new ArrayList<>();
        for(Destination destination : destinationService.findAll()) {
            list.add(getDestinationWrapper(destination));
        }

        return list;
    }

    @RequestMapping(value = PATH, method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> createDestination(@RequestBody DestinationWrapper wrapper) {
        if(wrapper == null || wrapper.getId() != null || wrapper.getUrl() != null) {
            throw new BadRequestException();
        }

        Destination destination = getDestinationFromWrapper(wrapper);
        try {
            destinationService.persist(destination);
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }

        return getResponseCreated(getResourceDestination(destination.getId()));
    }

    @RequestMapping(value = PATH + "/{destinationId}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void updateDestination(@PathVariable Long destinationId, @RequestBody DestinationWrapper wrapper) {
        if(destinationService.find(destinationId) == null) {
            throw new ResourceNotFoundException();
        }

        if(wrapper == null || wrapper.getId() != null || wrapper.getUrl() != null) {
            throw new BadRequestException();
        }

        Destination destination = getDestinationFromWrapper(wrapper);
        destination.setId(destinationId);
        try {
            destinationService.merge(destination);
        } catch (PersistenceException e) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = PATH + "/{destinationId}", method = RequestMethod.DELETE)
    public void deleteDestination(@PathVariable Long destinationId) {
        Destination destination = destinationService.find(destinationId);
        if(destination == null) {
            //OK takova destinace neni v DB
            return;
        }

        destinationService.delete(destination.getId());
    }

    private DestinationWrapper getDestinationWrapper(Destination destination) {
        if(destination == null) return null;

        DestinationWrapper wrapper = new DestinationWrapper();
        wrapper.setId(destination.getId());
        wrapper.setName(destination.getName());
        wrapper.setLat(destination.getLat());
        wrapper.setLon(destination.getLon());

        if(destination.getId() != null) {
            wrapper.setUrl(getResourceDestination(destination.getId()));
        }

        return wrapper;
    }

    private Destination getDestinationFromWrapper(DestinationWrapper wrapper) {
        if(wrapper == null) return null;

        Destination destination = new Destination();
        destination.setName(wrapper.getName());
        destination.setLat(wrapper.getLat());
        destination.setLon(wrapper.getLon());

        return destination;
    }

    @Override
    protected String getResourceDestination(Long id) {
        return PATH + "/" + id;
    }
}

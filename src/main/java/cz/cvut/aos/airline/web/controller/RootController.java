package cz.cvut.aos.airline.web.controller;

import cz.cvut.aos.airline.web.wrapper.DiscoverResourcesWrapper;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jakubchalupa
 * @since 21.11.16
 */
@RestController
public class RootController extends AbstractController {

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public DiscoverResourcesWrapper discoverResources() {
        DiscoverResourcesWrapper wrapper = new DiscoverResourcesWrapper();
        wrapper.add(ControllerLinkBuilder.linkTo(DestinationController.class).withRel("destinations"));
        wrapper.add(ControllerLinkBuilder.linkTo(FlightController.class).withRel("flights"));
        wrapper.add(ControllerLinkBuilder.linkTo(ReservationController.class).withRel("reservations"));

        return wrapper;
    }

}

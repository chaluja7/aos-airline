package cz.cvut.aos.airline.web.controller;

import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.FlightService;
import cz.cvut.aos.airline.service.ReservationService;
import cz.cvut.aos.airline.service.exception.InvalidReservationDeleteException;
import cz.cvut.aos.airline.service.exception.InvalidStateChangeException;
import cz.cvut.aos.airline.service.exception.NotEnoughSeatsException;
import cz.cvut.aos.airline.web.exception.BadRequestException;
import cz.cvut.aos.airline.web.exception.ResourceNotFoundException;
import cz.cvut.aos.airline.web.wrapper.CreateReservationWrapper;
import cz.cvut.aos.airline.web.wrapper.PayReservationWrapper;
import cz.cvut.aos.airline.web.wrapper.ReservationWrapper;
import cz.cvut.aos.airline.web.wrapper.UpdateReservationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
@RestController
@RequestMapping(value = "/reservation")
public class ReservationController extends AbstractController {

    private static final String PATH = "/reservation";

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FlightService flightService;

    @RequestMapping(value = "/{reservationId}", method = RequestMethod.GET)
    public ReservationWrapper getReservation(@PathVariable Long reservationId) {
        ReservationWrapper reservation = getReservationWrapper(reservationService.find(reservationId));
        if(reservation == null) {
            throw new ResourceNotFoundException();
        }

        return reservation;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ReservationWrapper> getReservations() {
        List<ReservationWrapper> list = new ArrayList<>();
        for(Reservation reservation : reservationService.findAll()) {
            list.add(getReservationWrapper(reservation));
        }

        return list;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationWrapper wrapper) {
        Reservation reservation = getReservationFromCreateWrapper(wrapper);
        if(reservation == null || reservation.getSeats() == null || reservation.getSeats() <= 0) {
            throw new BadRequestException();
        }

        try {
            reservationService.persist(reservation);
        } catch (NotEnoughSeatsException | PersistenceException e) {
            throw new BadRequestException();
        }

        return getResponseCreated(getReservationWrapper(reservationService.find(reservation.getId())), getResourceDestination(reservation.getId()));
    }

    @RequestMapping(value = "/{reservationId}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void updateReservation(@PathVariable Long reservationId, @RequestBody UpdateReservationWrapper wrapper) {
        //tenhle PUT neni vubec idempotentni, ale vychazi ze zadani: Only reservation in the state “new” can be canceled.
        //neni jasne, co se ma stat, pokud zavola put na rezervaci, ktera new neni?

        Reservation reservation = reservationService.find(reservationId);
        if(reservation == null) {
            throw new ResourceNotFoundException();
        }

        StateChoices newState = getNewStateFromUpdateWrapper(wrapper);
        if(!StateChoices.CANCELLED.equals(newState) || !StateChoices.NEW.equals(reservation.getState())) {
            //pres put je umozneno pouze stornovat novou
            throw new BadRequestException();
        }

        try {
            reservationService.updateState(reservation.getId(), reservation.getState(), newState);
        } catch (InvalidStateChangeException | PersistenceException e) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/{reservationId}", method = RequestMethod.DELETE)
    public void deleteReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.find(reservationId);
        if(reservation == null) {
            //OK takova destinace neni v DB
            return;
        }

        if(StateChoices.PAID.equals(reservation.getState())) {
            //zaplacena rezervace nemuze byt smazana
            throw new BadRequestException();
        }

        try {
            reservationService.deleteWithStateControl(reservation.getId(), reservation.getState());
        } catch (InvalidReservationDeleteException e) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/{reservationId}/payment", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> payReservation(@PathVariable Long reservationId, @RequestBody PayReservationWrapper wrapper) {
        Reservation reservation = reservationService.find(reservationId);
        if(reservation == null) {
            throw new ResourceNotFoundException();
        }

        if(!StateChoices.NEW.equals(reservation.getState()) || wrapper == null || wrapper.getCardNo() == null) {
            //zaplatit se da pouze NEW rezervace
            throw new BadRequestException();
        }

        try {
            reservationService.updateState(reservation.getId(), reservation.getState(), StateChoices.PAID);
        } catch (InvalidStateChangeException | PersistenceException e) {
            throw new BadRequestException();
        }

        //protoze neexistuje entita "platba", neni ze zadani zrejme, kam nyni presmerovat
        //vyreseno tak, ze se v Location hlavicce vrati pouze url rezervace.
        return getResponseCreated(null, getResourceDestination(reservation.getId()));
    }

    private ReservationWrapper getReservationWrapper(Reservation reservation) {
        if(reservation == null) return null;

        ReservationWrapper wrapper = new ReservationWrapper();
        wrapper.add(ControllerLinkBuilder.linkTo(ReservationController.class).slash(reservation.getId()).withSelfRel());
        wrapper.setEntityId(reservation.getId());
        wrapper.setSeats(reservation.getSeats());
        wrapper.setState(reservation.getState().name());
        wrapper.setPassword(reservation.getPassword());

        if(reservation.getFlight() != null) {
            wrapper.setFlight(reservation.getFlight().getId());
            wrapper.add(ControllerLinkBuilder.linkTo(FlightController.class).slash(reservation.getFlight().getId()).withRel("flight"));
        }

        if(reservation.getCreated() != null) {
            wrapper.setCreated(reservation.getCreated().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }

        return wrapper;
    }

    private Reservation getReservationFromCreateWrapper(CreateReservationWrapper wrapper) {
        if(wrapper == null) return null;

        Reservation reservation = new Reservation();
        reservation.setSeats(wrapper.getSeats());

        if(wrapper.getFlight() != null) {
            reservation.setFlight(flightService.find(wrapper.getFlight()));
            if(reservation.getFlight() == null) {
                throw new BadRequestException();
            }
        }

        return reservation;
    }

    private StateChoices getNewStateFromUpdateWrapper(UpdateReservationWrapper wrapper) {
        if(wrapper == null) return null;

        return StateChoices.fromStringCode(wrapper.getState());
    }

    public static String getResourceDestination(Long id) {
        return PATH + "/" + id;
    }
}

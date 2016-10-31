package cz.cvut.aos.airline.web.controller;

import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.FlightService;
import cz.cvut.aos.airline.service.InvalidStateChangeException;
import cz.cvut.aos.airline.service.NotEnoughSeatsException;
import cz.cvut.aos.airline.service.ReservationService;
import cz.cvut.aos.airline.web.exception.BadRequestException;
import cz.cvut.aos.airline.web.exception.ResourceNotFoundException;
import cz.cvut.aos.airline.web.wrapper.CreateReservationWrapper;
import cz.cvut.aos.airline.web.wrapper.PayReservationWrapper;
import cz.cvut.aos.airline.web.wrapper.ReservationWrapper;
import cz.cvut.aos.airline.web.wrapper.UpdateReservationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReservationController extends AbstractController {

    private static final String PATH = "/reservation";

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FlightService flightService;

    @RequestMapping(value = PATH + "/{reservationId}", method = RequestMethod.GET)
    public ReservationWrapper getReservation(@PathVariable Long reservationId) {
        ReservationWrapper reservation = getReservationWrapper(reservationService.find(reservationId));
        if(reservation == null) {
            throw new ResourceNotFoundException();
        }

        return reservation;
    }

    @RequestMapping(value = PATH, method = RequestMethod.GET)
    public List<ReservationWrapper> getReservations() {
        List<ReservationWrapper> list = new ArrayList<>();
        for(Reservation reservation : reservationService.findAll()) {
            list.add(getReservationWrapper(reservation));
        }

        return list;
    }

    @RequestMapping(value = PATH, method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> createReservation(@RequestBody CreateReservationWrapper wrapper) {
        Reservation reservation = getReservationFromCreateWrapper(wrapper);
        if(reservation == null || reservation.getSeats() == null || reservation.getSeats() <= 0) {
            throw new BadRequestException();
        }

        try {
            reservationService.persist(reservation);
        } catch (NotEnoughSeatsException | PersistenceException e) {
            throw new BadRequestException();
        }

        return getResponseCreated(getResourceDestination(reservation.getId()));
    }

    @RequestMapping(value = PATH + "/{reservationId}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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
            reservationService.updateState(reservationId, newState);
        } catch (InvalidStateChangeException | PersistenceException e) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = PATH + "/{reservationId}", method = RequestMethod.DELETE)
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

        reservationService.delete(reservation.getId());
    }

    @RequestMapping(value = PATH + "/{reservationId}/payment", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> payReservation(@PathVariable Long reservationId, @RequestBody PayReservationWrapper wrapper) {
        Reservation reservation = reservationService.find(reservationId);
        if(reservation == null) {
            throw new ResourceNotFoundException();
        }

        if(!StateChoices.NEW.equals(reservation.getState()) || wrapper == null || wrapper.getCardNo() == null) {
            //zaplatit se da pouze NEW rezervace
            throw new BadRequestException();
        }

        //pri soubehu se zde muze stat, ze by se rezervace zaplatila vicekrat, v ramci teto app nereseno ale vime o tom.

        try {
            reservationService.updateState(reservationId, StateChoices.PAID);
        } catch (InvalidStateChangeException | PersistenceException e) {
            throw new BadRequestException();
        }

        //protoze neexistuje entita "platba", neni ze zadani zrejme, kam nyni presmerovat
        //vyreseno tak, ze se v Location hlavicce vrati pouze url rezervace.
        return getResponseCreated(getResourceDestination(reservation.getId()));
    }

    private ReservationWrapper getReservationWrapper(Reservation reservation) {
        if(reservation == null) return null;

        ReservationWrapper wrapper = new ReservationWrapper();
        wrapper.setId(reservation.getId());
        wrapper.setSeats(reservation.getSeats());
        wrapper.setState(reservation.getState().name());

        if(reservation.getFlight() != null) {
            wrapper.setFlight(reservation.getFlight().getId());
            wrapper.setFlightUrl(FlightController.getResourceDestination(reservation.getFlight().getId()));
        }

        if(reservation.getCreated() != null) {
            wrapper.setCreated(reservation.getCreated().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }

        if(reservation.getId() != null) {
            wrapper.setUrl(getResourceDestination(reservation.getId()));
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

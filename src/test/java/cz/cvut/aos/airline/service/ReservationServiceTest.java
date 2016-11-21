package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import cz.cvut.aos.airline.service.exception.InvalidStateChangeException;
import cz.cvut.aos.airline.service.exception.NotEnoughSeatsException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public class ReservationServiceTest extends AbstractServiceTest {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private ReservationService reservationService;

    @Test
    public void testCRUD() throws NotEnoughSeatsException, InvalidStateChangeException {
        Reservation reservation = getNewReservation(2);
        destinationService.persist(reservation.getFlight().getFrom());
        destinationService.persist(reservation.getFlight().getTo());
        flightService.persist(reservation.getFlight());
        reservationService.persist(reservation);

        int numberOfReservedSeats = flightService.getNumberOfReservedSeats(reservation.getFlight().getId());
        Assert.assertTrue(numberOfReservedSeats > 0);

        Reservation retrievedReservation = reservationService.find(reservation.getId());
        Assert.assertNotNull(retrievedReservation);
        Assert.assertEquals(StateChoices.NEW, retrievedReservation.getState());

        Flight newFlight = FlightServiceTest.getNewFlight("NejakyLet222", 80.56, 30, 49.5, ZonedDateTime.now());
        newFlight.setFrom(reservation.getFlight().getFrom());
        newFlight.setTo(reservation.getFlight().getTo());
        flightService.persist(newFlight);

        reservationService.updateState(retrievedReservation.getId(), retrievedReservation.getState(), StateChoices.PAID);
        retrievedReservation = reservationService.find(retrievedReservation.getId());
        Assert.assertNotNull(retrievedReservation);
        Assert.assertEquals(StateChoices.PAID, retrievedReservation.getState());

        reservationService.delete(retrievedReservation.getId());
        retrievedReservation = reservationService.find(retrievedReservation.getId());
        Assert.assertNull(retrievedReservation);
    }

    public static Reservation getNewReservation(Integer seats) {
        Reservation reservation = new Reservation();
        reservation.setSeats(seats);

        Flight flight = FlightServiceTest.getNewFlight("NejakyLet", 80.56, 30, 49.5, ZonedDateTime.now());
        reservation.setFlight(flight);

        return reservation;
    }

}

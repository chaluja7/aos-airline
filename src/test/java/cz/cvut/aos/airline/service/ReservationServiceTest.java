package cz.cvut.aos.airline.service;

import cz.cvut.aos.airline.entity.Flight;
import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.entity.StateChoices;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

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
    public void testCRUD() {
        Reservation reservation = getNewReservation(2);
        destinationService.persist(reservation.getFlight().getFrom());
        destinationService.persist(reservation.getFlight().getTo());
        flightService.persist(reservation.getFlight());
        reservationService.persist(reservation);

        Reservation retrievedReservation = reservationService.find(reservation.getId());
        Assert.assertNotNull(retrievedReservation);
        Assert.assertEquals(StateChoices.NEW, retrievedReservation.getState());

        Flight newFlight = FlightServiceTest.getNewFlight("NejakyLet222", 100.56, 30, 49.5, new Date());
        newFlight.setFrom(reservation.getFlight().getFrom());
        newFlight.setTo(reservation.getFlight().getTo());
        flightService.persist(newFlight);

        retrievedReservation.setFlight(newFlight);
        retrievedReservation.setState(StateChoices.PAID);
        reservationService.merge(retrievedReservation);

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

        Flight flight = FlightServiceTest.getNewFlight("NejakyLet", 100.56, 30, 49.5, new Date());
        reservation.setFlight(flight);

        return reservation;
    }

}

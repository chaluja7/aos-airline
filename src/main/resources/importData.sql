-- noinspection SqlNoDataSourceInspectionForFile
INSERT INTO destinations(id, name, lat, lon) VALUES (1, 'London', 54.3, 105.2);
INSERT INTO destinations(id, name, lat, lon) VALUES (2, 'Prague', 47.5, 172.3);
INSERT INTO destinations(id, name, lat, lon) VALUES (3, 'Paris', 65.3, 123.3);
INSERT INTO destinations(id, name, lat, lon) VALUES (4, 'Barcelona', 24.8, 131.5);

INSERT INTO flights(id, name, price, seats, distance, dateofdeparture, destination_from_id, destination_to_id) VALUES (1, 'Flight 1', 199.5, 50, 256.7, '2016-10-21 09:10:59', 1, 2);
INSERT INTO flights(id, name, price, seats, distance, dateofdeparture, destination_from_id, destination_to_id) VALUES (2, 'Flight 2', 149.5, 70, 308.5, '2016-10-24 11:15:41', 3, 4);

INSERT INTO reservations(id, seats, password, state, created, flight_id) VALUES (1, 2, 'heslo1', 'NEW', '2016-10-22 10:05:40', 1);
INSERT INTO reservations(id, seats, password, state, created, flight_id) VALUES (2, 5, 'heslo2', 'NEW', '2016-10-23 11:15:50', 1);
INSERT INTO reservations(id, seats, password, state, created, flight_id) VALUES (3, 1, 'heslo3', 'CANCELLED', '2016-10-24 12:25:30', 1);
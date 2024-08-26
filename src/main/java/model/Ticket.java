package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class Ticket {
    private String origin;
    private String originName;
    private String destination;
    private String destinationName;
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate departureDate, arrivalDate;
    @JsonFormat(pattern = "H:mm")
    private LocalTime departureTime, arrivalTime;
    private String carrier;
    private int stops;
    private int price;

    public LocalDateTime getDepartureDateTime() {
        return LocalDateTime.of(departureDate, departureTime);
    }

    public LocalDateTime getArrivalDateTime() {
        return LocalDateTime.of(arrivalDate, arrivalTime);
    }
}
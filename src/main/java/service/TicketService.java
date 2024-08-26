package service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import model.Ticket;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketService {
    private List<Ticket> tickets;

    public TicketService() {
        this.tickets = new ArrayList<>();
    }

    public void loadTickets(String filePath) throws IOException {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        var map = mapper.readValue(new File(filePath), Map.class);
        tickets = mapper.convertValue(map.get("tickets"), mapper.getTypeFactory().constructCollectionType(List.class, Ticket.class));
    }

    public Map<String, Long> calculateMinimumFlightTimes(String origin, String destination) {
        Map<String, Long> minFlightTimes = new HashMap<>();

        for (Ticket ticket : tickets) {
            if (!ticket.getOrigin().equals(origin) || !ticket.getDestination().equals(destination))
                continue;

            LocalDateTime departure = ticket.getDepartureDateTime();
            LocalDateTime arrival = ticket.getArrivalDateTime();
            long flightDuration = Duration.between(departure, arrival).toMinutes();

            minFlightTimes.merge(ticket.getCarrier(), flightDuration, Math::min);
        }

        return minFlightTimes;
    }

    public double calculateAveragePrice(String origin, String destination) {
        return tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination))
                .mapToInt(Ticket::getPrice)
                .average()
                .orElse(0);
    }

    public double calculateMedianPrice(String origin, String destination) {
        List<Integer> prices = tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination))
                .map(Ticket::getPrice)
                .sorted()
                .toList();

        int size = prices.size();
        if (size == 0) {
            return 0;
        } else if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }
}

import service.TicketService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String ticketsFilePath = "tickets.json";
        if (args.length > 0)
            ticketsFilePath = args[0];

        TicketService ticketService = new TicketService();
        try {
            ticketService.loadTickets(ticketsFilePath);
        } catch (IOException e) {
            System.err.println("Не удалось загрузить билеты: " + e.getMessage());
            return;
        }

        System.out.println("Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика:");
        var minFlightTimes = ticketService.calculateMinimumFlightTimes("VVO", "TLV");
        minFlightTimes.forEach((carrier, time) ->
                System.out.println(carrier + " - " + time + " мин."));

        System.out.println("\nРазница между средней ценой билета и медианой для полета между городами Владивосток и Тель-Авив:");
        double average = ticketService.calculateAveragePrice("VVO", "TLV");
        double median = ticketService.calculateMedianPrice("VVO", "TLV");
        System.out.println(average - median);
    }
}

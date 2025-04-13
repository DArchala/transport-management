package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.component.ApplicationTime;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.entity.Road;
import com.mxkoo.transport_management.repository.RoadRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final RoadRepository roadRepository;
    private final ApplicationTime applicationTime;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkRoads() {
        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            if (ChronoUnit.DAYS.between(applicationTime.today(), road.getDepartureDate()) == 1) {
                createAndSendSMS(road.getDriver());
            }
        }

    }

    private void createAndSendSMS(Driver driver) {
        String recipientNumber = driver.getContactNumber()
                                       .toString();
        String twilioNumber = System.getenv("TWILIO_NUMBER");
        Optional<Road> roadOptional = roadRepository.findFirstByDriverIdOrderByDepartureDateAsc(driver.getId());
        if (roadOptional.isPresent()) {
            String destination = roadOptional.get()
                                             .getTo();
            Message.creator(
                           new PhoneNumber(recipientNumber),
                           new PhoneNumber(twilioNumber),
                           "Został 1 dzień do trasy do " + destination)
                   .create();
        }

    }


}

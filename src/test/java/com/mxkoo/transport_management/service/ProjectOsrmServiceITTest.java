package com.mxkoo.transport_management.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProjectOsrmServiceITTest {

    @Autowired
    ProjectOsrmService projectOsrmService;

    @Test
    void getRoute() {
        //given
        var origin = "13.388860,52.517037";
        var destination = "13.397634,52.529407";

        //when
        var distance = projectOsrmService.getRoute(origin, destination).routes().getFirst().distance();

        //then
        assertEquals(0, distance.compareTo(1886.8));
    }

}
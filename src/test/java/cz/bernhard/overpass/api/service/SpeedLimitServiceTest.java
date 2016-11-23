package cz.bernhard.overpass.api.service;


import cz.bernhard.overpass.api.service.SpeedLimitService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpeedLimitServiceTest {

    @Test
    public void speedLimitForGivenGps() {

        // given

        /* Olbrachtova 50.043260, 14.440141 */
        double latitude = 50.043260;
        double longitude = 14.440141 ;

        SpeedLimitService service = new SpeedLimitService();

        // when
        int speedLimit = service.speedLimit(latitude, longitude);

        // then
        assertEquals("Ocekavana rychlost v Olbrachtova, Praha je 50 km/h", 50, speedLimit);

    }

}

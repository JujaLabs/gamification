package juja.microservices.gamification.entity;

import juja.microservices.WithoutScheduling;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Vadim Dyachenko
 */
public class KeeperDTOTest implements WithoutScheduling {
    @Test
    public void shouldReturnKeeperDTO() {
        String expectedUuid = "000A2";
        List<String> expectedDirection = Collections.singletonList("first direction");
        List<String> direction = Collections.singletonList("first direction");
        KeeperDTO keeper = new KeeperDTO("000A2", direction);

        assertNotNull(keeper);
        assertEquals(expectedUuid, keeper.getUuid());
        assertEquals(expectedDirection, keeper.getDirections());
    }
}

package juja.microservices.gamification.entity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Vadim Dyachenko
 */
public class KeeperDTOTest {
    @Test
    public void shouldReturnKeeperDTO() {
        String expectedUuid = "000A2";
        List<String> expectedDirection = new ArrayList<>();
        expectedDirection.add("first direction");

        List<String> direction = new ArrayList<>();
        direction.add("first direction");
        KeeperDTO keeper = new KeeperDTO("000A2", direction);

        assertNotNull(keeper);
        assertEquals(expectedUuid, keeper.getUuid());
        assertEquals(expectedDirection, keeper.getDirection());
    }
}

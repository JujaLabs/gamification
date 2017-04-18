package juja.microservices.gamification.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KeeperTest {

        @Test
        public void shouldReturnKeeper() {
            String expectedFrom = "alex";
            String expectedKeeperOf = "codenjoy";
            String expectedUuid = "ab2test";

            Keeper keeper = new Keeper("ab2test", "codenjoy", "alex");

            assertNotNull(keeper);
            assertEquals(expectedUuid, keeper.getUuid());
            assertEquals(expectedFrom, keeper.getFrom());
            assertEquals(expectedKeeperOf, keeper.getKeeperOf());
        }
}
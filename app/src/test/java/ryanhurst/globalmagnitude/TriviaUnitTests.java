package ryanhurst.globalmagnitude;

import org.junit.Test;

import ryanhurst.globalmagnitude.models.TriviaGame;

import static org.junit.Assert.*;

/**
 * Unit test to check timer
 */
public class TriviaUnitTests {
    @Test
    public void testElapsedTime() throws Exception {
        assertTrue("00:00.15".equals(GmHelper.formatElapsedTime(151)));
        assertTrue("00:30.23".equals(GmHelper.formatElapsedTime(30231)));
        assertTrue("17:10.04".equals(GmHelper.formatElapsedTime(1030041)));
    }

    @Test
    public void testRoundCreation() throws Exception {
        for(int i = 0; i < 100; i++) {
            TriviaGame game = new TriviaGame();
            for(TriviaGame.Round r : game.rounds) {
                assertEquals(r.answers.size(), TriviaGame.Round.NUMBER_OF_ANSWERS);
                assertTrue(r.factor1 < TriviaGame.Round.MAX_NUMBER);
                assertTrue(r.factor1 >= TriviaGame.Round.MIN_NUMBER);

                assertTrue(r.factor2 < TriviaGame.Round.MAX_NUMBER);
                assertTrue(r.factor2 >= TriviaGame.Round.MIN_NUMBER);

                for(Integer answer : r.answers) {
                    assertTrue(answer < TriviaGame.Round.MAX_NUMBER * TriviaGame.Round.MAX_NUMBER);
                    assertTrue(answer >= TriviaGame.Round.MIN_NUMBER * TriviaGame.Round.MIN_NUMBER);
                }
            }
        }
    }
}
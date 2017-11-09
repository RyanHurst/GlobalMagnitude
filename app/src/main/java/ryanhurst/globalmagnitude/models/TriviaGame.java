package ryanhurst.globalmagnitude.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * model to represent a game
 *
 * Created by Ryan on 11/8/2017.
 */

public class TriviaGame {
    public static final int NUMBER_OF_ROUNDS = 20;

    public ArrayList<Round> rounds;

    public int currentRoundIndex = 0;
    public long startTime;
    public long endTime;

    public TriviaGame() {
        startTime = System.currentTimeMillis();
        rounds = new ArrayList<>(NUMBER_OF_ROUNDS);
        for(int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            rounds.add(new Round());
        }
    }

    public Round getCurrentRound() {
        return rounds.get(currentRoundIndex);
    }

    public class Round {
        public static final int NUMBER_OF_ANSWERS = 6;
        public static final int MAX_NUMBER = 100;
        public static final int MIN_NUMBER = 10;

        public int factor1, factor2;
        public int userAnswerIndex;

        public ArrayList<Integer> answers;

        public Round() {
            userAnswerIndex = -1;
            factor1 = generateFactor();
            factor2 = generateFactor();

            HashSet<Integer> answerSet = new HashSet<>();
            answerSet.add(factor1 * factor2);
            for(int i = 1; i < NUMBER_OF_ANSWERS; i++) {
                int generatedAnswer = generateAnswer(factor1, factor2);
                while(answerSet.contains(generatedAnswer)) {
                    generatedAnswer = generateAnswer(factor1, factor2);
                }
                answerSet.add(generatedAnswer);
            }

            answers = new ArrayList<>(answerSet);

            Collections.shuffle(answers);
        }

        private int generateAnswer(int factor1, int factor2) {
            int low = ((factor1/10) * (factor2/10)) * 100;
            int high = ((factor1/10 + 1) * (factor2/10 + 1)) * 100;

            return (int) (Math.random() * (high - low) + low);
        }

        private int generateFactor() {
            return (int)(Math.random() * (MAX_NUMBER - MIN_NUMBER) + MIN_NUMBER);
        }

    }

}

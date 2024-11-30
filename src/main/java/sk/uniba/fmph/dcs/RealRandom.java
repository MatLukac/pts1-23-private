package sk.uniba.fmph.dcs;

import interfaces.Constants;
import interfaces.RandomGeneratorInterface;

import java.util.Random;

public class RealRandom implements RandomGeneratorInterface, Constants {
    private final Random random = new Random();

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}

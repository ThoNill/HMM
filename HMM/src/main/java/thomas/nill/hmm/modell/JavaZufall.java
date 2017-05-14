package thomas.nill.hmm.modell;

import java.util.Random;

public class JavaZufall implements Zufall {
    Random random = new Random();

    public JavaZufall() {
    }

    @Override
    public double zufälligesDouble() {
        return random.nextDouble();
    }

}

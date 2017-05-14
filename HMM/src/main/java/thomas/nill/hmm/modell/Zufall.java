package thomas.nill.hmm.modell;

public interface Zufall {
    double zufälligesDouble();

    default int zufälligerIndex(double[] häufigkeit) {
        double wert = zufälligesDouble();
        double summe = 0;
        for (int index = 0; index < häufigkeit.length; index++) {
            summe += häufigkeit[index];
            if (summe >= wert) {
                return index;
            }
        }
        return häufigkeit.length - 1;
    }
}

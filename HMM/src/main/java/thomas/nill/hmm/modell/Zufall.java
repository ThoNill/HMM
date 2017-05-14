package thomas.nill.hmm.modell;

public interface Zufall {
    double zuf�lligesDouble();

    default int zuf�lligerIndex(double[] h�ufigkeit) {
        double wert = zuf�lligesDouble();
        double summe = 0;
        for (int index = 0; index < h�ufigkeit.length; index++) {
            summe += h�ufigkeit[index];
            if (summe >= wert) {
                return index;
            }
        }
        return h�ufigkeit.length - 1;
    }
}

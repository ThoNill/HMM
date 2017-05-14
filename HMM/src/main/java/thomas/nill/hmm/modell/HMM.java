package thomas.nill.hmm.modell;


import java.io.Serializable;

public interface HMM extends Serializable {
    int getAnzahlDerZust‰nde();

    int getAnzahlDerEmissionen();

    double getStartH‰ufigkeit(int zustand);

    void setStartH‰ufigkeit(int zustand, double p);

    double get‹bergang(int vonZustand, int nachZustand);

    void set‹bergang(int vonZustand, int nachZustand, double p);

    double getEmissionsH‰ufigkeit( int zustand, int emission);

    void setEmissionsH‰ufigkeit( int zustand,int emission, double p);

    String getZustandsName(int zustand);

    String getEmissionsName(int emission);

    double[] emissionsH‰ufigkeiten(int zustand);

    double[] folgezustandsH‰ufigkeiten(int zustand);

    double[] getStartH‰ufigkeiten();

    default boolean testeWerte() {
        return testStartH‰ufigkeiten() && testEmissionsH‰ufigkeiten()
                && testFolgezustandsH‰¸figkeiten()
                && testInverseFolgezustandsH‰ufigkeiten();
    }

    default boolean testStartH‰ufigkeiten() {
        boolean erg = vergleich(1, summe(getStartH‰ufigkeiten()));
        if (!erg) {
            System.out.println("Inkonsistenz in den StartH‰ufigkeiten");
        }
        return erg;
    }

    default boolean testEmissionsH‰ufigkeiten(int zustand) {
        return vergleich(1, summe(emissionsH‰ufigkeiten(zustand)));
    }

    default boolean testEmissionsH‰ufigkeiten() {
        for (int zustand = 0; zustand < getAnzahlDerZust‰nde(); zustand++) {
            if (!testEmissionsH‰ufigkeiten(zustand)) {
                System.out.println("Inkonsistenz in den EmissionsH‰ufigkeiten");
                return false;
            }
        }
        return true;
    }

    default boolean testFolgezustandsH‰¸figkeiten() {
        for (int zustand = 0; zustand < getAnzahlDerZust‰nde(); zustand++) {
            if (!testFolgezustandsH‰ufigkeiten(zustand)) {
                System.out
                        .println("Inkonsistenz in den FolgeZustandsH‰ufigkeiten in Zustand " + zustand);

                return false;
            }
        }
        return true;
    }

    default boolean testFolgezustandsH‰ufigkeiten(int zustand) {
        return vergleich(1, summe(folgezustandsH‰ufigkeiten(zustand)));
    }

    default boolean testInverseFolgezustandsH‰ufigkeiten() {
        for (int zustand = 0; zustand < getAnzahlDerZust‰nde(); zustand++) {
            if (!testInverseFolgezustandsH‰ufigkeiten(zustand)) {
                System.out
                        .println("Inkonsistenz in den inversen FolgeZustandsH‰ufigkeiten in zustand "+zustand);

                return false;
            }
        }
        return true;
    }

    default double[] inverseFolgezustandsH‰ufigkeiten(int zustand) {
        int anzahl = getAnzahlDerZust‰nde();
        double[] werte = new double[anzahl];
        for (int z = 0; z < anzahl; z++) {
            werte[z] = get‹bergang(zustand, z);
        }
        ausgabe(werte);
        return werte;
    };

    default boolean testInverseFolgezustandsH‰ufigkeiten(int zustand) {
        return vergleich(1, summe(inverseFolgezustandsH‰ufigkeiten(zustand)));
    }

    default boolean vergleich(double erwartet, double wert) {
        boolean gleich   = vergleich(erwartet, wert, 0.0001);
        if (!gleich) {
            System.out.println("a= " + erwartet + " b= " + wert);
        }
        return gleich;
    }

    default boolean vergleich(double erwartet, double wert, double grenze) {
        return Math.abs(erwartet - wert) <= grenze;
    }

    default double summe(double[] werte) {
        double summe = 0.0;
        for (int i = 0; i < werte.length; i++) {
            summe += werte[i];
        }
        return summe;
    }

    default void ausgabe(double[] werte) {
        System.out.println();
        for (int i = 0; i < werte.length; i++) {
            System.out.print(werte[i] + " ");
        }
    }
}
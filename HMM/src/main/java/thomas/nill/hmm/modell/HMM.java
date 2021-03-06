package thomas.nill.hmm.modell;


import java.io.Serializable;

public interface HMM extends Serializable {
    int getAnzahlDerZustände();

    int getAnzahlDerEmissionen();

    double getStartHäufigkeit(int zustand);

    void setStartHäufigkeit(int zustand, double p);

    double getÜbergang(int vonZustand, int nachZustand);

    void setÜbergang(int vonZustand, int nachZustand, double p);

    double getEmissionsHäufigkeit( int zustand, int emission);

    void setEmissionsHäufigkeit( int zustand,int emission, double p);

    String getZustandsName(int zustand);

    String getEmissionsName(int emission);

    double[] emissionsHäufigkeiten(int zustand);

    double[] folgezustandsHäufigkeiten(int zustand);

    double[] getStartHäufigkeiten();

    default boolean testeWerte() {
        return testStartHäufigkeiten() && testEmissionsHäufigkeiten()
                && testFolgezustandsHäüfigkeiten()
                && testInverseFolgezustandsHäufigkeiten();
    }

    default boolean testStartHäufigkeiten() {
        boolean erg = vergleich(1, summe(getStartHäufigkeiten()));
        if (!erg) {
            System.out.println("Inkonsistenz in den StartHäufigkeiten");
        }
        return erg;
    }

    default boolean testEmissionsHäufigkeiten(int zustand) {
        return vergleich(1, summe(emissionsHäufigkeiten(zustand)));
    }

    default boolean testEmissionsHäufigkeiten() {
        for (int zustand = 0; zustand < getAnzahlDerZustände(); zustand++) {
            if (!testEmissionsHäufigkeiten(zustand)) {
                System.out.println("Inkonsistenz in den EmissionsHäufigkeiten");
                return false;
            }
        }
        return true;
    }

    default boolean testFolgezustandsHäüfigkeiten() {
        for (int zustand = 0; zustand < getAnzahlDerZustände(); zustand++) {
            if (!testFolgezustandsHäufigkeiten(zustand)) {
                System.out
                        .println("Inkonsistenz in den FolgeZustandsHäufigkeiten in Zustand " + zustand);

                return false;
            }
        }
        return true;
    }

    default boolean testFolgezustandsHäufigkeiten(int zustand) {
        return vergleich(1, summe(folgezustandsHäufigkeiten(zustand)));
    }

    default boolean testInverseFolgezustandsHäufigkeiten() {
        for (int zustand = 0; zustand < getAnzahlDerZustände(); zustand++) {
            if (!testInverseFolgezustandsHäufigkeiten(zustand)) {
                System.out
                        .println("Inkonsistenz in den inversen FolgeZustandsHäufigkeiten in zustand "+zustand);

                return false;
            }
        }
        return true;
    }

    default double[] inverseFolgezustandsHäufigkeiten(int zustand) {
        int anzahl = getAnzahlDerZustände();
        double[] werte = new double[anzahl];
        for (int z = 0; z < anzahl; z++) {
            werte[z] = getÜbergang(zustand, z);
        }
        ausgabe(werte);
        return werte;
    };

    default boolean testInverseFolgezustandsHäufigkeiten(int zustand) {
        return vergleich(1, summe(inverseFolgezustandsHäufigkeiten(zustand)));
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
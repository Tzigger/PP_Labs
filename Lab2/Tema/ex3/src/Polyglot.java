//import libraria principala polyglot din graalvm
import jdk.dynalink.linker.LinkerServices;
import org.graalvm.polyglot.*;
import java.util.*;

//clasa principala - aplicatie JAVA
class Polyglot {
    //metoda privata pentru conversie low-case -> up-case folosind functia toupper() din R
    private static double calc_prob_R(int nr_aruncari, int x){
        //construim un context care ne permite sa folosim elemente din R
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei funcitiei R, toupper(String)
        //pentru aexecuta instructiunea I din limbajul X, folosim functia graalvm polyglot.eval("X", "I");

        String R_Script = "pbinom(" + x + ", size=" + nr_aruncari + ", prob=0.5)";

        Value result = polyglot.eval("R", R_Script);
        //utilizam metoda asString() din variabila incarcata cu output-ul executiei pentru a mapa valoarea generica la un String
        double result_double = result.asDouble();
        // inchidem contextul Polyglot
        polyglot.close();

        return result_double;
    }

    //metoda privata pentru evaluarea unei sume de control simple a literelor unui text ASCII, folosind PYTHON
    private static int citeste_nr_aruncari(){
        //construim un context care ne permite sa folosim elemente din PYTHON
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei functiei PYTHON, sum()
        //avem voie sa inlocuim anumite elemente din scriptul pe care il construim spre evaluare, aici token provine din JAVA, dar va fi interpretat de PYTHON

        String py_script = "n = int(input('Numarul de aruncari: '))\n" + "n";

        Value result = polyglot.eval("python", py_script);
        //utilizam metoda asInt() din variabila incarcata cu output-ul executiei, pentru a mapa valoarea generica la un Int
        int nr_aruncari = result.asInt();
        // inchidem contextul Polyglot
        polyglot.close();

        return nr_aruncari;
    }

    private static int citeste_x(){
        //construim un context care ne permite sa folosim elemente din PYTHON
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei functiei PYTHON, sum()
        //avem voie sa inlocuim anumite elemente din scriptul pe care il construim spre evaluare, aici token provine din JAVA, dar va fi interpretat de PYTHON

        String py_script = "n = int(input('x : '))\n" + "n";

        Value result = polyglot.eval("python", py_script);
        //utilizam metoda asInt() din variabila incarcata cu output-ul executiei, pentru a mapa valoarea generica la un Int
        int x = result.asInt();
        // inchidem contextul Polyglot
        polyglot.close();

        return x;
    }


    //functia MAIN
    public static void main(String[] args) {

        int nr_aruncari =  citeste_nr_aruncari();

        int x = citeste_x();

        while(x < 1 || x > nr_aruncari)
        {
            System.out.println("EROARE: x trebuie să fie între 1 și " + nr_aruncari);
            x = citeste_x();
        }

        double probabilitate = calc_prob_R(nr_aruncari, x);

        System.out.printf("Probabilitatea de a obține cel mult %d pajură: %.4f", x, probabilitate);

    }
}


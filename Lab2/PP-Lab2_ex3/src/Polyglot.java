//import libraria principala polyglot din graalvm
import org.graalvm.polyglot.*;

import java.util.Arrays;
import java.util.stream.Collectors;

//clasa principala - aplicatie JAVA
class Polyglot {

    //metoda privata pentru generarea aleatoare a unei liste de nr intregi, folosind PYTHON
    private static int[] gen_numbers() {
        //construim un context care ne permite sa folosim elemente din PYTHON
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei functiei PYTHON, sum()
        //avem voie sa inlocuim anumite elemente din scriptul pe care il construim spre evaluare, aici token provine din JAVA, dar va fi interpretat de PYTHON

        String pythonExpression = "import random\n" +
                "random.seed()\n" +
                "[random.randint(1,100) for _ in range(20)]";
        Value result = polyglot.eval("python", pythonExpression);

        //utilizam metoda asInt() din variabila incarcata cu output-ul executiei, pentru a mapa valoarea generica la un Int
        int[] numbers = new int[(int) result.getArraySize()];
        for (int i = 0; i < result.getArraySize(); i++) {
            numbers[i] = result.getArrayElement(i).asInt();
        }
        // inchidem contextul Polyglot
        polyglot.close();

        return numbers;
    }

    //metoda privata pentru afisarea numerelor, folosind js
    private static void displayNumbers(int[] numbers) {
        Context polyglot = Context.create();
        String jsArray = "[" + java.util.Arrays.stream(numbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", ")) + "]";
        String jsScript = "console.log('Generated numbers: ' + " + jsArray + ".join(', '))";
        polyglot.eval("js", jsScript);
        polyglot.close();
    }

    //cine mai foloseste R ffs...... will just chatgpt this sh*t if it can't figure it out
    private static double functie_R(int[] numbers) {
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        String rArray = "c(" + java.util.Arrays.stream(numbers)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", ")) + ")";
        String rScript = rArray +
                " sorted <- sort(" + rArray + ", decreasing=FALSE)\n" +
                " n <- length(sorted)\n" +
                " trimmed <- sorted[(ceiling(n * 0.2) + 1):(n - floor(n * 0.2))]\n" +
                " mean(trimmed)";

        Value result = polyglot.eval("R", rScript);
        double mean = result.asDouble();
        polyglot.close();
        return mean;
    }

    //functia MAIN
    public static void main(String[] args) {
        int[] numbers = gen_numbers();
        displayNumbers(numbers);
        double mean = functie_R(numbers);
        System.out.println("Lista sortata: " + mean);
    }
}


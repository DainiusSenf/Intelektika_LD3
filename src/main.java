import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Dainiuss on 2016-04-20.
 */
public class main {

    private static String wantedProbabiltiy;
    private static String knownFactors;
    private static int iterationsCount;
    private static ArrayList<Node> nodes = new ArrayList<>();
    private static ArrayList<Node> cells = new ArrayList<>();
    private static ArrayList<ArrayList<Node>> rows = new ArrayList<>();
    private static BigDecimal wantedSum = new BigDecimal("0");
    private static BigDecimal totalSum = new BigDecimal("0");

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("failas.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Node n = new Node();
                ArrayList<String> splitedLine = new ArrayList<>(Arrays.asList(line.split(" ")));
                n.name = splitedLine.get(0);
                n.fathersCount = Integer.valueOf(splitedLine.get(1));
                for(int i=1;i<=n.fathersCount;i++)
                    n.fathers.add(Integer.valueOf(splitedLine.get(1+i)));

                for(Integer i=2+n.fathersCount;i<splitedLine.size();i++)
                    n.probabilities.add(new BigDecimal(splitedLine.get(i)));

                nodes.add(n);
            }
        }

        readInput();

        ArrayList<String> factorsArray = new ArrayList<>(Arrays.asList(knownFactors.split(" ")));
        for(int i = 0; i < iterationsCount; i++) {
            cells = new ArrayList<>();
            for (Node node : nodes) {
                Node newNode = new Node();
                newNode.name = node.name;
                if (factorsArray.contains(node.name)) {
                    int probIndex = getProbabilityIndex(node);
                    newNode.probability = node.probabilities.get(probIndex);
                    newNode.state = true;
                } else if (factorsArray.contains("-" + node.name)) {
                    int probIndex = getProbabilityIndex(node);
                    BigDecimal one = new BigDecimal("1");
                    newNode.probability = one.subtract(node.probabilities.get(probIndex));
                    newNode.state = false;
                } else if (node.fathers.isEmpty()) {
                    newNode.probability = node.probabilities.get(0);
                    newNode.state = getTrueOrFalse(node.probabilities.get(0));
                } else {
                    int probIndex = getProbabilityIndex(node);
                    newNode.probability = node.probabilities.get(probIndex);
                    newNode.state = getTrueOrFalse(node.probabilities.get(probIndex));
                }
                cells.add(newNode);
            }
            Node s = new Node();
            s.name = "S";
            s.probability = getNodeSProbability(factorsArray);
//            s.probability = new BigDecimal("1");
//            for(Node c : cells){
//                if(factorsArray.contains(c.name) || factorsArray.contains("-" + c.name)){
//                    s.probability = s.probability.multiply(c.probability);
//                }
//            }
//            for(Node c : cells){
//                if((Objects.equals(wantedProbabiltiy, c.name) && c.state)
//                        || (Objects.equals(wantedProbabiltiy, "-"+c.name) && !c.state)){
//                    wantedSum = wantedSum.add(s.probability);
//                }
//            }
            getWantedSum(s);
            totalSum = totalSum.add(s.probability);
            cells.add(s);
            rows.add(cells);
        }
        printRezults();

//        for(Node n : cells){
//            System.out.format("%10s | ", n.name);
//        }
//        System.out.println();
//        for(ArrayList<Node> row : rows){
//            for(Node n : row){
//                System.out.format("%5s %4s | ", n.state,String.valueOf(n.probability));
//            }
//            System.out.println();
//        }
//
//        System.out.format("%5s %4s | ", String.valueOf(wantedSum),String.valueOf(totalSum));
//        System.out.println();
//        Double probability = wantedSum.doubleValue() / totalSum.doubleValue();
//        System.out.println(probability);
    }

    private static void printRezults(){
        for(Node n : cells){
            System.out.format("%10s | ", n.name);
        }
        System.out.println();
        for(ArrayList<Node> row : rows){
            for(Node n : row){
                System.out.format("%5s %4s | ", n.state,String.valueOf(n.probability));
            }
            System.out.println();
        }

        System.out.format("\nWanted probabilty sum: %5s \nAll probabilities sum: %4s ", String.valueOf(wantedSum),String.valueOf(totalSum));
        System.out.println();
        Double probability = wantedSum.doubleValue() / totalSum.doubleValue();
        System.out.print("Results of P("+ wantedProbabiltiy+"|"+Arrays.asList(knownFactors.split(" "))+") = ");
        System.out.println(probability);
    }

    private static BigDecimal getNodeSProbability(ArrayList<String> factorsArray){
        BigDecimal probability = new BigDecimal("1");
        for(Node c : cells){
            if(factorsArray.contains(c.name) || factorsArray.contains("-" + c.name)){
                probability = probability.multiply(c.probability);
            }
        }
        return probability;
    }

    private static void getWantedSum(Node nodeS){
        for(Node c : cells){
            if((Objects.equals(wantedProbabiltiy, c.name) && c.state)
                    || (Objects.equals(wantedProbabiltiy, "-"+c.name) && !c.state)){
                wantedSum = wantedSum.add(nodeS.probability);
            }
        }
    }

    private static void readInput() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Scanner scan = new Scanner(System.in);
        System.out.print("Kokia tikimybe domina?\n");
        wantedProbabiltiy = scan.next();
        System.out.print("Kas yra zinoma?\n");
        knownFactors = br.readLine();
        System.out.print("Kiek iteraciju?\n");
        iterationsCount = scan.nextInt();
    }

    private static boolean getTrueOrFalse(BigDecimal p){
        Random random = new Random();
        double number = p.doubleValue();
        double randomDouble = random.nextDouble();
        return randomDouble < number;
    }

    private static int getProbabilityIndex(Node node){
        String fatherStates = "";
        if(node.fathers.isEmpty())
            return 0;
        for(Integer father : node.fathers){
            if(cells.get(father).state)
                fatherStates = fatherStates + "1";
            else
                fatherStates = fatherStates + "0";
        }
        return Integer.parseInt(fatherStates, 2);
    }
}

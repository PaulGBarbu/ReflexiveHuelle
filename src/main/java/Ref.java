import com.google.gson.Gson;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Ref {

    static class Graph {
        List<String> nodes = new ArrayList<>();
        List<List<String>> edges = new ArrayList<>();

        void addNode(int newNode) {
            addNode(String.valueOf(newNode));
        }

        void addNode(String newNode) {
            int node = Integer.parseInt(newNode);

            node++;

            nodes.add(String.valueOf(node));
        }

        void addEdge(int node1, int node2) {
            addEdge(String.valueOf(node1), String.valueOf(node2));
        }

        void addEdge(String node1, String node2) {
            int node1_int = Integer.parseInt(node1);
            int node2_int = Integer.parseInt(node2);

            node1_int++;
            node2_int++;

            List<String> edge = new ArrayList<>();
            edge.add(String.valueOf(node1_int));
            edge.add(String.valueOf(node2_int));

            edges.add(edge);
        }

    }

    public static void main(String[] args) {
        int n = 3;
        int[][] array = new int[n][n];

        int[][] testArray = {
                {0, 0, 1},
                {0, 0, 1},
                {0, 0, 1}};

        int[][] calculatedArray = calculateRefTransHuelle(testArray);

        System.out.println("Calculated Matrix:");
        printMatrix(calculatedArray);

        createJSON(calculatedArray);

        //TODO: In Intellij!
        //  Go to File >> Settings >> Build, Execution, Deployment >> Debugger
        //  then check the Allow unsigned requests. (Tick mark that)
        openHTML();
    }

    static int[][] calculateRefTransHuelle(int[][] inputArray) {
        int[][] outputArray = inputArray;
        int dimension = inputArray.length;
        boolean foundOne = false;
        boolean checkedAlready[][] = new boolean[inputArray.length][inputArray.length];

        // Insert all the reflexive ones:
        for (int i = 0; i < dimension; i++) {
            outputArray[i][i] = 1;
        }

        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray.length; j++) {
                // Wenn eine 1 gefunden wird, die nicht reflexiv ist (i != j), schaue dir die dazugehörige Zeile k an
                if (i != j && outputArray[i][j] == 1) {
                    for (int k = 0; k < inputArray.length; k++) {
                        // Wenn eine 1 in der k-Spalte vorhanden ist, übernehme sie für die i-Spalte
                        if (outputArray[j][k] == 1) {
                            outputArray[i][k] = 1;
                        }
                    }
                }
            }
        }

        return outputArray;
    }

    static void printMatrix(int[][] inputArray) {
        System.out.println("-------------------");
        for (int i = 0; i < inputArray[0].length; i++) {
            System.out.print("|  ");
            for (int j = 0; j < inputArray.length; j++) {
                System.out.print((inputArray[i][j]) + "  |  ");
            }
            System.out.println();
            System.out.println("-------------------");
        }
    }

    static void createJSON(int[][] matrix) {
        Graph graph = new Graph();


        // create Graph from Matrix

        // add nodes to graph
        for (int col = 0; col < matrix[0].length; col++) {
            graph.addNode(col);
        }

        // add Edges
        for (int col = 0; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length; row++) {
                if (matrix[col][row] == 1) {
                    graph.addEdge(col, row);
                }

                // Special Case Diagonale
                if(col == 1 && row == 1) {
                    // TODO: self looping case
                }
            }
        }

        Gson gson = new Gson();

        String json = gson.toJson(graph);

        try {
            Files.write(Path.of("webpage/graph.json"), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void openHTML() {
        try {
            Desktop.getDesktop().browse(URI.create("http://localhost:63342/DSJava/webpage/index.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
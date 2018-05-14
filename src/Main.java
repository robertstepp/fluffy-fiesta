
// Java Program by Robert Stepp and Freddie Krueger 05/20/2018
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import javax.swing.JOptionPane;

public class Main {
	private static HashMap<Integer, Double> rainfall = new HashMap<Integer, Double>();
	private static PriorityQueue pq = new PriorityQueue();
	private static ArrayList<Integer> dateArray = new ArrayList<Integer>();
	static String[] menuOptions = { "Output all", "Total rainfall",
			"Average monthly rainfall", "Month with the most rain",
			"Month with the least rain" };
	static String fileName = "";
	static Object menu = null;

	// Imports the file
	// https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	public static void importFile(String name) {
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(name));
			while ((line = br.readLine()) != null) {
				String[] tempStorage = line.split(",");
				rainfall.put(
						Integer.parseInt(tempStorage[0]),
						Double.parseDouble(tempStorage[1]));

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Populates the priority queue to sort the dates.
	private static void populatePQ() {
		Object[] tempArray = rainfall.keySet().toArray(); // Puts the keys from
															// the hashmap into
															// a temporary
															// array.
		for (int i = 0; i < rainfall.keySet().size(); i++) {
			pq.add(tempArray[i]);
		}
	}

	// Populates the date arraylist with sorted dates from priority queue (Early
	// to present).
	private static void populateDA() {
		int pqSize = pq.size();
		for (int i = 0; i < pqSize; i++) {
			dateArray.add((Integer) pq.remove());
		}
	}

	// Returns the amount of rainfall for every month.
	private static void returnRainfallMonthly() {
		for (int j = 0; j < dateArray.size(); j++) {
			System.out.print(dateArray.get(j) + "\t");
			System.out.println(rainfall.get(dateArray.get(j)));
		}
	}

	// Gets input from user of the filename and how they want the output.
	public static void input() {
		fileName = JOptionPane.showInputDialog(null,
				"Please enter filename:", "./src/Rainfall.csv");
		menu = JOptionPane.showInputDialog(null,
				"Choose output:", "Choose wisely",
				JOptionPane.QUESTION_MESSAGE, null, menuOptions, null);
	}

	// Takes the chosen output method and passes it to the associated method.
	public static void chosenOutput() {
		if (menu.equals(menuOptions[0])) {
			returnRainfallMonthly();
		} else if (menu.equals(menuOptions[1])) {
			System.out.println(1);
		} else if (menu.equals(menuOptions[2])) {
			System.out.println(2);
		} else if (menu.equals(menuOptions[3])) {
			System.out.println(3);
		} else if (menu.equals(menuOptions[4])) {
			System.out.println(4);
		}
	}

	public static void main(String[] args) {
		input();
		importFile(fileName);
		populatePQ();
		populateDA();
		chosenOutput();

	}

}

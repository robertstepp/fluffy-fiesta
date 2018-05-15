// Java Program by Robert Stepp and Freddy Krueger 05/20/2018
/**
 * CS143 Team Project
 * 3. (Based on Gaddis programming challenges 7.1) Rainfall Class
 * Create a text file that has 18 doubles with the average monthly rainfall for Olympia in the last year.
 * Write a RainFall class that stores the total rainfall for each of 18 months into an array of doubles. The program must accept the input out of chronological order and store it in sorted order.
 * Input dates as an integer, stored in the file as YYYYMM. 
 * Sample input file:
 * 201802  1.3
 * 201701  2.5     
 * 201803  4.0
 * 201711  3.5
 * 201802  1.2
 *  . . . etc.
 * The program should have methods that return the following:
 * • Output the rainfall and date (YYYYMM) in order sorted by date
 * • The total rainfall on record
 * • The average monthly rainfall
 * • The month with the most rain
 * • The month with the least rain
 * Demonstrate the class in a complete program. Do not perform input validation.
 * @author Robert Stepp
 * @author Freddy Krueger
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

import javax.swing.JOptionPane;

public class Main {
	private static HashMap<Integer, Double> rainfall = new HashMap<Integer, Double>();
	private static PriorityQueue<Object> pq = new PriorityQueue<Object>();
	private static ArrayList<Integer> dateArray = new ArrayList<Integer>();
	static String[] menuOptions = { "Output every month", "Total rainfall",
			"Average monthly rainfall", "Month with the most rain",
			"Month with the least rain", "Output all previous choices" };
	static String[] months = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };
	static String fileName = "";
	static HashMap<Integer, Character> rainfallGraphFill = new HashMap<Integer, Character>() {
		{
			put(500, '#');
			put(100, '*');
			put(50, '|');
			put(25, '¦');
			put(10, '!');
			put(5, ':');
			put(1, '.');
		}
	};
	static HashMap<Integer, Integer> graphValues = new HashMap<Integer, Integer>() {
		{
			put(500, 0);
			put(100, 0);
			put(50, 0);
			put(25, 0);
			put(10, 0);
			put(5, 0);
			put(1, 0);
		}
	};
	static Object[] keys = graphValues.keySet().toArray();
	static Object menu = null;
	static int monthMax = 0;
	static int monthMin = 0;
	static double monthMaxRainfall = 0.0;
	static double monthMinRainfall = 100.0;
	static double totalMonthlyRainfall = 0.0;

	/**
	 * Imports the file
	 * 
	 * @param name
	 *            From input user supplied
	 */
	// https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	public static void importFile(String name) {
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(name));
			while ((line = br.readLine()) != null) {
				String[] tempStorage = line.split(",");
				String[] trimStorage = new String[tempStorage.length];
				for (int i = 0; i < tempStorage.length; i++) {
					trimStorage[i] = tempStorage[i].trim();
				}
				rainfall.put(
						Integer.parseInt(trimStorage[0]),
						Double.parseDouble(trimStorage[1]));
				findMax(Double.parseDouble(trimStorage[1]),
						Integer.parseInt(trimStorage[0]));
				findMin(Double.parseDouble(trimStorage[1]),
						Integer.parseInt(trimStorage[0]));

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

	/**
	 * Populates the priority queue to sort the dates.
	 * 
	 * 
	 */
	public static void populatePQ() {
		Object[] tempArray = rainfall.keySet().toArray(); // Puts the keys from
		// the hashmap into
		// a temporary
		// array.
		for (int i = 0; i < rainfall.keySet().size(); i++) {
			pq.add(tempArray[i]);
		}
	}

	/**
	 * Populates the date arraylist with sorted dates from priority queue (Early
	 * to present).
	 * 
	 * 
	 */
	public static void populateDA() {
		int pqSize = pq.size();
		for (int i = 0; i < pqSize; i++) {
			dateArray.add((Integer) pq.remove());
		}
	}

	/**
	 * Returns the amount of rainfall for every month.
	 * 
	 * 
	 */
	public static void returnRainfallMonthly() {
		System.out.println("--Outputting the rainfall for all months.--");
		System.out.printf("%-40s║%s\n", "Data", "Graph of data");
		for (int j = 0; j < dateArray.size(); j++) {
			parseRainfall(rainfall.get(dateArray.get(j)));
			System.out.printf("%s had %.2f inches of rain.\t║%s\n",
					parseMonth(dateArray.get(j)),
					rainfall.get(dateArray.get(j)),
					graphRainfall());
		}
		printGraphLegend();
	}

	/**
	 * Gets input from user of the filename and how they want the output.
	 * 
	 * 
	 */
	public static void input() {
		fileName = JOptionPane.showInputDialog(null,
				"Please enter filename:", "./src/Rainfall.csv");
		menu = JOptionPane.showInputDialog(null,
				"Choose output:", "Choose wisely",
				JOptionPane.QUESTION_MESSAGE, null, menuOptions, null);
	}

	/**
	 * Takes the chosen output method and passes it to the associated method.
	 * 
	 * 
	 */
	public static void chosenOutput() {
		if (menu.equals(menuOptions[0])) { // Return output for every month.
			returnRainfallMonthly();
		} else if (menu.equals(menuOptions[1])) { // Return total rainfall for
			// all months combined.
			System.out.printf("The total rainfall was %.2f inches.",
					totalMonthlyRainfall);
		} else if (menu.equals(menuOptions[2])) { // Return average monthly
			// rainfall.
			System.out.printf(
					"The average rainfall was %.2f inches over %d months.\n",
					averageRainfall(), dateArray.size());
		} else if (menu.equals(menuOptions[3])) { // Return month with the most
			// rain.
			mostRain();
		} else if (menu.equals(menuOptions[4])) { // Return month with the least
			// rain.
			leastRain();
		} else if (menu.equals(menuOptions[5])) { // Return all above options.
			returnRainfallMonthly();
			System.out.println(
					"--Outputting the total rainfall for all months combined.--");
			System.out.printf("The total rainfall was %.2f inches.\n",
					totalMonthlyRainfall);
			System.out.println(
					"--Outputting the average rainfall for all months combined.--");
			System.out.printf(
					"The average rainfall was %.2f inches over %d months.\n",
					averageRainfall(), dateArray.size());
			System.out.println("--Outputting the month with the most rain.--");
			mostRain();
			System.out.println("--Outputting the month with the least rain.--");
			leastRain();
		}
	}

	/**
	 * Parses the month digits YYYYMM to the text version of the month.
	 * 
	 * @param date
	 *            from multiple sources
	 * @return outputDate (String of Month YYYY)
	 */
	public static String parseMonth(Integer date) {
		String month = "";
		String outputDate = "";
		char[] dateNumArray = String.valueOf(date).toCharArray();
		int monthNum = Integer.parseInt(dateNumArray[4] + "" + dateNumArray[5]);
		month = months[monthNum - 1];
		outputDate = String.format("%s %s", month,
				(dateNumArray[0] + "" + dateNumArray[1] + ""
						+ dateNumArray[2] + "" + dateNumArray[3]));
		return outputDate;
	}

	/**
	 * Finds the max rainfall and stores it and the date.
	 * 
	 * @param rain
	 *            from importFile tempArray
	 * @param date
	 *            from importFile tempArray
	 */
	public static void findMax(double rain, Integer date) {
		if (rain > monthMaxRainfall) {
			monthMaxRainfall = rain;
			monthMax = date;
		}
	}

	/**
	 * Finds the min rainfall and stores it and the date.
	 * 
	 * @param rain
	 *            from importFile tempArray
	 * @param date
	 *            from importFile tempArray
	 */
	public static void findMin(double rain, Integer date) {
		if (rain < monthMinRainfall) {
			monthMinRainfall = rain;
			monthMin = date;
		}
	}

	/**
	 * Outputs the month with the least rainfall. Includes an if statement for
	 * no rain.
	 * 
	 * 
	 */
	public static void leastRain() {
		if (monthMinRainfall == 0.0) { // rain.
			System.out.printf("%s had no measureable rainfall.\n",
					parseMonth(monthMin));
		} else
			System.out.printf("%s had the least rainfall at %.2f inches.\n",
					parseMonth(monthMin), monthMinRainfall);
	}

	/**
	 * Outputs the month with the most rainfall.
	 * 
	 * 
	 */
	public static void mostRain() {
		System.out.printf("%s had the most rainfall at %.2f inches.\n",
				parseMonth(monthMax), monthMaxRainfall);
	}

	/**
	 * Calculates the total rainfall.
	 * 
	 * 
	 */
	public static void totalRainfall() {
		for (int t = 0; t < dateArray.size(); t++) {
			totalMonthlyRainfall = totalMonthlyRainfall
					+ rainfall.get(dateArray.get(t));
		}
	}

	/**
	 * Uses the total and dateArray to calculate the average rainfall.
	 * 
	 * @return Average Monthly Rainfall
	 */
	public static double averageRainfall() {
		double averageMonthlyRainfall = totalMonthlyRainfall / dateArray.size();
		return averageMonthlyRainfall;
	}

	/**
	 * Parses rainfall and fills graphValues
	 * 
	 */
	public static void parseRainfall(double rain) {
		emptyGraphValues();
		double rain100 = rain * 100;
		int rainChanged = (int) rain100;
		while (rainChanged >= 500) {
			double d = rainChanged / 500;
			graphValues.put(500, (int) Math.floor(d));
			rainChanged = rainChanged - (500 * graphValues.get(500));
		}
		while (rainChanged >= 100) {
			double d = rainChanged / 100;
			graphValues.put(100, (int) Math.floor(d));
			rainChanged = rainChanged - (100 * graphValues.get(100));
		}
		while (rainChanged > 50) {
			double d = rainChanged / 50;
			graphValues.put(50, (int) Math.floor(d));
			rainChanged -= (50 * graphValues.get(50));
		}
		while (rainChanged >= 25) {
			double d = rainChanged / 25;
			graphValues.put(25, (int) Math.floor(d));
			rainChanged -= (25 * graphValues.get(25));
		}
		while (rainChanged >= 10) {
			double d = rainChanged / 10;
			graphValues.put(10, (int) Math.floor(d));
			rainChanged -= (10 * graphValues.get(10));
		}
		while (rainChanged >= 5) {
			double d = rainChanged / 5;
			graphValues.put(5, (int) Math.floor(d));
			rainChanged -= (5 * graphValues.get(5));
		}
		while (rainChanged >= 1) {
			graphValues.put(1, rainChanged);
			rainChanged -= rainChanged;
		}

	}

	public static void emptyGraphValues() {
		Object[] keys = graphValues.keySet().toArray();
		for (int gV = 0; gV < graphValues.size(); gV++) {
			graphValues.put((int) keys[gV], 0);
		}
	}

	/**
	 * Puts the keys from graphValues in order to display in descending order.
	 */
	public static void orderedGraphKeys() {
		PriorityQueue keysOrdered = new PriorityQueue();
		Stack<Integer> reverseKeys = new Stack<Integer>();
		for (int kO = 0; kO < keys.length; kO++) {
			keysOrdered.add(keys[kO]);
		}
		for (int kR = 0; kR < keys.length; kR++) {
			reverseKeys.push((int) keysOrdered.remove());
		}
		for (int kT = 0; kT < keys.length; kT++) {
			keys[kT] = reverseKeys.pop();
		}
	}

	/**
	 * Graphs the output of the rainfall per month. *
	 * 
	 * @param args
	 * @return graph
	 */
	public static String graphRainfall() {
		String builtGraph = "";
		orderedGraphKeys();
		for (int s = 0; s < keys.length; s++) {
			for (int t = 0; t < graphValues.get(keys[s]); t++) {
				builtGraph += rainfallGraphFill.get(keys[s]);
			}
		}

		return builtGraph;
	}

	public static void printGraphLegend() {
		int i = 0;
		int j = 0;
		int max = 0;
		String test = String.format("|  %d = %s  |\n", keys[i],
				rainfallGraphFill.get(keys[i]));
		if (test.length() > max) {
			max = test.length();
		}
		while (j < max) {
			if (j == 0)
				System.out.print("╔");
			else if (j != max - 1)
				System.out.print("═");
			else if (j == max - 1)
				System.out.print("╗");
			j++;
		}
		String title = "Graph Legend";
		for (title.length(); title.length() < max;) {
			title = "║" + title + "║";
		}
		System.out.println("\n" + title);
		j = 0;
		while (i < keys.length) {
			System.out.printf("║  %2s = %3d  ║\n",
					rainfallGraphFill.get(keys[i]),
					keys[i]);
			i++;
		}
		while (j < max) {
			if (j == 0)
				System.out.print("╚");
			else if (j != max - 1)
				System.out.print("═");
			else if (j == max - 1)
				System.out.print("╝");
			j++;
		}
		System.out.println();
	}

	public static void main(String[] args) {
		input();
		importFile(fileName);
		populatePQ();
		populateDA();
		totalRainfall();
		chosenOutput();
	}

}

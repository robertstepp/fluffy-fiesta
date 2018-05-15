
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
	static String[] menuOptions = { "Output every month", "Total rainfall",
			"Average monthly rainfall", "Month with the most rain",
			"Month with the least rain", "Output all previous choices" };
	static String[] months = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };
	static String fileName = "";
	static Object menu = null;
	static int monthMax = 0;
	static int monthMin = 0;
	static double monthMaxRainfall = 0.0;
	static double monthMinRainfall = 100.0;
	static double totalMonthlyRainfall = 0.0;

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
				findMax(Double.parseDouble(tempStorage[1]),
						Integer.parseInt(tempStorage[0]));
				findMin(Double.parseDouble(tempStorage[1]),
						Integer.parseInt(tempStorage[0]));

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
			System.out.print(parseMonth(dateArray.get(j)) + "\t");
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
			System.out.println("--Outputting the rainfall for all months.--");
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

	static String parseMonth(Integer date) { // Parses the month digits YYYYMM
												// to the text version of the
												// month.
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

	static void findMax(double rain, Integer date) {
		if (rain > monthMaxRainfall) {
			monthMaxRainfall = rain;
			monthMax = date;
		}
	}

	static void findMin(double rain, Integer date) {
		if (rain < monthMinRainfall) {
			monthMinRainfall = rain;
			monthMin = date;
		}
	}

	static void leastRain() {
		if (monthMinRainfall == 0.0) { // rain.
			System.out.printf("%s had no measureable rainfall.\n",
					parseMonth(monthMin));
		} else
			System.out.printf("%s had the least rainfall at %.2f inches.\n",
					parseMonth(monthMin), monthMinRainfall);
	}

	static void mostRain() {
		System.out.printf("%s had the most rainfall at %.2f inches.\n",
				parseMonth(monthMax), monthMaxRainfall);
	}

	static void totalRainfall() {
		for (int t = 0; t < dateArray.size(); t++) {
			totalMonthlyRainfall = totalMonthlyRainfall
					+ rainfall.get(dateArray.get(t));
		}
	}

	static double averageRainfall() {
		double averageMonthlyRainfall = totalMonthlyRainfall / dateArray.size();
		return averageMonthlyRainfall;
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


// Java Program by Robert Stepp and Freddie Krueger 05/20/2018
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Main {
	private static HashMap<Integer, Double> rainfall = new HashMap<Integer, Double>();
	private static PriorityQueue pq = new PriorityQueue();

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

	public static void main(String[] args) {
		String filename = "./src/Rainfall.csv";
		importFile(filename);

	}

}

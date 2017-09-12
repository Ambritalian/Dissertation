import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class TransTable {
	private BufferedReader br = null;
	private int[] commas = new int[3];	// Set to no. of commas in each line
	private String curState;
	private String[] finalState; 
	private int registers;
	private boolean error = false;
	ArrayList<Quad<String, String, String, Integer>> tTable = new ArrayList<Quad<String, String, String, Integer>>();
	public ArrayList<Quad<String, String, String, Integer>> initTransTable(String Specification_File_Name) throws IOException {
		// Find Specification file
		File f = new File(Specification_File_Name); 
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e1) {
			System.out.println("Specification file not found");
		}
		String line;
		try {
			line = br.readLine();
		} catch (IOException e) {
			line = null;
			e.printStackTrace();
		}
		int lineNo = 0;
		// for each line in specification.txt
		while (line != null && !error) {
			int size = 0;
			// Find initial and final states
			if (lineNo==0) {
				// Find number of commas
				char[] in = line.toCharArray();
				for (int i=0;i<in.length;i++) {
					if (in[i] == ',') size++;
				}
				// No commas, line full of blank space or line specified incorrectly
				if (size == 0 || line.replaceAll("\\s+","").length() < 3) {
					System.out.println("Initial state and final state(s) must be specified");
					error = true;
					break;
				}
				// First state found is always start state
				int[] fCommas = new int[size];
				finalState = new String[size];
				fCommas[0] = line.indexOf(',');
				curState = line.substring(0, fCommas[0]);
				// get the indexes of all the states given
				for (int i = 1; i < size; i++) {
					fCommas[i] = line.substring(fCommas[i-1]+1).indexOf(',')+fCommas[i-1]+1;
				}
				// remove white space and grab the actual state definition
				for (int i=0;i<size-1;i++) {
					finalState[i] = removePrecedingWhiteSpace(line.substring(fCommas[i]+1, fCommas[i+1]));
				}
				// final state is always after the last comma (with white space removed) and EoL
				finalState[size-1] = removePrecedingWhiteSpace(line.substring(fCommas[size-1]+1, line.length()));
				
			// # of registers is on second line	
			} else if (lineNo == 1) {
				// Remove white space, check that number specified is valid
				if (line.replaceAll("\\s+","").length() == 0 || Integer.parseInt(line.substring(0, line.length())) <= 0) { 
					System.out.println("Number of registers must be specified with a positive, non-zero integer");
					error = true;
					break;
				}
				registers = Integer.parseInt(line.substring(0, line.length()));
			// for each subsequent transition, find indexes of all commas
			} else {
				// Minimum length of correctly defined line is of length 10
				if (line.replaceAll("\\s+","").length() < 10) {
					System.out.println("Please specify a quad with the following format: source, sink, true/false, register");
					error = true;
					break;
				}
				commas[0] = line.indexOf(',');
				for (int i = 1; i < commas.length; i++) {
					commas[i] = line.substring(commas[i-1]+1).indexOf(',')+commas[i-1]+1;
				}
				// The structure is already defined, so store each value in their appropriate place
				String left = line.substring(0, commas[0]);
				String midLeft =  removePrecedingWhiteSpace(line.substring(commas[0]+1, commas[1]));
				String midRight = removePrecedingWhiteSpace(line.substring(commas[1]+1, commas[2]));
				// Freshness must be true or false
				if (!midRight.equals("false") && !midRight.equals("true")) {
					System.out.println("Freshness must be either true or false");
					error = true;
					break;
				}
				// Ensure # of register is valid
				Integer right =  Integer.parseInt(removePrecedingWhiteSpace(line.substring(commas[2]+1, line.length())));
				if (right < 0 || right >= registers) {
					System.out.println("Register in transition " + (lineNo-1) + " is invalid");
					error = true;
					break;
				}
				// Create quad and add to list of transitions
				Quad<String, String, String, Integer> quad = new Quad<String, String, String, Integer>(left, midLeft, midRight, right);
				tTable.add(quad);
			}
			lineNo++;
			line = br.readLine();
		}
		return tTable;
	}
	// Function to remove preceding white space from strings
	private static String removePrecedingWhiteSpace(String s) {
		if (s.length() == 0) return s;
		if (s.charAt(0) == ' ') return removePrecedingWhiteSpace(s.substring(1));
		else return s;
	}
	public String getCurState() {
		return curState;
	}
	public boolean getFinalState(String state) {
		for(int i=0; i<finalState.length;i++) {
			if(finalState[i].equals(state)) return true;
		}
		return false;
	}
	public int getRegisters() {
		return registers;
	}
	// Return appropriate transitions
	public ArrayList<Quad<String, String, String, Integer>> getTransitions(Pair<String, String> key){
		ArrayList<Quad<String, String, String, Integer>> trans = new ArrayList<Quad<String, String, String, Integer>>();
		for (int i=0; i<tTable.size(); i++) {
			if(tTable.get(i).getLeft().equals(key.getLeft()) & tTable.get(i).getMidRight().equals(key.getRight())) trans.add(tTable.get(i));
		}
		return trans;
	}
	// Find appropriate transitions
	public boolean containsKey(Pair<String, String> key){
		for (int i=0; i<tTable.size(); i++) {
			if(tTable.get(i).getLeft().equals(key.getLeft()) & tTable.get(i).getMidRight().equals(key.getRight())) return true;
		}
		return false;
	}
}
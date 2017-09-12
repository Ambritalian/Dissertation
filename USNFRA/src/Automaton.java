import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class Automaton {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		TransTable transTable = new TransTable();
		transTable.initTransTable("specification.txt");
		Register register = new Register(transTable.getRegisters());
		String curState = transTable.getCurState();
		int i = 0;
		int countFresh = 0; 
		int countOld = 0;
		ArrayList<String> xmlIn = null;
		// parse XML input file
		xmlParser parser = new xmlParser();
		try {
			xmlIn = parser.parse("books.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Whilst there is input to be processed
		// Get input value
		// Check for freshness
		// Create key from pair
		// Check whether transTable contains Key
		while(i<xmlIn.size()) {
			String aChar = xmlIn.get(i);
			boolean aKey = register.isFresh(aChar);
			String response = "";
			if (aKey) response = "true";
			else response = "false";
			Pair<String,String> key = new Pair<String, String>(curState, response);
			// Get transitions
			if (transTable.containsKey(key)) {	
				ArrayList<Quad<String, String, String, Integer>> o = transTable.getTransitions(key);
				// Check if input value is fresh
				if (register.isFresh(aChar)) {
					// Count number of fresh transitions, impossible to be zero as if there aren't any transitions for a certain key,
					// the key will not exist in the transition table
					for(int j=0; j<o.size(); j++) {
						if (o.get(j).getMidRight().equals("true")) {
							countFresh++;
						}
					}
					// If there is only one, execute it
					if (countFresh == 1) {
						System.out.println("Stored " + aChar + " in register " + o.get(0).getRight() + "; "+ o.get(0).getLeft() + "----->" + o.get(0).getMidLeft());
						register.add(aChar, o.get(0).getRight());
						curState = o.get(0).getMidLeft();
					} 
					// Otherwise, get the user to choose which transition to execute
					else { 
						System.out.println("Choose between the following " + countFresh + " transitions:");
						for (int k = 0; k<countFresh; k++) {
							System.out.println((k) + ": Store " + aChar + " in register " + o.get(k).getRight() + "; "+ o.get(k).getLeft() + "----->" + o.get(k).getMidLeft());
						}
						System.out.println("By entering the corresponding number on the keyboard");
						int choice = sc.nextInt();
						while (choice < 0 || choice >= countFresh) {
							System.out.println("Please enter integer corresponding to the transitions available");
							choice = sc.nextInt();
						}
						// Write value to register specified in transition
						register.add(aChar, o.get(choice).getRight());
						curState = o.get(choice).getMidLeft();
					}
				// Else get number of stale transitions, impossible to be zero as if there aren't any transitions for a certain key,
				// the key will not exist in the transition table
				} else { 
					for(int j=0; j<o.size(); j++) {
						if (o.get(j).getMidRight().equals("false")) {
							countOld++;
						}
					}
					// If there is only one, execute it
					if (countOld == 1) {
						System.out.println("Stored " + aChar + " in register " + o.get(0).getRight() + "; "+ o.get(0).getLeft() + "----->" + o.get(0).getMidLeft());
						register.add(aChar, o.get(0).getRight());
						curState = o.get(0).getMidLeft();
					}
					// Otherwise, get the user to choose which transition to execute
					else { 
						System.out.println("Choose between the following " + countOld + " transitions:");
						for (int k = 0; k<o.size(); k++) {
							System.out.println((k) + ": Store " + aChar + " in register " + o.get(k).getRight() + "; "+ o.get(k).getLeft() + "----->" + o.get(k).getMidLeft());
						}
						System.out.println("By entering the corresponding number on the keyboard");
						int choice = sc.nextInt();
						while (choice < 0 || choice >= countOld) {
							System.out.println("Please enter integer corresponding to the transitions available");
							choice = sc.nextInt();
						}
						// Write value to register specified in transition
						register.add(aChar, o.get(choice).getRight());
						curState = o.get(choice).getMidLeft();
					}
				}
			} else {
				System.out.println("Key not found in transition table");
				i = xmlIn.size();
				curState = transTable.getCurState();
				break;
			}
			// Reset counters
			countFresh = 0; 
			countOld = 0;
			i++;
		}
		// Accept if state (after processing entire input) is final state, reject otherwise
		if (transTable.getFinalState(curState)) System.out.println("Valid input");
		else System.out.println("Invalid input");
		sc.close();
	}
}
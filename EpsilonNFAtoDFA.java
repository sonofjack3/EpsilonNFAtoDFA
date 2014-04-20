/*	Main driver program allows user to choose a file containing an eNFA in Grail format.
 *	Calculates the epsilon closure for all states in the NFA and calculates the
 *	equivalent DFA and outputs results to files.
 	By Evan Jackson */

import java.util.*;
import java.io.*;
import javax.swing.*;

public class EpsilonNFAtoDFA
{

	public static void main(String[] args) throws FileNotFoundException
	{
		JFileChooser chooser = new JFileChooser();
		int status = chooser.showOpenDialog(null);
		if (status != JFileChooser.APPROVE_OPTION)
			System.out.println("No file chosen.");
		else
		{
			File file = chooser.getSelectedFile();
			boolean verbose = false; //whether to display details of the calculations
			boolean enumerate = false; //whether to enumerate the DFA states or leave them as sets of states
			System.out.println("For the NFA contained in the file " + file.getName() + ":");
			EpsilonNFA automaton = new EpsilonNFA(file);
			automaton.full_epsilon_closure(verbose); //calculate and display all epsilon closures and display details
			System.out.println();
			automaton.calculateAndDisplayDFA(verbose, enumerate); //calculate and display DFA and display details
		}
	}
}
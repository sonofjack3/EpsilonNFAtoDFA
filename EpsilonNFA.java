/*	Represents a epsilon-nondeterministic finite automaton
	By Evan Jackson */

import java.util.*;
import java.io.*;

public class EpsilonNFA
{
	private Scanner fileScan; //scanner for reading in the NFA from a file
	private ArrayList<State> states; //set of states in the NFA
	private ArrayList<String> language; //language recognized by the NFA

	//Read in NFA from a file and stores it
	public EpsilonNFA(File file) throws FileNotFoundException
	{
		states = new ArrayList<State>();
		language = new ArrayList<String>();
		fileScan = new Scanner(file);
		String[] temp;
		String current;
		State newState;
		boolean containsFirstState, containsSecondState;
		int currIndex1 = 0, currIndex2 = 0;

		while (fileScan.hasNextLine())
		{
			containsFirstState = false;
			containsSecondState = false;
			currIndex1 = 0;
			currIndex2 = 0;
			current = fileScan.nextLine();
			temp = current.split(" ", 3); //parse line based on spaces
			for (int i = 0; i < states.size() && !containsFirstState; i++)
			{
				if (states.get(i).getName().equals(temp[0])) //the NFA already contains this state
				{
					containsFirstState = true;
					currIndex1 = i;
				}
			}
			if (temp[0].equals("(START)") && temp[1].equals("|-")) //store initial state (may be more than one)
				states.add(new State(temp[2], true, false));
			else if (!containsFirstState && temp[1].equals("-|") && temp[2].equals("(FINAL)")) //final state has not yet been encountered
				states.add(new State(temp[0], false, true));
			else if (containsFirstState && temp[1].equals("-|") && temp[2].equals("(FINAL)")) //final state has already been encountered
				states.get(currIndex1).setFinal();
			else if (!containsFirstState) //a normal state that has not yet been encountered
			{
				newState = new State(temp[0], false, false);
				newState.addTransition(temp[1]);

				//Add to language
				if (!language.contains(temp[1]) && !temp[1].equals("?"))
					language.add(temp[1]);

				for (int i = 0; i < states.size() && !containsSecondState; i++)
				{
					if (states.get(i).getName().equals(temp[2])) //the transition state is already in the NFA
					{
						containsSecondState = true;
						currIndex2 = i;
						break;
					}
				}
				if (containsSecondState) //add the transition state
					newState.addState(states.get(currIndex2));
				else //add the new state and its transition state
				{
					newState.addState(new State(temp[2], false, false));
					states.add(newState);
				}
			}
			else if (containsFirstState) //a normal state that has been encountered
			{
				states.get(currIndex1).addTransition(temp[1]);

				//Add to language
				if (!language.contains(temp[1]) && !temp[1].equals("?"))
					language.add(temp[1]);

				for (int i = 0; i < states.size() && !containsSecondState; i++)
				{
					if (states.get(i).getName().equals(temp[2])) //the transition state is already in the NFA
					{
						containsSecondState = true;
						currIndex2 = i;
						break;
					}
				}
				if (containsSecondState) //add the transition state
					states.get(currIndex1).addState(states.get(currIndex2));
				else //add the new state and its transition state
				{
					newState = new State(temp[2], false, false);
					states.get(currIndex1).addState(newState);
					states.add(newState);
				}
			}
		}
	}

	//Returns the set of initial states in the NFA
	public ArrayList<State> getInitial()
	{
		ArrayList<State> initialStates = new ArrayList<State>();
		for (int i = 0; i < states.size(); i++)
			if (states.get(i).isInitial())
				initialStates.add(states.get(i));
		return initialStates;
	}

	//Calculates and prints epsilon closure for all states in the NFA
	public void full_epsilon_closure(boolean verbose)
	{
		ArrayList<State> set = new ArrayList<State>();
		ArrayList<State> ep_set;
		System.out.println("The epsilon closure of each state:");
		for (int i = 0; i < states.size(); i++)
		{
			ep_set = epsilon_closure(states.get(i), set, verbose);
			System.out.print("Epsilon closure of " + states.get(i).getName() + ": ");
			for (int j = 0; j < ep_set.size(); j++)
				System.out.print(ep_set.get(j).getName() + " ");
			System.out.println();
			ep_set.clear();
			set.clear();
		}
	}

	//Recursive method calculates the epsilon closure of state
	private ArrayList<State> epsilon_closure(State state, ArrayList<State> set, boolean verbose)
	{
		if (verbose) System.out.println("Now calculating epsilon closure for " + state.getName());
		if (!set.contains(state)) //add state to the eclosure if it does not already contain it
			set.add(state);
		for (int i = 0; i < state.getToStates().size(); i++)
		{
			if (state.getTransitions().get(i).contains("?"))
			{
				if (!set.contains(state.getToStates().get(i)))
					set.addAll(epsilon_closure(state.getToStates().get(i), set, verbose));
			}
		}
		set = clearArrayList(set); //remove duplicates
		return set;
	}

	//Finds the next state that is reached from state by transition, including epsilon transitions
	private ArrayList<State> findNextStates(State state, String transition, ArrayList<State> set, ArrayList<State> set2, boolean verbose)
	{
		if (verbose) System.out.println("Now calculating next states for " + state.getName() + " with transition " + transition);
		if (set2.contains(state)) //prevent endless loops (once a state is re-encountered, break the loop)
			return set;
		set2.add(state);
		for (int i = 0; i < state.getToStates().size(); i++)
		{
			if (state.getTransitions().get(i).equals("?") && !set.contains(state))
				set.addAll(findNextStates(state.getToStates().get(i), transition, set, set2, verbose));
			else if (state.getTransitions().get(i).equals(transition) && !set.contains(state.getToStates().get(i)))
				set.add(state.getToStates().get(i));
		}
		set = clearArrayList(set); //remove duplicates

		//Add members of the epsilon closure to the set
		int size = set.size();
		ArrayList<State> temp = new ArrayList<State>();
		for (int j = 0; j < size; j++)
			set.addAll(epsilon_closure(set.get(j), temp, verbose));
		set = clearArrayList(set); //remove duplicates

		return set;
	}

	//Recursive method calculates the DFA given a StateSet and a DFA
	private DFA calculateDFAstate(StateSet state, DFA dfa, boolean verbose)
	{
		if (verbose) System.out.println("Calculating DFA using state: {" + state.getName() + "}");

		ArrayList<State> set, set2, set3, set4;
		StateSet newState;

		set = new ArrayList<State>();
		set2 = new ArrayList<State>();
		set3 = new ArrayList<State>();
		set4 = new ArrayList<State>();
		for (int i = 0; i < language.size(); i++)
		{
			set4.clear();
			for (int j = 0; j < state.size(); j++)
			{
				set2.clear();
				set3.clear();
				set4.addAll(findNextStates(state.getStates().get(j), language.get(i), set2, set3, verbose));
			}
			set4 = clearArrayList(set4);
			newState = new StateSet();
			newState.addToSet(set4);

			/*Determine if newState is a final state*/
			for (int k = 0; k < newState.size(); k++)
			{
				//newState is final if the states within it was final in the e-NFA
				if (newState.getStates().get(k).isFinal())
				{
					newState.setFinal();
					break;
				}
			}

			/*Add state to the DFA (if it does not contain it already) and calculate the rest of the DFA*/
			if (dfa.find(newState) == null) //newState is not in the DFA
			{
				state.addTransition(language.get(i));
				state.addToState(newState);
				dfa.addState(newState);
				dfa = calculateDFAstate(newState, dfa, verbose);
			}
			else //newState is already in the DFA
			{
				state.addTransition(language.get(i));
				state.addToState(dfa.find(newState));
			}
		}
		return dfa;
	}

	//Returns a description of this eNFA (each State describes itself)
	public String toString()
	{
		String str = "";
		for (int i = 0; i < states.size(); i++)
			str += states.get(i);
		return str;
	}

	//Calculates the corresponding DFA
	public void calculateAndDisplayDFA(boolean verbose, boolean enumer)
	{
		if (verbose) System.out.println("Calculating the DFA...");
		DFA dfa = new DFA();

		if (verbose) System.out.println("Calculating the initial states...");
		//First calculate the initial state(s) (epsilon closure of initial state(s) from the NFA)
		ArrayList<State> set;
		ArrayList<State> initialState;
		StateSet init;
		ArrayList<StateSet> initialStates = new ArrayList<StateSet>(); //track the new initial states in the DFA
		for (int i = 0; i < getInitial().size(); i++)
		{
			initialState = new ArrayList<State>();
			set = new ArrayList<State>();
			initialState = epsilon_closure(getInitial().get(i), set, verbose);
			init = new StateSet();
			init.addToSet(initialState);
			init.setInitial();
			if (dfa.find(init) == null) //if DFA does not contain init
			{
				initialStates.add(init);
				dfa.addState(init);
			}
			//else the DFA contains init and no new state is needed
		}

		//Calculate the rest of the DFA
		for (int j = 0; j < initialStates.size(); j++)
			dfa = calculateDFAstate(initialStates.get(j), dfa, verbose);

		//If enum is true, set the DFA states to enumerated
		if (enumer)
			dfa.enumStates();

		//Display DFA
		System.out.println("The equivalent DFA is: \n" + dfa);
	}

	//Removes duplicate elements from ArrayLists
	private ArrayList<State> clearArrayList(ArrayList<State> set)
	{
		Set setItems = new LinkedHashSet(set);
		set.clear();
		set.addAll(setItems);
		return set;
	}
}
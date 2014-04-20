/*	Represents a deterministic finite automaton
	By Evan Jackson */

import java.util.*;

public class DFA
{
	private ArrayList<StateSet> states; //set of states in the DFA
	
	//Constructor initializes the set of states in the DFA
	public DFA()
	{
		states = new ArrayList<StateSet>();
	}

	//Returns the set of states in the DFA
	public ArrayList<StateSet> getStates()
	{
		return states;
	}

	//Adds a new state (StateSet) to the DFA
	public void addState(StateSet state)
	{
		states.add(state);
	}

	//Returns the number of states in the DFA
	public int size()
	{
		return states.size();
	}

	//Finds and returns the state (StateSet) in the DFA matching the passed StateSet (returns null if not found)
	public StateSet find(StateSet state)
	{
		for (int i = 0; i < states.size(); i++)
			if (states.get(i).equals(state))
				return states.get(i);
		return null;
	}

	public void enumStates()
	{
		for (int i = 0; i < states.size(); i++)
			states.get(i).setNum(i);
	}

	//Returns a description of the DFA (each state describes itself)
	public String toString()
	{
		String str = "";
		for (int i = 0; i < states.size(); i++)
			str += states.get(i);
		return str;
	}
}
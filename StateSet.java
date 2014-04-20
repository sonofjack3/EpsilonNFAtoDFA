/*	Represents a state in a DFA which has been converted from an e-NFA; a StateSet is a set of states from the NFA
	By Evan Jackson */

import java.util.*;

public class StateSet
{
	private int num; //state number in the DFA
	private boolean enumer; //boolean for enumerating this state
	private ArrayList<State> setStates = new ArrayList<State>(); //states from the NFA contained in this StateSet
	private ArrayList<StateSet> toStates = new ArrayList<StateSet>(); //set of transition states for this StateSet (ie: other StateSets)
	private ArrayList<String> transitions = new ArrayList<String>(); //set of transitions for this StateSet
	private boolean isInitial, isFinal; //booleans for determining if this StateSet is initial or final

	//Constructor sets this StateSet's initial and final status both to false
	public StateSet()
	{
		isInitial = false;
		isFinal = false;
	}

	//Returns the number of regular states contained in this StateSet
	public int size()
	{
		return setStates.size();
	}

	public void setNum(int num)
	{
		enumer = true;
		this.num = num;
	}

	//Adds a new State to this StateSet
	public void addToSet(State newState)
	{
		setStates.add(newState);
	}

	//Overloaded method for adding an ArrayList of States to this StateSet
	public void addToSet(ArrayList<State> newState)
	{
		setStates.addAll(newState);
	}

	//Adds a new transition state (StateSet) to this StateSet's set of transition states
	public void addToState(StateSet toState)
	{
		toStates.add(toState);
	}

	//Adds a new transition to this StateSet's transitions
	public void addTransition(String newTransition)
	{
		transitions.add(newTransition);
	}

	//Sets this StateSet as a final state in the automaton
	public void setFinal()
	{
		isFinal = true;
	}

	//Returns true if this StateSet is a final state
	public boolean isFinal()
	{
		return isFinal;
	}

	//Returns the set of transitions for this StateSet
	public ArrayList<String> getTransitions()
	{
		return transitions;
	}

	//Returns the set of transition states (StateSets) for this StateSet
	public ArrayList<StateSet> getToStates()
	{
		return toStates;
	}

	//Returns the set of regular States contained in this StateSet
	public ArrayList<State> getStates()
	{
		return setStates;
	}

	//Returns true if this StateSet is an initial state
	public boolean isInitial()
	{
		return isInitial;
	}

	//Sets this StateSet as an initial state
	public void setInitial()
	{
		isInitial = true;
	}

	//Returns a the name of this StateSet (names of all States contained)
	public String getName()
	{
		String name = "";
		if (!setStates.isEmpty())
		{
			for (int i = 0; i < setStates.size(); i++)
			{
				if (i == setStates.size() - 1) //trim last space
					name += setStates.get(i).getName();
				else
					name += setStates.get(i).getName() + " ";
			}
		}
		else //this is the empty set
		{
			return "Empty_set";
		}
		return name;
	}

	//Returns true if the passed StateSet contains the same states as this StateSet
	public boolean equals(StateSet state2)
	{
		if (state2.getStates().size() == setStates.size())
		{
			for (int i = 0; i < state2.getStates().size(); i++)
			{
				if (!setStates.contains(state2.getStates().get(i)))
					return false;
			}
			return true;
		}
		return false;
	}

	public int getNum()
	{
		return num;
	}


	//Returns a description of this StateSet (name, transitions, transition states)
	public String toString()
	{
		String str = "";

		if (!enumer) //print actual state set
		{
			if (!transitions.isEmpty() && !toStates.isEmpty())
			{
				if (isInitial())
					str += ("(START) |- {" + getName() + "} \n");
				if (isFinal())
					str += ("{" + getName() + "} -| (FINAL)\n");
				for (int i = 0; i < transitions.size(); i++)
				{
					str += ("{" + getName() + "} ");
					str += (transitions.get(i) + " {" + toStates.get(i).getName() + "}\n");
				}
			}	
			else
			{
				str += "{Empty_set}\n";
			}
			str.replaceAll(" ", "");
		}
		else //print using the state's number in the DFA
		{
			if (!transitions.isEmpty() && !toStates.isEmpty())
			{
				if (isInitial())
					str += ("(START) |- " + num + "\n");
				if (isFinal())
					str += (num + " -| (FINAL)\n");
				for (int i = 0; i < transitions.size(); i++)
				{
					str += (num + " ");
					str += (transitions.get(i) + " " + toStates.get(i).getNum() + "\n");
				}
			}
		}
		return str;
	}
}
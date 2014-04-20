/*	Represents a finite automaton state
	By Evan Jackson */

import java.util.*;

public class State
{
	private String name; //name of this state
	private ArrayList<State> toStates = new ArrayList<State>(); //set of transition states
	private ArrayList<String> transitions = new ArrayList<String>(); //set of transitions
	private boolean isInitial, isFinal; //booleans for determining if this state is initial or final

	//Constructor initializes state name and initial/final status
	public State(String name, boolean isInitial, boolean isFinal)
	{
		this.name = name;
		this.isInitial = isInitial;
		this.isFinal = isFinal;
	}

	//Adds a new state to this state's set of transition states
	public void addState(State newState)
	{
		toStates.add(newState);
	}

	//Adds a new transition to this state's set of transitions
	public void addTransition(String newTransition)
	{
		transitions.add(newTransition);
	}

	//Returns this state's name
	public String getName()
	{
		return name;
	}

	//Sets this state as a final state
	public void setFinal()
	{
		isFinal = true;
	}

	//Returns true if this state is a final state
	public boolean isFinal()
	{
		return isFinal;
	}

	//Returns this state's set of transitions
	public ArrayList<String> getTransitions()
	{
		return transitions;
	}

	//Returns this state's set of transition states
	public ArrayList<State> getToStates()
	{
		return toStates;
	}

	//Returns true if this state is an initial state
	public boolean isInitial()
	{
		return isInitial;
	}

	//Returns a description of this state (name, transitions, transition states)
	public String toString()
	{
		String str = "";
		if (isInitial)
			str += "State " + name + " is an initial state.\n";
		if (isFinal)
			str += "State " + name + " is a final state.\n";
		str += ("State " + name + " has the following transitions\n");
		for (int i = 0; i < transitions.size(); i++)
			str += (transitions.get(i) + "\t" + toStates.get(i).getName() + "\n");
		return str;
	}
}
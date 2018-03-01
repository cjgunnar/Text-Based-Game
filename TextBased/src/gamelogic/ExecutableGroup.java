package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.SceneObject;

public class ExecutableGroup implements Executable
{
	/** List of executables in this group */
	ArrayList<Executable> executables;
	
	/** Only used for display. Can be NONE, PASS, or FAIL */
	int type;
	
	/** Not part of a special group */
	public static final int NONE = 0;
	/** Part of a Condition pass group */
	public static final int PASS = 1;
	/** Part of a condition fail group */
	public static final int FAIL = 2;
	
	//special restrictions that can be put on executables
	
	/** Stop executing when this many executables execute successfully */
	int max_executions;
	
	/** Default Constructor */
	public ExecutableGroup()
	{
		executables = new ArrayList<Executable>();
		max_executions = 0;
		type = NONE;
	}
	
	/** Constructor with type specification */
	public ExecutableGroup(int type)
	{
		executables = new ArrayList<Executable>();
		max_executions = 0;
		this.type = type;
	}
	
	public void setMaxExecutions(int max_executions)
	{
		this.max_executions = max_executions;
	}
	
	@Override
	public void run()
	{
		//run with max_executions limit
		if(max_executions != 0)
		{
			int successfulExecutions = 0;
			for(Executable exe: executables)
			{
				exe.run();
				if(exe.getSuccess())
					successfulExecutions++;
				
				if(successfulExecutions > max_executions)
					break;
			}
		}
		
		//run default
		else
		{
			for(Executable exe: executables)
			{
				exe.run();
			}
		}
	}

	@Override
	public void addExecutable(Executable executable)
	{
		executables.add(executable);
	}

	@Override
	public List<Executable> getExecutables()
	{
		return executables;
	}

	@Override
	public void setParentObject(SceneObject obj)
	{
		for(Executable exe: executables)
		{
			exe.setParentObject(obj);
		}
	}

	@Override
	public void setGame(Game game)
	{
		for(Executable exe : executables)
		{
			exe.setGame(game);
		}
	}

	@Override
	public boolean getSuccess()
	{
		//executable group can't fail
		return true;
	}
	
	@Override
	public String toString()
	{
		String type_template = "GROUP EXECUTABLES";
		if(type == PASS) {type_template= "PASS";}
		if(type == FAIL) {type_template= "FAIL";}
		
		String exes_template= ": " + executables.size() + " executables";
		String max_template = max_executions > 0 ? "" : ", max=" + max_executions;
		
		return type_template + ": " + exes_template + max_template;
	}

}

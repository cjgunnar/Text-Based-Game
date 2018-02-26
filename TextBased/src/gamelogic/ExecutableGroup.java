package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.SceneObject;

public class ExecutableGroup implements Executable
{
	ArrayList<Executable> executables;
	
	//special restrictions that can be put on executables
	
	/** Stop executing when this many executables execute successfully */
	int max_executions;
	
	public ExecutableGroup()
	{
		executables = new ArrayList<Executable>();
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

}

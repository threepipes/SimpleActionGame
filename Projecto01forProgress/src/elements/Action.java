package elements;

public abstract class Action {
	protected final int priority;
	protected ActiveElement parent;
	protected String name = null;
	protected boolean cancelable = true;
	
	public Action(int priority, ActiveElement parent) {
		this.priority = priority;
		this.parent =  parent;
		
		// put name by Action
	}
	
	public int getPriority(){
		return priority;
	}
	
	public int getIAct(){
		return priority*2+(parent.dx == -1 ? 1 : 0);
	}
	
	public String getName(){
		return name;
	}
	
	public boolean actionable(int priority){
		if(this.priority > priority || cancelable){
			return true;
		}
		return false;
	}
	
	public abstract void action();

}

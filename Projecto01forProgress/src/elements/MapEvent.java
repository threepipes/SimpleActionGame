package elements;


public class MapEvent extends Event{
	public final int toX;
	public final int toY;
	public final int toMap;

	public MapEvent(int x,int y, int toMap,int toX, int toY) {
		super(x,y);
		this.toX = toX;
		this.toY = toY;
		this.toMap = toMap;
	}
	

}

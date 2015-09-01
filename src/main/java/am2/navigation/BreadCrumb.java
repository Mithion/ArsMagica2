package am2.navigation;

import java.util.Comparator;

public class BreadCrumb implements Comparable<BreadCrumb>{
	
	public Point3D position;
    public BreadCrumb next;
    public int cost = Integer.MAX_VALUE;
    public boolean onClosedList = false;
    public boolean onOpenList = false;
	
    public BreadCrumb(Point3D position)
    {
        this.position = position;
    }
    
    public void unshift(){
    	this.position = this.position.Unshift();
    }
    
    public BreadCrumb(Point3D position, BreadCrumb parent)
    {
        this.position = position; 
        this.next = parent;
    }
    
    @Override
	public boolean equals(Object o) {
		if (o instanceof BreadCrumb) {
			BreadCrumb b = (BreadCrumb) o;
		    	if (this.position.equals(b.position)) 
		    		return true;
		}
		return false;
	}

	@Override
	public int compareTo(BreadCrumb o) {
		return this.cost < o.cost ? -1 : this.cost > o.cost ? 1 : 0;
	}
}

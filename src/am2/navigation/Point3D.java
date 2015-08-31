package am2.navigation;

public class Point3D {
	public int x;
	public int y;
	public int z;
	
	private int offsetX;
	private int offsetY;
	private int offsetZ;
	
	public Point3D Unshift(){
		return new Point3D(x + offsetX, y + offsetY, z + offsetZ);
	}
	
	public int shiftX(){
		offsetX = x;
		x = 0;
		return offsetX;
	}
	
	public void shiftX(int offset){
		offsetX = offset;
		x -= offset;
	}
	
	public int shiftY(){
		offsetY = y;
		y = 0;
		return offsetY;
	}
	
	public void shiftY(int offset){
		offsetY = offset;
		y -= offset;
	}
	
	public int shiftZ(){
		offsetZ = z;
		z = 0;
		return offsetZ;
	}	
	
	public void shiftZ(int offset){
		offsetZ = offset;
		z -= offset;
	}
	
	public Point3D(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.offsetX = this.offsetY = this.offsetZ = 0;
	}
	
	public Point3D setOffsets(int offsetX, int offsetY, int offsetZ){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		return this;
	}
	
	public double GetDistanceSq(Point3D point){
		int dx = this.x - point.x;
        int dy = this.y - point.y;
        int dz = this.z - point.z;
        return (dx * dx) + (dy * dy) + (dz * dz); 
	}
	
	public Point3D add(Point3D point){
		return new Point3D(this.x + point.x, this.y + point.y, this.z + point.z).setOffsets(offsetX, offsetY, offsetZ);
	}
	
	public boolean isZero(){
		return this.x == 0 && this.y == 0 && this.z == 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Point3D) {
			Point3D p = (Point3D) o;
		    	if (this.x == p.x && this.y == p.y && this.z == p.z) 
		    		return true;
		}
		return false;
	}
	
	/**
     * Returns a point3d with all coordinates trimmed.  Should only be used as an estimate, not for checking if blocks are clear.
     */
	public static Point3D fromDoubleCoordinates(double x, double y, double z){
		return new Point3D((int)x, (int)y, (int)z);
	}
}

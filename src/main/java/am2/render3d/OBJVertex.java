package am2.render3d;

public class OBJVertex{
	public float x;
	public float y;
	public float z;

	public OBJVertex(){
	}

	public OBJVertex(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object obj){
		if (obj == null) return false;
		if (!(obj instanceof OBJVertex)) return false;

		OBJVertex right = (OBJVertex)obj;
		return this.x == right.x && this.y == right.y && this.z == right.z;
	}

	@Override
	public int hashCode(){
		return (Float.floatToIntBits(x) ^ Float.floatToIntBits(y)) ^ Float.floatToIntBits(z);
	}
}

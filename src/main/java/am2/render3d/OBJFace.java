package am2.render3d;

public class OBJFace{
	public int[] v;
	public int[] t;
	public int[] n;

	public OBJNormal faceNormal;

	public OBJFace(int[] v, int[] t, int[] n){
		this.v = v;
		this.t = t;
		this.n = n;
		faceNormal = null;
	}

	public void setNormal(OBJNormal normal){
		this.faceNormal = normal;
	}
}

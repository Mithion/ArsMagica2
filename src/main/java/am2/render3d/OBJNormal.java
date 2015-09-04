package am2.render3d;

public class OBJNormal{
	public float i;
	public float j;
	public float k;

	public OBJNormal(){
	}

	public OBJNormal(float i, float j, float k){
		this.i = i;
		this.j = j;
		this.k = k;
	}

	public void normalize(){
		float a = (float)Math.sqrt((i * i) + (j * j) + (k * k));
		i = i / a;
		j = j / a;
		k = k / a;
	}

	public void flip(){
		i = 1 - i;
		j = 1 - j;
		k = 1 - k;
	}
}

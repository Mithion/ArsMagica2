package am2.api.math;

public class AMLineSegment{
	private AMVector3 a, b;

	public AMLineSegment(AMVector3 a, AMVector3 b){
		this.a = a;
		this.b = b;
	}

	public AMVector3 closestPointOnLine(AMVector3 vPoint){

		// Create the vector from end point vA to our point vPoint.
		AMVector3 vVector1 = vPoint.copy().sub(a);

		// Create a normalized direction vector from end point vA to end point vB
		AMVector3 vVector2 = b.copy().sub(a).normalize();

		// Use the distance formula to find the distance of the line segment (or magnitude)
		float d = (float)a.distanceTo(b);

		// Using the dot product, we project the vVector1 onto the vector vVector2.
		// This essentially gives us the distance from our projected vector from vA.
		float t = AMVector3.dotProduct(vVector2, vVector1);

		// If our projected distance from vA, "t", is less than or equal to 0, it must
		// be closest to the end point vA.  We want to return this end point.
		if (t <= 0)
			return a.copy();

		// If our projected distance from vA, "t", is greater than or equal to the magnitude
		// or distance of the line segment, it must be closest to the end point vB.  So, return vB.
		if (t >= d)
			return b.copy();

		AMVector3 vVector3 = vVector2.copy().scale(t);
		AMVector3 vClosestPoint = a.copy().add(vVector3);

		return vClosestPoint;
	}
}

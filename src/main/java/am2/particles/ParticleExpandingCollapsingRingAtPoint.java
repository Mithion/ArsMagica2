package am2.particles;


public class ParticleExpandingCollapsingRingAtPoint extends ParticleController{

	private double minDistance;
	private double maxDistance;
	private double speed;
	private CoordStore target;
	private boolean collapseOnce;
	private boolean isCollapsing;
	private boolean hasCollapsed;

	public ParticleExpandingCollapsingRingAtPoint(AMParticle particleEffect, double x, double y, double z, double minDistance, double maxDistance, double speed, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);

		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.speed = speed;
		this.target = new CoordStore(x, y, z);
	}

	public ParticleExpandingCollapsingRingAtPoint setCollapseOnce(){
		collapseOnce = true;
		return this;
	}

	public ParticleExpandingCollapsingRingAtPoint setExpanding(){
		isCollapsing = false;
		return this;
	}

	@Override
	public void doUpdate(){
		double deltax = target.x - particle.posX;
		double deltaz = target.z - particle.posZ;

		double angle = Math.atan2(deltaz, deltax);
		double distance = Math.sqrt(deltaz * deltaz + deltax * deltax);
		double factor = 0;
		if (isCollapsing){
			factor = speed;
		}else{
			factor = -speed;
		}

		double posX = Math.cos(angle) * factor;
		double posZ = Math.sin(angle) * factor;

		if (distance <= minDistance){
			isCollapsing = false;
			hasCollapsed = true;
		}else if (distance >= maxDistance){
			if (collapseOnce && hasCollapsed){
				this.finish();
			}else{
				isCollapsing = true;
			}
		}

		particle.setPosition(particle.posX + posX, particle.posY, particle.posZ + posZ);
	}

	private class CoordStore{
		public double x, y, z;

		public CoordStore(double x, double y, double z){
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	@Override
	public ParticleController clone(){
		return new ParticleExpandingCollapsingRingAtPoint(particle, target.x, target.y, target.z, minDistance, maxDistance, speed, priority, collapseOnce);
	}

}

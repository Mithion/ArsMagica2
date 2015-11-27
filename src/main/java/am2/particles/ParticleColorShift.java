package am2.particles;

public final class ParticleColorShift extends ParticleController{

	private float minRed;
	private float maxRed;
	private float minGreen;
	private float maxGreen;
	private float minBlue;
	private float maxBlue;

	private float targetRed;
	private float targetGreen;
	private float targetBlue;

	private boolean endOnReachingColor = false;

	private float shiftSpeed;

	private float redShift;
	private float greenShift;
	private float blueShift;

	public ParticleColorShift(AMParticle particleEffect, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);
		minRed = 0f;
		minGreen = 0f;
		minBlue = 0f;

		maxRed = 1.0f;
		maxGreen = 1.0f;
		maxBlue = 1.0f;

		shiftSpeed = 0.01f;

		GenerateNextColorTarget();
	}

	public ParticleColorShift SetColorTarget(float red, float green, float blue){
		targetRed = red;
		targetGreen = green;
		targetBlue = blue;

		redShift = Math.abs(particle.GetParticleRed() - targetRed) * shiftSpeed;
		greenShift = Math.abs(particle.GetParticleGreen() - targetGreen) * shiftSpeed;
		blueShift = Math.abs(particle.GetParticleBlue() - targetBlue) * shiftSpeed;

		return this;
	}

	public ParticleColorShift SetShiftSpeed(float speed){
		this.shiftSpeed = speed;

		redShift = shiftSpeed;
		greenShift = shiftSpeed;
		blueShift = shiftSpeed;

		return this;
	}

	public ParticleColorShift SetColorRange(float minRed, float minBlue, float minGreen, float maxRed, float maxGreen, float maxBlue){
		this.minRed = minRed;
		this.maxRed = maxRed;
		this.minGreen = minGreen;
		this.maxGreen = maxGreen;
		this.minBlue = minBlue;
		this.maxBlue = maxBlue;
		return this;
	}

	public ParticleColorShift SetEndOnReachingTargetColor(){
		endOnReachingColor = true;
		return this;
	}

	private void GenerateNextColorTarget(){
		targetRed = (particle.worldObj.rand.nextFloat() * (maxRed - minRed)) + minRed;
		targetGreen = (particle.worldObj.rand.nextFloat() * (maxGreen - minGreen)) + minGreen;
		targetBlue = (particle.worldObj.rand.nextFloat() * (maxBlue - minBlue)) + minBlue;
	}

	@Override
	public void doUpdate(){

		float currentRed = particle.GetParticleRed();
		float currentGreen = particle.GetParticleGreen();
		float currentBlue = particle.GetParticleBlue();

		if (currentRed == targetRed && currentGreen == targetGreen && currentBlue == targetBlue){
			if (endOnReachingColor){
				this.finish();
				return;
			}else{
				GenerateNextColorTarget();
			}
		}

		particle.setRGBColorF(ShiftValue(currentRed, targetRed, redShift), ShiftValue(currentGreen, targetGreen, greenShift), ShiftValue(currentBlue, targetBlue, blueShift));
	}

	private float ShiftValue(float current, float target, float step){
		float curDist = Math.abs(target - current);
		if (curDist < step){
			step = curDist;
		}
		if (current < target){
			current += step;
		}else if (current > target){
			current -= step;
		}
		return current;
	}

	@Override
	public ParticleController clone(){
		ParticleColorShift clone = new ParticleColorShift(particle, priority, endOnReachingColor).SetShiftSpeed(shiftSpeed).SetColorRange(minRed, minBlue, minGreen, maxRed, maxGreen, maxBlue).SetColorTarget(targetRed, targetGreen, targetBlue);
		if (this.endOnReachingColor) clone.SetEndOnReachingTargetColor();
		return clone;
	}

}

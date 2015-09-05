package am2.models;

public class SpriteRenderInfo{
	public int startFrame;
	public int endFrame;
	public int speed;
	public int curFrame;
	public boolean shouldReverse;
	public boolean isReversing;

	public boolean isDone;

	public SpriteRenderInfo(int StartFrame, int EndFrame, int Speed){
		startFrame = StartFrame;
		endFrame = EndFrame;
		speed = Speed;
		curFrame = StartFrame;
		shouldReverse = false;
	}

	public void incrementIndex(){
		if (!isReversing){
			curFrame++;
			if (curFrame >= endFrame){
				if (!shouldReverse){
					isDone = true;
				}else{
					isReversing = true;
				}
			}
		}else{
			curFrame--;
			if (curFrame <= startFrame){
				isDone = true;
			}
		}
	}

	public void reset(boolean resetToPlayBackwards){
		if (!resetToPlayBackwards){
			curFrame = startFrame;
			isDone = false;
			isReversing = false;
		}else{
			curFrame = endFrame;
			isReversing = true;
			isDone = false;
		}
	}
}

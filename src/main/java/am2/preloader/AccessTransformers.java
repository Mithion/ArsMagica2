package am2.preloader;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class AccessTransformers extends AccessTransformer{

	public AccessTransformers() throws IOException{
		super("AM2_at.cfg");
	}

}

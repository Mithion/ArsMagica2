package am2.preloader;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class AccessTransformers extends AccessTransformer{

	public AccessTransformers() throws IOException {
		super("AM2_at.cfg");
	}

}

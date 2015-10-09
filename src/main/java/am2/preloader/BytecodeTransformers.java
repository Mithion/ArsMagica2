package am2.preloader;

import am2.LogHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.Iterator;

public class BytecodeTransformers implements IClassTransformer{
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes){
		boolean is_obfuscated = !AM2PreloaderContainer.isDevEnvironment;
		
		if (transformedName.equals("am2.armor.ItemMageHood") && AM2PreloaderContainer.foundThaumcraft){
			LogHelper.info("Core: Altering definition of " + transformedName + " to be thaumcraft compatible.");
			ClassReader cr = new ClassReader(bytes);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);

			cn.interfaces.add("thaumcraft/api/IGoggles");
			cn.interfaces.add("thaumcraft/api/nodes/IRevealer");

			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cn.accept(cw);

			bytes = cw.toByteArray();
		}else if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer")){
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			bytes = alterEntityRenderer(bytes, is_obfuscated);
		}else if (transformedName.equals("net.minecraft.client.entity.EntityPlayerSP")){
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			bytes = alterEntityPlayerSP(bytes, is_obfuscated);

		/*
		// this part is actually no longer necessary
		// it was used to handle storing the player's information on dimension transfers
		// but we take care of that now in the event handler
		// (it's actually a stroke of good luck that this code was never updated to 1.7.10 and never activated in a dev environment)
		// (otherwise, I'd most likely not have been able to implement the fix for soulbound items in the End)
		
		}else if (transformedName.equals("net.minecraft.entity.EntityPlayerMP")){
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			bytes = alterEntityPlayerMP(bytes, is_obfuscated);
		*/

		// MC r1.6.4: NetServerHandler.handleFlying, ka/a
		// MC r1.7.10, NetHandlerPlayServer.processPlayer, nh/a
		// }else if (transformedName.equals("net.minecraft.network.NetServerHandler")){
		}else if (transformedName.equals("net.minecraft.network.NetHandlerPlayServer")){
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			// bytes = alterNetServerHandler(bytes);
			bytes = alterNetHandlerPlayServer(bytes, is_obfuscated);
		}else if (transformedName.equals("net.minecraft.client.renderer.entity.RendererLivingEntity")){
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			bytes = alterRendererLivingEntity(bytes, is_obfuscated);
		}else if (transformedName.equals("net.minecraft.world.World")){
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			bytes = alterWorld(bytes, is_obfuscated);
		}else if (transformedName.equals("net.minecraft.potion.PotionEffect") && !AM2PreloaderContainer.foundDragonAPI){
			// DragonAPI already has its own way to handle potion list ID extension
			// you get horrible crashes when the two extensions are applied at the same time
			// also, there's no sense in duplicating effort, so we'll do nothing in that situation
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			bytes = alterPotionEffect(bytes, is_obfuscated);
		}else if (transformedName.equals("net.minecraft.network.play.server.S1DPacketEntityEffect") && !AM2PreloaderContainer.foundDragonAPI){
			LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
			String passedname = name.replace(".", "/");
			bytes = alterS1DPacketEntityEffect(bytes, is_obfuscated, passedname);
		}else if (transformedName.equals("net.minecraft.client.network.NetHandlerPlayClient") && !AM2PreloaderContainer.foundDragonAPI){
		  LogHelper.info("Core: Altering definition of " + transformedName + ", " + (is_obfuscated ? " (obfuscated)" : "(not obfuscated)"));
		  bytes = alterNetHandlerPlayClient(bytes, is_obfuscated);
		}

		return bytes;
	}

	private byte[] alterRendererLivingEntity(byte[] bytes, boolean is_obfuscated){
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		// Minecraft r1.7.10:
		// net.minecraft.client.renderer.entity.RendererLivingEntity.java = boh.class
		
		// RendererLivingEntity.doRender = boh.a
		// MCP mapping: boh/a (Lsv;DDDFF)V net/minecraft/client/renderer/entity/RendererLivingEntity/func_76986_a (Lnet/minecraft/entity/EntityLivingBase;DDDFF)V
		
		// RendererLivingEntity.renderLivingAt = boh.a
		// MCP mapping: boh/a (Lsv;DDD)V net/minecraft/client/renderer/entity/RendererLivingEntity/func_77039_a (Lnet/minecraft/entity/EntityLivingBase;DDD)V

		obf_deobf_pair method1_name = new obf_deobf_pair();
		method1_name.setVal("doRender", false);
		method1_name.setVal("a", true);

		obf_deobf_pair method1_desc = new obf_deobf_pair();
		method1_desc.setVal("(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", false);
		method1_desc.setVal("(Lsv;DDDFF)V", true);

		obf_deobf_pair method1_searchinstruction_function = new obf_deobf_pair();
		method1_searchinstruction_function.setVal("renderLivingAt", false);
		method1_searchinstruction_function.setVal("a", true);

		obf_deobf_pair method1_searchinstruction_desc = new obf_deobf_pair();
		method1_searchinstruction_desc.setVal("(Lnet/minecraft/entity/EntityLivingBase;DDD)V", false);
		method1_searchinstruction_desc.setVal("(Lsv;DDD)V", true);
		
		obf_deobf_pair method1_replaceinstruction_desc = new obf_deobf_pair();
		method1_replaceinstruction_desc.setVal("(Lnet/minecraft/entity/EntityLivingBase;)V", false);
		method1_replaceinstruction_desc.setVal("(Lsv;)V", true);

		for (MethodNode mn : cn.methods){
			if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc.getVal(is_obfuscated))){
				AbstractInsnNode target = null;
				LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
				while (instructions.hasNext()){
					AbstractInsnNode node = instructions.next();
					if (node instanceof MethodInsnNode){
						MethodInsnNode method = (MethodInsnNode)node;
						if (method.name.equals(method1_searchinstruction_function.getVal(is_obfuscated)) && method.desc.equals(method1_searchinstruction_desc.getVal(is_obfuscated))){ //renderLivingAt(EntityLivingBase, double, double, double)
							target = node;
							break;
						}
					}
				}

				if (target != null){
					VarInsnNode aLoad = new VarInsnNode(Opcodes.ALOAD, 1);
					MethodInsnNode callout = new MethodInsnNode(Opcodes.INVOKESTATIC, "am2/utility/RenderUtilities", "setupShrinkRender", method1_replaceinstruction_desc.getVal(is_obfuscated));

					mn.instructions.insert(target, aLoad);
					mn.instructions.insert(aLoad, callout);
					LogHelper.debug("Core: Success!  Inserted opcodes!");
					break;
				}
			}
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);

		return cw.toByteArray();
	}

	private byte[] alterEntityRenderer(byte[] bytes, boolean is_obfuscated){
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		// Minecraft r1.7.10:
		// net.minecraft.client.renderer.EntityRenderer.java = blt.class
		
		// method 1:
		// EntityRenderer.setupCameraTransform = blt.a
		// MCP mapping: blt/a (FI)V net/minecraft/client/renderer/EntityRenderer/func_78479_a (FI)V
		obf_deobf_pair method1_name = new obf_deobf_pair();
		method1_name.setVal("setupCameraTransform", false);
		method1_name.setVal("a", true);
		
		String method1_desc = "(FI)V";
		// search for this function call:
		// EntityRenderer.orientCamera = blt.g
		// MCP mapping: blt/g (F)V net/minecraft/client/renderer/EntityRenderer/func_78475_f (F)V

		obf_deobf_pair method1_searchinstruction_function = new obf_deobf_pair();
		method1_searchinstruction_function.setVal("orientCamera", false);
		method1_searchinstruction_function.setVal("g", true);

		String method1_searchinstruction_desc = "(F)V";
		// we are also searching for gluPerspective, description (FFFF)V, but this is a GL function call and is not obfuscated.

		// method 2:
		// EntityRenderer.updateCameraAndRender = blt.b
		// MCP mapping: MD: blt/b (F)V net/minecraft/client/renderer/EntityRenderer/func_78480_b (F)V
		obf_deobf_pair method2_name = new obf_deobf_pair();
		method2_name.setVal("updateCameraAndRender", false);
		method2_name.setVal("b", true);
		
		String method2_desc = "(F)V";
		
		// search for this function call:
		// net.minecraft.profiler.Profiler.startSection = qi.a
		// MCP mapping: MD: qi/a (Ljava/lang/String;)V net/minecraft/profiler/Profiler/func_76320_a (Ljava/lang/String;)V
		obf_deobf_pair method2_searchinstruction_class = new obf_deobf_pair();
		method2_searchinstruction_class.setVal("net/minecraft/profiler/Profiler", false);
		method2_searchinstruction_class.setVal("qi", true);

		obf_deobf_pair method2_searchinstruction_function = new obf_deobf_pair();
		method2_searchinstruction_function.setVal("startSection", false);
		method2_searchinstruction_function.setVal("a", true);

		String method2_searchinstruction_desc = "(Ljava/lang/String;)V";

		// we will be inserting a call to am2.guis.AMGuiHelper.overrideMouseInput()
		// description (Lnet/minecraft/client/renderer/EntityRenderer;FZ)Z
		obf_deobf_pair method2_insertinstruction_desc = new obf_deobf_pair();
		method2_insertinstruction_desc.setVal("(Lnet/minecraft/client/renderer/EntityRenderer;FZ)Z", false);
		method2_insertinstruction_desc.setVal("(Lblt;FZ)Z", true);

		for (MethodNode mn : cn.methods){
			if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc)){ // setupCameraTransform
				AbstractInsnNode orientCameraNode = null;
				AbstractInsnNode gluPerspectiveNode = null;
				LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
				while (instructions.hasNext()){
					AbstractInsnNode node = instructions.next();
					if (node instanceof MethodInsnNode){
						MethodInsnNode method = (MethodInsnNode)node;
						if (orientCameraNode == null && method.name.equals(method1_searchinstruction_function.getVal(is_obfuscated)) && method.desc.equals(method1_searchinstruction_desc)){ //orientCamera
							LogHelper.debug("Core: Located target method insn node: " + method.name + method.desc);
							orientCameraNode = node;
							continue;
						}else if (gluPerspectiveNode == null && method.name.equals("gluPerspective") && method.desc.equals("(FFFF)V")){
							LogHelper.debug("Core: Located target method insn node: " + method.name + method.desc);
							gluPerspectiveNode = node;
							continue;
						}
					}

					if (orientCameraNode != null && gluPerspectiveNode != null){
						//found all nodes we're looking for
						break;
					}
				}
				if (orientCameraNode != null){
					VarInsnNode floatset = new VarInsnNode(Opcodes.FLOAD, 1);
					MethodInsnNode callout = new MethodInsnNode(Opcodes.INVOKESTATIC, "am2/guis/AMGuiHelper", "shiftView", "(F)V");
					mn.instructions.insert(orientCameraNode, callout);
					mn.instructions.insert(orientCameraNode, floatset);
					LogHelper.debug("Core: Success!  Inserted callout function op (shift)!");
				}
				if (gluPerspectiveNode != null){
					VarInsnNode floatset = new VarInsnNode(Opcodes.FLOAD, 1);
					MethodInsnNode callout = new MethodInsnNode(Opcodes.INVOKESTATIC, "am2/guis/AMGuiHelper", "flipView", "(F)V");
					mn.instructions.insert(gluPerspectiveNode, callout);
					mn.instructions.insert(gluPerspectiveNode, floatset);
					LogHelper.debug("Core: Success!  Inserted callout function op (flip)!");
				}

			}else if (mn.name.equals(method2_name.getVal(is_obfuscated)) && mn.desc.equals(method2_desc)){  //updateCameraAndRender
				AbstractInsnNode target = null;
				LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
				AbstractInsnNode node = null;
				boolean mouseFound = false;
				while (instructions.hasNext()){
					node = instructions.next();
					//look for the line:
					//this.mc.mcProfiler.startSection("mouse");
					if (!mouseFound){
						if (node instanceof LdcInsnNode){
							if (((LdcInsnNode)node).cst.equals("mouse")){
								mouseFound = true;
							}
						}
					}else{
						if (node instanceof MethodInsnNode){
							MethodInsnNode method = (MethodInsnNode)node;
							if (method.owner.equals(method2_searchinstruction_class.getVal(is_obfuscated)) && method.name.equals(method2_searchinstruction_function.getVal(is_obfuscated)) && method.desc.equals(method2_searchinstruction_desc)){
								LogHelper.debug("Core: Located target method insn node: " + method.owner + "." + method.name + ", " + method.desc);
								target = node;
								break;
							}
						}
					}
				}

				if (target != null){
					int iRegister = AM2PreloaderContainer.foundOptifine ? 3 : 2;

					VarInsnNode aLoad = new VarInsnNode(Opcodes.ALOAD, 0);
					VarInsnNode fLoad = new VarInsnNode(Opcodes.FLOAD, 1);
					VarInsnNode iLoad = new VarInsnNode(Opcodes.ILOAD, iRegister);
					MethodInsnNode callout = new MethodInsnNode(Opcodes.INVOKESTATIC, "am2/guis/AMGuiHelper", "overrideMouseInput", method2_insertinstruction_desc.getVal(is_obfuscated));
					VarInsnNode iStore = new VarInsnNode(Opcodes.ISTORE, iRegister);

					mn.instructions.insert(target, iStore);
					mn.instructions.insert(target, callout);
					mn.instructions.insert(target, iLoad);
					mn.instructions.insert(target, fLoad);
					mn.instructions.insert(target, aLoad);
					LogHelper.debug("Core: Success!  Inserted opcodes!");
				}
			}
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);

		return cw.toByteArray();
	}

	private byte[] alterEntityPlayerSP(byte[] bytes, boolean is_obfuscated){
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		// Minecraft r1.7.10: net.minecraft.client.entity.EntityPlayerSP.java = blk.class

		// EntityPlayerSP.onLivingUpdate() = blk/e
		// MCP mapping: blk/e ()V net/minecraft/client/entity/EntityPlayerSP/func_70636_d ()V
		obf_deobf_pair method1_name = new obf_deobf_pair();
		method1_name.setVal("onLivingUpdate", false);
		method1_name.setVal("e", true);

		String method1_desc = "()V";

		// MovementInput.updatePlayerMoveState() = bli/a
		// note that we don't need the class name, it's referencing an internal variable
		obf_deobf_pair method1_searchinstruction = new obf_deobf_pair();
		method1_searchinstruction.setVal("updatePlayerMoveState", false);
		method1_searchinstruction.setVal("a", true);

		String searchinstruction_desc = "()V";

		for (MethodNode mn : cn.methods){
			if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc)){ //onLivingUpdate
				AbstractInsnNode target = null;
				LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
				//look for the line:
				//this.movementInput.updatePlayerMoveState();
				while (instructions.hasNext()){
					AbstractInsnNode node = instructions.next();
					if (node instanceof VarInsnNode && ((VarInsnNode)node).getOpcode() == Opcodes.ALOAD){ //this.
						node = instructions.next();
						if (node instanceof FieldInsnNode && ((FieldInsnNode)node).getOpcode() == Opcodes.GETFIELD){ //movementInput.
							node = instructions.next();
							if (node instanceof MethodInsnNode){
								MethodInsnNode method = (MethodInsnNode)node;
								if (method.name.equals(method1_searchinstruction.getVal(is_obfuscated)) && method.desc.equals(searchinstruction_desc)){ //updatePlayerMoveState
									LogHelper.debug("Core: Located target method insn node: " + method.name + method.desc);
									target = node;
									break;
								}
							}
						}
					}
				}

				if (target != null){
					MethodInsnNode callout = new MethodInsnNode(Opcodes.INVOKESTATIC, "am2/guis/AMGuiHelper", "overrideKeyboardInput", "()V");
					mn.instructions.insert(target, callout);
					LogHelper.debug("Core: Success!  Inserted operations!");
					break;
				}
			}
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}

	private byte[] alterWorld(byte[] bytes, boolean is_obfuscated){
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		// Minecraft r1.7.10:
		// net.minecraft.world.World.java = ahb.class

		// World.playSoundAtEntity = ahb.a
		// MCP mapping: ahb/a (Lsa;Ljava/lang/String;FF)V net/minecraft/world/World/func_72956_a (Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V
		
	      obf_deobf_pair method1_name = new obf_deobf_pair();
	      method1_name.setVal("playSoundAtEntity", false);
	      method1_name.setVal("a", true);

	      obf_deobf_pair method1_desc = new obf_deobf_pair();
	      method1_desc.setVal("(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V", false);
	      method1_desc.setVal("(Lsa;Ljava/lang/String;FF)V", true);

	      obf_deobf_pair method1_replacement_desc = new obf_deobf_pair();
	      method1_replacement_desc.setVal("(Lnet/minecraft/entity/Entity;F)F", false);
	      method1_replacement_desc.setVal("(Lsa;F)F", true);

		for (MethodNode mn : cn.methods){
			if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc.getVal(is_obfuscated))){
				AbstractInsnNode target = null;
				LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
				while (instructions.hasNext()){
					AbstractInsnNode node = instructions.next();
					if (node instanceof VarInsnNode &&
							((VarInsnNode)node).getOpcode() == Opcodes.ALOAD &&
							((VarInsnNode)node).var == 5){
						AbstractInsnNode potentialMatch = node;
						node = instructions.next();
						if (node instanceof FieldInsnNode &&
								((FieldInsnNode)node).getOpcode() == Opcodes.GETFIELD &&
								((FieldInsnNode)node).name.equals("name") &&
								((FieldInsnNode)node).desc.equals("Ljava/lang/String;") &&
								((FieldInsnNode)node).owner.equals("net/minecraftforge/event/entity/PlaySoundAtEntityEvent")){
							LogHelper.debug("Core: Located target method insn node: " + ((FieldInsnNode)node).name + ", " + ((FieldInsnNode)node).desc);
							target = potentialMatch;
							break;
						}
					}
				}

				if (target != null){
					VarInsnNode aload1 = new VarInsnNode(Opcodes.ALOAD, 1);
					VarInsnNode fload4 = new VarInsnNode(Opcodes.FLOAD, 4);
					MethodInsnNode callout = new MethodInsnNode(Opcodes.INVOKESTATIC, "am2/utility/EntityUtilities", "modifySoundPitch", method1_replacement_desc.getVal(is_obfuscated));
					VarInsnNode fstore4 = new VarInsnNode(Opcodes.FSTORE, 4);
					mn.instructions.insertBefore(target, aload1);
					mn.instructions.insertBefore(target, fload4);
					mn.instructions.insertBefore(target, callout);
					mn.instructions.insertBefore(target, fstore4);
					LogHelper.debug("Core: Success!  Inserted operations!");
					break;
				}
			}
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);

		return cw.toByteArray();
	}

	// this function is no longer necessary in 1.7.10, since our event handler seems to handle dimension transfers properly
	// (in fact, it never actually triggered in 1.7.10 - the variable and function names were never updated)
	// the commented out code has been left here for future reference, in case the current event handler approach fails in the future
	/*
	private byte[] alterEntityPlayerMP(byte[] bytes){
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		for (MethodNode mn : cn.methods){
			LogHelper.debug("%s %s", mn.name, mn.desc);
			if (mn.name.equals("b_") && mn.desc.equals("(Luf;)V")){ //travelToDimension
				AbstractInsnNode target = null;
				LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
				//look for the line:
				//this.worldObj.removeEntity(this)
				while (instructions.hasNext()){
					AbstractInsnNode node = instructions.next();
					if (node instanceof MethodInsnNode){
						MethodInsnNode method = (MethodInsnNode)node;
						if (method.name.equals("e") && method.desc.equals("(Lnn;)V")){ //removeEntity
							//instructions for the method go:
							//ALOAD 0
							//GETFIELD...
							//ALOAD 0
							//INVOKEVIRTUAL
							target = method.getPrevious().getPrevious().getPrevious(); //back up to the first ALOAD
							break;
						}
					}
				}

				if (target != null){
					//GETSTATIC am2/AMCore.proxy : Lam2/proxy/CommonProxy;
					FieldInsnNode proxyGet = new FieldInsnNode(Opcodes.GETSTATIC, "am2/AMCore", "proxy", "Lam2/proxy/CommonProxy");
					//GETFIELD am2/proxy/CommonProxy.playerTracker : Lam2/PlayerTracker;
					FieldInsnNode fieldGet = new FieldInsnNode(Opcodes.GETFIELD, "am2/proxy/CommonProxy", "playerTracker", "Lam2/PlayerTracker");
					//ALOAD 0
					VarInsnNode aLoad = new VarInsnNode(Opcodes.ALOAD, 0);
					//INVOKEVIRTUAL am2/PlayerTracker.onPlayerDeath (Lnet/minecraft/entity/player/EntityPlayer;)V
					MethodInsnNode callout = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "am2/PlayerTracker", "onPlayerDeath", "(Luf;)V");

					mn.instructions.insertBefore(target, proxyGet);
					mn.instructions.insertBefore(target, fieldGet);
					mn.instructions.insertBefore(target, aLoad);
					mn.instructions.insertBefore(target, callout);

					LogHelper.debug("Core: Success!  Inserted opcodes!");
				}
			}
		}

		return bytes;
	}
	*/

	private byte[] alterNetHandlerPlayServer(byte[] bytes, boolean is_obfuscated){
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		// Minecraft r1.6.4: NetServerHandler.java = ka.class
		// Minecraft r1.7.10: net.minecraft.network.NetHandlerPlayServer.java = nh.class

		// NetHandlerPlayServer.processPlayer(C03PacketPlayer), nh.a(jd)
		// MCP mapping: nh/a (Ljd;)V net/minecraft/network/NetHandlerPlayServer/func_147347_a (Lnet/minecraft/network/play/client/C03PacketPlayer;)V

		obf_deobf_pair method1_name = new obf_deobf_pair();
		method1_name.setVal("processPlayer", false);
		method1_name.setVal("a", true);

		obf_deobf_pair method1_desc = new obf_deobf_pair();
		method1_desc.setVal("(Lnet/minecraft/network/play/client/C03PacketPlayer;)V", false);
		method1_desc.setVal("(Ljd;)V", true);

		// in MC r1.6.4, this was a GETFIELD
		// fetching Packet10Flying.stance and Packet10Flying.yPosition
		// in MC r1.7.10, these are no longer public variables and we now have to call their access wrapper functions
		// INVOKEVIRTUAL in both cases
		// net/minecraft/network/play/client/C03PacketPlayer/func_149471_f = jd/f
		// net/minecraft/network/play/client/C03PacketPlayer/func_149467_d = jd/d
		// both have description ()D

		obf_deobf_pair method1_searchinstruction_class = new obf_deobf_pair();
		method1_searchinstruction_class.setVal("net/minecraft/network/play/client/C03PacketPlayer", false);
		method1_searchinstruction_class.setVal("jd", true);
		
		obf_deobf_pair method1_searchinstruction_function1 = new obf_deobf_pair();
		method1_searchinstruction_function1.setVal("func_149471_f", false);
		method1_searchinstruction_function1.setVal("f", true);

		obf_deobf_pair method1_searchinstruction_function2 = new obf_deobf_pair();
		method1_searchinstruction_function2.setVal("func_149467_d", false);
		method1_searchinstruction_function2.setVal("d", true);

		String method1_searchinstructions_desc = "()D";
		
		for (MethodNode mn : cn.methods){
			if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc.getVal(is_obfuscated))){ //processPlayer
				AbstractInsnNode target = null;
				LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
				Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();

				//look for the line:
				// d4 = p_147347_1_.func_149471_f() - p_147347_1_.func_149467_d();
				// p_147347_1_ = C03PacketPlayer
				//in MC r1.6.4, d4 = par1Packet10Flying.stance - par1Packet10Flying.yPosition;
				while (instructions.hasNext()){
					AbstractInsnNode node = instructions.next();
					if (node instanceof VarInsnNode && ((VarInsnNode)node).var == 1 && ((VarInsnNode)node).getOpcode() == Opcodes.ALOAD){ //ALOAD 1
						node = instructions.next();
						if (node instanceof MethodInsnNode && matchMethodNode((MethodInsnNode)node, Opcodes.INVOKEVIRTUAL, method1_searchinstruction_class.getVal(is_obfuscated), method1_searchinstruction_function1.getVal(is_obfuscated), method1_searchinstructions_desc)){
							node = instructions.next();
							if (node instanceof VarInsnNode && ((VarInsnNode)node).var == 1 && ((VarInsnNode)node).getOpcode() == Opcodes.ALOAD){ //ALOAD 1
								node = instructions.next();
								if (node instanceof MethodInsnNode && matchMethodNode((MethodInsnNode)node, Opcodes.INVOKEVIRTUAL, method1_searchinstruction_class.getVal(is_obfuscated), method1_searchinstruction_function2.getVal(is_obfuscated), method1_searchinstructions_desc)){
									node = instructions.next();
									if (node instanceof InsnNode && ((InsnNode)node).getOpcode() == Opcodes.DSUB){ //DSUB
										node = instructions.next();
										if (node instanceof VarInsnNode && ((VarInsnNode)node).var == 13 && ((VarInsnNode)node).getOpcode() == Opcodes.DSTORE){ //DSTORE 13
											target = node;
											break;
										}
									}
								}
							}
						}
					}
				}

				if (target != null){
					LdcInsnNode ldc = new LdcInsnNode(1.5);
					VarInsnNode dstore13 = new VarInsnNode(Opcodes.DSTORE, 13);

					mn.instructions.insert(target, ldc);
					mn.instructions.insert(ldc, dstore13);

					LogHelper.debug("Core: Success!  Inserted opcodes!");
				}
			}
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}
	
	private byte[] alterPotionEffect(byte[] bytes, boolean is_obfuscated){
	      ClassReader cr = new ClassReader(bytes);
	      ClassNode cn = new ClassNode();
	      cr.accept(cn, 0);
	      
	      // Minecraft r1.7.10:
	      // PotionEffect.java --> rw.class
	      
	      // PotionEffect.writeCustomPotionEffectToNBT = rw.a
	      // MCP mapping: rw/a (Ldh;)Ldh; net/minecraft/potion/PotionEffect/func_82719_a (Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound
	      
	      // PotionEffect.readCustomPotionEffectFromNBT = rw.b
	      // MCP mapping: rw/b (Ldh;)Lrw; net/minecraft/potion/PotionEffect/func_82722_b (Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/potion/PotionEffect;
	      
	      // PotionEffect.getPotionID = rw.a
	      // MCP mapping: rw/a ()I net/minecraft/potion/PotionEffect/func_76456_a ()I
	      // note: called as this.getPotionID(), original has a typecast to byte before calling it
	      
	      // NBTTagCompound.java --> dh.class
	      
	      // NBTTagCompound.setByte(string, value) = dh.a
	      // MCP mapping: dh/a (Ljava/lang/String;B)V net/minecraft/nbt/NBTTagCompound/func_74774_a (Ljava/lang/String;B)V
	      
	      // NBTTagCompound.setInteger(string, value) = dh.a
	      // MCP mapping: dh/a (Ljava/lang/String;I)V net/minecraft/nbt/NBTTagCompound/func_74768_a (Ljava/lang/String;I)V
	      
	      // NBTTagCompound.getByte(string) = dh.d
	      // MCP mapping: dh/d (Ljava/lang/String;)B net/minecraft/nbt/NBTTagCompound/func_74771_c (Ljava/lang/String;)B
	      
	      // NBTTagCompound.getInteger(string) = dh.f
	      // MCP mapping: dh/f (Ljava/lang/String;)I net/minecraft/nbt/NBTTagCompound/func_74762_e (Ljava/lang/String;)I
	      
	      obf_deobf_pair method1_name = new obf_deobf_pair();
	      method1_name.setVal("writeCustomPotionEffectToNBT", false);
	      method1_name.setVal("a", true);

	      obf_deobf_pair method1_desc = new obf_deobf_pair();
	      method1_desc.setVal("(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;", false);
	      method1_desc.setVal("(Ldh;)Ldh;", true);

	      // don't forget, you need to remove the i2b instruction which immediately precedes this one
	      obf_deobf_pair method1_searchinstruction_class = new obf_deobf_pair();
	      method1_searchinstruction_class.setVal("net/minecraft/nbt/NBTTagCompound", false);
	      method1_searchinstruction_class.setVal("dh", true);

	      obf_deobf_pair method1_searchinstruction_function = new obf_deobf_pair();
	      method1_searchinstruction_function.setVal("setByte", false);
	      method1_searchinstruction_function.setVal("a", true);

	      obf_deobf_pair method1_searchinstruction_desc = new obf_deobf_pair();
	      method1_searchinstruction_desc.setVal("(Ljava/lang/String;B)V", false);
	      method1_searchinstruction_desc.setVal("(Ljava/lang/String;B)V", true);


	      // replace instruction class is the same as the search instruction class for method1

	      obf_deobf_pair method1_replaceinstruction_function = new obf_deobf_pair();
	      method1_replaceinstruction_function.setVal("setInteger", false);
	      method1_replaceinstruction_function.setVal("a", true);

	      obf_deobf_pair method1_replaceinstruction_desc = new obf_deobf_pair();
	      method1_replaceinstruction_desc.setVal("(Ljava/lang/String;I)V", false);
	      method1_replaceinstruction_desc.setVal("(Ljava/lang/String;I)V", true);

	      obf_deobf_pair method2_name = new obf_deobf_pair();
	      method2_name.setVal("readCustomPotionEffectFromNBT", false);
	      method2_name.setVal("b", true);

	      obf_deobf_pair method2_desc = new obf_deobf_pair();
	      method2_desc.setVal("(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/potion/PotionEffect;", false);
	      method2_desc.setVal("(Ldh;)Lrw;", true);

	      obf_deobf_pair method2_searchinstruction_class = new obf_deobf_pair();
	      method2_searchinstruction_class.setVal("net/minecraft/nbt/NBTTagCompound", false);
	      method2_searchinstruction_class.setVal("dh", true);

	      obf_deobf_pair method2_searchinstruction_function = new obf_deobf_pair();
	      method2_searchinstruction_function.setVal("getByte", false);
	      method2_searchinstruction_function.setVal("d", true);

	      obf_deobf_pair method2_searchinstruction_desc = new obf_deobf_pair();
	      method2_searchinstruction_desc.setVal("(Ljava/lang/String;)B", false);
	      method2_searchinstruction_desc.setVal("(Ljava/lang/String;)B", true);

	      // replace instruction class is the same as the search instruction class for method2 as well

	      obf_deobf_pair method2_replaceinstruction_function = new obf_deobf_pair();
	      method2_replaceinstruction_function.setVal("getInteger", false);
	      method2_replaceinstruction_function.setVal("f", true);

	      obf_deobf_pair method2_replaceinstruction_desc = new obf_deobf_pair();
	      method2_replaceinstruction_desc.setVal("(Ljava/lang/String;)I", false);
	      method2_replaceinstruction_desc.setVal("(Ljava/lang/String;)I", true);
	      
	      // LogHelper.debug("Core: looking for method " + method1_name.getVal(is_obfuscated) + " with signature " + method1_desc.getVal(is_obfuscated));
	      // LogHelper.debug("Core: looking for method " + method2_name.getVal(is_obfuscated) + " with signature " + method2_desc.getVal(is_obfuscated));
	      
	      
	      for (MethodNode mn : cn.methods){
		      // LogHelper.debug("Currently on: method " + mn.name + ", description " + mn.desc);
		      if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc.getVal(is_obfuscated))){
			      // writeCustomPotionEffectToNBT
			      AbstractInsnNode target = null;
			      AbstractInsnNode toRemove = null;
			      LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
			      
			      Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
			      while (instructions.hasNext()){
				      AbstractInsnNode node = instructions.next();
				      if (node instanceof MethodInsnNode){
					      MethodInsnNode method = (MethodInsnNode)node;
					      if (method.owner.equals(method1_searchinstruction_class.getVal(is_obfuscated)) && method.name.equals(method1_searchinstruction_function.getVal(is_obfuscated)) && method.desc.equals(method1_searchinstruction_desc.getVal(is_obfuscated))){ //setByte(string, byte)
						      target = method;
						      // don't forget, you need to remove the i2b instruction which immediately precedes this one
						      toRemove = method.getPrevious();
						      break;
					      }
				      }
			      }

			      if (target != null){
				MethodInsnNode newset = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, method1_searchinstruction_class.getVal(is_obfuscated), method1_replaceinstruction_function.getVal(is_obfuscated), method1_replaceinstruction_desc.getVal(is_obfuscated));
				
				InsnNode new_nop = new InsnNode(Opcodes.NOP);
				
				// removing instructions makes the game crash, we'll just replace it with NOP
				mn.instructions.set(toRemove, new_nop);
				mn.instructions.set(target, newset);
				
				LogHelper.debug("Core: Success!  Replaced opcodes!");
			      }
		      } else if (mn.name.equals(method2_name.getVal(is_obfuscated)) && mn.desc.equals(method2_desc.getVal(is_obfuscated))){
			      // readCustomPotionEffectFromNBT
			      AbstractInsnNode target = null;
			      LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
			      
			      Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
			      while (instructions.hasNext()){
				      AbstractInsnNode node = instructions.next();
				      if (node instanceof MethodInsnNode){
					      MethodInsnNode method = (MethodInsnNode)node;
					      if (method.owner.equals(method2_searchinstruction_class.getVal(is_obfuscated)) && method.name.equals(method2_searchinstruction_function.getVal(is_obfuscated)) && method.desc.equals(method2_searchinstruction_desc.getVal(is_obfuscated))){ //getByte(string, byte)
						      target = method;
						      break;
					      }
				      }
			      }

			      if (target != null){
				MethodInsnNode newset = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, method2_searchinstruction_class.getVal(is_obfuscated), method2_replaceinstruction_function.getVal(is_obfuscated), method2_replaceinstruction_desc.getVal(is_obfuscated));
				
				mn.instructions.set(target, newset);
				
				LogHelper.debug("Core: Success!  Replaced opcodes!");
			      }
		      }
	      }

	      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
	      cn.accept(cw);
	      return cw.toByteArray();
	}
	
	private byte[] alterS1DPacketEntityEffect(byte[] bytes, boolean is_obfuscated, String classname){
	      ClassReader cr = new ClassReader(bytes);
	      ClassNode cn = new ClassNode();
	      cr.accept(cn, 0);
	      
	      // Minecraft r1.7.10:
	      // S1DPacketEntityEffect.java --> in.class
	      
	      // S1DPacketEntityEffect.field_149432_b = in.b
	      
	      // S1DPacketEntityEffect.<init> = in.<init>
	      // Method name and description: S1DPacketEntityEffect/<init> (ILnet/minecraft/potion/PotionEffect;)V;
	      // Obfuscated: in/<init>, (ILrw;)V;
	      // changes in init: look for invokevirtual rw/a, signature ()I
	      // check to see if the 3 following instructions are SIPUSH, IAND, I2B
	      // if so, replace all with NOP
	      // the fourth instruction, PUTFIELD, might possibly need its signature changed
	      // but I'm going to see exactly what the ASM documentation means when it says
	      // "hopefully ASM hides all the details related to the constant pool"
	      
	      // well, the documentation is talking bollocks
	      // we need to change the constant pool manually... which is not an easy thing to accomplish
	      
	      
	      // S1DPacketEntityEffect.readPacketData(PacketBuffer) = in.a(et)
	      // MCP mapping: in/a (Let;)V net/minecraft/network/play/server/S1DPacketEntityEffect/func_148837_a (Lnet/minecraft/network/PacketBuffer;)V
	      
	      // --conveniently, PacketBuffer extends an external library, so the important bits can't be obfuscated
	      // PacketBuffer.readByte() = et.readByte()
	      // MCP mapping: et/readByte ()B net/minecraft/network/PacketBuffer/readByte ()B
	      
	      // PacketBuffer.readInt() = et.readInt()
	      // MCP mapping: et/readInt ()I net/minecraft/network/PacketBuffer/readInt ()I
	      
	      
	      // S1DPacketEntityEffect.writePacketData(PacketBuffer) = in.b(et)
	      // MCP mapping: in/b (Let;)V net/minecraft/network/play/server/S1DPacketEntityEffect/func_148840_b (Lnet/minecraft/network/PacketBuffer;)V
	      
	      // PacketBuffer/writeByte() = et.writeByte()
	      // MCP mapping: MD: et/writeByte (I)Lio/netty/buffer/ByteBuf; net/minecraft/network/PacketBuffer/writeByte (I)Lio/netty/buffer/ByteBuf;
	      
	      // PacketBuffer.writeInt() = et.writeInt()
	      // MCP mapping: MD: et/writeInt (I)Lio/netty/buffer/ByteBuf; net/minecraft/network/PacketBuffer/writeInt (I)Lio/netty/buffer/ByteBuf;
	      
	      
	      // S1DPacketEntityEffect.func_149427_e() = in.e()
	      // MCP mapping: in/e ()B net/minecraft/network/play/server/S1DPacketEntityEffect/func_149427_e ()B
	      // need to change signature to ()I, don't change the method at all
	      
	      obf_deobf_pair potionid_bytevar_name = new obf_deobf_pair();
	      potionid_bytevar_name.setVal("field_149432_b", false);
	      potionid_bytevar_name.setVal("b", true);
	      String potionid_bytevar_origdesc = "B";
	      String potionid_bytevar_newdesc = "I";
	      
	      // we'll have to make a new field for this
	      // existing one had description B (change this to I), access 2, signature null, value null
	      
	      
	      String initmethod_name = "<init>";
	      obf_deobf_pair initmethod_desc = new obf_deobf_pair();
	      initmethod_desc.setVal("(ILnet/minecraft/potion/PotionEffect;)V", false);
	      initmethod_desc.setVal("(ILrw;)V", true);
	      
	      obf_deobf_pair initmethod_searchinstruction_owner = new obf_deobf_pair();
	      initmethod_searchinstruction_owner.setVal("net/minecraft/potion/PotionEffect", false);
	      initmethod_searchinstruction_owner.setVal("rw", true);
	      
	      obf_deobf_pair initmethod_searchinstruction_function = new obf_deobf_pair();
	      initmethod_searchinstruction_function.setVal("getPotionID", false);
	      initmethod_searchinstruction_function.setVal("a", true);
	      
	      String initmethod_searchinstruction_desc = "()I";
	      
	      
	      obf_deobf_pair method1_name = new obf_deobf_pair();
	      method1_name.setVal("readPacketData", false);
	      method1_name.setVal("a", true);
	      
	      obf_deobf_pair method1_desc = new obf_deobf_pair();
	      method1_desc.setVal("(Lnet/minecraft/network/PacketBuffer;)V", false);
	      method1_desc.setVal("(Let;)V", true);
	      
	      obf_deobf_pair method1_searchinstruction_owner = new obf_deobf_pair();
	      method1_searchinstruction_owner.setVal("net/minecraft/network/PacketBuffer", false);
	      method1_searchinstruction_owner.setVal("et", true);
	      
	      String method1_searchinstruction_function = "readByte";
	      String method1_searchinstruction_desc = "()B";
	      
	      // replacement class is the same
	      String method1_replaceinstruction_function = "readInt";
	      String method1_replaceinstruction_desc = "()I";
	      
	      
	      obf_deobf_pair method2_name = new obf_deobf_pair();
	      method2_name.setVal("writePacketData", false);
	      method2_name.setVal("b", true);
	      
	      obf_deobf_pair method2_desc = new obf_deobf_pair();
	      method2_desc.setVal("(Lnet/minecraft/network/PacketBuffer;)V", false);
	      method2_desc.setVal("(Let;)V", true);
	      
	      obf_deobf_pair method2_searchinstruction_owner = new obf_deobf_pair();
	      method2_searchinstruction_owner.setVal("net/minecraft/network/PacketBuffer", false);
	      method2_searchinstruction_owner.setVal("et", true);
	      
	      String method2_searchinstruction_function = "writeByte";
	      String method2_searchinstruction_desc = "(I)Lio/netty/buffer/ByteBuf";
	      
	      // replacement class is the same
	      // so is the instruction, but we'll leave it as being explicitly defined just in case
	      String method2_replaceinstruction_function = "writeInt";
	      String method2_replaceinstruction_desc = "(I)Lio/netty/buffer/ByteBuf";
	      
	      // need to replace only this method descriptor
	      obf_deobf_pair method3_name = new obf_deobf_pair();
	      method3_name.setVal("func_149427_e", false);
	      method3_name.setVal("e", true);
	      
	      String method3_desc = "()B";
	      String method3_newdesc = "()I";
	      
	      
	      String potionID_intvar_name = "potionID_intvalue";
	      String potionID_intvar_desc = "I";
	      int potionID_intvar_access = 2;
	      
	      FieldNode potionID_intvar = new FieldNode(potionID_intvar_access, potionID_intvar_name, potionID_intvar_desc, null, null);
	      cn.fields.add(potionID_intvar);
	      
	      for (MethodNode mn : cn.methods){
		      // LogHelper.debug("Currently on: method " + mn.name + ", description " + mn.desc);
		      if (mn.name.equals(initmethod_name) && mn.desc.equals(initmethod_desc.getVal(is_obfuscated))){
			      // init method
			      AbstractInsnNode toRemove1 = null;
			      AbstractInsnNode toRemove2 = null;
			      AbstractInsnNode toRemove3 = null;
			      AbstractInsnNode toReplace = null;
			      LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
			      
			      Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
			      while (instructions.hasNext()){
				      AbstractInsnNode node = instructions.next();
				      if (node instanceof MethodInsnNode){
					      MethodInsnNode method = (MethodInsnNode)node;
					      if (method.owner.equals(initmethod_searchinstruction_owner.getVal(is_obfuscated)) && method.name.equals(initmethod_searchinstruction_function.getVal(is_obfuscated)) && method.desc.equals(initmethod_searchinstruction_desc)){ //getPotionID
						      node = instructions.next();
						      if (node instanceof IntInsnNode && ((IntInsnNode)node).getOpcode() == Opcodes.SIPUSH){
							      toRemove1 = node;
							      node = instructions.next();
							      if (node instanceof InsnNode && ((InsnNode)node).getOpcode() == Opcodes.IAND){
								      toRemove2 = node;
								      node = instructions.next();
								      if (node instanceof InsnNode && ((InsnNode)node).getOpcode() == Opcodes.I2B){
									      toRemove3 = node;
									      node = instructions.next();
									      if (node instanceof FieldInsnNode && ((FieldInsnNode)node).getOpcode() == Opcodes.PUTFIELD && ((FieldInsnNode)node).name.equals(potionid_bytevar_name.getVal(is_obfuscated)) && ((FieldInsnNode)node).desc.equals(potionid_bytevar_origdesc)){
										      toReplace = node;
										      break;
									      }
								      }
							      }
						      }
					      }
				      }
			      }
			      
			      if (toRemove1 != null && toRemove2 != null && toRemove3 != null && toReplace != null){
				      InsnNode new_nop = new InsnNode(Opcodes.NOP);
				      FieldInsnNode new_potion_id_variable = new FieldInsnNode(Opcodes.PUTFIELD, classname, potionID_intvar_name, potionID_intvar_desc);
				      
				      mn.instructions.set(toRemove1, new_nop);
				      mn.instructions.set(toRemove2, new_nop);
				      mn.instructions.set(toRemove3, new_nop);
				      mn.instructions.set(toReplace, new_potion_id_variable);
				      LogHelper.debug("Core: Success! Replaced opcodes!" + mn.name + mn.desc);
			      }
		      } else if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc.getVal(is_obfuscated))){
			      // readPacketData
			      AbstractInsnNode target = null;
			      AbstractInsnNode target2 = null;
			      LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
			      
			      Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
			      while (instructions.hasNext()){
				      AbstractInsnNode node = instructions.next();
				      if (node instanceof MethodInsnNode){
					      MethodInsnNode method = (MethodInsnNode)node;
					      if (method.owner.equals(method1_searchinstruction_owner.getVal(is_obfuscated)) && method.name.equals(method1_searchinstruction_function) && method.desc.equals(method1_searchinstruction_desc)){
						      target = node;
						      node = instructions.next();
						      if (node instanceof FieldInsnNode && ((FieldInsnNode)node).getOpcode() == Opcodes.PUTFIELD && ((FieldInsnNode)node).name.equals(potionid_bytevar_name.getVal(is_obfuscated)) && ((FieldInsnNode)node).desc.equals(potionid_bytevar_origdesc)){
							    target2 = node;
							    break;
						      }
					      }
				      }
			      }
			      
			      if (target != null && target2 != null){
				      MethodInsnNode new_readint = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, method1_searchinstruction_owner.getVal(is_obfuscated), method1_replaceinstruction_function, method1_replaceinstruction_desc);
				      FieldInsnNode new_potion_id_variable = new FieldInsnNode(Opcodes.PUTFIELD, classname, potionID_intvar_name, potionID_intvar_desc);
				      
				      mn.instructions.set(target, new_readint);
				      mn.instructions.set(target2, new_potion_id_variable);
				      LogHelper.debug("Core: Success!  Replaced opcodes!");
			      }
		      } else if (mn.name.equals(method2_name.getVal(is_obfuscated)) && mn.desc.equals(method2_desc.getVal(is_obfuscated))){
			      // writePacketData
			      AbstractInsnNode target = null;
			      AbstractInsnNode target2 = null;
			      LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
			      
			      Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
			      while (instructions.hasNext()){
				      AbstractInsnNode node = instructions.next();
				      if (node instanceof FieldInsnNode && ((FieldInsnNode)node).getOpcode() == Opcodes.GETFIELD && ((FieldInsnNode)node).name.equals(potionid_bytevar_name.getVal(is_obfuscated)) && ((FieldInsnNode)node).desc.equals(potionid_bytevar_origdesc)){
					      target = node;
					      node = instructions.next();
					      if (node instanceof MethodInsnNode && ((MethodInsnNode)node).owner.equals(method2_searchinstruction_owner.getVal(is_obfuscated)) && ((MethodInsnNode)node).name.equals(method2_searchinstruction_function) && ((MethodInsnNode)node).desc.equals(method2_searchinstruction_desc)){
						      target2 = node;
						      break;
					      }
					
				      }
			      }
			      
			      if (target != null && target2 != null){
				      FieldInsnNode new_potion_id_variable = new FieldInsnNode(Opcodes.GETFIELD, classname, potionID_intvar_name, potionID_intvar_desc);
				      MethodInsnNode new_writeint = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, method2_searchinstruction_owner.getVal(is_obfuscated), method2_replaceinstruction_function, method2_replaceinstruction_desc);
				      
				      mn.instructions.set(target, new_potion_id_variable);
				      mn.instructions.set(target2, new_writeint);
				      LogHelper.debug("Core: Success!  Replaced opcodes!");
			      }
		      }
	      }
	      
	      // access qualifier = 1 = public
	      LogHelper.debug("Core: Creating new getPotionID method");
	      MethodNode newGetPotionIDMethod = new MethodNode(1, "getPotionID", method3_newdesc, null, null);
	      newGetPotionIDMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
	      newGetPotionIDMethod.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, classname, potionID_intvar_name, potionID_intvar_desc));
	      newGetPotionIDMethod.instructions.add(new InsnNode(Opcodes.IRETURN));
	      cn.methods.add(newGetPotionIDMethod);

	      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
	      cn.accept(cw);
	      return cw.toByteArray();
	}
	
	private byte[] alterNetHandlerPlayClient(byte[] bytes, boolean is_obfuscated){
	      ClassReader cr = new ClassReader(bytes);
	      ClassNode cn = new ClassNode();
	      cr.accept(cn, 0);
	      
	      // Minecraft r1.7.10:
	      // NetHandlerPlayClient.java = bjb.class
	      
	      // NetHandlerPlayClient.handleEntityEffect(S1DPacketEntityEffect) = bjb.a(in)
	      // MCP mapping: bjb/a (Lin;)V net/minecraft/client/network/NetHandlerPlayClient/func_147260_a (Lnet/minecraft/network/play/server/S1DPacketEntityEffect;)V
	      
	      // Instruction which we're searching for: invokevirtual,
	      // S1DPacketEntityEffect/func_149427_e ()B [deobfuscated]
	      // in/e ()B [obfuscated]
	      // replace with S1DPacketEntityEffect/func_149427_e or in/e, ()I
	      
	      obf_deobf_pair method1_name = new obf_deobf_pair();
	      method1_name.setVal("handleEntityEffect", false);
	      method1_name.setVal("a", true);
	      
	      obf_deobf_pair method1_desc = new obf_deobf_pair();
	      method1_desc.setVal("(Lnet/minecraft/network/play/server/S1DPacketEntityEffect;)V", false);
	      method1_desc.setVal("(Lin;)V", true);
	      
	      obf_deobf_pair method1_searchinstruction_class = new obf_deobf_pair();
	      method1_searchinstruction_class.setVal("net/minecraft/network/play/server/S1DPacketEntityEffect", false);
	      method1_searchinstruction_class.setVal("in", true);
	      
	      obf_deobf_pair method1_searchinstruction_function = new obf_deobf_pair();
	      method1_searchinstruction_function.setVal("func_149427_e", false);
	      method1_searchinstruction_function.setVal("e", true);
	      
	      String method1_searchinstruction_desc = "()B";
	      
	      String method1_replaceinstruction_function = "getPotionID";
	      String method1_replaceinstruction_desc = "()I";
	      
	      for (MethodNode mn : cn.methods){
		      // LogHelper.debug("Currently on: method " + mn.name + ", description " + mn.desc);
		      if (mn.name.equals(method1_name.getVal(is_obfuscated)) && mn.desc.equals(method1_desc.getVal(is_obfuscated))){
			      AbstractInsnNode target = null;
			      LogHelper.debug("Core: Located target method " + mn.name + mn.desc);
			      
			      Iterator<AbstractInsnNode> instructions = mn.instructions.iterator();
			      while (instructions.hasNext()){
				      AbstractInsnNode node = instructions.next();
				      if (node instanceof MethodInsnNode){
					      MethodInsnNode method = (MethodInsnNode)node;
					      if (method.owner.equals(method1_searchinstruction_class.getVal(is_obfuscated)) && method.name.equals(method1_searchinstruction_function.getVal(is_obfuscated)) && method.desc.equals(method1_searchinstruction_desc)){
						      target = method;
						      break;
					      }
				      }
			      }

			      if (target != null){
				MethodInsnNode newset = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, method1_searchinstruction_class.getVal(is_obfuscated), method1_replaceinstruction_function, method1_replaceinstruction_desc);

				mn.instructions.set(target, newset);
				
				LogHelper.debug("Core: Success!  Replaced opcode!");
			      }
		      }
	      }
	      
	      
	      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
	      cn.accept(cw);
	      return cw.toByteArray();
	}

	private boolean matchFieldNode(FieldInsnNode node, int opcode, String owner, String name, String desc){
		if (node.getOpcode() == opcode && node.owner.equals(owner) && node.name.equals(name) && node.desc.equals(desc))
			return true;
		return false;
	}

	private boolean matchMethodNode(MethodInsnNode node, int opcode, String owner, String name, String desc){
		if (node.getOpcode() == opcode && node.owner.equals(owner) && node.name.equals(name) && node.desc.equals(desc))
			return true;
		return false;
	}

	private void debugPrintInsns(MethodNode mn){
		Iterator<AbstractInsnNode> it = mn.instructions.iterator();

		LogHelper.debug("Core: Beginning dump of Insns for %s %s", mn.name, mn.desc);

		LogHelper.debug("================================================================================");
		LogHelper.log(Level.INFO, "");

		while (it.hasNext()){
			AbstractInsnNode node = it.next();
			String className = node.toString().split("@")[0];
			className = className.substring(className.lastIndexOf(".") + 1);
			if (node instanceof VarInsnNode){
				LogHelper.log(Level.INFO, opcodeToString(node.getOpcode()) + " " + ((VarInsnNode)node).var);
			}else if (node instanceof FieldInsnNode){
				LogHelper.log(Level.INFO, opcodeToString(node.getOpcode()) + " " + ((FieldInsnNode)node).owner + "/" + ((FieldInsnNode)node).name + " (" + ((FieldInsnNode)node).desc + ")");
			}else if (node instanceof MethodInsnNode){
				LogHelper.log(Level.INFO, opcodeToString(node.getOpcode()) + " " + ((MethodInsnNode)node).owner + "/" + ((MethodInsnNode)node).name + " " + ((MethodInsnNode)node).desc);
			}else{
				LogHelper.log(Level.INFO, className + " >> " + opcodeToString(node.getOpcode()));
			}
		}

		LogHelper.debug("================================================================================");
		LogHelper.debug("Core: End");
	}

	private String opcodeToString(int opcode){
		Field[] fields = Opcodes.class.getFields();
		for (Field f : fields){
			if (f.getType() == Integer.class || f.getType() == int.class){
				try{
					if (f.getInt(null) == opcode){
						return f.getName();
					}
				}catch (Throwable t){ /* Don't care */ }
			}
		}
		return "OPCODE_UNKNOWN";
	}

	public class obf_deobf_pair{
		private String deobf_val;
		private String obf_val;

		public obf_deobf_pair(){
			deobf_val = "";
			obf_val = "";
		}

		public void setVal(String value, boolean is_obfuscated){
			if (is_obfuscated){
				obf_val = value;
			} else{
				deobf_val = value;
			}
		}

		public String getVal(boolean is_obfuscated){
			if (is_obfuscated){
				return obf_val;
			}
			else{
				return deobf_val;
			}
		}
	}
}

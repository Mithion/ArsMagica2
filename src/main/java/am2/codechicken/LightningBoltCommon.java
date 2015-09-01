package am2.codechicken;

import am2.AMCore;
import am2.api.math.AMVector3;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLiving;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;


//import net.minecraftforge.ISpecialResistance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class LightningBoltCommon
{
  ArrayList segments;
  AMVector3 start;
  AMVector3 end;
  HashMap splitparents;
  public float multiplier;
  public float length;
  public int numsegments0;
  public int increment;
  public int type = 0;
  public boolean nonLethal = false;
  private int numsplits;
  private boolean finalized;
  private boolean canhittarget;
  private Random rand;
  public long seed;
  public int particleAge;
  public int particleMaxAge;
  private AxisAlignedBB boundingBox;
  private World world;
  public Entity wrapper;
  public int damage;

  public LightningBoltCommon(World world, AMVector3 jammervec, AMVector3 targetvec, long seed)
  {
    this.segments = new ArrayList();
    this.splitparents = new HashMap();
    this.canhittarget = true;
    this.world = world;
    this.start = jammervec;
    this.end = targetvec;
    this.seed = seed;
    this.rand = new Random(seed);
    this.numsegments0 = 1;
    this.increment = 1;
    this.length = this.end.copy().sub(this.start).length();
    this.particleMaxAge = 30;//(3 + this.rand.nextInt(3) - 1);
    this.multiplier = 1.0F;
    this.particleAge = (-(int)(this.length * 3.0F));
    this.boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    this.boundingBox.setBB(AxisAlignedBB.getBoundingBox(Math.min(this.start.x, this.end.x), Math.min(this.start.y, this.end.y), Math.min(this.start.z, this.end.z), Math.max(this.start.x, this.end.x), Math.max(this.start.y, this.end.y), Math.max(this.start.z, this.end.z)).expand(this.length / 2.0F, this.length / 2.0F, this.length / 2.0F));

    this.segments.add(new Segment(this.start, this.end));
  }

  public LightningBoltCommon(World world, Entity detonator, Entity target, long seed)
  {
    this(world, new AMVector3(detonator), new AMVector3(target), seed);
  }

  public LightningBoltCommon(World world, Entity detonator, Entity target, long seed, int speed)
  {
    this(world, new AMVector3(detonator), new AMVector3(target.posX, target.posY + target.getEyeHeight() - 0.699999988079071D, target.posZ), seed);
    this.increment = speed;
    this.multiplier = 0.4F;
  }

  public LightningBoltCommon(World world, TileEntity detonator, Entity target, long seed)
  {
    this(world, new AMVector3(detonator), new AMVector3(target), seed);
  }

  public LightningBoltCommon(World world, TileEntity detonator, double x, double y, double z, long seed)
  {
    this(world, new AMVector3(detonator), new AMVector3(x, y, z), seed);
  }

  public LightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi)
  {
    this(world, new AMVector3(x1, y1, z1), new AMVector3(x, y, z), seed);
    this.particleMaxAge = (duration + this.rand.nextInt(duration) - duration / 2);
    this.multiplier = multi;
  }

  public LightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed)
  {
    this(world, new AMVector3(x1, y1, z1), new AMVector3(x, y, z), seed);
    this.particleMaxAge = (duration + this.rand.nextInt(duration) - duration / 2);
    this.multiplier = multi;
    this.increment = speed;
  }

  public void setWrapper(Entity entity)
  {
    this.wrapper = entity;
  }

  public void setMultiplier(float m) {
    this.multiplier = m;
  }
  
  public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle)
  {
    if (this.finalized)
      return;
    ArrayList oldsegments = this.segments;
    this.segments = new ArrayList();
    Segment prev = null;
    for (Iterator iterator = oldsegments.iterator(); iterator.hasNext(); )
    {
      Segment segment = (Segment)iterator.next();
      prev = segment.prev;
      AMVector3 subsegment = segment.diff.copy().scale(1.0F / splits);
      BoltPoint[] newpoints = new BoltPoint[splits + 1];
      AMVector3 startpoint = segment.startpoint.point;
      newpoints[0] = segment.startpoint;
      newpoints[splits] = segment.endpoint;
      for (int i = 1; i < splits; i++)
      {
        AMVector3 randoff = AMVector3.getPerpendicular(segment.diff).rotate(this.rand.nextFloat() * 360.0F, segment.diff);
        randoff.scale((this.rand.nextFloat() - 0.5F) * amount);
        AMVector3 basepoint = startpoint.copy().add(subsegment.copy().scale(i));
        newpoints[i] = new BoltPoint(basepoint, randoff);
      }

      for (int i = 0; i < splits; i++)
      {
        Segment next = new Segment(newpoints[i], newpoints[(i + 1)], segment.light, segment.segmentno * splits + i, segment.splitno);
        next.prev = prev;
        if (prev != null)
          prev.next = next;
        if ((i != 0) && (this.rand.nextFloat() < splitchance))
        {
          AMVector3 splitrot = AMVector3.xCrossProduct(next.diff).rotate(this.rand.nextFloat() * 360.0F, next.diff);
          AMVector3 diff = next.diff.copy().rotate((this.rand.nextFloat() * 0.66F + 0.33F) * splitangle, splitrot).scale(splitlength);
          this.numsplits += 1;
          this.splitparents.put(Integer.valueOf(this.numsplits), Integer.valueOf(next.splitno));
          Segment split = new Segment(newpoints[i], new BoltPoint(newpoints[(i + 1)].basepoint, newpoints[(i + 1)].offsetvec.copy().add(diff)), segment.light / 2.0F, next.segmentno, this.numsplits);
          split.prev = prev;
          this.segments.add(split);
        }
        prev = next;
        this.segments.add(next);
      }

      if (segment.next != null) {
        segment.next.prev = prev;
      }
    }
    this.numsegments0 *= splits;
  }

  public void defaultFractal()
  {
	
    fractal(2, this.length * this.multiplier / 8.0F, 0.7F, 0.1F, 45.0F);
    fractal(2, this.length * this.multiplier / 12.0F, 0.5F, 0.1F, 50.0F);
    fractal(2, this.length * this.multiplier / 17.0F, 0.5F, 0.1F, 55.0F);
    fractal(2, this.length * this.multiplier / 23.0F, 0.5F, 0.1F, 60.0F);
    fractal(2, this.length * this.multiplier / 30.0F, 0.0F, 0.0F, 0.0F);
    fractal(2, this.length * this.multiplier / 34.0F, 0.0F, 0.0F, 0.0F);
    if (AMCore.config.LowGFX()){
    	fractal(2, this.length * this.multiplier / 40.0F, 0.0F, 0.0F, 0.0F);
    }
    
    if (AMCore.config.FullGFX()){
    	fractal(2, this.length * this.multiplier / 8.0F, 0.7F, 0.1F, 45.0F);
    	fractal(2, this.length * this.multiplier / 8.0F, 0.7F, 0.1F, 45.0F);
    }
  }
 
  private float rayTraceResistance(AMVector3 start, AMVector3 end, float prevresistance)
  {
	  MovingObjectPosition mop = this.world.rayTraceBlocks(start.toVec3D(), end.toVec3D());
    if (mop == null){
      return prevresistance;
    }
    if (mop.typeOfHit == MovingObjectType.BLOCK)
    {
      Block block = this.world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
      if (block == Blocks.air){
        return prevresistance;
      }
     /* if ((Block.blocksList[blockID] instanceof ISpecialResistance))
      {
        ISpecialResistance isr = (ISpecialResistance)Block.blocksList[blockID];
        return prevresistance + (isr.getSpecialExplosionResistance(this.world, mop.blockX, mop.blockY, mop.blockZ, start.x, start.y, start.z, this.wrapper) + 0.3F);
      }*/

      return prevresistance + (block.getExplosionResistance(this.wrapper) + 0.3F);
    }

    return prevresistance;
  }

  private void calculateCollisionAndDiffs()
  {
    HashMap lastactivesegment = new HashMap();
    Collections.sort(this.segments, new SegmentSorter());
    int lastsplitcalc = 0;
    int lastactiveseg = 0;
    float splitresistance = 0.0F;
    for (Iterator iterator = this.segments.iterator(); iterator.hasNext(); )
    {
      Segment segment = (Segment)iterator.next();
      if (segment.splitno > lastsplitcalc)
      {
        lastactivesegment.put(lastsplitcalc, lastactiveseg);
        lastsplitcalc = segment.splitno;
        lastactiveseg = ((Integer)lastactivesegment.get(this.splitparents.get(segment.splitno))).intValue();
        splitresistance = lastactiveseg >= segment.segmentno ? 0.0F : 50.0F;
      }
      if (splitresistance < 40.0F * segment.light)
      {
        lastactiveseg = segment.segmentno;
      }
    }

    lastactivesegment.put(lastsplitcalc, lastactiveseg);
    lastsplitcalc = 0;
    lastactiveseg = ((Integer)lastactivesegment.get(0)).intValue();
    Segment segment;
    for (Iterator iterator = this.segments.iterator(); iterator.hasNext(); segment.calcEndDiffs())
    {
      segment = (Segment)iterator.next();
      if (lastsplitcalc != segment.splitno)
      {
        lastsplitcalc = segment.splitno;
        lastactiveseg = ((Integer)lastactivesegment.get(segment.splitno)).intValue();
      }
      if (segment.segmentno > lastactiveseg) {
        iterator.remove();
      }
    }
    if (((Integer)lastactivesegment.get(0)).intValue() + 1 < this.numsegments0)
      this.canhittarget = false;
  }

  public void finalizeBolt()
  {
    if (this.finalized)
    {
      return;
    }

    this.finalized = true;
    calculateCollisionAndDiffs();
    Collections.sort(this.segments, new SegmentLightSorter());
  }

  public void onUpdate()
  {
    this.particleAge += this.increment;
    if (this.particleAge > this.particleMaxAge) this.particleAge = this.particleMaxAge;
  }

  public class SegmentSorter
    implements Comparator
  {
    final LightningBoltCommon this$0;

    public int compare(LightningBoltCommon.Segment o1, LightningBoltCommon.Segment o2)
    {
      int comp = Integer.valueOf(o1.splitno).compareTo(Integer.valueOf(o2.splitno));
      if (comp == 0) {
        return Integer.valueOf(o1.segmentno).compareTo(Integer.valueOf(o2.segmentno));
      }
      return comp;
    }

    public int compare(Object obj, Object obj1)
    {
      return compare((LightningBoltCommon.Segment)obj, (LightningBoltCommon.Segment)obj1);
    }

    public SegmentSorter()
    {
      this.this$0 = LightningBoltCommon.this;
    }
  }

  public class SegmentLightSorter
    implements Comparator
  {
    final LightningBoltCommon this$0;

    public int compare(LightningBoltCommon.Segment o1, LightningBoltCommon.Segment o2)
    {
      return Float.compare(o2.light, o1.light);
    }

    public int compare(Object obj, Object obj1)
    {
      return compare((LightningBoltCommon.Segment)obj, (LightningBoltCommon.Segment)obj1);
    }

    public SegmentLightSorter()
    {
      this.this$0 = LightningBoltCommon.this;
    }
  }

  public class Segment
  {
    public LightningBoltCommon.BoltPoint startpoint;
    public LightningBoltCommon.BoltPoint endpoint;
    public AMVector3 diff;
    public Segment prev;
    public Segment next;
    public AMVector3 nextdiff;
    public AMVector3 prevdiff;
    public float sinprev;
    public float sinnext;
    public float light;
    public int segmentno;
    public int splitno;
    //final LightningBoltCommon this$0;

    public void calcDiff()
    {
      this.diff = this.endpoint.point.copy().sub(this.startpoint.point);
    }

    public void calcEndDiffs()
    {
      if (this.prev != null)
      {
        AMVector3 prevdiffnorm = this.prev.diff.copy().normalize();
        AMVector3 thisdiffnorm = this.diff.copy().normalize();
        this.prevdiff = thisdiffnorm.add(prevdiffnorm).normalize();
        this.sinprev = (float)Math.sin(AMVector3.anglePreNorm(thisdiffnorm, prevdiffnorm.scale(-1.0F)) / 2.0F);
      }
      else {
        this.prevdiff = this.diff.copy().normalize();
        this.sinprev = 1.0F;
      }
      if (this.next != null)
      {
        AMVector3 nextdiffnorm = this.next.diff.copy().normalize();
        AMVector3 thisdiffnorm = this.diff.copy().normalize();
        this.nextdiff = thisdiffnorm.add(nextdiffnorm).normalize();
        this.sinnext = (float)Math.sin(AMVector3.anglePreNorm(thisdiffnorm, nextdiffnorm.scale(-1.0F)) / 2.0F);
      }
      else {
        this.nextdiff = this.diff.copy().normalize();
        this.sinnext = 1.0F;
      }
    }

    public String toString()
    {
      return this.startpoint.point.toString() + " " + this.endpoint.point.toString();
    }

    public Segment(LightningBoltCommon.BoltPoint start, LightningBoltCommon.BoltPoint end, float light, int segmentnumber, int splitnumber)
    {
      //this.this$0 = paramBoltPoint1;
      this.startpoint = start;
      this.endpoint = end;
      this.light = light;
      this.segmentno = segmentnumber;
      this.splitno = splitnumber;
      calcDiff();
    }

    public Segment(AMVector3 start, AMVector3 end)
    {
      this(new LightningBoltCommon.BoltPoint(start, new AMVector3(0.0D, 0.0D, 0.0D)), new LightningBoltCommon.BoltPoint(end, new AMVector3(0.0D, 0.0D, 0.0D)), 1.0F, 0, 0);
    }
  }

  public class BoltPoint
  {
    AMVector3 point;
    AMVector3 basepoint;
    AMVector3 offsetvec;
    //final LightningBoltCommon this$0;

    public BoltPoint(AMVector3 basepoint, AMVector3 offsetvec)
    {
      //this.this$0 = LightningBoltCommon.this;
      this.point = basepoint.copy().add(offsetvec);
      this.basepoint = basepoint;
      this.offsetvec = offsetvec;
    }
  }
}

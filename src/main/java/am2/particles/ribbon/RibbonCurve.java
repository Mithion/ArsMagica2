package am2.particles.ribbon;

import java.util.LinkedList;

import am2.api.math.AMVector3;
import am2.particles.AMParticleIcons;

public class RibbonCurve {
	float ribbonWidth;
    float resolution;		
    AMVector3 startPt, endPt, controlPt;
    int stepId;
    float ribbonColor = 0;
    LinkedList quads;

    RibbonCurve(AMVector3 pStartPt, AMVector3 pEndPt, AMVector3 pControlPt, float pwidth, float presolution, float pcolor){
        startPt = pStartPt;
        endPt = pEndPt;
        controlPt = pControlPt;
        resolution = presolution;
        ribbonWidth = pwidth;
        stepId = 0;
        ribbonColor = pcolor;
        quads = new LinkedList();
    }

    void draw(){
        int size = quads.size();         
        for (int i = 0; i < size; i++) {
            Quad3D q = (Quad3D) quads.get(i);
            q.draw();
        } 
    }

    void removeSegment(){
        if (quads.size() > 1) quads.removeFirst();
    }

    void addSegment()
    {
        float t =  stepId / resolution;							
        AMVector3 p0 = getOffsetPoint( t, 0 );			
        AMVector3 p3 = getOffsetPoint( t, ribbonWidth );

        stepId ++;
        if ( stepId > resolution) return;

        t =  stepId / resolution;	
        AMVector3 p1 = getOffsetPoint( t, 0 );			
        AMVector3 p2 = getOffsetPoint( t, ribbonWidth );

        Quad3D q = new Quad3D(p0,p1,p2,p3, AMParticleIcons.instance.getIconByName("symbols"));
        quads.add(q);
    }	

    /**
     * Given a bezier curve defined by 3 points, an offset distance (k) and a time (t), returns an AMVector3 
     */

    AMVector3 getOffsetPoint( float t, float k ){			
        AMVector3 p0 = startPt;
        AMVector3 p1 = controlPt;
        AMVector3 p2 = endPt;

        //-- x(t), y(t)
        float xt = ( 1 - t ) * ( 1 - t ) * p0.x + 2 * t * ( 1 - t ) * p1.x + t * t * p2.x;
        float yt = ( 1 - t ) * ( 1 - t ) * p0.y + 2 * t * ( 1 - t ) * p1.y + t * t * p2.y;
        float zt = ( 1 - t ) * ( 1 - t ) * p0.z + 2 * t * ( 1 - t ) * p1.z + t * t * p2.z;

        //-- x'(t), y'(t)
        float xd = t*(p0.x - 2*p1.x + p2.x) - p0.x + p1.x;
        float yd = t*(p0.y - 2*p1.y + p2.y) - p0.y + p1.y;
        float zd = t*(p0.z - 2*p1.z + p2.z) - p0.z + p1.z;
        float dd = (float) Math.pow( xd * xd + yd * yd + zd * zd,1/3); 

        return new AMVector3(xt + ( k * yd ) / dd, yt - ( k * xd ) / dd, zt - ( k * xd ) / dd);

    }	
}

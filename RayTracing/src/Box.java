package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple box class. A box is defined by it's lower (@see min) and upper (@see max) corner. 
 */
public class Box extends Intersectable {

	public Point3d max;
	public Point3d min;
	
    /**
     * Default constructor. Creates a 2x2x2 box centered at (0,0,0)
     */
    public Box() {
    	super();
    	this.max = new Point3d( 1, 1, 1 );
    	this.min = new Point3d( -1, -1, -1 );
    }	

	@Override
	public void intersect(Ray ray, IntersectResult result) {
		// TODO: Objective 6: intersection of Ray with axis aligned box
		Vector3d dir = ray.viewDirection;
		Point3d e= ray.eyePoint;
		Vector3d tmin = new Vector3d();
		tmin.sub(min,e);
		Vector3d tmax = new Vector3d();
		tmax.sub(max,e);
		tmin.x /= dir.x;
		tmin.y /= dir.y;
		tmin.z /= dir.z;
		tmax.x /= dir.x;
		tmax.y /= dir.y;
		tmax.z /= dir.z;
		boolean xflip = false;
		boolean yflip = false;
		boolean zflip = false;
		if(tmin.x > tmax.x){
			double tmp = tmin.x;
			tmin.x = tmax.x;
			tmax.x = tmp;
			xflip  = true;
		}
		if(tmin.y > tmax.y){
			double tmp = tmin.y;
			tmin.y = tmax.y;
			tmax.y = tmp;
			yflip  = true;
		}
		if(tmin.z > tmax.z){
			double tmp = tmin.z;
			tmin.z = tmax.z;
			tmax.z = tmp;
			zflip  = true;
		}
		double tMin = Math.max(tmin.x, Math.max(tmin.y, tmin.z));
		double tMax = Math.min(tmax.x, Math.min(tmax.y, tmax.z));
		if(tMin < tMax && tMin > 1e-9 && tMin < result.t){
			result.t = tMin;
			result.p.scaleAdd(tMin, dir, e);
			result.material = this.material;
			if(tmin.x > Math.max(tmin.y, tmin.z)){
				if(!xflip){
					result.n.set(-1,0,0);
				}else
					result.n.set(1,0,0);
			}else if(tmin.y > Math.max(tmin.x, tmin.z)){
				if(!yflip){
					result.n.set(0,-1,0);
				}else
					result.n.set(0,1,0);
			}else{
				if(!zflip){
					result.n.set(0,0,-1);
				}else
					result.n.set(0, 0, 1);
			}

		}
		
	}	
}

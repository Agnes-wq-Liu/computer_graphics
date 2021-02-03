package comp557.a4;

import javax.vecmath.Vector3d;
import javax.vecmath.Point3d;
/**
 * Class for a plane at y=0.
 * 
 * This surface can have two materials.  If both are defined, a 1x1 tile checker 
 * board pattern should be generated on the plane using the two materials.
 */
public class Plane extends Intersectable {
    
	/** The second material, if non-null is used to produce a checker board pattern. */
	Material material2;
	
	/** The plane normal is the y direction */
	public static final Vector3d n = new Vector3d( 0, 1, 0 );
    
    /**
     * Default constructor
     */
    public Plane() {
    	super();
    }

        
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
        // TODO: Objective 4: intersection of ray with plane
    	Vector3d dir = ray.viewDirection;
    	Point3d e = ray.eyePoint;
//    	exclude case when plane is parallel to ray viewing direction
    	if(n.dot(dir) != 0) {
//    		t = (p0-e)dot(n)/((dir)dot(n))
            Vector3d vector = new Vector3d(0,0,0);
            vector.sub(e);
            double t = (n.dot(vector)/n.dot(dir));
            
//          update result
//          p on plane = e+dir*t
            if (t > 1e-9 && t < result.t) { 
                result.t = t;
                double px = e.x + t*dir.x;
                double py = e.y + t*dir.y;
                double pz = e.z + t*dir.z;
                result.p.set(new Point3d(px,py,pz));
                result.n.set(n);
                result.material = this.material;
                if(material2 != null) {
                    int x = (int)Math.abs(Math.floor(px));
                    int z = (int)Math.abs(Math.floor(pz));
                    if((x+z)%2 != 0)
                        result.material = this.material2;

                }
            }  
    	}
    }
    
}

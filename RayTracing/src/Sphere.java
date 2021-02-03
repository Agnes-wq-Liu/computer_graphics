package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
/**
 * A simple sphere class.
 */
public class Sphere extends Intersectable {
    
	/** Radius of the sphere. */
	public double radius = 1;
    
	/** Location of the sphere center. */
	public Point3d center = new Point3d( 0, 0, 0 );
    
    /**
     * Default constructor
     */
    public Sphere() {
    	super();
    }
    
    /**
     * Creates a sphere with the request radius and center. 
     * 
     * @param radius
     * @param center
     * @param material
     */
    public Sphere( double radius, Point3d center, Material material ) {
    	super();
    	this.radius = radius;
    	this.center = center;
    	this.material = material;
    }
    
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
        // TODO: Objective 2: intersection of ray with sphere
    	//set the normal n, pos p, param t (material)
    	Point3d e = ray.eyePoint;
    	Vector3d dir = ray.viewDirection;
    	Vector3d ec = new Vector3d();
    	ec.sub(e,this.center);
    	double b = dir.dot(ec);
    	double ac = dir.lengthSquared()*(ec.lengthSquared()-Math.pow(this.radius, 2));
    	double discrim = Math.pow(b, 2)-ac;
    	double root = (-b-Math.sqrt(discrim))/(dir.lengthSquared());
    	if (discrim >=0.0) {
    		if (root>0 && root < result.t) {
	    		result.t = root;
	    		result.material = this.material;
	    		ray.getPoint( result.t, result.p);
	    		Vector3d norm = new Vector3d();
	    		result.n.sub(result.p,this.center);
	    		result.n.normalize();
    		}
    	} 	       	

    }  
}

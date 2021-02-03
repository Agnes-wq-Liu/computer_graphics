package comp557.a4;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector4d;

public class Quadric extends Intersectable {
    
	/**
	 * Radius of the sphere.
	 */
	public Matrix4d Q = new Matrix4d();
	public Matrix3d A = new Matrix3d();
	public Vector3d B = new Vector3d();
	public double C;
	public Point3d center = new Point3d();
	
	/**
	 * The second material, e.g., for front and back?
	 */
	Material material2 = null;
	
	public Quadric() {
	
	}
	public Quadric(Matrix4d q, Matrix3d a, Vector3d b, double c, Point3d center) {
		this.Q = q;
		this.A = a;
		this.B = b;
		this.C = c;
		this.center = center;
	}
	@Override
	public void intersect(Ray ray, IntersectResult result) {
		Point3d e = ray.eyePoint;
		Vector3d dir =ray.viewDirection;
		Matrix4d u = new Matrix4d();
		Matrix4d p = new Matrix4d();
		//make a matrix from direction vector
		u.setColumn(0, dir.x,dir.y,dir.z,0);
		u.setColumn(1,0,0,0,0);
		u.setColumn(2,0,0,0,0);
		u.setColumn(3,0,0,0,0);
		//matrix from eyepoint
		p.setColumn(0,e.x,e.y,e.z,1);
		p.setColumn(1,0,0,0,0);
		p.setColumn(2,0,0,0,0);
		p.setColumn(3,0,0,0,0);
		//get u transpose, p transpose
		Matrix4d ut = new Matrix4d(u);
		ut.transpose();
		Matrix4d pt = new Matrix4d(p);
		pt.transpose();
		Matrix4d utQp = new Matrix4d();
		utQp.mul(this.Q,p);
		utQp.mul(ut,utQp);
		double b = utQp.m00;
		
		Matrix4d utQu = new Matrix4d();
		utQu.mul(this.Q,u);
		utQu.mul(ut,utQu);
		double a = utQu.m00;
		
		Matrix4d ptQp = new Matrix4d();
		ptQp.mul(this.Q,p);
		ptQp.mul(pt,ptQp);
		double c = ptQp.m00;
    	double ac = a*c;
    	double discrim = Math.pow(b, 2)-ac;
    	double root = (-b-Math.sqrt(discrim))/a;
		if (discrim >=0.0) {
    		if (root < result.t) {
	    		result.t = root;
	    		result.material = this.material;
	    		ray.getPoint( result.t, result.p);
//	    		calculate point o intersection
	    		Matrix3d x = new Matrix3d();
	    		Vector3d xcoord = new Vector3d(dir);
	    		xcoord.scaleAdd(root, e);
	    		x.setColumn(0,xcoord);
	    		x.setColumn(1,0,0,0);
	    		x.setColumn(2,0,0,0);
	    		x.mul(this.A,x);
	    		x.mul(2.0);
	    		Vector3d newvec = new Vector3d(x.m00,x.m10,x.m20);
	    		Vector3d btimes2 = new Vector3d(this.B.x*2,this.B.y*2,this.B.z*2);
	    		newvec.sub(btimes2);
	    		result.n.set(newvec);
	    		result.n.normalize();
    		}
    	} 	       	
  
	}
	
}

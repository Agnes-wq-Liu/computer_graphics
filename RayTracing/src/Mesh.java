package comp557.a4;

import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
public class Mesh extends Intersectable {
	
	/** Static map storing all meshes by name */
	public static Map<String,Mesh> meshMap = new HashMap<String,Mesh>();
	
	/**  Name for this mesh, to allow re-use of a polygon soup across Mesh objects */
	public String name = "";
	
	/**
	 * The polygon soup.
	 */
	public PolygonSoup soup;

	public Mesh() {
		super();
		this.soup = null;
	}			
		
	@Override
	public void intersect(Ray ray, IntersectResult result) {
		Point3d e = ray.eyePoint;
		Vector3d dir = ray.viewDirection;
		Vector3d e1 = new Vector3d();
		Vector3d e2 = new Vector3d();
		Vector3d pVec = new Vector3d();
		Vector3d tVec = new Vector3d();
		Vector3d qVec = new Vector3d();
		float t = -1;

		for(int[] face : soup.faceList) { 
			//compute t for each face 
			Point3d p0 = soup.vertexList.get(face[0]).p;
			Point3d p1 = soup.vertexList.get(face[1]).p;
			Point3d p2 = soup.vertexList.get(face[2]).p;
			e1.sub(p1, p0);
			e2.sub(p2, p0);
			pVec.cross(dir, e2);
			float det = (float) e1.dot(pVec);
			if (det < 1e-9) { 
				e1.sub(p2, p0);
				e2.sub(p1, p0);
			}
			if (det > 1e-9) { 
				float dinv = 1.0f/det;
				tVec.sub(e, p0);
				float u = (float)tVec.dot(pVec) * dinv;
				if (u > 0 && u < 1) {
					qVec.cross(tVec, e1);
					float v = (float) dir.dot(qVec) * dinv;
					if (v > 0 && (u+v) < 1)
						t = (float) e2.dot(qVec) * dinv;
				}
			}
			//if current intersection is closer, update intersect result
			if (t > 1e-9 && t < result.t) { 
				result.t = t;
				Point3d p = new Point3d(e.x + t*dir.x,e.y + t*dir.y,e.z + t*dir.z);
				result.p.set(p);

				Vector3d n = new Vector3d();
				n.cross(e1,e2);
				result.n.set(n);

				result.material = this.material;
			}
		}
	}

}

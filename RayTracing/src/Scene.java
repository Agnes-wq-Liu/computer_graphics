package comp557.a4;

import java.util.*;
import javax.vecmath.*;

/**
 * Simple scene loader based on XML file format.
 */
public class Scene {

    /**
     * List of surfaces in the scene
     */
    public List<Intersectable> surfaceList = new ArrayList<Intersectable>();

    /**
     * All scene lights
     */
    public Map<String, Light> lights = new HashMap<String, Light>();

    /**
     * Contains information about how to render the scene
     */
    public Render render;

    /**
     * The ambient light colour
     */
    public Color3f ambient = new Color3f();
    
    public static double epsilon = 0.0001;
    //reflection/refraction offset
    /**
     * Default constructor.
     */
    public Scene() {
        this.render = new Render();
    }

    /**
     * renders the scene
     */
    public void render(boolean showPanel) {

        Camera cam = render.camera;
        int w = cam.imageSize.width;
        int h = cam.imageSize.height;

        render.init(w, h, showPanel);

        for (int j = 0; j < h && !render.isDone(); j++) {
            for (int i = 0; i < w && !render.isDone(); i++) {
                Vector3f sampleColor = new Vector3f();
                Ray ray = new Ray();               
                int numSample = (int) Math.sqrt(render.samples);               
                for (int k = 0; k < numSample; k++) {
                    for (int l = 0; l < numSample; l++) {
                    	// TODO: Objective 1: generate a ray (use the generateRay method)
                        double[] offset = new double[2];
                        if (render.jitter)
                        	Arrays.fill(offset, new Random().nextDouble()*0.5);
                        else
                        	Arrays.fill(offset, 0.5);
                        generateRay(j, i, offset, cam, ray);
                        
                        // TODO: Objective 2: test for intersection with scene surfaces
                        IntersectResult result = new IntersectResult();
                        for (Intersectable surface : surfaceList) { //surface.intersect(ray, result);
                        	surface.intersect(ray, result);
                        // TODO: Objective 3: compute the shaded result for the intersection point (perhaps requiring shadow rays)
	                        if (result.t != Double.POSITIVE_INFINITY) {
	                            Color3f color = new Color3f(ambient);
	                            for (Light light : lights.values()) {	                           
	                                Color3f meanLight = new Color3f();
	                                boolean inShadow = inShadow(result, light, surfaceList, new IntersectResult(), new Ray());
	                                if (!inShadow) {
		                                Vector3d lightDirection = PointSub(result.p, light.from);
		                                Vector3d viewDirection = PointSub(result.p, ray.eyePoint);
		                                Vector3d bisector = VectorSub(new Vector3d(), lightDirection, viewDirection);
		                                double ld = Math.max(0, lightDirection.dot(result.n)) * light.power;
		                                double ls = Math.pow(Math.max(0, bisector.dot(result.n)), result.material.shinyness) * light.power;
		                                //light source type
		                                if (light.type.equals("area"))
		                                	color.add(AreaLight(color, surface, result, light, lightDirection, ray, 3));
		                                else {
		                                	meanLight.add(colorCalc(light.color, ls, result.material.specular));
			                                meanLight.add(colorCalc(light.color, ld, result.material.diffuse));
			                                color.add(meanLight);
		                                }
		                                //reflection & refraction
		                                if(result.material.mirror) {		                            
		                                	getReflection(ray, result, light, color, surfaceList, lightDirection, bisector);    
		                                }	
		                                if(result.material.refracN!=0) {
		                                	getRefraction(ray, result,light, color, surfaceList, lightDirection, bisector);
		                                }
                                	}else {
                                		if(light.type.contentEquals("area")) {
                                			Vector3d lightDirection = PointSub(result.p, light.from);
    		                                Vector3d viewDirection = PointSub(result.p, ray.eyePoint);    		                                  	                               
    		                                color.add(AreaLight(color, surface, result, light, lightDirection, ray, 3));
                                		}
                                	}
                                }
	                            color.clamp(0, 1);
	                            sampleColor.add(color);
	                                
	                        }else sampleColor.add(new Color3f(render.bgcolor.x,render.bgcolor.y,render.bgcolor.z));
	                   }
                    }
                }
                
                sampleColor.scale(1.f / render.samples);

                // Here is an example of how to calculate the pixel value.
                int r = (int) (255 * sampleColor.x);
                int g = (int) (255 * sampleColor.y);
                int b = (int) (255 * sampleColor.z);
                int a = 255;
                int argb = (a << 24 | r << 16 | g << 8 | b);

                // update the render image
                render.setPixel(i, j, argb);
            }
        }

        // save the final render image
        render.save();

        // wait for render viewer to close
        render.waitDone();
    }
    private Color3f AreaLight(Color3f c, Intersectable surface, IntersectResult result, Light light, Vector3d lightDir, Ray ray, int jitter){
    	Vector3d normToLightPlane = new Vector3d(lightDir);
    	normToLightPlane.normalize();
    	//calculate the plane directions
    	Vector3d u = new Vector3d();
    	u.cross(normToLightPlane,ray.viewDirection);
    	u.normalize();
    	Vector3d v = new Vector3d();
    	v.cross(u,normToLightPlane);
    	
    	Color3f[] r = new Color3f[jitter*jitter];
    	Color3f[] s = new Color3f[jitter*jitter];
    	boolean[] inS = new boolean[jitter*jitter];
    	for (int i =0;i<jitter;i++) {
    		for(int j=0;j<jitter;j++) {
    			double ep1 = i/jitter;
    			double ep2 = j/jitter;
    			Light lightPt = new Light(light);
    			lightPt.from.scaleAdd(ep1, u, lightPt.from);
    			lightPt.from.scaleAdd(ep2,v,lightPt.from);
    			
    			Vector3d lightDirection = PointSub(result.p, lightPt.from);
                Vector3d viewDirection = PointSub(result.p, ray.eyePoint);
                Vector3d bisector = VectorSub(new Vector3d(), lightDirection, viewDirection);
                inS[j*jitter+i] = inShadow(result, lightPt, surfaceList, new IntersectResult(), new Ray());
                if(result.material!=null) {
                	double ld = Math.max(0, lightDirection.dot(result.n)) * light.power;
	                double ls = Math.pow(Math.max(0, bisector.dot(result.n)), result.material.shinyness) * light.power;	   
                	if(!inS[j*jitter+i]) {		               
		                r[(j)*jitter+i] = colorCalc(light.color, ld, result.material.diffuse);
		                r[j*jitter+i].add(colorCalc(light.color, ls, result.material.specular));
		                s[(j)*jitter+i] = colorCalc(ambient, ls, result.material.specular);
		                s[(j)*jitter+i].add(colorCalc(ambient, ld, result.material.diffuse));
                	}
                	else {
                		s[(j)*jitter+i] = colorCalc(light.color, ls, ambient);
    	                s[(j)*jitter+i].add(colorCalc(light.color, ld, ambient));
    	                r[j*jitter+i] = new Color3f (ambient);
                	}
                }else {  
	                r[(j-1)*jitter+i] = new Color3f(0,0,0);
	                s[(j-1)*jitter+i] = new Color3f(0,0,0);
                }
    		}
    	}
    	//shuffle
    	for(int i=jitter*jitter-1;i>=0;i--) {
    		int j = (int)(Math.random() * i);
    		Color3f tmp = new Color3f(s[j]);
    		s[j].set(s[i]);
    		s[i].set(tmp);
    	}
    	Color3f mean = new Color3f();
    	for (int i=0;i<jitter*jitter;i++) {
    		mean.add(r[i]);	
    		mean.sub(s[i]);
    	}
    	mean.scale((float)(1/Math.pow(jitter, 2)));
    	c.add(mean);
    	return c;
    }
    private void getReflection(Ray ray, IntersectResult result, Light light, Color3f color, List<Intersectable> surfaceList, Vector3d lDir, Vector3d bisector) {
		//first use my ray and result for reflectRay's viewDir
		Vector3d incDir = new Vector3d (ray.viewDirection);
		Vector3d refNorm = new Vector3d(result.n);
		refNorm.scale(refNorm.dot(incDir)*2);
		incDir.sub(refNorm);
		//make new incident ray from offsetted incident point
		Point3d startPt = new Point3d();
		startPt.scaleAdd(epsilon,incDir,result.p);
		Ray incRay = new Ray(startPt,incDir);
		for (Intersectable surf: surfaceList) {
			IntersectResult tmpResult = new IntersectResult();
			surf.intersect(incRay, tmpResult);
		
			if (tmpResult.material!=null) {                         			
				double ld = Math.max(0, lDir.dot(tmpResult.n)) * light.power;
	            color.add(colorCalc(color, ld, tmpResult.material.diffuse));
	            double ls = Math.pow(Math.max(0, bisector.dot(result.n)), tmpResult.material.shinyness) * light.power;
	            color.add(colorCalc(color, ls, tmpResult.material.specular));
			}
		}
    }
    private void getRefraction(Ray ray, IntersectResult result, Light light, Color3f color, List<Intersectable> surfaceList, Vector3d lDir, Vector3d bisector) {
	    Vector3d incDir = new Vector3d (ray.viewDirection);
		Vector3d tmp = new Vector3d(incDir);
		tmp.dot(result.n);
		Matrix3d tmpright = new Matrix3d();
		tmpright.setRow(0,tmp.x,tmp.y,tmp.z);
		Matrix3d tmpleft = new Matrix3d();
		tmpleft.setColumn(0,result.n.x,result.n.y,result.n.z);
		tmpleft.mul(tmpleft,tmpright);
		tmp.sub(incDir, new Vector3d(tmpleft.m00,tmpleft.m10,tmpleft.m20));
		tmp.scale((double)result.material.refracN);
		tmp.scale((double)(1/result.material.refracNt));
		double product = incDir.dot(result.n);
		product = 1-(Math.pow(result.material.refracN, 2)*(1-Math.pow(product,2)));
		product = result.material.refracN*Math.sqrt(product);
		tmp = new Vector3d(tmp.x-product,tmp.y-product,tmp.z-product);
	//                                	tmp is the vector for refraction ray
		Point3d startPt = new Point3d();
		startPt.scaleAdd(epsilon,incDir,result.p);
		Ray refRay = new Ray(startPt,tmp);
		for (Intersectable surf: surfaceList) {
			IntersectResult tmpResult = new IntersectResult();
			surf.intersect(refRay, tmpResult);
			if (tmpResult.material!=null) {
				double ld = Math.max(0, lDir.dot(tmpResult.n)) * light.power;
	            color.add(colorCalc(color, ld, tmpResult.material.diffuse));
	            double ls = Math.pow(Math.max(0, bisector.dot(result.n)), tmpResult.material.shinyness) * light.power;
	            color.add(colorCalc(color, ls, tmpResult.material.specular));  
			}   
		}
	}
    private Color3f colorCalc(Color3f light, double scale, Color3f c) {
    	Color3f color = new Color3f();
    	color.scale((float) scale, light);
    	color.x *= c.x;
    	color.y *= c.y;
    	color.z *= c.z;
        return color;
	}
    
    private static Vector3d VectorSub(Vector3d sub, Vector3d ... vector3ds) {
		Vector3d vector3d = sub;
		vector3d.negate();
		for(Vector3d vector: vector3ds) vector3d.add(vector);
		vector3d.normalize();
		return vector3d;
	}
    
    private static Vector3d PointSub(Point3d sub, Point3d ... point3ds) {
    	Vector3d vector3d = new Vector3d(sub);
    	vector3d.negate();
		for(Point3d point: point3ds) vector3d.add(point);
		vector3d.normalize();
		return vector3d;
	}
    
    
    /**
     * Generate a ray through pixel (i,j).
     * 
     * @param i The pixel row.
     * @param j The pixel column.
     * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
     * @param cam The camera.
     * @param ray Contains the generated ray.
     */
	public static void generateRay(final int i, final int j, final double[] offset, final Camera cam, Ray ray) {
		
		// TODO: Objective 1: generate rays given the provided parmeters
		//viewing axis w
		Vector3d w = new Vector3d (cam.from.x-cam.to.x,cam.from.y-cam.to.y,cam.from.z-cam.to.z);
		w.normalize();
		Vector3d u = new Vector3d();
		u.cross(cam.up,w);
		u.normalize();
		Vector3d v = new Vector3d();
		v.cross(u, w); //normalization no longer needed
		
		double d = 1;
		double ratio = cam.imageSize.getWidth() / cam.imageSize.getHeight();
	    double t = Math.abs(Math.tan(Math.toRadians(0.5*cam.fovy)) * d);
	    double b = -t; 
	    double r = t * ratio;
	    double l = b * ratio;
	    double width = cam.imageSize.getWidth();
	    double height = cam.imageSize.getHeight();
	
	    double upos = l + (r-l)*(j + offset[1])/width;
	    double vpos = b + (t-b)*(i + offset[0])/height;
	    //view dir = -w
	    double dirX =  ((upos*u.x) + (vpos*v.x) - (d*w.x));
	    double dirY =  ((upos*u.y) + (vpos*v.y) - (d*w.y));
	    double dirZ =  ((upos*u.z) + (vpos*v.z) - (d*w.z));
	    
	    Vector3d dir = new Vector3d(dirX,dirY,dirZ);
	    
	    dir.normalize();
	    ray.set(cam.from,dir);
		
	}

	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param root The scene node
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	public static boolean inShadow(final IntersectResult result, final Light light, final List<Intersectable> surfaceList, IntersectResult shadowResult, Ray shadowRay) {
		
		// TODO: Objective 5: check for shdows and use it in your lighting computation
		Vector3d d = new Vector3d();
		//get direction from light origin and intersection position
        d.sub(light.from, result.p);
        d.normalize();
        	
        Point3d point = new Point3d(d);
        point.scaleAdd(1e-9, result.p);

        shadowRay.set(point, d);
        //check for occlusion
        for (Intersectable surface : surfaceList) {
            surface.intersect(shadowRay, shadowResult);
            if( shadowResult.t > 1e-9 && shadowResult.t != Double.POSITIVE_INFINITY) { 
            	Vector3d lenLight = new Vector3d ();
            	lenLight.sub(light.from,point);
            	lenLight.lengthSquared();
            	double l1 = Math.sqrt(lenLight.x+lenLight.y+lenLight.z);
            	
            	Vector3d lenShadow = new Vector3d ();
            	lenShadow.sub(shadowResult.p,point);
            	lenShadow.lengthSquared();
                double l2 = Math.sqrt(lenShadow.x+lenShadow.y+lenShadow.z);
                
                if(l2 < l1)
                    return true;
            }
        }
		return false;
	}    

}

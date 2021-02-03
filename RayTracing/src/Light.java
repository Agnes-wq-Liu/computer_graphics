package comp557.a4;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

public class Light {
	
	/** Light name */
    public String name = "";
    
    /** Light colour, default is white */
    public Color3f color = new Color3f(1,1,1);
    
    /** Light position, default is the origin */
    public Point3d from = new Point3d(0,0,0);
    
    /** Light intensity, I, combined with colour is used in shading */
    public double power = 1.0;
    
    /** Type of light, default is a point light */
    public String type = "point";

    /**
     * Default constructor 
     */
    public Light() {
    	// do nothing
    }
    public Light(Light other) {
    	this.color = other.color;
    	this.from = other.from;
    	this.power = other.power;
    	this.type = other.type;
    }
}

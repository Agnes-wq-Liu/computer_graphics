package comp557.a4;

import java.util.HashMap;

import java.util.Map;

import javax.vecmath.Color3f;

/**
 * A class defining the material properties of a surface, 
 * such as colour and specularity. 
 */
public class Material {
	
	/** Static member to access all the materials */
	public static Map<String,Material> materialMap = new HashMap<String,Material>();
	
	/** Material name */
    public String name = "";
    
    /** Diffuse colour, defaults to white */
    public Color3f diffuse = new Color3f(1,1,1);
    
    /** Specular colour, default to black (no specular highlight) */
    public Color3f specular = new Color3f(0,0,0);
    
    /** Specular hardness, or exponent, default to a reasonable value */ 
    public float shinyness = 64;
 
    public boolean mirror = false;
    public float refracN = 0;
    public float refracNt = 0;
    /**
     * Default constructor
     */
    public Material() {
    	// do nothing
    }
    
}

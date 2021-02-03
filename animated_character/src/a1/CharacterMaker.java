package comp557.a1;
//Agnes Liu 260713093
import javax.swing.JTextField;
import mintools.parameters.BooleanParameter;
import com.jogamp.opengl.GLAutoDrawable;

import javax.vecmath.*;

public class CharacterMaker {

	static public String name = "CHARACTER NAME - YOUR NAME AND STUDENT NUMBER";
	
	// TODO: Objective 8: change default of load from file to true once you start working with xml
//	static BooleanParameter loadFromFile = new BooleanParameter( "Load from file (otherwise by procedure)", false );
	static BooleanParameter loadFromFile = new BooleanParameter( "Load from file (otherwise by procedure)", true );
	static JTextField baseFileName = new JTextField("data/a1data/character");
	
	/**
	 * Creates a character, either procedurally, or by loading from an xml file
	 * @return root node
	 */
	
	static public GraphNode create() {
		
		if ( loadFromFile.getValue() ) {
			// TODO: Objectives 6: create your character in the character.xml file 
			return CharacterFromXML.load( baseFileName.getText() + ".xml");
		} else {
			// TODO: Objective 3,4,5,6: test DAG nodes by creating a small DAG in the CharacterMaker.create() method 
			
			// Use this for testing, but ultimately it will be more interesting
			// to create your character with an xml description (see example).
			
			// Here we just return null, which will not be very interesting, so write
			// some code to create a test or partial character and return the root node.

			FreeJoint fj = new FreeJoint("test1");
			SphericalJoint sph = new SphericalJoint("test2");
			Vector3d postion_sph = new Vector3d(1,1,1);
			sph.setPosition(postion_sph);
			fj.add(sph);

			RotaryJoint rot = new RotaryJoint("test3");
			Vector3d postion_rot = new Vector3d(1,2,1);
			rot.setPosition(postion_rot);
			Vector3d postion_axis = new Vector3d(0,1,0);
			rot.setAxis(postion_axis);
			sph.add(rot);
			return fj;
		}
	}
}

package bunwarpj;


import ij.IJ;
import ij.ImageJ;

public class TestGUI {

	/**
	 * Main method to test and debug the bUnwarpJ GUI
	 *  
	 * @param args
	 */
	public static void main( final String[] args )
	{		
		ImageJ.main( args );

		IJ.open( TestGUI.class.getResource( "/files/bridge.png" ).getFile() );
		IJ.open( TestGUI.class.getResource( "/files/deformed-bridge.png" ).getFile() );

		new bUnwarpJ_().run( null );
	}
}

package bunwarpj;

/**
 * bUnwarpJ plugin for ImageJ(C).
 * Copyright (C) 2005-2010 Ignacio Arganda-Carreras and Jan Kybic 
 *
 * More information at http://biocomp.cnb.csic.es/%7Eiarganda/bUnwarpJ/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation (http://www.gnu.org/licenses/gpl.txt )
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

import ij.IJ;
import ij.ImagePlus;
import ij.io.OpenDialog;
import ij.io.SaveDialog;
import ij.plugin.frame.Recorder;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JOptionPane;
/**
 * Class to create the Input/Output dialog to deal with the files 
 * to keep the information of bUnwarpJ.
 */
public class IODialog extends Dialog implements ActionListener
{ /* begin class IODialog */

	// Macro recording constants (corresponding to
	// static method names to be called)
	/** bUnwarpJ_ method name to evaluate similarity between two images */
	public static final String EVALUATE_SIMILARITY = "evaluateImageSimilarity";
	/** bUnwarpJ_ method name to load landmarks from file */
	public static final String LOAD_LANDMARKS = "loadLandmarks";
	/** bUnwarpJ_ method name to save landmarks to a file */
	public static final String SAVE_LANDMARKS = "saveLandmarks";
	/** bUnwarpJ_ method name to show landmarks on a table */
	public static final String SHOW_LANDMARKS = "showLandmarks";
	/** bUnwarpJ_ method name to load an elastic transform */
	public static final String LOAD_ELASTIC_TRANSF = "loadElasticTransform";
	/** bUnwarpJ_ method name to load a raw transform */
	public static final String LOAD_RAW_TRANSF = "loadRawTransform";
	/** bUnwarpJ_ method name to compare opposite elastic transforms */
	public static final String COMPARE_OPPOSITE_ELASTIC = "compareOppositeElasticTransforms";
	/** bUnwarpJ_ method name to compare elastic and raw transforms */
	public static final String COMPARE_ELASTIC_RAW = "compareElasticRawTransforms";
	/** bUnwarpJ_ method name to compare raw transforms */
	public static final String COMPARE_RAW = "compareRawTransforms";
	/** bUnwarpJ_ method name to convert elastic transform to raw format */
	public static final String CONVERT_TO_RAW = "convertToRaw";
	/** bUnwarpJ_ method name to convert raw transform to B-spline format */
	public static final String CONVERT_TO_ELASTIC = "convertToElastic";
	/** bUnwarpJ_ method name to compose elastic transforms */
	public static final String COMPOSE_ELASTIC = "composeElasticTransforms";
	/** bUnwarpJ_ method name to compose raw transforms */
	public static final String COMPOSE_RAW = "composeRawTransforms";
	/** bUnwarpJ_ method name to compose raw and elastic transforms */
	public static final String COMPOSE_RAW_ELASTIC = "composeRawElasticTransforms";

	/*....................................................................
       Private variables
    ....................................................................*/
	
	/** Generated serial version UID */
	private static final long serialVersionUID = 2016840469406208859L;
	/** Pointer to the source image representation */
	private ImagePlus sourceImp;
	/** Pointer to the target image representation */
	private ImagePlus targetImp;
	/** Point handler for the source image */
	private PointHandler sourcePh;
	/** Point handler for the target image */
	private PointHandler targetPh;
	/** Dialog for bUnwarpJ interface */
	private MainDialog       dialog;

	/*....................................................................
       Public methods
    ....................................................................*/

	/*------------------------------------------------------------------*/
	/**
	 * Create a new instance of IODialog.
	 *
	 * @param parentWindow pointer to the parent window
	 * @param sourceImp pointer to the source image representation
	 * @param targetImp pointer to the target image representation
	 * @param sourcePh point handler for the source image
	 * @param targetPh point handler for the source image
	 * @param dialog dialog for bUnwarpJ interface
	 */
	public IODialog (
			final Frame parentWindow,
			final ImagePlus sourceImp,
			final ImagePlus targetImp,
			final PointHandler sourcePh,
			final PointHandler targetPh,
			final MainDialog       dialog)
	{
		super(parentWindow, "I/O Menu", true);
		this.sourceImp = sourceImp;
		this.targetImp = targetImp;
		this.sourcePh = sourcePh;
		this.targetPh = targetPh;
		this.dialog   = dialog;
		setLayout(new GridLayout(0, 1));

		final Button saveAsButton = new Button("Save Landmarks As...");
		final Button loadButton = new Button("Load Landmarks...");
		final Button show_PointsButton = new Button("Show Landmarks");
		final Button loadTransfButton = new Button("Load Elastic Transformation");
		final Button loadRawTransfButton = new Button("Load Raw Transformation");
		final Button compareOppositeTransfButton = new Button("Compare Opposite Elastic Transformations");
		final Button compareElasticRawTransfButton = new Button("Compare Elastic/Raw Transformations");
		final Button compareRawButton = new Button("Compare Raw Transformations");
		final Button convertToRawButton = new Button("Convert Transformation To Raw");
		final Button convertToElasticButton = new Button("Convert Transformation To Elastic");
		final Button composeElasticButton = new Button("Compose Elastic Transformations");
		final Button composeRawButton = new Button("Compose Raw Transformations");
		final Button composeRawElasticButton = new Button("Compose Raw and Elastic Transformations");
		final Button invertRawButton = new Button("Invert Raw Transformation");
		final Button evaluateSimilarityButton = new Button("Evaluate Image Similarity");
		final Button adaptCoeffsButton = new Button("Adapt Coefficients");
		final Button loadSourceMaskButton = new Button("Load Source Mask");
		final Button loadSourceInitialAffineMatrixButton = new Button("Load Source Initial Affine Matrix");
		final Button cancelButton = new Button("Cancel");

		saveAsButton.addActionListener(this);
		loadButton.addActionListener(this);
		show_PointsButton.addActionListener(this);
		loadTransfButton.addActionListener(this);
		loadRawTransfButton.addActionListener(this);
		cancelButton.addActionListener(this);
		compareOppositeTransfButton.addActionListener(this);
		compareElasticRawTransfButton.addActionListener(this);
		compareRawButton.addActionListener(this);
		convertToRawButton.addActionListener(this);
		convertToElasticButton.addActionListener(this);
		composeElasticButton.addActionListener(this);
		composeRawButton.addActionListener(this);
		composeRawElasticButton.addActionListener(this);
		invertRawButton.addActionListener(this);
		evaluateSimilarityButton.addActionListener(this);
		adaptCoeffsButton.addActionListener(this);
		loadSourceMaskButton.addActionListener(this);
		loadSourceInitialAffineMatrixButton.addActionListener(this);

		final Label separation1 = new Label("");
		final Label separation2 = new Label("");
		add(separation1);
		add(loadButton);
		add(saveAsButton);
		add(show_PointsButton);
		add(loadTransfButton);
		add(loadRawTransfButton);
		add(compareOppositeTransfButton);
		add(compareElasticRawTransfButton);
		add(compareRawButton);
		add(convertToRawButton);
		add(convertToElasticButton);
		add(composeElasticButton);
		add(composeRawButton);
		add(composeRawElasticButton);
		add(invertRawButton);
		add(evaluateSimilarityButton);
		add(adaptCoeffsButton);
		add(loadSourceMaskButton);
		add(loadSourceInitialAffineMatrixButton);
		add(separation2);
		add(cancelButton);
		pack();
	} /* end IODialog */    

	/*------------------------------------------------------------------*/
	/**
	 * Actions to be taking during the dialog.
	 */
	public void actionPerformed (final ActionEvent ae)
	{
		this.setVisible(false);
		if (ae.getActionCommand().equals("Save Landmarks As...")) {
			savePoints();
		}
		else if (ae.getActionCommand().equals("Load Landmarks...")) {
			loadPoints();
		}
		else if (ae.getActionCommand().equals("Show Landmarks")) {
			showPoints();
		}
		else if (ae.getActionCommand().equals("Load Elastic Transformation")) {
			loadTransformation();
		}
		else if (ae.getActionCommand().equals("Load Raw Transformation")) {
			loadRawTransformation();
		}
		else if (ae.getActionCommand().equals("Compare Opposite Elastic Transformations")) {
			compareOppositeElasticTransformations();
		}
		else if (ae.getActionCommand().equals("Compare Elastic/Raw Transformations")) {
			compareElasticWithRaw();
		}
		else if (ae.getActionCommand().equals("Compare Raw Transformations")) {
			compareRawTransformations();
		}
		else if (ae.getActionCommand().equals("Convert Transformation To Raw")) {
			saveTransformationInRaw();
		}
		else if (ae.getActionCommand().equals("Convert Transformation To Elastic")) {
			saveTransformationInElastic();
		}
		else if (ae.getActionCommand().equals("Compose Elastic Transformations")) {
			composeElasticTransformations();
		}
		else if (ae.getActionCommand().equals("Compose Raw Transformations")) {
			composeRawTransformations();
		}
		else if (ae.getActionCommand().equals("Compose Raw and Elastic Transformations")) {
			composeRawElasticTransformations();
		}
		else if (ae.getActionCommand().equals("Invert Raw Transformation")) {
			invertRawTransformation();
		}
		else if (ae.getActionCommand().equals("Evaluate Image Similarity")) {
			evaluateSimilarity();
		}
		else if (ae.getActionCommand().equals("Adapt Coefficients")) {
			adaptCoefficients();
		}
		else if (ae.getActionCommand().equals("Load Source Mask")) {
			loadSourceMask();
		}
		else if (ae.getActionCommand().equals("Load Source Initial Affine Matrix")) {
			loadSourceInitialAffineMatrix();
		}
		else if (ae.getActionCommand().equals("Cancel")) {
		}
	} /* end actionPerformed */

	/*------------------------------------------------------------------*/
	/**
	 * Get the insets.
	 *
	 * @return new insets
	 */
	public Insets getInsets ()
	{
		return(new Insets(0, 20, 20, 20));
	} /* end getInsets */



	/*....................................................................
       Private methods
    ....................................................................*/

	/*------------------------------------------------------------------*/
	/**
	 * Load the landmark points from an user-defined filed.
	 */
	private void loadPoints ()
	{
		final OpenDialog od = new OpenDialog("Load Points", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();
		if ((path == null) || (filename == null)) return;

		Stack <Point> sourceStack = new Stack <Point> ();
		Stack <Point> targetStack = new Stack <Point> ();
		MiscTools.loadPoints(path+filename,sourceStack,targetStack);

		sourcePh.removePoints();
		targetPh.removePoints();
		while ((!sourceStack.empty()) && (!targetStack.empty())) {
			Point sourcePoint = (Point)sourceStack.pop();
			Point targetPoint = (Point)targetStack.pop();
			sourcePh.addPoint(sourcePoint.x, sourcePoint.y);
			targetPh.addPoint(targetPoint.x, targetPoint.y);
		}
		// record macro command
		record( IODialog.LOAD_LANDMARKS, path+filename );
	} /* end loadPoints */



	/*------------------------------------------------------------------*/
	/**
	 * Load a transformation and apply it to the source image.
	 */
	private void loadTransformation ()
	{
		final OpenDialog od = new OpenDialog("Load Elastic Transformation", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();

		if ((path == null) || (filename == null))
			return;

		String fn_tnf = path+filename;

		int intervals = MiscTools.numberOfIntervalsOfTransformation(fn_tnf);

		double [][]cx = new double[intervals+3][intervals+3];
		double [][]cy = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation(fn_tnf, cx, cy);

		// Apply transformation
		dialog.applyTransformationToSource(intervals, cx, cy);
		// record macro call
		record( IODialog.LOAD_ELASTIC_TRANSF, path + filename );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Load a source mask image from a file.
	 */
	private void loadSourceMask ()
	{
		final OpenDialog od = new OpenDialog("Load Source Mask", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();

		if ((path == null) || (filename == null))
			return;

		String fnSourceMask = path+filename;
		dialog.setSourceMask(fnSourceMask);
		dialog.grayImage(sourcePh);

	}
	/* end loadSourceMask */

	/*------------------------------------------------------------------*/
	/**
	 * Load a source initial affine matrix.
	 */
	private void loadSourceInitialAffineMatrix ()
	{
		final OpenDialog od = new OpenDialog("Load Source Initial Affine Matrix", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();

		if ((path == null) || (filename == null))
			return;

		double[][] affineMatrix = new double[2][3];
		MiscTools.loadAffineMatrix(path+filename, affineMatrix);

		this.dialog.setSourceAffineMatrix(affineMatrix);
	}
	/* end loadSourceInitialAffineMatrix */

	/*------------------------------------------------------------------*/
	/**
	 * Load a raw transformation and apply it to the source image.
	 */
	private void loadRawTransformation ()
	{
		final OpenDialog od = new OpenDialog("Load Raw Transformation", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();

		if ((path == null) || (filename == null))
			return;

		String fn_tnf = path+filename;

		double [][]transformation_x = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double [][]transformation_y = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];

		MiscTools.loadRawTransformation(fn_tnf, transformation_x, transformation_y);

		// Apply transformation
		dialog.applyRawTransformationToSource(transformation_x, transformation_y);
		// record macro call
		record( IODialog.LOAD_RAW_TRANSF, path + filename );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Adapt the transformation coefficients to a new image size.
	 * It asks the user to introduce the image factor between the previous
	 * and the new image size. This factor can be a double to represent
	 * image size reductions. Powers of two (positive or negative) expected.
	 */
	private void adaptCoefficients ()
	{
		// We ask the user for the elastic transformation file
		final OpenDialog od = new OpenDialog("Adapt Coefficients", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf = path+filename;

		int intervals=MiscTools.numberOfIntervalsOfTransformation(fn_tnf);

		double [][]cx = new double[intervals+3][intervals+3];
		double [][]cy = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation(fn_tnf, cx, cy);


		// We ask the user for the image factor
		String sInput = JOptionPane.showInputDialog(null, "Image Factor?", "Adapt Coefficients", JOptionPane.QUESTION_MESSAGE);

		// Adapt coefficients.
		double dImageSizeFactor = Double.parseDouble(sInput);

		for(int i = 0; i < (intervals+3); i++)
			for(int j = 0; j < (intervals+3); j++)
			{
				cx[i][j] *= dImageSizeFactor;
				cy[i][j] *= dImageSizeFactor;
			}

		// Save transformation
		OpenDialog odSave = new OpenDialog("Saving adapted transformation file","");
		String path_save = odSave.getDirectory();
		String filename_save = odSave.getFileName();
		if ((path_save == null) || (filename_save == null))
			return;

		String sNewFileName = path_save + filename_save;
		MiscTools.saveElasticTransformation(intervals, cx, cy, sNewFileName);
	}


	/*------------------------------------------------------------------*/
	/**
	 * Save an elastic transformation in raw format
	 */
	private void saveTransformationInRaw ()
	{
		// We ask the user for the elastic transformation file
		final OpenDialog od = new OpenDialog("Load elastic transformation file", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf = path+filename;

		int intervals=MiscTools.numberOfIntervalsOfTransformation(fn_tnf);

		double [][]cx = new double[intervals+3][intervals+3];
		double [][]cy = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation(fn_tnf, cx, cy);


		// We ask the user for the raw deformation file.
		OpenDialog od_raw = new OpenDialog("Saving in raw - select raw transformation file", "");
		String path_raw = od_raw.getDirectory();
		String filename_raw = od_raw.getFileName();
		if ((path_raw == null) || (filename_raw == null))
			return;

		String fn_tnf_raw = path_raw + filename_raw;

		// We calculate the transformation raw table.
		double[][] transformation_x = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double[][] transformation_y = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];

		MiscTools.convertElasticTransformationToRaw(this.targetImp, intervals, cx, cy, transformation_x, transformation_y);

		MiscTools.saveRawTransformation(fn_tnf_raw, this.targetImp.getWidth(), this.targetImp.getHeight(), transformation_x, transformation_y);
		// record macro call
		record( IODialog.CONVERT_TO_RAW, fn_tnf, fn_tnf_raw );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Save a raw transformation in elastic (B-spline) format
	 */
	private void saveTransformationInElastic ()
	{
		// We ask the user for the input raw transformation file
		final OpenDialog od = new OpenDialog("Load raw transformation file", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf = path+filename;

		double[][] transformation_x = new double[targetImp.getHeight()] [targetImp.getWidth()];
		double[][] transformation_y = new double[targetImp.getHeight()] [targetImp.getWidth()];

		MiscTools.loadRawTransformation(fn_tnf, transformation_x, transformation_y);

		// We ask the user for the output elastic deformation file.
		OpenDialog od_elastic = new OpenDialog("Saving in elastic - select elastic transformation file", "");
		String path_elastic = od_elastic.getDirectory();
		String filename_elastic = od_elastic.getFileName();
		if ((path_elastic == null) || (filename_elastic == null))
			return;

		String fn_tnf_elastic = path_elastic + filename_elastic;


		// We ask the user for the number of intervals in the B-spline grid.
		String sInput = JOptionPane.showInputDialog(null, "Number of intervals for B-spline grid?", "Save as B-spline coefficients", JOptionPane.QUESTION_MESSAGE);

		// Read value.
		int intervals = Integer.parseInt(sInput);

		// We calculate the B-spline transformation coefficients.
		double [][]cx = new double[intervals+3][intervals+3];
		double [][]cy = new double[intervals+3][intervals+3];

		MiscTools.convertRawTransformationToBSpline(this.targetImp, intervals, transformation_x, transformation_y, cx, cy);

		MiscTools.saveElasticTransformation(intervals, cx, cy, fn_tnf_elastic);
		// record macro call
		record( IODialog.CONVERT_TO_ELASTIC, fn_tnf, fn_tnf_elastic,
				String.valueOf( intervals ) );
	}	// end  method saveTransformationInElastic


	/*------------------------------------------------------------------*/
	/**
	 * Invert a raw transformation
	 */
	private void invertRawTransformation ()
	{
		// We ask the user for the input raw transformation file
		final OpenDialog od = new OpenDialog("Load raw transformation file", "");
		final String path = od.getDirectory();
		final String filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf = path+filename;

		double[][] transformation_x = new double[targetImp.getHeight()] [targetImp.getWidth()];
		double[][] transformation_y = new double[targetImp.getHeight()] [targetImp.getWidth()];

		MiscTools.loadRawTransformation(fn_tnf, transformation_x, transformation_y);

		// We ask the user for the output raw deformation file.
		OpenDialog od_inverse = new OpenDialog("Saving in raw - select raw transformation file", "");
		String path_inverse = od_inverse.getDirectory();
		String filename_inverse = od_inverse.getFileName();
		if ((path_inverse == null) || (filename_inverse == null))
			return;

		String fn_tnf_inverse = path_inverse + filename_inverse;

		double[][] inv_x = new double[targetImp.getHeight()] [targetImp.getWidth()];
		double[][] inv_y = new double[targetImp.getHeight()] [targetImp.getWidth()];


		MiscTools.invertRawTransformation(targetImp, transformation_x, transformation_y, inv_x, inv_y);

		MiscTools.saveRawTransformation(fn_tnf_inverse, targetImp.getWidth(), 
				targetImp.getHeight(), inv_x, inv_y);


	}	// end  method saveTransformationInElastic


	/*------------------------------------------------------------------*/
	/**
	 * Compare two opposite transformations (direct and inverse)
	 * represented by B-splines through the warping index.
	 */
	private void compareOppositeElasticTransformations ()
	{
		// We ask the user for the direct transformation file
		OpenDialog od = new OpenDialog("Comparing - Load Direct Elastic Transformation", "");
		String path = od.getDirectory();
		String filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		final String directTransfFilename = path+filename;

		int intervals=MiscTools.numberOfIntervalsOfTransformation(directTransfFilename);

		double [][]cx_direct = new double[intervals+3][intervals+3];
		double [][]cy_direct = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation(directTransfFilename, cx_direct, cy_direct);


		// We ask the user for the inverse transformation file
		od = new OpenDialog("Comparing - Load Inverse Elastic Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		final String inverseTransfFilename = path+filename;

		intervals =
			MiscTools.numberOfIntervalsOfTransformation( inverseTransfFilename );

		double [][]cx_inverse = new double[intervals+3][intervals+3];
		double [][]cy_inverse = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation( inverseTransfFilename, cx_inverse,
				cy_inverse );


		// Now we compare both transformations through the "warping index", which is
		// a method equivalent to our consistency measure.

		double warpingIndex = MiscTools.warpingIndex(this.sourceImp, this.targetImp, intervals, cx_direct, cy_direct, cx_inverse, cy_inverse);

		if(warpingIndex != -1)
			IJ.log(" Warping index = " + warpingIndex);
		else
			IJ.log(" Warping index could not be evaluated because not a single pixel matched after the deformation!");

		// record macro call
		record( IODialog.COMPARE_OPPOSITE_ELASTIC,
				directTransfFilename, inverseTransfFilename );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Compose two transformations represented by elastic B-splines
	 * into a raw mapping table (saved as usual).
	 */
	private void composeElasticTransformations ()
	{

		// We ask the user for the first transformation file
		OpenDialog od = new OpenDialog("Composing - Load First Elastic Transformation", "");
		String path = od.getDirectory();
		String filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		final String elasticTransfPath1 = path+filename;

		int intervals =
				MiscTools.numberOfIntervalsOfTransformation( elasticTransfPath1 );

		double [][]cx1 = new double[intervals+3][intervals+3];
		double [][]cy1 = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation(elasticTransfPath1, cx1, cy1);


		// We ask the user for the second transformation file
		od = new OpenDialog("Composing - Load Second Elastic Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		final String elasticTransfPath2 = path+filename;

		intervals =
				MiscTools.numberOfIntervalsOfTransformation( elasticTransfPath2 );

		double [][]cx2 = new double[intervals+3][intervals+3];
		double [][]cy2 = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation(elasticTransfPath1, cx2, cy2);

		double [][] outputTransformation_x = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double [][] outputTransformation_y = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];

		// Now we compose them and get as result a raw transformation mapping.
		MiscTools.composeElasticTransformations(this.targetImp, intervals,
				cx1, cy1, cx2, cy2, outputTransformation_x, outputTransformation_y);

		// We ask the user for the raw deformation file where we will save the mapping table.
		od = new OpenDialog("Composing - Save Raw Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf_raw = path + filename;

		MiscTools.saveRawTransformation(fn_tnf_raw, this.targetImp.getWidth(),
				this.targetImp.getHeight(), outputTransformation_x, outputTransformation_y);
		// record macro call
		record( IODialog.COMPOSE_ELASTIC, elasticTransfPath1,
				elasticTransfPath2, fn_tnf_raw );
	}


	/*------------------------------------------------------------------*/
	/**
	 * Compose a raw transformation with an elastic transformation
	 * represented by elastic B-splines into a raw mapping table (saved as usual).
	 */
	private void composeRawElasticTransformations ()
	{
		// We ask the user for the first transformation file
		OpenDialog od = new OpenDialog("Composing - Load First (Raw) Transformation", "");
		String path = od.getDirectory();
		String filename = od.getFileName();

		if ((path == null) || (filename == null)) {
			return;
		}
		final String rawTransfPath = path+filename;

		double[][] transformation_x_1 = new double[targetImp.getHeight()][targetImp.getWidth()];
		double[][] transformation_y_1 = new double[targetImp.getHeight()][targetImp.getWidth()];

		MiscTools.loadRawTransformation(
				rawTransfPath, transformation_x_1, transformation_y_1 );

		// We ask the user for the second transformation file
		od = new OpenDialog("Composing - Load Second (Elastic) Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();

		if ((path == null) || (filename == null)) {
			return;
		}
		final String elasticTransfPath = path+filename;

		int intervals =
				MiscTools.numberOfIntervalsOfTransformation( elasticTransfPath );

		double [][]cx2 = new double[intervals+3][intervals+3];
		double [][]cy2 = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation( elasticTransfPath, cx2, cy2 );

		double [][] outputTransformation_x = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double [][] outputTransformation_y = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];

		// Now we compose them and get as result a raw transformation mapping.
		MiscTools.composeRawElasticTransformations(this.targetImp, intervals,
				transformation_x_1, transformation_y_1, cx2, cy2, outputTransformation_x, outputTransformation_y);

		// We ask the user for the raw deformation file where we will save the mapping table.
		od = new OpenDialog("Composing - Save Raw Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();

		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf_raw = path + filename;

		MiscTools.saveRawTransformation(fn_tnf_raw, this.targetImp.getWidth(),
				this.targetImp.getHeight(), outputTransformation_x, outputTransformation_y);
		// record macro call
		record( IODialog.COMPOSE_RAW_ELASTIC, rawTransfPath, elasticTransfPath,
				fn_tnf_raw );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Compose two random (raw) deformations.
	 */
	private void composeRawTransformations ()
	{

		// We ask the user for the first raw deformation file.
		OpenDialog od = new OpenDialog("Composing - Load First Raw Transformation", "");
		String path = od.getDirectory();
		String filename = od.getFileName();

		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf_raw = path + filename;

		// We load the transformation raw file.
		double[][] transformation_x_1 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double[][] transformation_y_1 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		MiscTools.loadRawTransformation(fn_tnf_raw, transformation_x_1, transformation_y_1);

		// We ask the user for the second raw deformation file.
		od = new OpenDialog("Composing - Load Second Raw Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();

		if ((path == null) || (filename == null))
			return;

		String fn_tnf_raw_2 = path + filename;

		// We load the transformation raw file.
		double[][] transformation_x_2 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double[][] transformation_y_2 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		MiscTools.loadRawTransformation(fn_tnf_raw_2, transformation_x_2, transformation_y_2);

		double [][] outputTransformation_x = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double [][] outputTransformation_y = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];

		// Now we compose them and get as result a raw transformation mapping.
		MiscTools.composeRawTransformations(this.targetImp.getWidth(), this.targetImp.getHeight(),
				transformation_x_1, transformation_y_1, transformation_x_2, transformation_y_2,
				outputTransformation_x, outputTransformation_y);

		// We ask the user for the raw deformation file where we will save the mapping table.
		od = new OpenDialog("Composing - Save Raw Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();
		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf_raw_out = path + filename;

		MiscTools.saveRawTransformation(fn_tnf_raw_out, this.targetImp.getWidth(),
				this.targetImp.getHeight(), outputTransformation_x, outputTransformation_y);
		// record macro call
		record( IODialog.COMPOSE_RAW, fn_tnf_raw, fn_tnf_raw_2,
				fn_tnf_raw_out );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Compare an elastic B-spline transformation with a random deformation
	 * (in raw format) by the warping index.
	 */
	private void compareElasticWithRaw ()
	{
		// We ask the user for the direct transformation file
		OpenDialog od = new OpenDialog("Comparing - Load Elastic Transformation", "");
		String path = od.getDirectory();
		String filename = od.getFileName();

		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf = path+filename;

		int intervals = MiscTools.numberOfIntervalsOfTransformation(fn_tnf);

		double [][]cx_direct = new double[intervals+3][intervals+3];
		double [][]cy_direct = new double[intervals+3][intervals+3];

		MiscTools.loadTransformation(fn_tnf, cx_direct, cy_direct);


		// We ask the user for the raw deformation file.
		od = new OpenDialog("Comparing - Load Raw Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();

		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf_raw = path + filename;

		// We load the transformation raw file.
		double[][] transformation_x = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double[][] transformation_y = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		MiscTools.loadRawTransformation(fn_tnf_raw, transformation_x,
				transformation_y);

		double warpingIndex = MiscTools.rawWarpingIndex(this.sourceImp,
				this.targetImp, intervals, cx_direct, cy_direct, transformation_x, transformation_y);

		if(warpingIndex != -1)
			IJ.log(" Warping index = " + warpingIndex);
		else
			IJ.log(" Warping index could not be evaluated because not a "
					+ "single pixel matched after the deformation!");
		// record macro call
		record( IODialog.COMPARE_ELASTIC_RAW, fn_tnf, fn_tnf_raw );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Compare two random (raw) deformations by the warping index.
	 */
	private void compareRawTransformations ()
	{

		// We ask the user for the first raw deformation file.
		OpenDialog od = new OpenDialog("Comparing - Load First Raw Transformation", "");
		String path = od.getDirectory();
		String filename = od.getFileName();

		if ((path == null) || (filename == null)) {
			return;
		}
		String fn_tnf_raw = path + filename;

		// We load the transformation raw file.
		double[][] transformation_x_1 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double[][] transformation_y_1 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		MiscTools.loadRawTransformation(fn_tnf_raw, transformation_x_1, transformation_y_1);

		// We ask the user for the second raw deformation file.
		od = new OpenDialog("Comparing - Load Second Raw Transformation", "");
		path = od.getDirectory();
		filename = od.getFileName();

		if ((path == null) || (filename == null))
			return;

		String fn_tnf_raw_2 = path + filename;

		// We load the transformation raw file.
		double[][] transformation_x_2 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		double[][] transformation_y_2 = new double[this.targetImp.getHeight()][this.targetImp.getWidth()];
		MiscTools.loadRawTransformation(fn_tnf_raw_2, transformation_x_2, transformation_y_2);

		double warpingIndex = MiscTools.rawWarpingIndex(this.sourceImp,
				this.targetImp, transformation_x_1, transformation_y_1, transformation_x_2, transformation_y_2);

		if(warpingIndex != -1)
			IJ.log(" Warping index = " + warpingIndex);
		else
			IJ.log(" Warping index could not be evaluated because not a single"
					+ " pixel matched after the deformation!");
		// record macro call
		record( IODialog.COMPARE_RAW, fn_tnf_raw, fn_tnf_raw_2 );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Calculate the similarity error between the source and target images.
	 * The error is calculated pixel by pixel without representing the
	 * images by B-splines coefficients. Mask are taken into account.
	 */
	private void evaluateSimilarity ()
	{
		if( MiscTools.imageSimilarity( targetImp, sourceImp,
				dialog.getTargetMask(), true ) != -1 )
			record( IODialog.EVALUATE_SIMILARITY, targetImp.getTitle(),
					sourceImp.getTitle() );
	}

	/*------------------------------------------------------------------*/
	/**
	 * Save the landmark points into a file.
	 */
	private void savePoints ()
	{
		String filename = targetImp.getTitle();

		final SaveDialog sd = new SaveDialog("Save Points", filename, ".txt");

		final String path = sd.getDirectory();
		filename = sd.getFileName();
		MiscTools.saveLandmarks( path + filename, sourcePh.getPoints(),
				targetPh.getPoints() );
		// record macro command
		record( IODialog.SAVE_LANDMARKS, path + filename );
	} /* end savePoints */

	/*------------------------------------------------------------------*/
	/**
	 * Display source and target landmark points on a results table.
	 */
	private void showPoints ()
	{
		final Vector <Point> sourceList = sourcePh.getPoints();
		final Vector <Point> targetList = targetPh.getPoints();
		MiscTools.showPoints( sourceList, targetList );
		// record macro call
		record( IODialog.SHOW_LANDMARKS );
	} /* end showPoints */

	/* **********************************************************
	 * Macro recording related methods
	 * *********************************************************/

	/**
	 * Macro-record a specific command. The command names match the static
	 * methods that reproduce that part of the code.
	 *
	 * @param command name of the command including package info
	 * @param args set of arguments for the command
	 */
	public static void record(String command, String... args)
	{
		command = "call(\"bunwarpj.bUnwarpJ_." + command;
		for(int i = 0; i < args.length; i++)
			command += "\", \"" + args[i];
		command += "\");\n";
		// in Windows systems, replace backslashes by double ones
		if( IJ.isWindows() )
			command = command.replaceAll( "\\\\", "\\\\\\\\" );
		if(Recorder.record)
			Recorder.recordString(command);
	}

} /* end class IODialog */


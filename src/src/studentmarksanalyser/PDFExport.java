package studentmarksanalyser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import studentmarksanalyser.gui.components.ProgressDialog;

public class PDFExport {
	
	/**
	 * Exports the PDF file to the chosen location, displaying a progress bar if the process takes too long.
	 * @param frame the frame to disable while exporting the PDF file, can be null
	 * @param students student data to use when producing the PDF
	 */
	public static void exportPDF(JFrame frame, StudentList students)
	{
		// Open file chooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(".PDF Document", "pdf"));
		int returnState = fileChooser.showSaveDialog(null);
		
		// Unless file chooser was cancelled, create the document
		if (returnState == JFileChooser.APPROVE_OPTION)
		{
			// Disable frame while saving
			frame.setEnabled(false);
			
			PDDocument document = new PDDocument();
			
			// Display progress bar
			ProgressDialog progressDialog = new ProgressDialog("Saving");
			
			// Create document on separate thread to track progress 
			SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
				double progress = 0;
				double progressStep = 100 / students.getModules().size();
				
				
				
				
	            /**
	             * Here is the code for producing the PDF document.
	             * Since the requirements specify that the user should be notified if this
	             * takes longer than 5 seconds, the code is run in a separate thread so that
	             * the main thread can continue running and time the duration of the operation.
	             */
	            @Override
	            public Boolean doInBackground() {
	            	for (String module : students.getModules())
	        		{
	            		// Add page for this module and open the content stream
	        			PDPage chartPage = new PDPage();
	        			document.addPage(chartPage);
	        			try {
	        				PDPageContentStream contentStream = new PDPageContentStream(document, chartPage);
	        				
	        				// Add page heading
	        				contentStream.beginText();
	        				contentStream.setFont(PDType1Font.HELVETICA, 24);
	        				contentStream.newLineAtOffset(
	        						(chartPage.getMediaBox().getWidth() - PDType1Font.HELVETICA.getStringWidth(module) / 1000 * 24) / 2,
	        						chartPage.getMediaBox().getHeight() - PDType1Font.HELVETICA.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 24 - 20);
	        				contentStream.showText(module);
	        				contentStream.endText();
		            		
		        			// Create chart
		        			BufferedImage lineChart = Charts.createFrequencyCurve(students, module);
		        			
		        			// Add chart to page
	        				PDImageXObject pdImage = JPEGFactory.createFromImage(document, lineChart);
	        				contentStream.drawImage(pdImage, (chartPage.getMediaBox().getWidth() - 500) / 2, 400, 500, 300);
	        				
	        				// Create averages chart
	        				BufferedImage avlineChart = Charts.createAveragesChart(students, module);
	        				
	        				// Add another chart (averages chart) to page
	        				PDImageXObject pdImage2 = JPEGFactory.createFromImage(document, avlineChart);
	        				contentStream.drawImage(pdImage2, (chartPage.getMediaBox().getWidth() - 500) / 2, 50, 500, 300);
	        				contentStream.close();
		        			
		        			// Update progress bar
		        			progress += progressStep;
		        			progressDialog.setProgress((int)progress);
	        			} 
	        			catch (IOException e)
	        			{
	        				e.printStackTrace();
	        			}
	        		}
	        		//Saving the document
	        		try {
						String fileName =fileChooser.getSelectedFile().toString();
						if (!fileName.endsWith(".pdf")){
							File file = new File(fileChooser.getSelectedFile().toString()+".pdf");
							fileChooser.setSelectedFile(file);
						}
	        			document.save(fileChooser.getSelectedFile());
	        		} catch (IOException e1) {
	        			e1.printStackTrace();
	        		}
	        	
	        		//Closing the document
	        		try {
	        			document.close();
	        		} catch (IOException e1) {
	        			e1.printStackTrace();
	        		}

					// Hide progress bar and enable frame
	        		progressDialog.setVisible(false);
					frame.setEnabled(true);
					// Bring the frame to the front
					frame.setAlwaysOnTop(true);
					frame.setAlwaysOnTop(false);
					
	        		return true;
	            }
	            
	            
	            
	            
	            
	        };
	        worker.execute();
		}
	}
}

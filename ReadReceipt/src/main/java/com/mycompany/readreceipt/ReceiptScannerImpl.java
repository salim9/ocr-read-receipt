package com.mycompany.readreceipt;
import com.mycompany.readreceipt.ReceiptScanner;
import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.bytedeco.javacpp.opencv_core.BORDER_DEFAULT;
import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.CV_32F;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import org.bytedeco.javacpp.opencv_core.CvType;
import org.bytedeco.javacpp.opencv_core.Point;
import static org.bytedeco.javacpp.opencv_core.cvConvertScale;
import static org.bytedeco.javacpp.opencv_core.log;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_TOZERO;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_CLOSE;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_TOPHAT;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
@Slf4j
public final class ReceiptScannerImpl implements ReceiptScanner{


	String timeStampDefinido = null;
	
	public ReceiptScannerImpl(){
		this.timeStampDefinido = getCurrentTimeStamp();
	}

	public String getTextFromReceiptImage(final String receiptImageFilePath){
		final String cleanedReceiptPathFile = preprocessaImagem(receiptImageFilePath);
		return getStringsFromImage(cleanedReceiptPathFile);
	}

	private String preprocessaImagem(final String receiptImageFilePath) {
		final File receiptImageFile = new File(receiptImageFilePath);
		final String receiptImagePathFile = receiptImageFile.getAbsolutePath();
		
		IplImage receiptImage = cvLoadImage(receiptImagePathFile);
               
		IplImage cannyEdgeImage = applyCannySquareEdgeDetectionOnImage(receiptImage, 30);


		CvSeq largestSquare = findLargestSquareOnCannyDetectedImage(cannyEdgeImage);
                
		receiptImage = cleanImageSmoothingForOCR(receiptImage);

		
		final String cleanedReceiptPathFile = salvaImagemPasso(receiptImage, "a1-cleanedFile.jpg");
		
		cvReleaseImage(cannyEdgeImage);
		cannyEdgeImage = null;
		cvReleaseImage(receiptImage);
		receiptImage = null;
	
		return cleanedReceiptPathFile;
	}
		



	private IplImage cleanImageSmoothingForOCR(IplImage srcImage){
		//
                int alpha=1;
                int beta=25;
		IplImage destImage = cvCreateImage( cvGetSize(srcImage), IPL_DEPTH_8U, 1);

                cvCvtColor(srcImage, destImage, CV_BGR2GRAY);
		cvSmooth(destImage, destImage, CV_MEDIAN, 3, 0, 0, 0);
                Mat src=new Mat(srcImage);
                Mat dest=new Mat(destImage);
               
                
                IplImage dest2=new IplImage(dest);
                IplImage src2=new IplImage(src);
                cvThreshold(dest2, dest2, 0, 255, CV_THRESH_OTSU);
		
                return destImage;
		
	}
	
	/*
	 * Call Tesseract with the receipt image and return the text founded
	 */
	private String getStringsFromImage(final String pathToReceiptImageFile){
		try {
			
			final URL tessDataResource = getClass().getResource("/");
			
			final File tessFolder = new File(tessDataResource.toURI());
		
			//original final String tessFolderPath = tessFolder.getAbsolutePath();
			final String tessFolderPath = "/Users/emircanaydin/Desktop/Tess4J/tessdata";
			
			//System.out.println("tessFolderPath:" + tessFolderPath);
			
			//
			BytePointer outText;
			
			TessBaseAPI api = new TessBaseAPI();
			
			api.SetVariable("tessedit_char_whitelist","123456789,/ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                        //Türkçe dili, Tesseract için ayarlanıyor...
			
			if ( api.Init(tessFolderPath, "tur") != 0 ){
				System.out.println("Deneme-9");
				System.out.println("Tesseract yüklenemedi");
				return "";
			}
			//Open input image with leptonica library
		
			PIX image = pixRead(pathToReceiptImageFile);
			
			
			api.SetImage(image);
			// Get OCR result
			
			outText = api.GetUTF8Text();
			
			String string = outText.getString();
                        
 
			// Destroy used object and release memory
			
			api.End();
			// api.close();
			
			outText.deallocate();
			
			pixDestroy(image);
			//
			
                        String trstring="1234567890,./ABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZabcçdefgğhıi:*#:-jklmnoprsştuüvwxyz);(:";
                        char c0 = '\0';
                        String temp=" ";
                        char s;
                        int p=0;
                        char[] trchar = string.toCharArray();
                        for(int i=0;i<string.length()-1;i++){
                            for(int k=0;k<trstring.length()-1;k++){
                                
                                if(string.charAt(i)!=trstring.charAt(k) && string.charAt(i)!=' ' && string.charAt(i)!='\n'){
                                    p++;
                                    if(p==trstring.length()-1){
                                        trchar[i]='\0';
                                        
                                    }
                                    
                                }
                                
                            }
                            p=0;
                        }
                        string=String.valueOf(trchar);
                        
                        
			return string;
			//
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		//
	}


	private String salvaImagemPasso(IplImage destImage, String nameImage) {
		
		File f = new File(System.getProperty("user.dir") + File.separator + "processamentos" + File.separator + getTimeStampDefinido() + "-" + nameImage);
                //İşlem sonrası fotoğraf kaydedildikten sonraki dosya ismi + dosyayolu
		//System.out.println(nameImage +": "+f.getAbsolutePath());
		cvSaveImage(f.getAbsolutePath(), destImage);
		
		return f.getAbsolutePath();
	}
	
	private String getTimeStampDefinido(){
		return timeStampDefinido;
	}
	
	public String getCurrentTimeStamp() {
		//
		//System.out.println("Şuanki tarih hesaplanıyor...");
		//
		// SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss.SSS");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		//System.out.println("Şuanki tarih: " + strDate);
		return strDate;
	}
	
	
}
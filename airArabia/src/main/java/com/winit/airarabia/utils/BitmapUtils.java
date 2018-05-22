package com.winit.airarabia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtils 
{
	public static Bitmap  resizeBitmap(Bitmap bitmapOrg,int newWidth,int newHeight)
	{
		Bitmap resizedBitmap = null;
		try
		{
        
		int width=bitmapOrg.getWidth();
        int height=bitmapOrg.getHeight();
        
        // calculate the scale - in this case = 0.4f 
        float scaleWidth = ((float) newWidth) / width; 
        float scaleHeight = ((float) newHeight) / height;
        
        // createa matrix for the manipulation 
        Matrix matrix = new Matrix(); 
        // resize the bit map 
        matrix.postScale(scaleWidth, scaleHeight); 
        // rotate the Bitmap 
        //matrix.postRotate(45); 
        resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,width, height, matrix, true);
        bitmapOrg.recycle();
		
		}
		catch(Throwable e)
		{
			
		}
        
        return resizedBitmap;
	}
	
	 public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPxRadius)
		{
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);
	     
	        final int color = 0xff424242;
	        final Paint paint = new Paint();
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	        final RectF rectF = new RectF(rect);
	        final float roundPx =roundPxRadius;
	     
	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	     
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	     
	        return output;
		}
	 
	 public void copyFile(String sourceFilePath,String destFilePath) throws IOException 
	    {
		 	File sourceFile = new File(sourceFilePath);
		 	File destFile 	= new File(destFilePath);
		 	
			if (!sourceFile.exists())
			{
			    return;
			}
			if (!destFile.exists()) {
			    destFile.createNewFile();
			}
			FileChannel source = null;
			FileChannel destination = null;
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			if (destination != null && source != null) {
			    destination.transferFrom(source, 0, source.size());
			}
			if (source != null)
			{
			    source.close();
			}
			if (destination != null)
			{
			    destination.close();
			}

	      }
	 
	 public static void CopyStream(InputStream is, OutputStream os)
	 {
	        final int buffer_size=1024;
	        try
	        {
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }

	    
}

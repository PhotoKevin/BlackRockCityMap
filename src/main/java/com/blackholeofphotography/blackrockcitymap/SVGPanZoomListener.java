package com.blackholeofphotography.blackrockcitymap;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import org.apache.batik.swing.JSVGCanvas;

public class SVGPanZoomListener extends MouseAdapter
{

   private final JSVGCanvas canvas;
   private Point dragStartScreen = null;

   // --- CONSTRAINT CONFIGURATIONS ---
   private static final double ZOOM_FACTOR = 1.1;
   private static final double MIN_ZOOM = 0.002;  // 20% size minimum
   private static final double MAX_ZOOM = 10.0; // 1000% size maximum
   private static final boolean RESTRICT_PAN = true; // Set false to unbind drag limits

   public SVGPanZoomListener (JSVGCanvas canvas)
   {
      this.canvas = canvas;
   }

   @Override
   public void mouseWheelMoved (MouseWheelEvent e)
   {
      AffineTransform currentAt = canvas.getRenderingTransform ();
      if (currentAt == null)
         currentAt = new AffineTransform ();

      double scaleMultiplier = (e.getWheelRotation () < 0) ? ZOOM_FACTOR : (1.0 / ZOOM_FACTOR);
      try
      {
         Point2D mouseSrc = e.getPoint ();

         // Scaling happens around 0,0. So we need to move the 
         // canvas to put the current mouse location there,
         // then scale, and then move it back
         AffineTransform zoomAt = new AffineTransform ();
         zoomAt.translate (mouseSrc.getX (), mouseSrc.getY ());
         zoomAt.scale (scaleMultiplier, scaleMultiplier);
         zoomAt.translate (-mouseSrc.getX (), -mouseSrc.getY ());

         currentAt.preConcatenate (zoomAt);
         canvas.setRenderingTransform (currentAt, true);
      }
      catch (Exception ex)
      {
         ex.printStackTrace ();
      }

   }

   @Override
   public void mousePressed (MouseEvent e)
   {
      if (e.getButton () == MouseEvent.BUTTON1)
      {
         dragStartScreen = e.getPoint ();
      }
      
      System.out.printf ("Pressed @ %d, %d\n", e.getX (), e.getY ());
   }

   @Override
   public void mouseDragged (MouseEvent e)
   {
      if (dragStartScreen == null)
         return;

      AffineTransform currentAt = canvas.getRenderingTransform ();
      if (currentAt == null)
         currentAt = new AffineTransform ();

      Point dragEndScreen = e.getPoint ();
      double deltaX = dragEndScreen.getX () - dragStartScreen.getX ();
      double deltaY = dragEndScreen.getY () - dragStartScreen.getY ();

      AffineTransform panAt = AffineTransform.getTranslateInstance (deltaX, deltaY);
      currentAt.preConcatenate (panAt);

      canvas.setRenderingTransform (currentAt, true);
      dragStartScreen = dragEndScreen;
      System.out.printf ("Dragged %f, %f\n", deltaX, deltaY);
      System.out.printf ("  to %f, %f\n", currentAt.getTranslateX (), currentAt.getTranslateY ());
   }

   @Override
   public void mouseReleased (MouseEvent e)
   {
      if (e.getButton () == MouseEvent.BUTTON1)
         dragStartScreen = null;
   }
}

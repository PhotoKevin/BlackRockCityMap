/*
 * Copyright (c) 2019, Kevin Nickerson (kevin@blackholeofphotography.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.blackholeofphotography.blackrockcitymap;

import com.blackholeofphotography.blackrockcitymap.path.Path;
import com.blackholeofphotography.blackrockcitymap.path.PathBounds;
import com.blackholeofphotography.llalocation.LLALocation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class BlackRockCityMapUI extends javax.swing.JFrame
{

   double docWidth = 1;
   double docHeight = 1;
   /**
    * Creates new form BlackRockCityMap
    */
   public BlackRockCityMapUI ()
   {
      initComponents ();
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      jPanel1 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      cbYear = new javax.swing.JComboBox<>();
      jButton1 = new javax.swing.JButton();
      jSVGCanvas1 = new org.apache.batik.swing.JSVGCanvas();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      addWindowListener(new java.awt.event.WindowAdapter()
      {
         public void windowOpened(java.awt.event.WindowEvent evt)
         {
            formWindowOpened(evt);
         }
      });

      jLabel1.setText("Year:");

      cbYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2014", "2015", "2016", "2017", "2018", "2019" }));
      cbYear.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            cbYearActionPerformed(evt);
         }
      });

      jButton1.setText("jButton1");
      jButton1.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            jButton1ActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton1)
            .addGap(86, 86, 86))
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton1))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jSVGCanvas1.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener()
      {
         public void ancestorMoved(java.awt.event.HierarchyEvent evt)
         {
         }
         public void ancestorResized(java.awt.event.HierarchyEvent evt)
         {
            jSVGCanvas1AncestorResized(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jSVGCanvas1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSVGCanvas1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void cbYearActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbYearActionPerformed
   {//GEN-HEADEREND:event_cbYearActionPerformed
      String y = (String) cbYear.getSelectedItem ().toString ();
      DrawMap (Integer.parseInt (y));
   }//GEN-LAST:event_cbYearActionPerformed

   private void formWindowOpened(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowOpened
   {//GEN-HEADEREND:event_formWindowOpened
      String y = (String) cbYear.getSelectedItem ().toString ();
      
      this.jSVGCanvas1.addGVTTreeRendererListener(new GVTTreeRendererAdapter() 
         {
            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) 
            {
                rescale ();
            }
         });      
      DrawMap (Integer.parseInt (y));
      rescale ();
   }//GEN-LAST:event_formWindowOpened

   private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
   {//GEN-HEADEREND:event_jButton1ActionPerformed
      //this.rootPane.repaint ();
      //rescale ();

   }//GEN-LAST:event_jButton1ActionPerformed

   private void jSVGCanvas1AncestorResized(java.awt.event.HierarchyEvent evt)//GEN-FIRST:event_jSVGCanvas1AncestorResized
   {//GEN-HEADEREND:event_jSVGCanvas1AncestorResized
      rescale ();
   }//GEN-LAST:event_jSVGCanvas1AncestorResized

   private void rescale ()
   {
      AffineTransform at = this.jSVGCanvas1.getRenderingTransform ();
      double scaleFactorX = this.jSVGCanvas1.getWidth () / docWidth;
      double scaleFactorY = this.jSVGCanvas1.getHeight () / docHeight;
      double scaleFactor = Math.min (scaleFactorX, scaleFactorY);
      at.setToScale (scaleFactor, scaleFactor);
      this.jSVGCanvas1.setRenderingTransform (at, true);
   }




   private void DrawMap (int year)
   {
      BlackRockCity city = new BlackRockCity (""+year);
      
      BurningKML.createKML (year);
      

      ArrayList<Path> map = city.drawCity  ();
      PathBounds b = city.Perimeter ().getBounds ();
      LLALocation base = b.UpperLeft;
      SVGGPSCoordinate ul = new SVGGPSCoordinate (base, b.UpperLeft);      
      SVGGPSCoordinate lr = new SVGGPSCoordinate (base, b.LowerRight);

      int x = (int) lr.xCoordinate ();
      int y = (int) lr.yCoordinate ();
      org.jfree.graphics2d.svg.SVGGraphics2D g2 = new org.jfree.graphics2d.svg.SVGGraphics2D (y, x);
      
      docWidth = lr.xCoordinate () - ul.xCoordinate ();
      docHeight = lr.yCoordinate () - ul.yCoordinate ();


      g2.setStroke (new BasicStroke (10));
      g2.setColor (Color.PINK);

      for (Path seg : map)
      {
         Path2D pp = new Path2D.Double ();
         pp.append (seg.toPath2D (base), true);
         g2.setColor (seg.getColor ());
         
         pp.closePath ();
         g2.draw (pp);
      }

      String svgElement = g2.getSVGElement ();

//      System.out.println (svgElement);


      //System.out.println (k.toString());
      File ko = new File (year + "BRC.svg");
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(ko)))
      {
         writer.write (svgElement);
      }      
      catch (IOException ex)
      {
      }

      displaySVG (svgElement);
   }
   
   
   private void displaySVG (String svgElement)
   {
      StringReader reader = new StringReader(svgElement);
      String uri = "";
      try 
      {
         String parser = XMLResourceDescriptor.getXMLParserClassName();
         SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
         SVGDocument doc = f.createSVGDocument(uri,reader);
         this.jSVGCanvas1.setDocument (doc);
      } 
      catch (IOException ex) 
      {
      } 
      finally 
      {
         reader.close();
      }  
   }
   
   /**
    * @param args the command line arguments
    */
   public static void main (String args[])
   {
      /* Set the Nimbus look and feel */
      //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
      /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try
      {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels ())
         {
            if ("Nimbus".equals (info.getName ()))
            {
               javax.swing.UIManager.setLookAndFeel (info.getClassName ());
               break;
            }
         }
      }
      catch (ClassNotFoundException ex)
      {
         java.util.logging.Logger.getLogger (BlackRockCityMapUI.class.getName ()).log (java.util.logging.Level.SEVERE, null, ex);
      }
      catch (InstantiationException ex)
      {
         java.util.logging.Logger.getLogger (BlackRockCityMapUI.class.getName ()).log (java.util.logging.Level.SEVERE, null, ex);
      }
      catch (IllegalAccessException ex)
      {
         java.util.logging.Logger.getLogger (BlackRockCityMapUI.class.getName ()).log (java.util.logging.Level.SEVERE, null, ex);
      }
      catch (javax.swing.UnsupportedLookAndFeelException ex)
      {
         java.util.logging.Logger.getLogger (BlackRockCityMapUI.class.getName ()).log (java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>
      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater (new Runnable ()
      {
         public void run ()
         {
            new BlackRockCityMapUI ().setVisible (true);
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JComboBox<String> cbYear;
   private javax.swing.JButton jButton1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JPanel jPanel1;
   private org.apache.batik.swing.JSVGCanvas jSVGCanvas1;
   // End of variables declaration//GEN-END:variables
}

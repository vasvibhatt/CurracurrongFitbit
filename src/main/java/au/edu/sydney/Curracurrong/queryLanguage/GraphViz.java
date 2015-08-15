/**
 * Copyright 2014 University of Sydney, Australia.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.edu.sydney.Curracurrong.queryLanguage;


import java.io.File;
import java.awt.image.*;
import javax.imageio.*;

/**
 * This class visualizes a stream graph by using Grappa graph drawing package
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class GraphViz implements Constants {

   /**
    * Constructor: creates a new GraphViz object that will contain
    * a graph.
    */
   public GraphViz() {


   }


   /**
    * Returns the graph as an image in binary format.
    * @param dotFileName Source of the graph to be drawn.
    * @return A byte array containing the image of the graph.
    */
   BufferedImage getGraph(String dotFileName)
   {
      String path = '"' + dotFileName + '"';      
      File imgFile;
      BufferedImage buffImage = null;

      try {
         imgFile = File.createTempFile("graph_", ".gif", new File(TEMP_DIR));
   
         Runtime rt = Runtime.getRuntime();         
         String cmd = DOT + " -Tgif "+path+" -o "+imgFile.getAbsolutePath();
         Process p = rt.exec(cmd);
         p.waitFor();

        buffImage = ImageIO.read(imgFile);
         if (imgFile.delete() == false)
            System.err.println("Warning: "+imgFile.getAbsolutePath()+" could not be deleted!"); 
      }
      catch (java.io.IOException ioe) {
         System.err.println("Error:    in I/O processing of tempfile in dir "+TEMP_DIR+"\n");
         System.err.println("       or in calling external command");
         ioe.printStackTrace();
      }
      catch (java.lang.InterruptedException ie) {
         System.err.println("Error: the execution of the external program was interrupted");
         ie.printStackTrace();
      }

      return buffImage;
   }
}
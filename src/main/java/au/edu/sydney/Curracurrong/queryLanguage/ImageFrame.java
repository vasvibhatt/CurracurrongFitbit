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

import java.awt.image.*;
import javax.swing.*;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class ImageFrame {

    private BufferedImage image;

    /**
     * Constructor of class 
     * @param img input image in BufferedImage format
     */
    public ImageFrame(BufferedImage img) {
        image = img;
    }

    /**
     * Creates and show image frame
     * @param h height of frame
     * @param w width of frame
     */
    @SuppressWarnings("deprecation")
    public void show(int h, int w) {
        JFrame frame = new JFrame();
        frame.setSize(w+30, h+30);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        ImagePanel panel = new ImagePanel(image);      
        frame.getContentPane().add(panel);         
        frame.show();
    }
}

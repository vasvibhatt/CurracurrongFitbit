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

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

/**
 * Creates panel contains image which gets in its constructor
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class ImagePanel extends JPanel {
	
    private static final long serialVersionUID = 1L;
    private BufferedImage backgroundImage;

    /**
     * Constructor
     * @param img input image in BufferedImage format
     */
    public ImagePanel(BufferedImage img) {
            super();

            this.setBackground(Color.white);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());

            ImageIcon icon = new ImageIcon(img);
            JLabel label = new JLabel();
            label.setIcon(icon);
            this.add(label);


            JScrollPane scrollPane = new JScrollPane(label);
            setPreferredSize(new Dimension(450, 500));
            this.add(scrollPane, BorderLayout.CENTER);


    }

    @Override
    public void paintComponent(Graphics g) {
            super.paintComponents(g);
            g.drawImage(this.backgroundImage, 0, 0, null);
    }
}

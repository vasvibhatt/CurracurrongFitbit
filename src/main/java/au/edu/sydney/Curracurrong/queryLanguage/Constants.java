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

/**
 * This class keeps the constant variables of the system
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public interface Constants {
	// Default directory for creating .dot and image files
	public static final String TEMP_DIR = "/tmp/";

	// Default directory for the results
	public static final String RESULTS_DIR = "/tmp/";

	// Default sample rate of a sense operation
	// if sample rate is not explicitly specified.
	public static final double DEFAULT_SAMPLE_RATE = 10;

	// package name for using in class.forname
	public static final String PACKAGE_NAME = "au.edu.sydney.Curracurrong.queryLanguage";

	// installed (Graphviz) dot executable
	public static final String DOT = "C:\\Program Files\\Graphviz2.26.3\\bin\\dot.exe";
}

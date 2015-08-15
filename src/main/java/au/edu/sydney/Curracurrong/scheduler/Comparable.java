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

package au.edu.sydney.Curracurrong.scheduler;


/**
 * An implementation of the standard java.lang.Comparable interface not included
 * within the Squawk API (<a
 * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Comparable.html"
 * >http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Comparable.html</a>)
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public interface Comparable {
	/**
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less than,
	 * equal to, or greater than the specified object.
	 * 
	 * @param o the object to be compared
	 * @return a negative integer, zero, or a positive integer as this object is
	 * less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Object o);
}

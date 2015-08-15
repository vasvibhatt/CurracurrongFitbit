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

package au.edu.sydney.Curracurrong.util;


/**
 * An unique integer generator. ID numbers start at 0 and reaches a maximum of
 * 2^64 = 214783647. Calling next() after this will throw a
 * IntegerOverflowException.
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class IDGenerator {
	private int current;
	
	public IDGenerator() {
		current = 0;
	}
	
	public IDGenerator(int start) {
		current = start;
	}
	
	/**
	 * Gets the next ID
	 * 
	 * @return an integer representing the next serial number
	 * @throws IntegerOverflowException
	 */
	public synchronized int next() throws IntegerOverflowException {
		//checks the id isnt maxed out
		if (current == Integer.MAX_VALUE) {
			reset();
			throw new IntegerOverflowException();
		}
		
		//increments the id for the group and returns
		current++;
		return current;
	}
	
	/**
	 * Resets the id to zero
	 */
	public synchronized void reset() {
		current = 0;
	}
	
	/**
	 * Set the id to a given value
	 * 
	 * @param val the value to set the current ID to
	 */
	public synchronized void set(int val) {
		current = val;
	}
	
	/**
	 * Return the current id
	 * 
	 * @return the current id
	 */
	public synchronized int current() {
		return current;
	}
	
	@SuppressWarnings("serial")
	private class IntegerOverflowException extends RuntimeException {
	}
}

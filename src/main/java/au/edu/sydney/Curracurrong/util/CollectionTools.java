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


import java.util.Vector;

/**
 * A collection of methods useful for processing "collections" with the Squawk
 * SDK.
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public final class CollectionTools {
	// hidden constructor for static only access
	private CollectionTools() {
	}
	
	/**
	 * Fills all elements in an array with a given value
	 * 
	 * @param array the array to fill
	 * @param value the default value
	 * @return the orignal array filled up with the default value
	 */
	public static int[] fill(int[] array, int value) {
		if (array != null)
			for (int i = 0; i != array.length; i++)
				array[i] = value;
		return array;
	}
	
	/**
	 * Fills all elements in an array with a given value
	 * 
	 * @param array the array to fill
	 * @param value the default value
	 * @return the orignal array filled up with the default value
	 */
	public static Object[] fill(Object[] array, Object value) {
		if (array != null)
			for (int i = 0; i != array.length; i++)
				array[i] = value;
		return array;
	}
	
	/**
	 * Converts a vector containing {@link Byte}'s into an array containing the
	 * primative equivilants of their Object wrapper values
	 * 
	 * @param vectorOfBytes the vector containing the values
	 * @return the primativ-arazed version of the vector
	 */
	public static byte[] vectorToArrayB(Vector vectorOfBytes) {
		byte[] array = new byte[vectorOfBytes.size()];
		for (int i = 0; i != vectorOfBytes.size(); i++)
			array[i] = ((Byte) vectorOfBytes.elementAt(i)).byteValue();
		return array;
	}
	
	/**
	 * Converts a vector containing {@link Long}'s into an array containing the
	 * primative equivilants of their Object wrapper values
	 * 
	 * @param vectorOfLongs the vector containing the values
	 * @return the primativ-arazed version of the vector
	 */
	public static long[] vectorToArrayL(Vector vectorOfLongs) {
		long[] array = new long[vectorOfLongs.size()];
		for (int i = 0; i != vectorOfLongs.size(); i++)
			array[i] = ((Long) vectorOfLongs.elementAt(i)).longValue();
		return array;
	}
	
	/**
	 * Converts a vector containing any objects into an array of {@link String} 
	 * 's, each being the {@link Object#toString()} version of each object
	 * 
	 * @param vector the vector
	 * @return the toString()'d version of all the objects in the vector
	 */
	public static String[] vectorToArrayS(Vector vector) {
		String[] array = new String[vector.size()];
		for (int i = 0; i != array.length; ++i)
			array[i] = vector.elementAt(i).toString();
		return array;
	}
	
	/**
	 * Copies a vector of objects into an array of objects
	 * 
	 * @param vector the vector
	 * @return array version of the provided vector
	 */
	public static Object[] vectorToArrayO(Vector vector) {
		Object[] array = new Object[vector.size()];
		vector.copyInto(array);
		return array;
	}
}

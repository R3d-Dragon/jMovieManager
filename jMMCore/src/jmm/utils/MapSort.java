/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Klasse um eine Map<K,V> nach ihrem Value zu sortieren
 *
 * @author Bryan Beck
 */
public class MapSort{

	/** inner class to do soring of the map **/
        public static class ValueComparer<K, V extends Comparable<V>> implements Comparator<K> {
            private final Map<K, V> map;

            public ValueComparer(Map<K, V> map) {
            this.map = map;
            }

            // Vergleiche 2 Values einer Map (absteigende Reihenfolge)
            @Override
            public int compare(K key1, K key2) {
                V value1 = this.map.get(key1);
                V value2 = this.map.get(key2);
                int c = value2.compareTo(value1);
                if (c != 0) {
                    return c;
                }
                if(value1 == null || value2 == null){
                    throw new RuntimeException("One of the values to compare is null" + value1 + " " + value2);
                }
                Integer hashCode1 = key1.hashCode();
                Integer hashCode2 = key2.hashCode();
                return hashCode1.compareTo(hashCode2);
            }
        }
}

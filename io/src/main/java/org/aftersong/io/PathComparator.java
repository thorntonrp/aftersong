/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.io;

import java.nio.file.Path;
import java.util.Comparator;

/**
 *
 * @author Robert P. Thornton
 */
public class PathComparator implements Comparator<Path> {

	@Override
	public int compare(Path o1, Path o2) {
		int nc1 = o1.getNameCount();
		int nc2 = o2.getNameCount();
		if (nc1 == 0 && nc2 == 0) {
			return o1.getFileName().toString().compareToIgnoreCase(o2.getFileName().toString());
		}
		for (int i = 0; i < nc1 && i < nc2; i++) {
			Path p1 = o1.getName(i);
			Path p2 = o2.getName(i);
			boolean dir1 = i < nc1 - 1;
			boolean dir2 = i < nc2 - 1;
			if (dir1 == dir2) {
				int n = p1.toString().compareToIgnoreCase(p2.toString());
				if (n != 0) {
					return n;
				}
			} else if (dir1) {
				return 1;
			} else if (dir2) {
				return -1;
			}
		}
		return nc1 - nc2;
	}
}

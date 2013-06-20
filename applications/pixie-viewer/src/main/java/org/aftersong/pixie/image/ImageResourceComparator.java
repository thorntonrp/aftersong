/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.image;

import java.nio.file.Path;
import java.util.Comparator;

/**
 *
 * @author Robert P. Thornton
 */
public class ImageResourceComparator implements Comparator<ImageResource> {

	private boolean ignoreCase;

	public ImageResourceComparator ignoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	@Override
	public int compare(ImageResource ir1, ImageResource ir2) {
		Path o1;
		Path o2;

		if (ir1.getArchiveEntry() != null) {
			o1 = ir1.getArchivePath().resolve(ir1.getArchiveEntry());
		} else {
			o1 = ir1.getImagePath();
		}

		if (ir2.getArchiveEntry() != null) {
			o2 = ir2.getArchivePath().resolve(ir2.getArchiveEntry());
		} else {
			o2 = ir2.getImagePath();
		}

		int nc1 = o1.getNameCount();
		int nc2 = o2.getNameCount();

		if (nc1 == 0 && nc2 == 0) {
			return compare(o1.getFileName().toString(), o2.getFileName().toString());
		}

		for (int i = 0; i < nc1 && i < nc2; i++) {
			Path p1 = o1.getName(i);
			Path p2 = o2.getName(i);
			boolean dir1 = i < nc1 - 1;
			boolean dir2 = i < nc2 - 1;
			if (dir1 == dir2) {
				int n = compare(p1.toString(), p2.toString());
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

	private int compare(String s1, String s2) {
		if (ignoreCase) {
			return s1.compareToIgnoreCase(s2);
		} else {
			return s1.compareTo(s2);
		}
	}
}

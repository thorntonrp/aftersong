/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.aftersong.collections.Lists;
import org.aftersong.collections.Maps;
import org.aftersong.pixie.image.ImageResource;
import org.aftersong.pixie.image.ImageResourceRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Robert P. Thornton
 */
@Component
public class ImageCanvasModel {

	private final List<ImageResource> imageResources;
	private final Map<ImageResource, Integer> imageResourceIndices;

	private final int first = 0;
	private final int last;
	private final int size;

	private int current = -1;

	@Inject
	public ImageCanvasModel(ImageResourceRepository imageResourceRepository) {
		imageResources = Lists.newList(imageResourceRepository.getImageResources());
		size = imageResources.size();
		last = size - 1;
		imageResourceIndices = Maps.newMap();
		for (int i = 0; i < size; i++) {
			imageResourceIndices.put(imageResources.get(i), i);
		}
	}

	public ImageResource get() {
		return imageResources.get(current);
	}

	public void set(int newIndex) {
		current = newIndex;
	}

	public ImageResource get(int index) {
		return imageResources.get(index);
	}

	public int size() {
		return size;
	}

	public int current() {
		return current;
	}

	public int next() {
		current = current == last ? first : current + 1;
		return current;
	}

	public int previous() {
		current = current == first ? last : current - 1;
		return current;
	}

	public int nextFolder() {
		Path currentFolder = imageResources.get(current).getImagePath().getParent();
		for (int i = current + 1; i < size; i++) {
			Path folder = imageResources.get(i).getImagePath().getParent();
			if (!folder.equals(currentFolder)) {
				current = i;
				return current;
			}
		}
		for (int i = 0; i < current; i++) {
			Path folder = imageResources.get(i).getImagePath().getParent();
			if (!folder.equals(currentFolder)) {
				current = i;
				return current;
			}
		}
		return -1;
	}

	public int previousFolder() {
		Path currentFolder = imageResources.get(current).getImagePath().getParent();
		for (int i = current - 1; i >= 0; i--) {
			Path folder = imageResources.get(i).getImagePath().getParent();
			if (!folder.equals(currentFolder)) {
				current = i;
				return current;
			}
		}
		for (int i = last; i > current; i--) {
			Path folder = imageResources.get(i).getImagePath().getParent();
			if (!folder.equals(currentFolder)) {
				current = i;
				return current;
			}
		}
		return -1;
	}

	public int nextMatch(String namePattern) {
		for (int i = current + 1; i < size; i++) {
			ImageResource resource = imageResources.get(i);
			if (resource.getImageUri().matches(namePattern)) {
				current = i;
				return current;
			}
		}
		for (int i = 0; i < current; i++) {
			ImageResource resource = imageResources.get(i);
			if (resource.getImageUri().matches(namePattern)) {
				current = i;
				return current;
			}
		}
		return -1;
	}

	public int previousMatch(String namePattern) {
		for (int i = current - 1; i >= 0; i--) {
			ImageResource resource = imageResources.get(i);
			if (resource.getImageUri().matches(namePattern)) {
				current = i;
				return current;
			}
		}
		for (int i = last; i > current; i--) {
			ImageResource resource = imageResources.get(i);
			if (resource.getImageUri().matches(namePattern)) {
				current = i;
				return current;
			}
		}
		return -1;
	}
}

/*
 * Copyright 2013 Robert P. Thornton. All rights reserved.
 * This notice may not be removed.
 */
package org.aftersong.pixie.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.aftersong.collections.Lists;
import org.aftersong.collections.Maps;
import org.aftersong.collections.Objects;

/**
 *
 * @author Robert P. Thornton
 */
public class DefaultImageResourceMetaDataRepository implements ImageResourceMetaDataRepository {

	private final File modelFile = Paths.get(
			System.getProperty("user.home"), "Pictures", "pixie.xml").toFile();

	private Map<String, ImageResourceMetaData> metaDataMap = Maps.newMap();
	private Map<String, String> aliases = Maps.newMap();

	@PostConstruct
	public void load() throws IOException {
		if (modelFile.exists()) {
			try {
				JAXBContext context = JAXBContext.newInstance(ImageResourceMetaDataModel.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				ImageResourceMetaDataModel model = (ImageResourceMetaDataModel) unmarshaller.unmarshal(modelFile);
				aliases.putAll(model.aliases());
				for (ImageResourceMetaData metaData : model) {
					metaDataMap.put(metaData.getImageUri(), metaData);
				}
			} catch (JAXBException ex) {
				throw new IOException(ex);
			}
		}
	}

	@PreDestroy
	public void flush() throws IOException {
		try {
			String baseUri = modelFile.getParentFile().toPath().toUri().toString();
			List<String> imageUris = Lists.newList(metaDataMap.keySet());
			Collections.sort(imageUris);
			List<ImageResourceMetaData> metaDataList = Lists.newList();
			for (String uri : imageUris) {
				if (uri.startsWith(baseUri)) {
					
				}
				metaDataList.add(metaDataMap.get(uri));
			}
			ImageResourceMetaDataModel model = new ImageResourceMetaDataModel(metaDataList, aliases);
			JAXBContext context = JAXBContext.newInstance(ImageResourceMetaDataModel.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.marshal(model, modelFile);
		} catch (JAXBException ex) {
			throw new IOException(ex);
		}
	}

	@Override
	public ImageResourceMetaData getImageResourceMetaData(String imageUri) {
		ImageResourceMetaData result = metaDataMap.get(imageUri);
		if (result == null) {
			for (Map.Entry<String, String> alias : Objects.in(aliases)) {
				if (imageUri.startsWith(alias.getKey())) {
					String uri = alias.getValue() + imageUri.substring(alias.getKey().length());
					result = metaDataMap.get(uri);
					if (result != null) {
						return result;
					}
				}
			}
			if (result == null) {
				result = new ImageResourceMetaData(imageUri);
				metaDataMap.put(imageUri, result);
			}
		}
		return result;
	}

	@XmlRootElement(name = "pixie")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class ImageResourceMetaDataModel implements Iterable<ImageResourceMetaData> {

		@XmlElementRef
		@XmlElementWrapper(name = "folder-aliases")
		private List<Alias> aliases;
		
		@XmlElementRef
		@XmlElementWrapper(name = "image-resources")
		private List<ImageResourceMetaData> metaData;

		ImageResourceMetaDataModel() {
		}

		ImageResourceMetaDataModel(List<ImageResourceMetaData> metaData, Map<String, String> aliases) {
			this.metaData = metaData;
			this.aliases = Lists.newList();
			for (Map.Entry<String, String> entry : Objects.in(aliases)) {
				this.aliases.add(new Alias(entry.getKey(), entry.getValue()));
			}
		}

		@Override
		public Iterator<ImageResourceMetaData> iterator() {
			return metaData.iterator();
		}

		public Map<String, String> aliases() {
			Map results = Maps.newMap();
			for (Alias alias : Objects.in(aliases)) {
				results.put(alias.forValue, alias.alternateValue);
			}
			return results;
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Alias {

		@XmlAttribute(name = "for")
		private String forValue;

		@XmlAttribute(name = "alternate")
		private String alternateValue;

		Alias() {}

		Alias(String originalValue, String alternateValue) {
			this.forValue = originalValue;
			this.alternateValue = alternateValue;
		}
	}
}

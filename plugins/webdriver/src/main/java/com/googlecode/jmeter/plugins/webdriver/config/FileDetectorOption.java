package com.googlecode.jmeter.plugins.webdriver.config;

import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.UselessFileDetector;

public enum FileDetectorOption {
	LOCAL("Local file detector", LocalFileDetector.class),
	USELESS("Useless file detector", UselessFileDetector.class);

	private final String name;

	private final Class<? extends FileDetector> clazz;

	FileDetectorOption(String name, Class<? extends FileDetector> clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public Class<? extends FileDetector> getClazz() {
		return clazz;
	}

	@Override
	public String toString() {
		return getName();
	}
}

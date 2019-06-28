package com.mindlin.jsast.impl.parser;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mindlin.nautilus.fs.SourceRange;

@NonNullByDefault
public abstract class AbstractJSParser {
	protected JSFeatureSet features;

	public AbstractJSParser(JSFeatureSet features) {
		this.features = features;
	}
	
	public JSFeatureSet getFeatureSet() {
		return this.features;
	}
	
	// Internal helper methods
	protected boolean supports(JSFeature feature) {
		return this.features.supports(feature);
	}
	
	protected void require(JSFeature feature, SourceRange range) {
		if (!this.supports(feature))
			throw new JSUnsupportedFeatureException(feature, range);//TODO
	}
}

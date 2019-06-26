package com.mindlin.jsast.impl.parser;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mindlin.nautilus.fs.SourceRange;

@NonNullByDefault
public class JSUnsupportedFeatureException extends JSParserException {
	private static final long serialVersionUID = 2336494112798933810L;
	
	protected @Nullable SourceRange range;
	protected JSFeature feature;
	
	public JSUnsupportedFeatureException(JSFeature feature) {
		this(feature, null);
	}
	
	public JSUnsupportedFeatureException(JSFeature feature, @Nullable SourceRange range) {
		this.range = range;
		this.feature = feature;
	}

	public @Nullable SourceRange getRange() {
		return range;
	}

	public JSFeature getFeature() {
		return feature;
	}
}

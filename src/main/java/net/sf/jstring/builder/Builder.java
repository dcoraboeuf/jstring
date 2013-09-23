package net.sf.jstring.builder;

public abstract class Builder<T> {
	
	public abstract T build();

    public abstract void merge (T source, BundleValueMergeMode mode);

}

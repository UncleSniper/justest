package org.unclesniper.test;

public interface ObjectFactory<InstanceT, ExceptionT extends Throwable> {

	InstanceT newObject() throws ExceptionT;

}

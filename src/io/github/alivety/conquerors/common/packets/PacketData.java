package io.github.alivety.conquerors.common.packets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.alivety.conquerors.common.events.Common;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketData {
	String description() default "N/A";

	int id();

	Class<?> recv() default Common.class;
}

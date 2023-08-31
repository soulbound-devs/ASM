package net.vakror.soulbound.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Soulbound extensions can annotate their extension class with this to automatically register it to the API. This method of registration is useful because it allows for soft dependencies
 * <br>
 * Using annotation <b>CANNOT</b> be done if the extension's constructor(s) have more than zero arguments
 * <br>
 * <br>
 * Annotated classes <b>MUST</b> implement {@link ISoulboundExtension}
 * Multiple extensions can be loaded per mod
 */
@Target(value = ElementType.TYPE)
public @interface Extension {}

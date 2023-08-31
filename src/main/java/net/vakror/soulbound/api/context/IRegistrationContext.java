package net.vakror.soulbound.api.context;

/**
 * Simple class for contexts to extend to get their name.
 * Name is used in extensions to know which context to use to register
 */
public interface IRegistrationContext {
    /**
     * @return the name of this context (soulbound uses "default").
     * USE SOMETHING UNIQUE, as you or someone else might accidentally use another person's context causing issues!
     * DO NOT USE DEFAULT
     */
    public String getContextName();
}

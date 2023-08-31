package net.vakror.soulbound.api.context;

public interface IRegistrationContext {
    /**
     * @return the name of this context (soulbound uses "default").
     * USE SOMETHING UNIQUE, as you or someone else might accidentally use another person's context causing issues!
     * DO NOT USE DEFAULT
     */
    public String getContextName();
}

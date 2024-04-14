package net.vakror.soulbound.model.wand.api;

import com.google.gson.JsonObject;

import java.util.List;

public interface IWandModelReader {
    List<AbstractWandLayer> getLayers(JsonObject model);
}

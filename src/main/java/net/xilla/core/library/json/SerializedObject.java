package net.xilla.core.library.json;

public interface SerializedObject {

    XillaJson getSerializedData();

    void loadSerializedData(XillaJson json);

}

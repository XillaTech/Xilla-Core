package net.xilla.core.reflection;

import lombok.Getter;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionManager extends Manager<Class, Reflection> {

    @Getter
    private static ReflectionManager instance = new ReflectionManager();

    public ReflectionManager() {
        super("Reflection", Reflection.class);

        instance = this;

        initialize();
    }

    public void initialize() {
        // INTEGER
        put(new Reflection<Integer>(Integer.class) {
            @Override
            public Integer loadFromSerializedData(Object obj, Field field, Object object) {
                return Integer.parseInt(object.toString());
            }

            @Override
            public Object getSerializedData(Object obj, Field field, Integer object) {
                return object;
            }
        });

        // LONG
        put(new Reflection<Long>(Long.class) {
            @Override
            public Long loadFromSerializedData(Object obj, Field field, Object object) {
                return Long.parseLong(object.toString());
            }

            @Override
            public Object getSerializedData(Object obj, Field field, Long object) {
                return object;
            }
        });

        // DOUBLE
        put(new Reflection<Double>(Double.class) {
            @Override
            public Double loadFromSerializedData(Object obj, Field field, Object object) {
                return Double.parseDouble(object.toString());
            }

            @Override
            public Object getSerializedData(Object obj, Field field, Double object) {
                return object;
            }
        });

        // FLOAT
        put(new Reflection<Float>(Float.class) {
            @Override
            public Float loadFromSerializedData(Object obj, Field field, Object object) {
                return Float.parseFloat(object.toString());
            }

            @Override
            public Object getSerializedData(Object obj, Field field, Float object) {
                return object;
            }
        });

        // BYTE
        put(new Reflection<Byte>(Byte.class) {
            @Override
            public Byte loadFromSerializedData(Object obj, Field field, Object object) {
                return Byte.parseByte(object.toString());
            }

            @Override
            public Object getSerializedData(Object obj, Field field, Byte object) {
                return object;
            }
        });

        // SHORT
        put(new Reflection<Short>(Short.class) {
            @Override
            public Short loadFromSerializedData(Object obj, Field field, Object object) {
                return Short.parseShort(object.toString());
            }

            @Override
            public Object getSerializedData(Object obj, Field field, Short object) {
                return object;
            }
        });

        // SERIALIZED OBJECTS / MANAGER OBJECTS
        put(new Reflection<SerializedObject>(SerializedObject.class) {

            @Override
            public SerializedObject loadFromSerializedData(Object obj, Field field, Object object) {
                try {
                    SerializedObject so = (SerializedObject)field.get(obj);
                    so.loadSerializedData(new XillaJson((JSONObject) object));
                    return so;
                } catch (IllegalAccessException e) {
                    Logger.log(LogLevel.ERROR, "Error while loading data from serialized object " + field.getName(), getClass());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object getSerializedData(Object obj, Field field, SerializedObject object) {
                return object.getSerializedData();
            }
        });
    }

}

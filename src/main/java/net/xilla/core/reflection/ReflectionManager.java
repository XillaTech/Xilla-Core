package net.xilla.core.reflection;

import lombok.Getter;
import net.xilla.core.library.config.ConfigFile;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReflectionManager extends Manager<Class, Reflection> {

    @Getter
    private static ReflectionManager instance = new ReflectionManager();

    public ReflectionManager() {
        super("Reflection", Reflection.class);

        instance = this;

        initialize();
    }

    public void initialize() {
        // STRING
        put(new Reflection<String>(String.class) {
            @Override
            public String loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return object.toString();
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, String object) {
                return object;
            }
        });

        // INTEGER
        put(new Reflection<Integer>(Integer.class) {
            @Override
            public Integer loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return Integer.parseInt(object.toString());
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, Integer object) {
                return object;
            }
        });

        // LONG
        put(new Reflection<Long>(Long.class) {
            @Override
            public Long loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return Long.parseLong(object.toString());
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, Long object) {
                return object;
            }
        });

        // DOUBLE
        put(new Reflection<Double>(Double.class) {
            @Override
            public Double loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return Double.parseDouble(object.toString());
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, Double object) {
                return object;
            }
        });

        // FLOAT
        put(new Reflection<Float>(Float.class) {
            @Override
            public Float loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return Float.parseFloat(object.toString());
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, Float object) {
                return object;
            }
        });

        // BYTE
        put(new Reflection<Byte>(Byte.class) {
            @Override
            public Byte loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return Byte.parseByte(object.toString());
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, Byte object) {
                return object;
            }
        });

        // SHORT
        put(new Reflection<Short>(Short.class) {
            @Override
            public Short loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return Short.parseShort(object.toString());
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, Short object) {
                return object;
            }
        });

        // UUID
        put(new Reflection<UUID>(UUID.class) {
            @Override
            public UUID loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                return UUID.fromString(object.toString());
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, UUID object) {
                return object.toString();
            }
        });

//        // UUID
//        put(new Reflection<List>(List.class) {
//            @Override
//            public List loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
//                Type type = field.getGenericType();
//                System.out.println("type: " + type);
//                if (type instanceof ParameterizedType) {
//                    ParameterizedType pt = (ParameterizedType) type;
//                    System.out.println("raw type: " + pt.getRawType());
//                    System.out.println("owner type: " + pt.getOwnerType());
//                    System.out.println("actual type args:");
//                    for (Type t : pt.getActualTypeArguments()) {
//                        System.out.println("    " + t);
//                    }
//                }
//
//                System.out.println();
//
//                try {
//                    Object list = field.get(object);
//                    System.out.println("obj: " + obj);
//                    System.out.println("obj class: " + obj.getClass());
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                return new ArrayList();
//            }
//
//            @Override
//            public Object getSerializedData(ConfigFile file, Object o, Field field, List object) {
//                List<Object> list = new ArrayList<>();
//                List<Object> data = (List<Object>)object;
//
//                for(Object temp : data) {
//                    Reflection reflection = ReflectionManager.getInstance().get(temp.getClass());
//
//                    if(reflection != null) {
//                        if (temp instanceof SerializedObject) {
//                            reflection = ReflectionManager.getInstance().get(SerializedObject.class);
//                        }
//
//                        try {
//                            Object loaded = reflection.loadFromSerializedData(file, this, field, temp);
//                            if(loaded != null) {
//                                field.set(this, loaded);
//                            } else {
//                                throw new Exception("Failed to load serialized data, as it returned null");
//                            }
//                        } catch (Exception ex) {
//                            Logger.log(LogLevel.ERROR, "Failed to set variable " + field.getName(), getClass());
//                            Logger.log(ex, getClass());
//                        }
//                    } else {
//                        try {
//                            Object obj = field.get(this);
//                            if (obj instanceof SerializedObject) {
//                                reflection = ReflectionManager.getInstance().get(SerializedObject.class);
//
//                                try {
//                                    Object loaded = reflection.loadFromSerializedData(file, this, field, temp);
//                                    if(loaded != null) {
//                                        list.add(loaded);
//                                    } else {
//                                        throw new Exception("Failed to load serialized data, as it returned null");
//                                    }
//                                } catch (Exception ex) {
//                                    Logger.log(LogLevel.ERROR, "Failed to set variable " + field.getName(), getClass());
//                                    Logger.log(ex, getClass());
//                                }
//                            }
//
//
//                            list.add(temp);
//                        } catch (Exception ex) {
//                            Logger.log(LogLevel.ERROR, "Failed to load variable " + field.getName(), getClass());
//                            Logger.log(ex, getClass());
//                        }
//                    }
//                }
//
//                return list;
//            }
//        });

        // SERIALIZED OBJECTS / MANAGER OBJECTS
        put(new Reflection<SerializedObject>(SerializedObject.class) {

            @Override
            public SerializedObject loadFromSerializedData(ConfigFile file, Object obj, Field field, Object object) {
                try {
                    SerializedObject so = (SerializedObject)field.get(obj);
                    so.loadSerializedData(new XillaJson(new JSONObject((Map)object)));
                    return so;
                } catch (IllegalAccessException e) {
                    Logger.log(LogLevel.ERROR, "Error while loading data from serialized object " + field.getName(), getClass());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object getSerializedData(ConfigFile file, Object obj, Field field, SerializedObject object) {
                return object.getSerializedData().getJson();
            }
        });
    }

}

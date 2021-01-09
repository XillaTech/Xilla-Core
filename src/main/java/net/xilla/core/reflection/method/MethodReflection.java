package net.xilla.core.reflection.method;

import lombok.Getter;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.reflection.InvalidReflectionException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodReflection extends ManagerObject {

    @Getter
    private Map<String, Method> methods = new ConcurrentHashMap<>();

    public MethodReflection(String identifier) {
        super(identifier, "StorageReflection");
    }

    public void put(String name, Method method) {
        methods.put(name, method);
    }

    public Method get(String name) {
        return methods.get(name);
    }

    public Method remove(String name) {
        return methods.remove(name);
    }

    public boolean containsKey(String name) {
        return methods.containsKey(name);
    }

    public Object run(String name, Object... data) throws InvalidReflectionException {
        Method method = get(name);
        if(method == null) {
            throw new InvalidReflectionException("No method found by that name!");
        }
        return method.run(data);
    }

}

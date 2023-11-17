package bank;

import com.google.gson.*;

import java.lang.reflect.*;
import java.util.Map;

/**
 * class for serializing transaction objects
 */
public class TransactionSerializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {
    /**
     * serializer for transaction objects
     * @param p_object object for serialization
     * @param p_type not used (for generics)
     * @param p_context for complex types
     * @return serialized json object
     */
    @Override
    public JsonElement serialize(Transaction p_object, Type p_type, JsonSerializationContext p_context) {
        JsonObject jsonObject = new JsonObject();

        String className = p_object.getClass().getSimpleName();

        //add CLASSNAME as json property
        jsonObject.addProperty("CLASSNAME", className);

        JsonObject instanceObject = new JsonObject();

        Class<?> objectClass = p_object.getClass();

        //I HATE JAVA
        Field[] fieldsDerived = objectClass.getDeclaredFields();
        for (Field field : fieldsDerived) {
            field.setAccessible(true);
            try {
                Object value = field.get(p_object);
                instanceObject.add(field.getName(), p_context.serialize(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //I HATE JAVA
        int inheritanceTree = 2;
        while(inheritanceTree >= 0) {
            objectClass = objectClass.getSuperclass();
            if(objectClass == null) break;
            Field[] fieldsBase = objectClass.getDeclaredFields();
            for (Field field : fieldsBase) {
                field.setAccessible(true);
                try {
                    Object value = field.get(p_object);
                    instanceObject.add(field.getName(), p_context.serialize(value));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            inheritanceTree--;
        }

        //add INSTANCE as json property
        jsonObject.add("INSTANCE", instanceObject);

        return jsonObject;
    }

    /**
     * deserializer for json objects
     * @param p_json json element for deserialization
     * @param p_type not used (for generics)
     * @param p_context used for multiple json objects
     * @return deserialized json object
     * @throws JsonParseException should never happen, but just in case class cannot be found
     */
    @Override
    public Transaction deserialize(JsonElement p_json, Type p_type, JsonDeserializationContext p_context) throws JsonParseException {
        JsonObject jsonObject = p_json.getAsJsonObject();

        //read CLASSNAME from json property
        JsonPrimitive classNameElement = jsonObject.getAsJsonPrimitive("CLASSNAME");
        JsonElement instanceElement = jsonObject.get("INSTANCE");

        if (classNameElement == null || instanceElement == null || !instanceElement.isJsonObject()) {
            throw new JsonParseException("Invalid JSON format");
        }

        String className = classNameElement.getAsString();

        try {
            Class<?> transactionClass = Class.forName("bank." + className);

            Object transactionObject = transactionClass.getDeclaredConstructor().newInstance();

            JsonObject instanceObject = instanceElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : instanceObject.entrySet()) {
                String fieldName = entry.getKey();
                JsonElement fieldValue = entry.getValue();

                try {
                    //I HATE JAVA
                    Field field = transactionClass.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    Object deserializedFieldValue = p_context.deserialize(fieldValue, field.getType());

                    field.set(transactionObject, deserializedFieldValue);
                } catch (NoSuchFieldException e1) {
                    Class<?> transactionSuperClass = transactionClass;
                    int inheritanceTree = 2;

                    while(inheritanceTree >= 0) {
                        transactionSuperClass = transactionSuperClass.getSuperclass();
                        if(transactionSuperClass == null) break;
                        //I HATE JAVA
                        try {
                            Field field = transactionClass.getSuperclass().getDeclaredField(fieldName);
                            field.setAccessible(true);

                            Object deserializedFieldValue = p_context.deserialize(fieldValue, field.getType());

                            field.set(transactionObject, deserializedFieldValue);
                        } catch (NoSuchFieldException e2) {
                            //this is to not trigger NoSuchFieldException
                        } catch (Exception e3) {
                            throw new NoSuchFieldException();
                        }
                        inheritanceTree--;
                    }
                }
            }

            if (transactionObject instanceof Transaction) {
                return (Transaction) transactionObject;
            } else {
                throw new JsonParseException("Unsupported class: " + className);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            throw new JsonParseException(e);
        }
    }
}
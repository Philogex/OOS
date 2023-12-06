package bank;

import com.google.gson.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * class for serializing any object originating from transaction base class
 */
public class TransactionSerializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    /**
     * gets all attributes from child and superclasses (applicable to any class)
     * @param p_objectClass object to gather fields for
     * @return array of fields populated by object attributes
     */
    private Field[] getAllFields(Class<?> p_objectClass) {
        List<Field> fieldsList = new ArrayList<>();
        while (p_objectClass != null) {
            fieldsList.addAll(Arrays.asList(p_objectClass.getDeclaredFields()));
            p_objectClass = p_objectClass.getSuperclass();
        }
        return fieldsList.toArray(new Field[0]);
    }

    /**
     * serializer for transaction objects
     * @param p_object object for serialization
     * @param p_type not used (for generics)
     * @param p_context for complex types
     * @return serialized json object
     */
    @Override
    public JsonElement serialize(Transaction p_object, Type p_type, JsonSerializationContext p_context) throws JsonParseException {
        JsonObject jsonObject = new JsonObject();

        String className = p_object.getClass().getSimpleName();

        //add CLASSNAME as json property
        jsonObject.addProperty("CLASSNAME", className);

        JsonObject instanceObject = new JsonObject();

        Field[] fields = getAllFields(p_object.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(p_object);
                instanceObject.add(field.getName(), p_context.serialize(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //add INSTANCE as json property
        jsonObject.add("INSTANCE", instanceObject);

        return jsonObject;
    }

    /**
     * deserializer for json objects. only works for objects created from classes inside of bank package
     * @param p_json json element for deserialization
     * @param p_type not used (for generics)
     * @param p_context used for multiple json objects
     * @return deserialized json object
     * @throws JsonParseException should never happen, but just in case class cannot be found
     */
    @Override
    public Transaction deserialize(JsonElement p_json, Type p_type, JsonDeserializationContext p_context) throws JsonParseException {
        //i literally can't imagine anything going wrong with this, unless you manually manipulate the json files
        JsonObject jsonObject = p_json.getAsJsonObject();

        //read json elements
        JsonPrimitive classnameElement = jsonObject.getAsJsonPrimitive("CLASSNAME");
        JsonElement instanceElement = jsonObject.get("INSTANCE");

        if (classnameElement == null || instanceElement == null || !instanceElement.isJsonObject()) {
            throw new JsonParseException("Invalid JSON format");
        }

        String classname = classnameElement.getAsString();
        Object transactionObject = null;

        try {
            //get class of object from classname
            Class<?> transactionClass = Class.forName("bank." + classname);
            Field[] fields = getAllFields(transactionClass);

            if (instanceElement.isJsonObject()) {
                //instantiate new transaction object for storage
                transactionObject = transactionClass.getDeclaredConstructor().newInstance();

                //this is probably redundant, since an correct adapter has to be registered for this function to be used
                if (!(transactionObject instanceof Transaction)) {
                    throw new JsonParseException("Unsupported class: " + classname);
                }

                JsonObject instanceObject = instanceElement.getAsJsonObject();

                //write to attributes
                for (Field field : fields) {
                    String fieldName = field.getName();
                    JsonElement fieldValue = instanceObject.get(fieldName);

                    if (fieldValue != null) {
                        field.setAccessible(true);
                        Object deserializedFieldValue = p_context.deserialize(fieldValue, field.getType());
                        field.set(transactionObject, deserializedFieldValue);
                    }
                }
            }

            return (Transaction) transactionObject;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new JsonParseException(e);
        }
    }
}
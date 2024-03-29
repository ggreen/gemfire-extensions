package com.vmware.data.services.gemfire.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.patterns.conversion.Converter;
import org.apache.geode.json.JsonDocument;

/**
 * @author Gregory Green
 */
public class ToJsonFromNonPdxObject implements Converter<Object,String>
{
    private ObjectMapper om = new ObjectMapper();
    @Override
    public String convert(Object obj)
    {
        if(obj == null)
            return null;

        if(obj instanceof JsonDocument )
            return ((JsonDocument) obj).toJson();

        try{
            String json = om.writeValueAsString(obj);


            if(!(obj instanceof SerializationJsonEntryWrapper))
            {
                String prefix = new StringBuilder().append("{\"")
                        .append(GemFireJson.JSON_TYPE_ATTRIBUTE)
                        .append("\":\"")
                        .append(obj.getClass().getName())
                        .append("\", ").toString();

                json = json.replaceFirst("\\{",prefix);
            }

            return json;
        }
        catch(JsonProcessingException e)
        {
            throw new FormatException(e.getMessage(),e);
        }


    }
}

package newsfeeds.Utils;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by samarth on 17/11/14.
 */
public class Utils {

    private static ObjectMapper objectMapper = null;

    public static ObjectMapper getObjectMapper(){
        if (objectMapper == null) {
            synchronized (Utils.class) {
                if (objectMapper == null) {
                    ObjectMapper mapper = new ObjectMapper();
                    objectMapper = mapper;
                }
            }
        }
        return objectMapper;
    }

}

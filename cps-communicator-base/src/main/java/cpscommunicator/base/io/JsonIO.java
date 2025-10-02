package cpscommunicator.base.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.emfcloud.jackson.module.EMFModule;

import java.io.File;
import java.io.IOException;

public class JsonIO {
    public static <T> void saveToJson(T object, String filename) throws IOException {
        File file = new File(filename);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new EMFModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File(filename), object);
    }

    public static <T> T loadFromJson(String filename, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new EMFModule());
        return objectMapper.readValue(new File(filename), clazz);

    }
}
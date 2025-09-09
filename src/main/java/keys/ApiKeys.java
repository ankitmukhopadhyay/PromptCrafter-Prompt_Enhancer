package keys;

import org.springframework.beans.factory.annotation.Value;

public class ApiKeys {

    @Value("$huggingface.api.token:#{null}}")  private String huggingFaceToken;
    public String getHuggingFaceToken() {
        if (huggingFaceToken != null) {
            return huggingFaceToken;
        }

      throw new IllegalArgumentException("HuggingFace API token is not set");

    }
}


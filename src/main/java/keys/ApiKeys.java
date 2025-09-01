package keys;

public class ApiKeys {
    // Add your OpenAI Key before running examples
    public  static final String API_KEY_OPEN_AI = "";
    private static final String API_KEY = "";
    //public final static String API_ANTHROPIC = "";
    private static final String API_KEY_OpenRouter_Deepseek = "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";


    /**
     * Get API key from environment variable or system property.
     * @return API_KEY_OpenRouter
     */
    public static String getApiKey(){

        if (API_KEY_OpenRouter_Deepseek == null || API_KEY_OpenRouter_Deepseek.isEmpty()){
            throw new IllegalArgumentException("API key is not set");
        }

        return API_KEY_OpenRouter_Deepseek;

    }
}

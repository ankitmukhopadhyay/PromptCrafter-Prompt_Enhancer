//package org.model_interaction;
//
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import dev.langchain4j.model.openai.OpenAiChatModel;
//import keys.ApiKeys;
//
//// From: https://github.com/langchain4j/langchain4j-examples
//public class GivingInstructions {
//
//    private String refinedPrompt = "";
//
//    public String PromptRefiner(String oldPrompt) {
//        // Before running this example, set your OpenAI API key in ApiKeys class
////        ChatLanguageModel cl = OpenAiChatModel.withApiKey(ApiKeys.getApiKey());
//
//        //Building the chat model with OpenRouter Deepseek R1 model API
//        ChatLanguageModel cl = OpenAiChatModel.builder().apiKey(ApiKeys.getApiKey()).baseUrl("https://openrouter.ai/api/v1").modelName("deepseek/deepseek-r1-0528:free").build();
//
//        refinedPrompt = cl.generate("\""+oldPrompt+"\""+": Refine this prompt, following these principles, but keep the meaning intact, and make the prompt such that the contextual understanding is provided better, and the best results are attained to the point\".\n" +
//                " The foundation of a good prompt includes defining a clear Role for the AI, such as \"act as a senior web-developer at a Fortune 500 company\" \n" +
//                " , which shapes the tone and expertise of the response. This is followed by a specific Task, which outlines the primary objective, like \"guide me through important decisions and technical support while building a high-valued SaaS product\".\n" +
//                " The Requirements detail the necessary constraints and expectations, such as providing \"full, documented code\" or \"tabulating data as much as possible\".\n" +
//                " Finally, Instructions provide precise directives on how to execute the task, including the desired output format, style, and any specific rules, such as \"use 2-3 sentences to explain the concept of prompt engineering to a high school student\".\n" +
//                " This structure ensures clarity and specificity, which are critical for generating high-quality, relevant outputs.\n" +
//                " Using affirmative directives instead of negative ones, such as \"create\" rather than \"don't do,\" further improves the model's ability to follow instructions.\n" +
//                " Incorporating examples or real-world cases can also significantly enhance the quality of the output by providing a reference point for the desired style and structure.");
//
////        refinedPrompt = cl.generate("\""+oldPrompt+"\""+": Refine this prompt, following these principles, but keep the meaning intact, and make the prompt such that the contextual understanding is provided better, and the best results are attained to the point");
//        return refinedPrompt;
//    }
//

//}

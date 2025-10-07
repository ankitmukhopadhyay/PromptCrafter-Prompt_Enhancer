# PromptCrafter

**PromptCrafter is a web application that enhances user prompts using AI and rule-based methods.**

### Team Members:
* Mohamed Sharif
* Ankit Mukhopadhyay

### Tech Stack:
- Spring Boot
- Java
- MySQL
- LangChain4j
- OpenAI

### Problems Faced:
- HuggingFace API was not working, so had to switch to OpenAI API for model integration.
- Had to do lots of modifications and testing for the prompt template builder, to get the optimal results for each of the classification of the prompts.
- The "DETAILED" prompt style works the best, along with the context "GENERAL". Others work fine, but can be improved, perhaps with more optimal prompt template modifications for each of the classification of the prompts.

### API Usage:
- Original prompt is put into a prompt template with the style and context.
- The prompt template is then sent to the OpenAI API for enhancement.
- The enhanced text, which is the enhanced prompt, is then returned to the user.

### Use of AI:
- implementation for the browser extension (with the popup html, css and js files).
- implementation for the database (with the entity classes and the repository interfaces).
- implementation for the frontend (with the prompt history response and the prompt response).
- implementation for the API (with the prompt enhancement controller and the prompt enhancement service).
- implementation for the API key and the curl request.

### Current Next Step:
- Prompt history implementation, database integration.

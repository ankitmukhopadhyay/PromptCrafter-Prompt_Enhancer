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

### Process:
- User enters the original prompt, and chooses the style and the context from the dropdown menus.
- The request is passed as a curl request, to the API rewrite endpoint.
- The API endpoint then calls the prompt enhancement service, as the original prompt is then put into the prompt template. The template contains the original prompt, and detailed instruction to enhance the prompt, according to the style and context chosen by the user.
- The prompt template is then sent to the OpenAI API for enhancement.
- The enhanced text, which is the enhanced prompt, is then returned to the API endpoint.
- The API endpoint then returns the enhanced text to the user.

### Problems Faced:
- HuggingFace API was not working, so had to switch to OpenAI API for model integration.
- Had to do lots of modifications and testing for the prompt template builder, to get the optimal results for each of the classification of the prompts.
- The "DETAILED" prompt style works the best, along with the context "GENERAL". Others work fine, but can be improved, perhaps with more optimal prompt template modifications for each of the classification of the prompts.

### API Usage:
- Original prompt is put into a prompt template with the style and context.
- The prompt template is then sent to the OpenAI API for enhancement.
- The enhanced text, which is the enhanced prompt, is then returned to the API endpoint.
- The API endpoint then returns the enhanced text to the user.

### Use of AI:
- implementation for the browser extension (with the popup html, css and js files).
- implementation for the database (with the entity classes and the repository interfaces).
- implementation for the frontend (with the prompt history response and the prompt response).
- implementation for the API (with the prompt enhancement controller and the prompt enhancement service).
- implementation for the API key and the curl request.

### Current Next Step:
- Prompt history implementation, database integration.

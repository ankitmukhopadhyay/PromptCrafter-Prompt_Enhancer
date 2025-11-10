# __PromptCrafter__

_**PromptCrafter is a web application that enhances user prompts using AI and rule-based methods.**_

### __Team Members:__
* __Mohamed Sharif__
* __Ankit Mukhopadhyay__

### __Tech Stack:__
- Spring Boot
- Java
- MySQL
- LangChain4j
- OpenAI

### __Process when enhance button is clicked in ChatGPT:__
- The enhance button is clicked in the prompt bar of ChatGPT.
- The original prompt is sent to the API endpoint.
- The API endpoint then calls the prompt enhancement service, as the original prompt is then put into the prompt template. The template contains the original prompt, and detailed instruction to enhance the prompt, according to the style and context chosen by the user.
- The prompt template is then sent to the OpenAI API for enhancement.
- The enhanced text, which is the enhanced prompt, is then returned to the API endpoint.
- The API endpoint then returns the enhanced text to the user, which is then written into the prompt bar of ChatGPT itself.

### __Process within Extension Popup:__
- User enters the original prompt, and chooses the style and the context from the dropdown menus.
- The request is passed as a curl request, to the API rewrite endpoint.
- The API endpoint then calls the prompt enhancement service, as the original prompt is then put into the prompt template. The template contains the original prompt, and detailed instruction to enhance the prompt, according to the style and context chosen by the user.
- The prompt template is then sent to the OpenAI API for enhancement.
- The enhanced text, which is the enhanced prompt, is then returned to the API endpoint.
- The API endpoint then returns the enhanced text to the user, which is then displayed in the popup.

### __Problems Faced:__
- HuggingFace API was not working, so had to switch to OpenAI API for model integration.
- Had to do lots of modifications and testing for the prompt template builder, to get the optimal results for each of the classification of the prompts.
- The "DETAILED" prompt style works the best, along with the context "GENERAL". Others work fine, but can be improved, perhaps with more optimal prompt template modifications for each of the classification of the prompts.

### __API Usage:__
- Original prompt is put into a prompt template with the style and context.
- The prompt template is then sent to the OpenAI API for enhancement.
- The enhanced text, which is the enhanced prompt, is then returned to the API endpoint.
- The API endpoint then returns the enhanced text to the user.

### __Use of AI:__
- implementation for the browser extension (with the popup html, css and js files).
- implementation for the database (with the entity classes and the repository interfaces).
- implementation for the frontend (with the prompt history response and the prompt response).
- implementation for the API (with the prompt enhancement controller and the prompt enhancement service).
- implementation for the API key and the curl request.
- implementation for the user interface (with the popup html, css and js files).
- implementation for the history (with the history response and the history service).
- implementation of enhance button directly in the prompt bar of ChatGPT.
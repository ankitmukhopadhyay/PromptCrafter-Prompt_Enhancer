from fastapi import FastAPI
from typing import Union
from transformers import pipeline
from pydantic import BaseModel
app = FastAPI()


text_simplifier = pipeline(
"text2text-generation",
    model="tuner007/pegasus_paraphrase",
    tokenizer="tuner007/pegasus_paraphrase",

)

class TextInput(BaseModel):
    text: str

class SimplifyPrompt(BaseModel):
    text: str
    max_length: int = 100
    min_length: int = 10

@app.get("/")
async def root():
    return {"message": "testing fastAPI  huggingface models"}


@app.post("/simplify")
async def simplify(prompt: SimplifyPrompt):
    try:
        if  len(prompt.text.split()) < 10:
            return{
                "original_prompt": prompt.text,
                "simplified_prompt": prompt.text,
                "note": "text is too short"
            }

        input_text =  f"paraphrase: {prompt.text}"

        result = text_simplifier(input_text, max_length=prompt.max_length, min_length=prompt.min_length, do_sample=False)

        simplified_prompt = result[0]['generated_text']

        return{
            'original_prompt': prompt.text,
            'simplified_prompt': simplified_prompt,
        }

    except Exception as e:
        return{
            "error": str(e)
        }


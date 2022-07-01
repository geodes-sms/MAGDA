
import pandas as pd
import gensim
from gensim.models import Word2Vec
import re
import nltk
from nltk.corpus import wordnet
from nltk.corpus import wordnet as wn
import os
import openai
from dotenv import load_dotenv

load_dotenv()

openai.api_key = os.getenv("OPEN_AI")

df=pd.read_csv('dataWithAssociation.csv')
df
df.dropna(inplace=True)


def predictRelation(couples):
    prompt = "predict association name: \n [employee , company] => worksIn\n[person, Home] =>  owns\n [car ,driver] => drives \n[personalCustomer, customer] => is\n[ man, women]=>  married\n[lion, meat] => eats\n[manager, employee ] =>  supervises\n[order,  customer] =>   places\n[user, account] =>  has\n[cat, milk] => drinks\n[employee, department] =>  worksIn\n"

    response = openai.Completion.create(
        engine="text-davinci-002",
        prompt=prompt + str(couples) + "=> ",
        temperature=0.7,
        max_tokens=1,
        top_p=1,
        frequency_penalty=0,
        presence_penalty=0
    )
    res = response.choices[0].text
    print(str(couples) + "=> " + res)

    return res
# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print('hello')


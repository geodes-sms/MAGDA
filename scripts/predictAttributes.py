import pandas as pd

import sys

import os
import openai
import wordninja

from dotenv import load_dotenv

load_dotenv()
openai.api_key = os.getenv("OPEN_AI")

df=pd.read_csv('/home/meriem/editorCD/class-diagram-editor/scripts/data/improvedAttributes.csv')
data_attributes=''

for i, row in df.iterrows():

  data_attributes= data_attributes+ '\n' + str(row['name']) +': '+str(row['attributes']).strip('{}')+ '.'
#print(data_attributes)

def intercept(args):
    ClassName=args[0]
    if len(args)>1:
        attributes= args[1]
    else :
        attributes=""
    return ClassName, attributes

def predictAttributes( className , args =""):
    prompt= "generate attributes for class names: \n " + data_attributes + '\n' + className +': [ '+ args
    response = openai.Completion.create(
      engine="text-davinci-002",
      prompt=prompt,
      temperature=0.7,
      max_tokens=15,
      top_p=1,
      frequency_penalty=0,
      presence_penalty=0
    )
    res= response.choices[0].text
    print('res: ' , res)
    return ( className + ': [ '+ args + res)


def interceptResults(results):
    results=results.replace("\n", "")
    results=results.replace("'", "")
    try:
        attributesStr= results[results.find("[")+1:results.find("]")]

    except:
        attributesStr= results.split("[")[1]
        print('error')

    try:
        Attributes = attributesStr.split(',')

    except:
        Attributes = wordninja.split(attributesStr)


    while "'" in Attributes:
        Attributes.remove( "'")

   
    return Attributes


if __name__ == '__main__':
    args = sys.argv[1:]
    ClassName, attributes= intercept(args)

    print(interceptResults(predictAttributes(ClassName,attributes)))



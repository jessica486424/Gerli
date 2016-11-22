from nltk.tag.stanford import StanfordPOSTagger
from collections import OrderedDict
import ServerPrint as sp
from Record import *
from syntaxJudge import *
import numpy as np

"""
    This program would load the parser
    Moreover, it provide several function to parse the structure of the sentence
"""

# The parser .jar path
jar = "/home/sunner/nltk_data/models/stanford-postagger-full-2015-12-09/stanford-postagger.jar"
model = "/home/sunner/nltk_data/models/stanford-postagger-full-2015-12-09/models/english-bidirectional-distsim.tagger"

# Load the parser
tagger = StanfordPOSTagger("english-bidirectional-distsim.tagger")
sp.show("Finish POS tagger loading")

# Variable
sentences = ""
parseString = ""
word_2_pos = OrderedDict()
__verb = ""
__subject = ""
__object = ""
__value = ""

# Constants
assignKeyword = "is"
activeKeyword = "spend"
similarThreshold = 0.3
embeddedSize = 100

# Variables
word_2_id = {"is" : 0, "spend" : 1}
wordEmbedded = np.array([[1, 0], [0, 1]])

def tag(record, string="I love you"):
    """
        Main function to do the POS tagging

        Arg:    The string you want to parse
    """
    if record == None:
        sp.show("record object not None, start POS tagging", Type=sp.yes)

        # POS tagging
        global parseString
        global word_2_pos
        global sentences
        word_2_pos = OrderedDict()
        sentences = string
        parseString = tagger.tag(string.split())
        #print parseString
        for element in parseString:
            word = element[1].split('#')[0]
            pos = element[1].split('#')[1]
            word_2_pos[word] = pos
        #print word_2_pos

        # Find each element
        findVerb()
        findSubject()
        findObject()
        filtIndirectObject()
        findValue()
        if __verb == "" or __subject == "" or __object == "" or __value == "":
            sp.show("Phrase empty error, end the POS tagger process", Type=sp.war)
            return None

        # Return Result
        if __verb == "":
            return None
        else:
            sp.show("end POS tagging successfully", Type=sp.yes)
            sim = np.zeros(3)

            # Judge the type of the verb
            sim[0] = similarity(__verb, assignKeyword)
            sim[1] = similarity(__verb, activeKeyword)
            print sim
        
            # Maximum likelihood estimation
            if np.max(sim) < similarThreshold:
                return None
            if sim[0] == np.max(sim):
                record = Record(__subject, __value)
            if sim[1] == np.max(sim):
                record = Record(__object, __value)
            return record
    else:
        return record

def findSubject():
    """
        Find the subject of the sentence
    """
    global __subject
    __subject = ""
    for key in word_2_pos:
        if key == __verb:
            for key_ in word_2_pos:
                if not key_ == key:
                    __subject = __subject + key_ + ' '
                else:
                    break

def findVerb():
    """
        Find the verb of the sentence
    """
    # Initialize
    global __verb
    global __subject
    global __object
    global __value
    __verb = ""

    # Find the verb by POS tagger
    for keys in word_2_pos:
        if word_2_pos[keys] == "VV" or word_2_pos[keys] == "VC":
            __verb = keys
            return True
    
    # Find the verb by dependency parser
    if len(__verb) == 0:
        sp.show("Didn't find the verb, end the POS tagger process", Type=sp.war)
        return False

def findObject():
    """
        Find the object of the sentence
    """
    global __object
    __object = ""
    hadFoundVerb = False
    for key in word_2_pos:
        if key == __verb:
            hadFoundVerb = True
        if (not key == __verb) and hadFoundVerb:
            __object = __object + key + ' '

def findValue():
    """
        Find the value of the transaction sentence
    """
    global __value
    for key in word_2_pos:
        if word_2_pos[key] == 'CD':
            __value = key

def filtIndirectObject():
    """
        Filter the proposition phrase if it has
    """
    global __object
    objList = __object.split()
    __object = ""
    hadFoundP = False
    for key in objList:
        if hadFoundP:
            __object = __object + key + ' '
        if word_2_pos[key] == 'P':
            hadFoundP = True
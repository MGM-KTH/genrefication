#!/usr/bin/env python2
# -*- coding: utf-8 -*-
import nltk.data
import re
import string
import sys, getopt


def tokenize_file(inputfile, outputfile, sentence_class):
   tokenizer = nltk.data.load('tokenizers/punkt/english.pickle')
   inp_f = open(inputfile)
   outp_f = open(outputfile, 'a')
   data = inp_f.read().replace('”', '').replace('\n', ' ').replace('-', ' ').replace('_', ' ').replace('*', '')
   data = data.decode('utf-8')
   data = data.replace(u'\xa0', ' ').replace(u'\xc2', ' ').encode('utf-8')
   tokens = tokenizer.tokenize(data)
   for i,token in enumerate(tokens):
      token = ' '.join(token.split())
      token = token.replace('"', '')
      token = token.replace('\r',' ')
      token = token.replace('\n',' ')
      token = token.strip()
      if len(token) > 1:
         outp_f.write("\"" + token + "\", " + sentence_class + "\n")
         # print("----Sentence number " + str(i) + "------")
         # print("\"" + token + "\", " + sentence_class + "\n")


def main(argv):
   inputfile = ''
   outputfile = ''
   sentence_class = 'null'
   try:
      opts, args = getopt.getopt(argv,"hi:o:c:",["ifile=","ofile=", "class="])
   except getopt.GetoptError:
      print 'sentencifier.py -i <inputfile> -o <outputfile> -c <class>'
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         print 'sentencifier.py -i <inputfile> -o <outputfile> -c <class>'
         sys.exit()
      elif opt in ("-i", "--ifile"):
         inputfile = arg
      elif opt in ("-o", "--ofile"):
         outputfile = arg
      elif opt in ("-c", "--class"):
         sentence_class = arg
   tokenize_file(inputfile, outputfile, sentence_class)

if __name__ == "__main__":
   main(sys.argv[1:])

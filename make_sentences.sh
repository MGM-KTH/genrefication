#!/usr/bin/bash
HORROR_SOURCE="data/texts/horror/*"
OTHER_SOURCE="data/texts/other/*"

HORROR_DEST="data/sentences/horror/"
OTHER_DEST="data/sentences/other/"

# remove all old texts
rm $HORROR_DEST/*
rm $OTHER_DEST/*

for text in $HORROR_SOURCE
do
    echo "saving to $HORROR_DEST$(basename $text)"
    ./sentencifier.py -i $text -o $HORROR_DEST$(basename $text) -c horror
done

for text in $OTHER_SOURCE
do
    echo "saving to $OTHER_DEST$(basename $text)"
    ./sentencifier.py -i $text -o $OTHER_DEST$(basename $text) -c other
done

#!/usr/bin/bash
HORROR_SOURCE="data/sentences/horror/*"
OTHER_SOURCE="data/sentences/other/*"

HORROR_DEST="data/sentences/horror_sentences.arff"
OTHER_DEST="data/sentences/other_sentences.arff"

DEST="data/sentences/sentences.arff"

rm $HORROR_DEST
rm $OTHER_DEST

for text in $HORROR_SOURCE
do
    cat $text >> $HORROR_DEST
done

for text in $OTHER_SOURCE
do
    cat $text >> $OTHER_DEST
done

cat $HORROR_DEST >> $DEST
cat $OTHER_DEST >> $DEST

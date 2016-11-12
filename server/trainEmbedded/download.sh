if [ ! -f ./text8 ]; then
    wget http://mattmahoney.net/dc/text8.zip -O text8.zip
    unzip text8.zip
fi
if [ ! -f ./questions-words.txt ]; then
    wget https://storage.googleapis.com/google-code-archive-source/v2/code.google.com/word2vec/source-archive.zip
    unzip -p source-archive.zip  word2vec/trunk/questions-words.txt > questions-words.txt
    rm source-archive.zip
fi
echo '<Train>     Download done'
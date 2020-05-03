package com.mtah.tools;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PreProcessor {
    public static final String[] STOPWORDS = new String[]{ "a","able","about","after","all","also","am",
            "an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","did",
            "do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her",
            "hers","him","his","how","I","if","in","into","is","it","its","just","let","like","likely","may","me",
            "might","most","must","my","neither","no","nor","not","of","off",
            "often","on","only","or","other","our","own","said","say","says","she",
            "should","so","some","than","that","the","their","them","then","there",
            "these","they","this","they're","to","too","that's","us","was","we","were",
            "what","when","where","which","while","who","whom","why","will","with",
            "would","yet","you","your", "you're",
            "s", "ve", "d", "nt"
    };
    private final String TAG = "INFO";
    private SentenceDetectorME sentenceDetector;
    private Tokenizer tokenizer;
    private DictionaryLemmatizer lemmatizer;
    private POSTaggerME posTagger;

    public PreProcessor(InputStream sentenceModel, InputStream tokenizerModel, InputStream lemmaModel, InputStream pos) throws IOException {
        SentenceModel sentModel = new SentenceModel(sentenceModel);
        sentenceDetector = new SentenceDetectorME(sentModel);
        TokenizerModel tokenModel = new TokenizerModel(tokenizerModel);
        tokenizer = new TokenizerME(tokenModel);
        POSModel posModel = new POSModel(pos);
        posTagger = new POSTaggerME(posModel);
        lemmatizer = new DictionaryLemmatizer(lemmaModel);
    }

    // separate sentences
    public String [] extractSentences (String documentText){
        String [] sentences = sentenceDetector.sentDetect(documentText.trim());

        Log.i(TAG, "extractSentences: Clean sentences");
        for (int i=0; i<sentences.length; i++){
            sentences[i] = sentences[i].trim().replace("\n", " ").replace("\r", " ");
            Log.i(TAG, sentences[i]);
            Log.i(TAG, "===================================");
        }
        return sentences;
    }

    public String [] [] tokenizeSentences(String [] sent){
        String [][] tokenized = new String[sent.length][];
        String stopWordsPattern = TextUtils.join("|", STOPWORDS);
        Pattern pattern = Pattern.compile("\\b(?:" + stopWordsPattern + ")\\b\\s*", Pattern.CASE_INSENSITIVE);

        Log.i(TAG, "tokenizeSentences: Tokenized, Lemmatized and Stop words removed");
        for(int i=0; i<sent.length; i++){
            //tokenize
            tokenized[i] = tokenizer.tokenize(sent[i]);//.replaceAll("\\p{P}", ""));
            //lemmatize
            tokenized[i] = lemmantizeTokens(tokenized[i]);
            //remove stop words
            for (int j=0; j<tokenized[i].length; j++){
                Matcher matcher = pattern.matcher(tokenized[i][j]);
                tokenized[i][j] = matcher.replaceAll("O");
            }
        }
        return tokenized;
    }

    public String [] lemmantizeTokens(String [] tokens){
        String [] tags = posTagger.tag(tokens);
        return lemmatizer.lemmatize(tokens, tags);
    }
}

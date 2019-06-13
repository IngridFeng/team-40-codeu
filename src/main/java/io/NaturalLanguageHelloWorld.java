package io.happycoding.naturallanguage;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;

public class NaturalLanguageHelloWorld {
  public static void main(String[] args) throws IOException {
    String text = "why are you like this";
    Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    languageService.close();

    System.out.println("Score is " + sentiment.getScore());
  }
}

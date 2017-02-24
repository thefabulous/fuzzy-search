package co.thefabulous.search.engine;

/**
 * Created by Bartosz Lipinski
 * 24.02.2017
 */
public interface SearchFunction {
    SearchResult search(String text);

    String pattern();
}
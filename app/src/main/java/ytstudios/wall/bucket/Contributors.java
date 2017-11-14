package ytstudios.wall.bucket;

/**
 * Created by Yugansh Tyagi on 14-11-2017.
 */

public class Contributors {

    String name;
    String about;
    String language;
    String link;

    public Contributors(String name, String about, String language, String link) {
        this.name = name;
        this.about = about;
        this.language = language;
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getLink() {
        return link;
    }
}

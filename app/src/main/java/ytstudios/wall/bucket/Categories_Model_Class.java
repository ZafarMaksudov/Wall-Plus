package ytstudios.wall.bucket;

/**
 * Created by Yugansh Tyagi on 11-09-2017.
 */

public class Categories_Model_Class  {
    String category_name;
    String category_url;

    public Categories_Model_Class(String category_name, String category_url) {
        this.category_name = category_name;
        this.category_url = category_url;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String  getCategory_image_id() {
        return category_url;
    }
}


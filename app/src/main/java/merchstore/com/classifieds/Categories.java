package merchstore.com.classifieds;

public class Categories {
    private String category;
    private String image;

    public Categories(String category, String image) {
        this.category = category;
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

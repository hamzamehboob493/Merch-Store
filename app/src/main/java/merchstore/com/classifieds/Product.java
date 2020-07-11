package merchstore.com.classifieds;

public class Product {
    private String title, description, author, category, image,author_name,id;
    private int price;
    private int status = 0;

    public Product(String title, String description, String author, String category, String image, int price,String author_name,String id) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.category = category;
        this.image = image;
        this.price = price;
        this.author_name = author_name;
        this.id = id;
    }

    public Product(String title, String description, String author, String category, String image, int price) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.category = category;
        this.image = image;
        this.price = price;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

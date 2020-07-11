package merchstore.com.classifieds;

public class Review {
    private String product_id, author_id, comment, date;


    public Review(String product_id, String author_id, String comment, String date) {
        this.product_id = product_id;
        this.author_id = author_id;
        this.comment = comment;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}

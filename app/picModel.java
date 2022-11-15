import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class picModel{
    private String imageName;
    private BitmapFactory factory;
    private Bitmap bitmap;

    public picModel(String imageName, BitmapFactory factory, Bitmap bitmap) {
        this.imageName = imageName;
        this.factory = factory;
        this.bitmap = bitmap;
    }

    public BitmapFactory getFactory() {
        return factory;
    }

    public void setFactory(BitmapFactory factory) {
        this.factory = factory;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

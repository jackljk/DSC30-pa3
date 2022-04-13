import java.util.Arrays;
import java.util.Locale;

public class PhotoMessage extends Message {

    /* Error message to use in OperationDeniedException */
    private static final String INVALID_INPUT =
            "The source path given cannot be parsed as photo.";

    /* instance variable */
    private String extension;

    /**
     * Constructor
     * @param sender The User
     * @param photoSource String of the photo
     * @throws OperationDeniedException When User is not Premium and photo-source is not of the
     * right extension.
     */
    public PhotoMessage(User sender, String photoSource)
                        throws OperationDeniedException {
        /* Constructor for Photo-messages */
        super(sender);
        if (photoSource == null){
            /* Throws Exception if the source is null */
            throw new IllegalArgumentException();
        } else if (sender instanceof StandardUser){
            /* Throws exception if the User is not a premium user */
            throw new OperationDeniedException(DENIED_USER_GROUP);
        } else {
            /* Check if the extension is valid, if it is save it to extension constructor else
            throws an exception. */
            String[] get_ext = photoSource.split(".");
            String[] exts = {"jpg", "jpeg", "gif", "png", "tif", "tiff", "raw"};
                if (Arrays.asList(exts).contains(get_ext[1])){
                    this.contents = get_ext[0];
                    this.extension = get_ext[1].toLowerCase();
                } else {
                    throw new OperationDeniedException(INVALID_INPUT);
                }
        }
    }

    /**
     * Getter Method
     * @return Name of sender, and source of the photo-message
     */
    public String getContents() {
        /* Returns the contents of the photo-message */
        return this.getSender().displayName() + ": Picture at " + this.getContents() +
                this.getExtension() ;
    }

    /**
     * Getter Method
     * @return Objects extension
     */
    public String getExtension() {
        /* Returns the extension of the object */
        return this.extension;
    }

}

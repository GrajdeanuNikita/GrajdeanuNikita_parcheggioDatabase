public class AlreadyPresentPlateException  extends  RuntimeException{
    public AlreadyPresentPlateException(String targa) {
        super("L'auto con targa " + targa + " è già presente nel parcheggio.");
    }
}
package utils.exceptions;

/**
 * Created by Nicolas Müller on 15.02.17.
 */
public class AssignArgumentToLocalExcpetion extends RuntimeException{


    private static final long serialVersionUID = 139471024128413L;

    public AssignArgumentToLocalExcpetion(String message) {
        super(message);
        printStackTrace();
    }
}

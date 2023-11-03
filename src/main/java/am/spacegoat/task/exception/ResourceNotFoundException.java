package am.spacegoat.task.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public ResourceNotFoundException(Class<?> resourceClass, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceClass.getSimpleName(), fieldName, fieldValue));
        this.resourceName = resourceClass.getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}

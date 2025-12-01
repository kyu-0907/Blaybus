package politicConnect.auth;

import jakarta.validation.ConstraintValidator;

public abstract class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public boolean isValid(String password, ConstraintValidator context){
        if(password ==null) return false;

        if(password.length() < 10 || password.length() >16) return false;

        boolean hasUpperLower = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        int count = 0;
        if(hasUpperLower) count++;
        if(hasDigit) count ++;
        if(hasSpecial) count ++;

        return count >=2;
    }
}

import { AbstractControl, ValidationErrors } from "@angular/forms";

function passwordStrength(control: AbstractControl): ValidationErrors | null {
    const password = control.value

    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumeric = /[0-9]/.test(password);
    const hasSpecial = /[^a-zA-Z0-9_]/.test(password);

    const isValid = 
        hasUpperCase && hasLowerCase && hasNumeric && hasSpecial;
    
    const validationErrors = {
        hasUpperCase: !hasUpperCase,
        hasLowerCase: !hasLowerCase,
        hasNumeric: !hasNumeric,
        hasSpecial: !hasSpecial
    }
    
    return isValid ? null : validationErrors;
}

function passMatch(control: AbstractControl): ValidationErrors | null {
    const confirmPass = control.value;
    const password = control?.parent?.get('password')?.value;
    if (!password) return null;

    return confirmPass === password ? null : {mismatch: true}
}

const PasswordValidator = {
    passwordStrength,
    passMatch,
}

export default PasswordValidator
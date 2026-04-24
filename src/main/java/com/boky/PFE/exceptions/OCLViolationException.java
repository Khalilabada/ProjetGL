package com.boky.PFE.exceptions;


public class OCLViolationException extends RuntimeException {

    private final String constraintName;

    public OCLViolationException(String constraintName, String message) {
        super(String.format("OCL Constraint Violation [%s]: %s", constraintName, message));
        this.constraintName = constraintName;
    }

    public String getConstraintName() {
        return constraintName;
    }
}

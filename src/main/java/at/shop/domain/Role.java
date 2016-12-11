package at.shop.domain;

public enum Role {
    USER, ADMIN, EMPLOYEE;

    public static Role noNullReturn(String value) {

        return (value == null) ? USER : Role.valueOf(value);
    }
}

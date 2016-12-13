package at.shop.domain;

public enum Role {
    ROLE_USER, ROLE_ADMIN, ROLE_EMPLOYEE;

    public static Role noNullReturn(String value) {

        return (value == null) ? ROLE_USER : Role.valueOf(value);
    }
}

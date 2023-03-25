package com.PhotoApi.ws.users.ui.payload;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {
    @NotNull(message="First name cannot be null")
    @Size(min=2,message="Name must be atleast 2 characters")
    private String firstName;
    @NotNull(message="Last name cannot be null")
    @Size(min=2,message="Name must be atleast 2 characters")
    private String lastName;
    @NotNull(message="First name cannot be null")
    @Size(min=8,max=16,message="Password should not be less than 8 characters")
    private String email;
    @NotNull(message="Email cannot be null")
    @Email
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

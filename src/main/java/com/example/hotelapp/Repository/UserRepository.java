package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.ChangedPassword;
import com.example.hotelapp.DTO.User.*;
import com.example.hotelapp.ExceptionHandlers.Exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    //Method to create a user account
    void create_user_account(UserDto userDto);
    //Method to get user credentials.User should not see this
    UserCredentials get_user_credentials(String credentials) throws UserNotFoundException;
    /*Method to update user details
     The user should be able to change password and other details
    *---------------HOW IT WORKS------------------
    1. Get user details and store them in the User details dto
    2. get new user details from the put method and store them in the UserUpdatedDto
    3. For password, check if the new password is the same as the old password
    4. Update the details of the user. Do this by updating user record and use the email in
    the where clause
     */
    int update_user(UserUpdatedDto userUpdatedDto,String credentials);
    ChangedPassword change_password(UserDetailsDto userDetailsDto);
    /*
    The final method is the user should be able to delete his/ her account
     */
    //Delete user account
    void delete_user(UserDto userDto);

}
package com.yalantis.model;

/**
 * Created by Ed on 08.09.2014.
 */
public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    //
    // public void saveDataFromServer(GetProfileDTO profileDTO) {
    // GetProfileDTO.ResultProfile result = profileDTO.getResult();
    // GetProfileDTO.ResultProfile.Core core = result.getCore();
    // GetProfileDTO.ResultProfile.ProfileFieldsDTO profileFieldsDTO = result.getProfileFields();
    // this.firstName = core.getFirstName();
    // this.lastName = core.getLastName();
    // this.userName = core.getUsername();
    // this.guid = core.getGuid();
    // this.gender = core.getGender();
    // if (profileFieldsDTO.getCity() != null) {
    // this.city = profileFieldsDTO.getCity().getValue();
    // }
    // if (profileFieldsDTO.getBirthdate() != null) {
    // this.birthdate = profileFieldsDTO.getBirthdate().getValue();
    // }
    // this.country = profileFieldsDTO.getCountryId().getValue();
    // this.logo = result.getAvatarUrl();
    // }
}

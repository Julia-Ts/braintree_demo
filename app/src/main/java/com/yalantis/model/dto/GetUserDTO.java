package com.yalantis.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ed on 08.09.2014.
 */
public class GetUserDTO extends BaseDTO {


    private ResultProfile result;

    public ResultProfile getResult() {
        return result;
    }

    public void setResult(ResultProfile result) {
        this.result = result;
    }

    public class ResultProfile {

        private String avatarUrl;
        private Core core;

        public Core getCore() {
            return core;
        }

        public void setCore(Core core) {
            this.core = core;
        }

        @SerializedName("profile_fields")
        private ProfileFieldsDTO profileFields;

        public ProfileFieldsDTO getProfileFields() {
            return profileFields;
        }

        public void setProfileFields(ProfileFieldsDTO profileFields) {
            this.profileFields = profileFields;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public class ProfileFieldsDTO {

            private Birthdate birthdate;
            private City city;
            private CityId cityId;
            private CountyId countryId;

            public City getCity() {
                return city;
            }

            public void setCity(City city) {
                this.city = city;
            }

            public Birthdate getBirthdate() {
                return birthdate;
            }

            public void setBirthdate(Birthdate birthdate) {
                this.birthdate = birthdate;
            }

            public CityId getCityId() {
                return cityId;
            }

            public void setCityId(CityId cityId) {
                this.cityId = cityId;
            }

            public CountyId getCountryId() {
                return countryId;
            }

            public void setCountryId(CountyId countryId) {
                this.countryId = countryId;
            }

            public class CountyId {

                private String label;
                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }
            }

            public class CityId {

                private String label;
                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }
            }
        }

        public class Core {

            private String firstName;



            private String lastName;
            private String username;
            private int guid;
            private String gender;

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String name) {
                this.firstName = name;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getGuid() {
                return guid;
            }

            public void setGuid(int guid) {
                this.guid = guid;
            }
        }
    }

    public class Birthdate {
        private String label;
        private String type;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public class City {
        private String label;
        private String type;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

}

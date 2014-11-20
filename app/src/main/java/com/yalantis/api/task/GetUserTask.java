package com.yalantis.api.task;

import retrofit.client.Response;

import com.yalantis.App;
import com.yalantis.api.request.UserApi;
import com.yalantis.model.dto.GetUserDTO;

/**
 * Created by Ed
 */
public class GetUserTask extends ApiTask<UserApi, GetUserDTO> {


    private String password;

    public GetUserTask(UserApi api, String password) {
        super(api,null);
        this.password = password;
    }


    @Override
    public void run() {
        api.getUser(password, this);
    }

    @Override
    public void onSuccess(GetUserDTO getProfileDTO, Response response) {
        App.dataManager.saveProfileFromServerData(getProfileDTO);
        // EventBus.getDefault().postSticky(new LoginEvent(String.valueOf(loginSuccessDTO.getId())));

    }
}

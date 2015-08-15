package au.edu.sydney.Curracurrong.runtime.api.client;

import au.edu.sydney.Curracurrong.runtime.api.model.APIResourceCredentials;

public interface FitbitApiCredentialsCache {

    APIResourceCredentials getResourceCredentials(LocalUserDetail user);

    APIResourceCredentials getResourceCredentialsByTempToken(String tempToken);

    APIResourceCredentials saveResourceCredentials(LocalUserDetail user, APIResourceCredentials resourceCredentials);

    APIResourceCredentials expireResourceCredentials(LocalUserDetail user);

}

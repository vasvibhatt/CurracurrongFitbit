/**
 * Copyright 2014 University of Sydney, Australia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.edu.sydney.Curracurrong.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.List;

import au.edu.sydney.Curracurrong.datatype.*;

import au.edu.sydney.Curracurrong.runtime.api.FitbitAPIException;


import au.edu.sydney.Curracurrong.runtime.api.client.*;
import au.edu.sydney.Curracurrong.runtime.api.client.service.FitbitAPIClientService;
import au.edu.sydney.Curracurrong.runtime.api.common.model.activities.Activities;
import au.edu.sydney.Curracurrong.runtime.api.common.model.activities.ActivitiesSummary;
import au.edu.sydney.Curracurrong.runtime.api.common.model.devices.Device;
import au.edu.sydney.Curracurrong.runtime.api.common.model.heart.Heart;
import au.edu.sydney.Curracurrong.runtime.api.common.model.heart.HeartLog;
import au.edu.sydney.Curracurrong.runtime.api.common.model.sleep.Sleep;
import au.edu.sydney.Curracurrong.runtime.api.common.model.sleep.SleepSummary;
import au.edu.sydney.Curracurrong.runtime.api.common.model.user.UserInfo;
import au.edu.sydney.Curracurrong.runtime.api.common.service.FitbitApiService;
import au.edu.sydney.Curracurrong.runtime.api.model.APIResourceCredentials;
import au.edu.sydney.Curracurrong.runtime.api.model.FitbitUser;


import org.joda.time.LocalDate;;

/**
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 */
public class FitbitSensor extends Sensor {

    /** FitBit access token following user approval of PIN */
    private static String ACCESS_TOKEN = null;

    /** FitBit API URL */
    private static final String API_URL = "api.fitbit.com";

    /** FitBit application key */
    private static final String CONSUMER_KEY = "5d2a24e69a9f4049b6f881cfcffd70f4";

    /** FitBit main URL */
    private static final String FITBIT_URL = "https://fitbit.com";

    /** FitBit secret application key */
    private static final String SECRET_KEY = "df50000afbe64f4090979edf033808d5";

    /** FitBit secret access token on approval of PIN */
    private static  String SECRET_TOKEN = null;

    /** FitBit username */
    private static String userName ;

    /** Collected data from Fitbit user account */
    private DRecord sense;

    /** get valid day for which data needs to be fetched */ 
    private String day;
    
    private FitbitAPIClientService<FitbitApiClientAgent> fitbit;
    
    private LocalUserDetail localUser;


    /* (non-Javadoc)
     * @see runtime.Sensor#initialize(int, java.util.Hashtable)
     */
    @Override
    void initialize(int opID, Hashtable propertyMap) {
        super.initialize(opID, propertyMap);
        DRecord property = (DRecord) propertyMap.get("date");
        DRecord userProperty = (DRecord) propertyMap.get("username");
        try {
        	day = property.getElement(1).tostring();
        	userName = userProperty.getElement(1).tostring();
        } catch(ParsingException ex) {
        	ex.printStackTrace();
        }
        
        FitbitApiCredentialsCache credentialsCache;
        
        FitbitAPIEntityCache entityCache;
        
        FitbitApiSubscriptionStorage subscriptionStore;
        
        // establish FitBit client
        credentialsCache =
                new FitbitApiCredentialsCacheMapImpl();
        entityCache = new FitbitApiEntityCacheMapImpl();
        subscriptionStore =
                new FitbitApiSubscriptionStorageInMemoryImpl();
        fitbit = new FitbitAPIClientService<FitbitApiClientAgent>(
                new FitbitApiClientAgent(API_URL, FITBIT_URL, credentialsCache),
                CONSUMER_KEY, SECRET_KEY, credentialsCache, entityCache,
                subscriptionStore);
        
        try{
	        // define local user and get credentials
	        localUser = new LocalUserDetail(userName);
	        String url = fitbit.getResourceOwnerAuthorizationURL(localUser, "");
	        APIResourceCredentials creds =
	                fitbit.getResourceCredentialsByUser(localUser);
	        
	        // set access tokens
	        if (ACCESS_TOKEN == null || SECRET_TOKEN == null) {
	            System.out.println("Allow application access at URL: " + url);
	            System.out.print("Enter PIN from web page: ");
	            String pin = readFromUser();
	            creds.setTempTokenVerifier(pin);
	            fitbit.getTokenCredentials(localUser);
	            System.out.println("ACCESS_TOKEN value: " + creds.getAccessToken());
	            System.out.println("SECRET_TOKEN value: " +
	                    creds.getAccessTokenSecret());
	
	        } else {
	            creds.setAccessToken(ACCESS_TOKEN);
	            creds.setAccessTokenSecret(SECRET_TOKEN);
	        }
        } catch(IOException ex) {
        	ex.printStackTrace();
        } catch(FitbitAPIException ex) {
        	ex.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see runtime.Sensor#read()
     */
    @Override
    public DRecord read() {
        try {
            senseAll();
        } catch (FitbitAPIException ex) {
            ex.printStackTrace();
        } catch (ParsingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sense;
    }

    /**
     * Collects reading from all sensors: Temperature, Light, x, y & z
     */
    private void senseAll() throws FitbitAPIException, IOException, ParsingException {

        // set given date
        FitbitApiClientAgent clientAgent = fitbit.getClient();
        LocalDate date = FitbitApiService.getValidLocalDateOrNull(day);

        // output user summary
        UserInfo profile = fitbit.getClient().getUserInfo(localUser);
        System.out.println("Member: name " + profile.getDisplayName() + ", since " +
                profile.getMemberSince());

        sense = new DRecord(8);

        sense.setElement(0, new DFloat(SystemStatus.getSystemStatusInstance().getExecutionTime()));
        sense.setElement(1, new DInteger64(System.currentTimeMillis()));
        sense.setElement(2, new DString(profile.getDisplayName()));
        sense.setElement(3, new DString(profile.getMemberSince().toString()));


        // output activity summary
        Activities activities = clientAgent.getActivities(localUser, FitbitUser.CURRENT_AUTHORIZED_USER, date);
        ActivitiesSummary activitiesSummary = activities.getSummary();

        sense.setElement(4, new DInteger32(activitiesSummary.getSteps()));
        sense.setElement(5, new DInteger32(activitiesSummary.getCaloriesOut()));


        // output sleep summary
        Sleep sleep = clientAgent.getSleep(
                localUser, FitbitUser.CURRENT_AUTHORIZED_USER, date);
        SleepSummary sleepSummary = sleep.getSummary();
        float hoursBed = (sleepSummary.getTotalTimeInBed() / 6) / 10.0f;
        float hoursSleep = (sleepSummary.getTotalMinutesAsleep() / 6) / 10.0f;

        sense.setElement(6, new DFloat(hoursBed));
        sense.setElement(7, new DFloat(hoursSleep));
        
        Heart heart = clientAgent.getLoggedHeartRate(localUser, FitbitUser.CURRENT_AUTHORIZED_USER, date);
        List<HeartLog> heartLog = heart.getHeartLog();
        for(int itr = 0; itr < heartLog.size(); itr++) {
        	HeartLog log = heartLog.get(itr);
        	System.out.println(log.getTime() + " : " + log.getHeartRate());
        }

    }

    private static String readFromUser() throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(System.in));
        String text = bufferedReader.readLine();
        if (text != null)
            text = text.trim();
        else
            text = "";
        return(text);
    }
 }

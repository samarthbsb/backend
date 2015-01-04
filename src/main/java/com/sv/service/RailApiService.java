/**
 * 
 */
package com.sv.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.sv.db.MongoDBManager;
import com.sv.dto.rail.FareDTO;
import com.sv.dto.rail.LiveStatus;
import com.sv.dto.rail.PNRStatus;
import com.sv.dto.rail.RouteDTO;
import com.sv.dto.rail.SeatAvailability;
import com.sv.dto.rail.Station;
import com.sv.dto.rail.Train;
import com.sv.dto.rail.TrainsAtStation;
import com.sv.util.APIResponse;
import com.sv.util.HttpClient;
import com.sv.util.Utils;

/**
 * @author vaibhav
 */
@Service
public class RailApiService
{
    private Logger railLogger = LoggerFactory.getLogger("railServiceLog");

    private Logger logger = LoggerFactory.getLogger(RailApiService.class.getCanonicalName());

    public static final String railApiEndPoint = "http://api.erail.in";

    public static final String api_key = "a77fba80-f6ba-421a-83ac-a50bfb1286a3";

    private int TIME_OUT = 50000;

    @Autowired
    private MongoDBManager mongoRailDBManager;

    public static final String STATION_COLLECTION_NAME = "stations";

    public APIResponse<PNRStatus> getPnrStatus(String pnr) throws Exception
    {
        PNRStatus pnrStatus = new PNRStatus();
        JSONObject json = null;
        String status = null;
        String result = null;
        StringBuilder builder =
            new StringBuilder(railApiEndPoint).append("/pnr?").append("key=").append(api_key).append("&pnr=")
                .append(pnr);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        if (!StringUtils.isEmpty(content)) {
            json = (JSONObject) JSONValue.parse(content);
            status = (String) json.get("status");
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("OK")) {
                if (json.containsKey("result")) {
                    result = json.get("result").toString();
                }
                ObjectMapper mapper = Utils.getObjectMapper();
                try {
                    pnrStatus = mapper.readValue(result, PNRStatus.class);
                    return APIResponse.getSuccessResponse(pnrStatus);
                } catch (IOException e) {
                    return APIResponse.getErrorResponse("1", "Exception", pnrStatus);
                }
            }
        }
        return APIResponse.getErrorResponse("2", "Failure", pnrStatus);
    }

    public APIResponse<LiveStatus> getLiveStatus(String trainno, String stnfrom, String date) throws Exception
    {
        LiveStatus liveStatus = new LiveStatus();
        JSONObject json = null;
        String status = null;
        String result = null;
        StringBuilder builder =
            new StringBuilder(railApiEndPoint).append("/live?").append("key=").append(api_key).append("&trainno=")
                .append(trainno).append("&stnfrom=").append(stnfrom).append("&date=").append(date);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        if (!StringUtils.isEmpty(content)) {
            json = (JSONObject) JSONValue.parse(content);
            status = (String) json.get("status");
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("OK")) {
                if (json.containsKey("result")) {
                    result = json.get("result").toString();
                }
                ObjectMapper mapper = Utils.getObjectMapper();
                try {
                    liveStatus = mapper.readValue(result, LiveStatus.class);
                    return APIResponse.getSuccessResponse(liveStatus);
                } catch (IOException e) {
                    return APIResponse.getErrorResponse("1", "Exception", liveStatus);
                }
            }
        }
        return APIResponse.getErrorResponse("2", "Failure", liveStatus);
    }

    public APIResponse<TrainsAtStation> getTrainsAtStation(String stnfrom, String stnto, String hr) throws Exception
    {
        TrainsAtStation trainsAtStation = new TrainsAtStation();
        JSONObject json = null;
        String status = null;
        String result = null;
        StringBuilder builder =
            new StringBuilder(railApiEndPoint).append("/trainsatstation?").append("key=").append(api_key)
                .append("&stnfrom=").append(stnfrom).append("&stnto=").append(stnto).append("&hr=").append(hr);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        if (!StringUtils.isEmpty(content)) {
            json = (JSONObject) JSONValue.parse(content);
            status = (String) json.get("status");
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("OK")) {
                if (json.containsKey("result")) {
                    result = json.get("result").toString();
                }
                ObjectMapper mapper = Utils.getObjectMapper();
                try {
                    trainsAtStation = mapper.readValue(result, TrainsAtStation.class);
                    return APIResponse.getSuccessResponse(trainsAtStation);
                } catch (IOException e) {
                    return APIResponse.getErrorResponse("1", "Exception", trainsAtStation);
                }
            }
        }
        return APIResponse.getErrorResponse("2", "Failure", trainsAtStation);
    }

    public APIResponse<List<Train>> getTrains(String stnfrom, String stnto, String date, String cls) throws Exception
    {
        List<Train> trains = new ArrayList<>();
        JSONObject json = null;
        String status = null;
        String result = null;
        StringBuilder builder =
            new StringBuilder(railApiEndPoint).append("/trains/?").append("key=").append(api_key).append("&stnfrom=")
                .append(stnfrom).append("&stnto=").append(stnto).append("&date=").append(date).append("&class=")
                .append(cls);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        if (!StringUtils.isEmpty(content)) {
            json = (JSONObject) JSONValue.parse(content);
            status = (String) json.get("status");
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("OK")) {
                if (json.containsKey("result")) {
                    result = json.get("result").toString();
                }
                ObjectMapper mapper = Utils.getObjectMapper();
                try {
                    trains = mapper.readValue(result, List.class);
                    return APIResponse.getSuccessResponse(trains);
                } catch (IOException e) {
                    return APIResponse.getErrorResponse("1", "Exception", trains);
                }
            }
        }
        return APIResponse.getErrorResponse("2", "Failure", trains);
    }

    public APIResponse<SeatAvailability> getSeatAvailabilty(String stnfrom, String stnto, String date, String cls,
        String trainno, String quota) throws Exception
    {
        SeatAvailability seatAvailability = new SeatAvailability();
        JSONObject json = null;
        String status = null;
        String result = null;
        StringBuilder builder =
            new StringBuilder(railApiEndPoint).append("/seats?").append("key=").append(api_key).append("&stnfrom=")
                .append(stnfrom).append("&stnto=").append(stnto).append("&date=").append(date).append("&class=")
                .append(cls).append("&trainno=").append(trainno).append("&quota=").append(quota);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        if (!StringUtils.isEmpty(content)) {
            json = (JSONObject) JSONValue.parse(content);
            status = (String) json.get("status");
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("OK")) {
                if (json.containsKey("result")) {
                    result = json.get("result").toString();
                }
                ObjectMapper mapper = Utils.getObjectMapper();
                try {
                    seatAvailability = mapper.readValue(result, SeatAvailability.class);
                    return APIResponse.getSuccessResponse(seatAvailability);
                } catch (IOException e) {
                    return APIResponse.getErrorResponse("1", "Exception", seatAvailability);
                }
            }
        }
        return APIResponse.getErrorResponse("2", "Failure", seatAvailability);
    }

    public APIResponse<FareDTO> getFare(String stnfrom, String stnto, String date, String age, String trainno,
        String quota) throws Exception
    {
        FareDTO fareDTO = new FareDTO();
        JSONObject json = null;
        String status = null;
        String result = null;
        StringBuilder builder =
            new StringBuilder(railApiEndPoint).append("/fare?").append("key=").append(api_key).append("&stnfrom=")
                .append(stnfrom).append("&stnto=").append(stnto).append("&date=").append(date).append("&age=")
                .append(age).append("&trainno=").append(trainno).append("&quota=").append(quota);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        if (!StringUtils.isEmpty(content)) {
            json = (JSONObject) JSONValue.parse(content);
            status = (String) json.get("status");
            ObjectMapper mapper = Utils.getObjectMapper();
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("OK")) {
                if (json.containsKey("result")) {
                    result = (String) json.get("result").toString();
                }
                try {
                    fareDTO = mapper.readValue(result, FareDTO.class);
                    return APIResponse.getSuccessResponse(fareDTO);
                } catch (IOException e) {
                    return APIResponse.getErrorResponse("1", "Exception", fareDTO);
                }
            }
        }
        return APIResponse.getErrorResponse("2", "Failure", fareDTO);
    }

    public APIResponse<List<Station>> getStations(String searchVal) throws Exception
    {
        logger.info("getting stations from db");
        Map<String, Object> queryParams1 = new HashMap<>();
        Map<String, Object> queryParams2 = new HashMap<>();
        List<Station> stations = new ArrayList<Station>();
        queryParams1.put("code", Pattern.compile(searchVal, Pattern.CASE_INSENSITIVE));
        queryParams2.put("name", Pattern.compile(searchVal, Pattern.CASE_INSENSITIVE));
        try {
            long count = mongoRailDBManager.getCount(STATION_COLLECTION_NAME, Collections.emptyMap());
            if (count == 0) {
                addStations();
            }
        } catch (Exception e) {
            addStations();
        }
        List<DBObject> dbObjects = mongoRailDBManager.getObjects(STATION_COLLECTION_NAME, queryParams1);
        dbObjects.addAll(mongoRailDBManager.getObjects(STATION_COLLECTION_NAME, queryParams2));
        for (int i = 0; i < dbObjects.size(); i++) {
            DBObject dbObj = dbObjects.get(i);
            Station station = new Station();
            station.setCode((String) dbObj.get("code"));
            station.setName((String) dbObj.get("name"));
            stations.add(station);
        }
        return APIResponse.getSuccessResponse(stations);
    }

    public APIResponse<RouteDTO> getRoute(String trainno) throws Exception
    {
        RouteDTO routeDto = new RouteDTO();
        JSONObject json = null;
        String status = null;
        String result = null;
        StringBuilder builder =
            new StringBuilder(railApiEndPoint).append("/route?").append("key=").append(api_key).append("&trainno=")
                .append(trainno);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        if (!StringUtils.isEmpty(content)) {
            json = (JSONObject) JSONValue.parse(content);
            status = (String) json.get("status");
            ObjectMapper mapper = Utils.getObjectMapper();
            if (!StringUtils.isEmpty(status) && status.equalsIgnoreCase("OK")) {
                if (json.containsKey("result")) {
                    result = (String) json.get("result").toString();
                }
                try {
                    routeDto = mapper.readValue(result, RouteDTO.class);
                    return APIResponse.getSuccessResponse(routeDto);
                } catch (IOException e) {
                    return APIResponse.getErrorResponse("1", "Exception", routeDto);
                }
            }
        }
        return APIResponse.getErrorResponse("2", "Failure", routeDto);
    }

    public void addStations() throws Exception
    {
        logger.info("adding stations to db");
        StringBuilder builder = new StringBuilder(railApiEndPoint).append("/stations?").append("key=").append(api_key);
        String url = builder.toString();
        logger.info(url);
        String content = HttpClient.getContent(url, TIME_OUT);
        JSONArray jsonArr = new JSONArray();
        if (!StringUtils.isEmpty(content)) {
            jsonArr = (JSONArray) JSONValue.parse(content);
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject json = (JSONObject) jsonArr.get(i);
                mongoRailDBManager.addObject(STATION_COLLECTION_NAME, json.toString());
            }

        }
    }
}

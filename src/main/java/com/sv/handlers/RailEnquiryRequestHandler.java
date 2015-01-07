/**
 * 
 */
package com.sv.handlers;

import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.sv.service.RailApiService;
import com.sv.util.Utils;

/**
 * @author vaibhav
 */
@Controller("/v1/rail.*")
public class RailEnquiryRequestHandler implements IUrlRequestHandler {

    @Autowired
    private RailApiService railApiService;

    private Gson           gson        = new Gson();

    private String         contentType = "application/json";

    @Override
    public HttpResponse handleRequest(String requestUri, String requestPayload, MessageEvent event, HttpRequest request) throws Exception {
        Map<String, List<String>> urlParameters = Utils.getUrlParameters(requestUri);
        if(requestUri.matches("/v1/rail/liveStatus.*")) {
            String trainno = Utils.getURLParam(urlParameters, "trainno");
            String stnfrom = Utils.getURLParam(urlParameters, "stnfrom");
            String date = Utils.getURLParam(urlParameters, "date");
            return Utils.createResponse(gson.toJson(railApiService.getLiveStatus(trainno, stnfrom, date)), contentType, HttpResponseStatus.OK);
        }
        else if(requestUri.matches("/v1/rail/trainsAtStation.*")) {
            String stnfrom = Utils.getURLParam(urlParameters, "stnfrom");
            String stnto = Utils.getURLParam(urlParameters, "stnto");
            String hr = Utils.getURLParam(urlParameters, "hr");
            return Utils.createResponse(gson.toJson(railApiService.getTrainsAtStation(stnfrom, stnto, hr)), contentType, HttpResponseStatus.OK);
        }
        else if(requestUri.matches("/v1/rail/seatAvailability.*")) {
            String stnfrom = Utils.getURLParam(urlParameters, "stnfrom");
            String stnto = Utils.getURLParam(urlParameters, "stnto");
            String trainno = Utils.getURLParam(urlParameters, "trainno");
            String quota = Utils.getURLParam(urlParameters, "quota");
            String cls = Utils.getURLParam(urlParameters, "class");
            String date = Utils.getURLParam(urlParameters, "date");
            return Utils.createResponse(gson.toJson(railApiService.getSeatAvailabilty(stnfrom, stnto, date, cls, trainno, quota)), contentType, HttpResponseStatus.OK);
        }
        else if(requestUri.matches("/v1/rail/trains.*")) {
            String stnfrom = Utils.getURLParam(urlParameters, "stnfrom");
            String stnto = Utils.getURLParam(urlParameters, "stnto");
            String cls = Utils.getURLParam(urlParameters, "class");
            String date = Utils.getURLParam(urlParameters, "date");
            return Utils.createResponse(gson.toJson(railApiService.getTrains(stnfrom, stnto, date, cls)), contentType, HttpResponseStatus.OK);
        }
        else if(requestUri.matches("/v1/rail/fare.*")) {
            String stnfrom = Utils.getURLParam(urlParameters, "stnfrom");
            String stnto = Utils.getURLParam(urlParameters, "stnto");
            String trainno = Utils.getURLParam(urlParameters, "trainno");
            String quota = Utils.getURLParam(urlParameters, "quota");
            String age = Utils.getURLParam(urlParameters, "age");
            String date = Utils.getURLParam(urlParameters, "date");
            return Utils.createResponse(gson.toJson(railApiService.getFare(stnfrom, stnto, date, age, trainno, quota)), contentType, HttpResponseStatus.OK);
        }
        else if(requestUri.matches("/v1/rail/stations.*")) {
            String searchVal = Utils.getURLParam(urlParameters, "searchVal");
            return Utils.createResponse(gson.toJson(railApiService.getStations(searchVal)), contentType, HttpResponseStatus.OK);
        }
        else if(requestUri.matches("/v1/rail/route.*")) {
            String trainno = Utils.getURLParam(urlParameters, "trainno");
            return Utils.createResponse(gson.toJson(railApiService.getRoute(trainno)), contentType, HttpResponseStatus.OK);
        }
        else if(requestUri.matches("/v1/rail/pnrStatus.*")) {
            String pnr = Utils.getURLParam(urlParameters, "pnr");
            return Utils.createResponse(gson.toJson(railApiService.getPnrStatus(pnr)), contentType, HttpResponseStatus.OK);
        }
        return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
    }
}

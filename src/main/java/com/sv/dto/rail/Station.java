/**
 * 
 */
package com.sv.dto.rail;

import org.json.simple.JSONObject;

/**
 * @author vaibhav
 *
 */
public class Station
{
    private String code;
    private String name;
    
    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String toJson(){
        JSONObject jsonObj = toJsonObj();
        return jsonObj.toString();
    }
    
    public JSONObject toJsonObj(){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("code", getCode());
        jsonObj.put("name", getName());
        return jsonObj;
    }
}

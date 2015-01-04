/**
 * 
 */
package com.sv.dto.rail;

import java.util.List;

/**
 * @author vaibhav
 *
 */
public class FareDTO
{
    private String trainno;
    private String type;
    private String from;
    private String to;
    private String age;
    private String quoto;
    private List<Fare> fare;
    
    public String getTrainno()
    {
        return trainno;
    }

    public void setTrainno(String trainno)
    {
        this.trainno = trainno;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public String getQuoto()
    {
        return quoto;
    }

    public void setQuoto(String quoto)
    {
        this.quoto = quoto;
    }

    public List<Fare> getFare()
    {
        return fare;
    }

    public void setFare(List<Fare> fare)
    {
        this.fare = fare;
    }

    public static class Fare {
        private String cls;
        private String fare;
        
        public String getCls()
        {
            return cls;
        }
        public void setCls(String cls)
        {
            this.cls = cls;
        }
        public String getFare()
        {
            return fare;
        }
        public void setFare(String fare)
        {
            this.fare = fare;
        }
    }
}

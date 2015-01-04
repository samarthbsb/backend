/**
 * 
 */
package com.sv.dto.rail;

import java.util.List;

/**
 * @author vaibhav
 *
 */
public class PNRStatus
{
    private String pnr;
    private String eticket;
    private String journey;
    private String trainno;
    private String name;
    private String from;
    private String to;
    private String brdg;
    private String cls;
    private List<Passenger> passengers;
    private String chart;
    private String error;
    
    public String getPnr()
    {
        return pnr;
    }

    public void setPnr(String pnr)
    {
        this.pnr = pnr;
    }

    public String getEticket()
    {
        return eticket;
    }

    public void setEticket(String eticket)
    {
        this.eticket = eticket;
    }

    public String getJourney()
    {
        return journey;
    }

    public void setJourney(String journey)
    {
        this.journey = journey;
    }

    public String getTrainno()
    {
        return trainno;
    }

    public void setTrainno(String trainno)
    {
        this.trainno = trainno;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public String getBrdg()
    {
        return brdg;
    }

    public void setBrdg(String brdg)
    {
        this.brdg = brdg;
    }

    public List<Passenger> getPassengers()
    {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers)
    {
        this.passengers = passengers;
    }

    public String getChart()
    {
        return chart;
    }

    public void setChart(String chart)
    {
        this.chart = chart;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public String getCls()
    {
        return cls;
    }

    public void setCls(String cls)
    {
        this.cls = cls;
    }

    public static class Passenger {
        private String bookingstatus;
        private String currentstatus;
        private String coach;
        
        public String getBookingstatus()
        {
            return bookingstatus;
        }
        public void setBookingstatus(String bookingstatus)
        {
            this.bookingstatus = bookingstatus;
        }
        public String getCurrentstatus()
        {
            return currentstatus;
        }
        public void setCurrentstatus(String currentstatus)
        {
            this.currentstatus = currentstatus;
        }
        public String getCoach()
        {
            return coach;
        }
        public void setCoach(String coach)
        {
            this.coach = coach;
        }
    }
}

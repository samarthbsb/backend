/**
 * 
 */
package com.sv.dto.rail;

import java.util.List;

/**
 * @author vaibhav
 *
 */
public class TrainsAtStation
{
    private String station;
    private String stationname;
    private String tostation;
    private String tostationname;
    private String hr;
    private List<TrainStation> trains;
    
    public String getStation()
    {
        return station;
    }
    public void setStation(String station)
    {
        this.station = station;
    }
    public String getStationname()
    {
        return stationname;
    }
    public void setStationname(String stationname)
    {
        this.stationname = stationname;
    }
    public String getTostation()
    {
        return tostation;
    }
    public void setTostation(String tostation)
    {
        this.tostation = tostation;
    }
    public String getTostationname()
    {
        return tostationname;
    }
    public void setTostationname(String tostationname)
    {
        this.tostationname = tostationname;
    }
    public String getHr()
    {
        return hr;
    }
    public void setHr(String hr)
    {
        this.hr = hr;
    }
    public List<TrainStation> getTrains()
    {
        return trains;
    }
    public void setTrains(List<TrainStation> trains)
    {
        this.trains = trains;
    }
    
    public static class TrainStation {
        
        private String from;
        private String fromname;
        private String schdep;
        private String expdep;
        private String delaydep;
        private String to;
        private String toname;
        private String scharr;
        private String exparr;
        private String delayarr;
        private String trainno;
        private String trainname;
        private String type;
        private String platform;
        
        public String getFrom()
        {
            return from;
        }
        public void setFrom(String from)
        {
            this.from = from;
        }
        public String getFromname()
        {
            return fromname;
        }
        public void setFromname(String fromname)
        {
            this.fromname = fromname;
        }
        public String getSchdep()
        {
            return schdep;
        }
        public void setSchdep(String schdep)
        {
            this.schdep = schdep;
        }
        public String getExpdep()
        {
            return expdep;
        }
        public void setExpdep(String expdep)
        {
            this.expdep = expdep;
        }
        public String getDelaydep()
        {
            return delaydep;
        }
        public void setDelaydep(String delaydep)
        {
            this.delaydep = delaydep;
        }
        public String getTo()
        {
            return to;
        }
        public void setTo(String to)
        {
            this.to = to;
        }
        public String getToname()
        {
            return toname;
        }
        public void setToname(String toname)
        {
            this.toname = toname;
        }
        public String getScharr()
        {
            return scharr;
        }
        public void setScharr(String scharr)
        {
            this.scharr = scharr;
        }
        public String getExparr()
        {
            return exparr;
        }
        public void setExparr(String exparr)
        {
            this.exparr = exparr;
        }
        public String getDelayarr()
        {
            return delayarr;
        }
        public void setDelayarr(String delayarr)
        {
            this.delayarr = delayarr;
        }
        public String getTrainno()
        {
            return trainno;
        }
        public void setTrainno(String trainno)
        {
            this.trainno = trainno;
        }
        public String getTrainname()
        {
            return trainname;
        }
        public void setTrainname(String trainname)
        {
            this.trainname = trainname;
        }
        public String getType()
        {
            return type;
        }
        public void setType(String type)
        {
            this.type = type;
        }
        public String getPlatform()
        {
            return platform;
        }
        public void setPlatform(String platform)
        {
            this.platform = platform;
        }
    }
}

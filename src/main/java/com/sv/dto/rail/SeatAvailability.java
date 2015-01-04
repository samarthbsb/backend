/**
 * 
 */
package com.sv.dto.rail;

import java.util.List;

/**
 * @author vaibhav
 *
 */
public class SeatAvailability
{
    private String trainno;
    private String from;
    private String to;
    private String cls;
    private String quota;
    private String error;
    private List<Seat> seats;
    
    public String getTrainno()
    {
        return trainno;
    }
    public void setTrainno(String trainno)
    {
        this.trainno = trainno;
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
    public String getCls()
    {
        return cls;
    }
    public void setCls(String cls)
    {
        this.cls = cls;
    }
    public String getQuota()
    {
        return quota;
    }
    public void setQuota(String quota)
    {
        this.quota = quota;
    }
    public String getError()
    {
        return error;
    }
    public void setError(String error)
    {
        this.error = error;
    }
    public List<Seat> getSeats()
    {
        return seats;
    }
    public void setSeats(List<Seat> seats)
    {
        this.seats = seats;
    }

    public static class Seat {
        
        private String date;
        private String seat;
        public String getDate()
        {
            return date;
        }
        public void setDate(String date)
        {
            this.date = date;
        }
        public String getSeat()
        {
            return seat;
        }
        public void setSeat(String seat)
        {
            this.seat = seat;
        }
    }
}

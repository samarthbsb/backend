/**
 * 
 */
package com.sv.dto.rail;

import java.util.List;

/**
 * @author vaibhav
 *
 */
public class RouteDTO
{
    private String trainno;
    private String name;
    private List<Route> route;
    
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

    public List<Route> getRoute()
    {
        return route;
    }

    public void setRoute(List<Route> route)
    {
        this.route = route;
    }

    public static class Route {
        private String cls;
        private List<StationForRoute> stn;
        
        public String getCls()
        {
            return cls;
        }

        public void setCls(String cls)
        {
            this.cls = cls;
        }

        public List<StationForRoute> getStn()
        {
            return stn;
        }

        public void setStn(List<StationForRoute> stn)
        {
            this.stn = stn;
        }

        public static class StationForRoute extends Station{
            private String arr;
            private String dep;
            private String day;
            
            public String getArr()
            {
                return arr;
            }
            public void setArr(String arr)
            {
                this.arr = arr;
            }
            public String getDep()
            {
                return dep;
            }
            public void setDep(String dep)
            {
                this.dep = dep;
            }
            public String getDay()
            {
                return day;
            }
            public void setDay(String day)
            {
                this.day = day;
            }
        }
    }
}

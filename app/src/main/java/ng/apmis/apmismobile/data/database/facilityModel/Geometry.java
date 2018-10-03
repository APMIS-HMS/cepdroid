package ng.apmis.apmismobile.data.database.facilityModel;

public class Geometry {

    private Viewport viewport;
    private Location location;

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    static class Location {

        private Double lng;
        private Double lat;

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

    }

    static class Viewport {

        private Double east;
        private Double north;
        private Double west;
        private Double south;

        public Double getEast() {
            return east;
        }

        public void setEast(Double east) {
            this.east = east;
        }

        public Double getNorth() {
            return north;
        }

        public void setNorth(Double north) {
            this.north = north;
        }

        public Double getWest() {
            return west;
        }

        public void setWest(Double west) {
            this.west = west;
        }

        public Double getSouth() {
            return south;
        }

        public void setSouth(Double south) {
            this.south = south;
        }

    }

}

## 🩸 RoktoKhoj

**RoktoKhoj** is a simple blood donor finder app built with a **Spring Boot backend** and a **JavaFX desktop frontend**.

Users can register themselves as blood donors by providing their **blood group**, **contact details**, and the **area where they’re usually available**. When someone needs blood urgently, they can search for donors based on blood group and location.

To make location-based searches fast and accurate, RoktoKhoj uses **PostgreSQL with the PostGIS extension** for geospatial queries. This allows the app to find and sort **nearby donors by distance**, showing the closest matches first along with their contact information.

### ✨ What it does

* Register as a blood donor
* Search donors by blood group and area
* Find nearest donors using geospatial queries (PostGIS)
* View donor contact details instantly
* Built with Spring Boot, JavaFX, PostgreSQL & PostGIS

### 🚀 Future Plans

* **Live availability sharing** for blood donors (online/offline/available now)
* **Real-time location updates** (opt-in) to improve nearby donor matching
* **Emergency broadcast alerts** to notify eligible donors instantly
* **Mobile app support** for wider reach
* **Improved privacy controls** for location and contact visibility

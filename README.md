# Distributed Systems – University Assignment

This project implements a **RESTful currency conversion service** using modern web technologies and follows a full-stack architecture.

- The **backend** is developed in **Java** using **Spring Boot**, exposing REST endpoints for currency conversion, exchange rate retrieval, historical tracking, and data export.  
- The application integrates with a third-party **currency exchange API** to fetch real-time rates.  
- Exchange operations are stored in a **PostgreSQL** database, allowing full CRUD operations on the history.  
- The **web client** is built with **React.js**, offering an interactive interface for users to convert currencies, view conversion results, edit or delete history entries, and download the data in JSON, XML, or **Protocol Buffers (.pb)** format.  
- Additionally, a **mobile client** is implemented in **React Native**, providing access to key features on mobile devices.

The system demonstrates multi-format API responses (JSON, XML, Protobuf) and efficient data handling through Protocol Buffers.

package com.example.data.model

object SampleDrivers {
    val DRIVERS = listOf(
        DriverProfile(
            id = "drv_1",
            name = "Ram Bahadur Thapa",
            nameNp = "राम बहादुर थापा",
            phone = "+977 9845012345",
            vehicleType = VehicleType.E_RICKSHAW,
            vehicleModel = "Mayuri Pro Deluxe 1200W Electric Safari",
            licensePlate = "बा २ ह ४५८९ (Ba 2 Ha 4589)",
            rating = 4.9f,
            totalTrips = 428,
            experienceYears = 4,
            currentLat = 27.6980,
            currentLng = 84.4285,
            isAvailable = true,
            reviews = listOf(
                DriverReview(
                    reviewerName = "Pooja Sharma",
                    rating = 5.0f,
                    comment = "Very polite uncle! Smooth battery ride, helped carry heavy vegetable sacks from Lions Chowk.",
                    dateText = "Yesterday"
                ),
                DriverReview(
                    reviewerName = "Santosh Adhikari",
                    rating = 4.8f,
                    comment = "Clean and quiet e-rickshaw. Paid via eSewa seamlessly.",
                    dateText = "3 days ago"
                ),
                DriverReview(
                    reviewerName = "Anish Giri",
                    rating = 5.0f,
                    comment = "Arrived within 3 minutes of booking in Bharatpur. Highly recommended!",
                    dateText = "1 week ago"
                )
            )
        ),
        DriverProfile(
            id = "drv_2",
            name = "Kiran Shrestha",
            nameNp = "किरण श्रेष्ठ",
            phone = "+977 9851098765",
            vehicleType = VehicleType.AUTO_RICKSHAW,
            vehicleModel = "Bajaj Compact RE 4-Stroke CNG Auto",
            licensePlate = "ना १ ह ८२३४ (Na 1 Ha 8234)",
            rating = 4.85f,
            totalTrips = 612,
            experienceYears = 6,
            currentLat = 27.6910,
            currentLng = 84.4340,
            isAvailable = true,
            reviews = listOf(
                DriverReview(
                    reviewerName = "Ramesh Poudel",
                    rating = 5.0f,
                    comment = "Fast and agile through rush-hour traffic near Chaubiskothi. Saved my time!",
                    dateText = "2 days ago"
                ),
                DriverReview(
                    reviewerName = "Sunita KC",
                    rating = 4.7f,
                    comment = "Reliable auto driver. Fare was exact as calculated on app.",
                    dateText = "Last week"
                )
            )
        ),
        DriverProfile(
            id = "drv_3",
            name = "Maya Tamang",
            nameNp = "माया तामाङ",
            phone = "+977 9813245678",
            vehicleType = VehicleType.E_RICKSHAW,
            vehicleModel = "Saarthi Shresth Eco-Clean Safari",
            licensePlate = "बा १ ह ६७२१ (Ba 1 Ha 6721)",
            rating = 5.0f,
            totalTrips = 540,
            experienceYears = 5,
            currentLat = 27.7040,
            currentLng = 84.4220,
            isAvailable = true,
            reviews = listOf(
                DriverReview(
                    reviewerName = "Deepa Gautam",
                    rating = 5.0f,
                    comment = "Inspiring lady driver! Extremely safe and friendly ride for female travelers at dusk.",
                    dateText = "Today"
                ),
                DriverReview(
                    reviewerName = "Binod Subedi",
                    rating = 5.0f,
                    comment = "Top tier service, battery safari had comfortable cushioned seats.",
                    dateText = "4 days ago"
                )
            )
        ),
        DriverProfile(
            id = "drv_4",
            name = "Bikash Chaudhary",
            nameNp = "बिकास चौधरी",
            phone = "+977 9804561234",
            vehicleType = VehicleType.CARGO_RICKSHAW,
            vehicleModel = "Atul Shakti E-Loader Cargo Rickshaw (150kg)",
            licensePlate = "प्रदेश ३-०३-००१ ह ९९०१ (Pradesh 3 Ha 9901)",
            rating = 4.95f,
            totalTrips = 312,
            experienceYears = 3,
            currentLat = 27.6840,
            currentLng = 84.4410,
            isAvailable = true,
            reviews = listOf(
                DriverReview(
                    reviewerName = "Bimal Hardware Store",
                    rating = 5.0f,
                    comment = "Delivered cement boxes and paint buckets directly to construction site without damage.",
                    dateText = "Yesterday"
                ),
                DriverReview(
                    reviewerName = "Sarita Shrestha",
                    rating = 4.9f,
                    comment = "Perfect parcel delivery. Bikash brother called receiver on arrival.",
                    dateText = "5 days ago"
                )
            )
        ),
        DriverProfile(
            id = "drv_5",
            name = "Dhan Bahadur Gurung",
            nameNp = "धन बहादुर गुरुङ",
            phone = "+977 9846712398",
            vehicleType = VehicleType.AUTO_RICKSHAW,
            vehicleModel = "TVS King Deluxe 200cc Auto Rickshaw",
            licensePlate = "ग १ ह ३४१२ (Ga 1 Ha 3412)",
            rating = 4.75f,
            totalTrips = 380,
            experienceYears = 3,
            currentLat = 27.7090,
            currentLng = 84.4370,
            isAvailable = true,
            reviews = listOf(
                DriverReview(
                    reviewerName = "Roshan Neupane",
                    rating = 4.8f,
                    comment = "Smooth driving, accepted Fonepay QR code directly.",
                    dateText = "3 days ago"
                )
            )
        )
    )
}
